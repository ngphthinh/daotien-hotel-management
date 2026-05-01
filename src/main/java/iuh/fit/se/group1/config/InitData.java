package iuh.fit.se.group1.config;

import iuh.fit.se.group1.dto.*;
import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.OrderBookStatus;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import iuh.fit.se.group1.mapper.BookingMapper;
import iuh.fit.se.group1.mapper.CustomerMapper;
import iuh.fit.se.group1.mapper.EmployeeMapper;
import iuh.fit.se.group1.mapper.OrderMapper;
import iuh.fit.se.group1.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class InitData {

    public static void initAllData() {
        initAmenity();
        initRole();
        initShift();
        initRoomType();
        initOrderType();
        initSurcharge();
        initRoom();
        initPromotion();
        initCustomer();
        initEmployee();
        initOrdersComplete();
        initOrdersPending();
    }





    private static void initEmployee() {
        try {
            EmployeeService employeeService = new EmployeeService();
            
            // Check if already exist
            if (employeeService.count() >= 5) {
                log.info("5 employees already exist, skipping initialization");
                return;
            }
            
            // Create 5 employees: 3 managers + 2 receptionists
            String[] names = {"Quản trị viên admin", "Trần Minh Hoàng", "Phạm Đức Anh", "Lê Thị Mai", "Vũ Văn Nam"};
            String[] phones = {"0901234567", "0902345678", "0903456789", "0904567890", "0905678901"};
            String[] emails = {"thinh@hotel.com", "hoang@hotel.com", "anh@hotel.com", "mai@hotel.com", "nam@hotel.com"};
            String[] citizenIds = {"082205000819", "079201001234", "079201001235", "079201001236", "079201001237"};
            String[] roleIds = {"MANAGER", "MANAGER", "MANAGER", "RECEPTIONIST", "RECEPTIONIST"};

            for (int i = 0; i < 5; i++) {
                EmployeeDTO employeeDTO = EmployeeDTO.builder()
                        .fullName(names[i])
                        .phone(phones[i])
                        .email(emails[i])
                        .gender(i % 2 == 0)
                        .citizenId(citizenIds[i])
                        .hireDate(LocalDate.of(2025, 12, 1).minusMonths(i % 3))
                        .build();
                
                employeeService.createEmployee(employeeDTO, roleIds[i]);
                log.info("Created employee: {}", names[i]);
            }
            
            log.info("5 employees created successfully");
        } catch (Exception e) {
            log.error("Error initializing employees", e);
        }
    }

    private static void initOrdersComplete() {
        initOrdersBatch(true);
    }

    private static void initOrdersPending() {
        initOrdersBatch(false);
    }

    private static void initOrdersBatch(boolean isComplete) {
        try {
            OrderService orderService = new OrderService();
            EmployeeService employeeService = new EmployeeService();
            CustomerService customerService = new CustomerService();
            RoomService roomService = new RoomService();
            
            String statusName = isComplete ? "COMPLETED" : "PROCESSING";
            OrderBookStatus bookingStatus = isComplete ? OrderBookStatus.COMPLETED : OrderBookStatus.PROCESSING;
            
            // Get all data
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
            List<CustomerDTO> customers = customerService.getAllCustomer();
            List<RoomViewDTO> rooms = roomService.getAllRooms();
            
            if (employees.size() < 2 || customers.isEmpty() || rooms.size() < 20) {
                log.warn("Not enough data to create {} orders: employees={}, customers={}, rooms={}",
                        statusName, employees.size(), customers.size(), rooms.size());
                return;
            }
            
            // Get order type from database
            OrderTypeDTO orderType = doInTransactionDTO(em -> {
                OrderType ot = em.createQuery(
                        "select ot from OrderType ot where ot.name = ?1",
                        OrderType.class
                ).setParameter(1, bookingStatus)
                 .getSingleResult();
                
                return OrderTypeDTO.builder()
                        .orderTypeId(ot.getOrderTypeId())
                        .name(ot.getName())
                        .build();
            });
            
            if (orderType == null) {
                log.warn("OrderType not found for {}", bookingStatus);
                return;
            }
            
            // Create 20 orders
            for (int i = 0; i < 20; i++) {
                EmployeeDTO employee = employees.get(i % employees.size());
                CustomerDTO customer = customers.get(i % customers.size());
                RoomViewDTO room = rooms.get(i % rooms.size());
                
                LocalDateTime baseDate = isComplete
                        ? LocalDateTime.now().minusMonths(3 - (i % 3)).withHour(14).withMinute(0)
                        : LocalDateTime.now().minusDays(5 - (i % 5)).withHour(14).withMinute(0);
                
                // Create booking DTO
                BookingViewDTO bookingDTO = BookingViewDTO.builder()
                        .checkInDate(baseDate)
                        .checkOutDate(baseDate.plusDays(3))
                        .bookingType(BookingType.DAILY)
                        .room(room)
                        .build();
                
                // Create order DTO
                OrderDTO orderDTO = OrderDTO.builder()
                        .orderDate(baseDate)
                        .employee(employee)
                        .orderType(orderType)
                        .customer(customer)
                        .totalAmount(BigDecimal.valueOf(500000 + (i * 50000)))
                        .deposit(BigDecimal.valueOf(150000 + (i * 10000)))
                        .bookings(List.of(bookingDTO))
                        .paymentDate(isComplete ? baseDate.toLocalDate().plusDays(7) : null)
                        .paymentType(isComplete ? PaymentType.CASH : null)
                        .employeePayment(isComplete ? employees.get((i + 1) % employees.size()) : null)
                        .build();
                
                orderService.createOrder(orderDTO, new ArrayList<>());
                log.debug("Created {} order #{}", statusName, i + 1);
            }
            
            log.info("Successfully created 20 {} orders", statusName);
        } catch (Exception e) {
            log.error("Error initializing {} orders", isComplete ? "complete" : "pending", e);
        }
    }




    public static void initOrderTest() {
        List<BookingViewDTO> bookings = List.of(
                BookingViewDTO.builder()
                        .checkInDate(LocalDateTime.of(2026, 4, 3, 14, 0))
                        .checkOutDate(LocalDateTime.of(2026, 5, 10, 12, 0))
                        .bookingType(BookingType.DAILY)
                        .room(RoomViewDTO.builder()
                                .roomId(1L)
                                .roomNumber("201")
                                .build())
                        .build()
        );
        OrderDTO order = OrderDTO.builder()
                .orderDate(LocalDateTime.of(2026, 5, 10, 10, 0))
                .totalAmount(BigDecimal.valueOf(9700000.0))
                .deposit(BigDecimal.valueOf(2880000.0))

                .employee(EmployeeDTO.builder()
                        .employeeId(1L)
                        .fullName("Quản Trị Viên Admin")
                        .phone("0123456789")
                        .email("nguyenphuocthinh0710@gmail.com")
                        .gender(false)
                        .citizenId("082205000819")
                        .hireDate(LocalDate.of(2026, 5, 1))
                        .account(AccountDTO.builder()
                                .accountId("f6c8feb8-394a-4472-a680-5c20a123bbe5")
                                .username("admin1")
                                .role(RoleDTO.builder()
                                        .roleId("MANAGER")
                                        .roleName("Nhân viên quản lí")
                                        .build())
                                .build())
                        .build())

                .orderType(OrderTypeDTO.builder()
                        .orderTypeId(3L)
                        .name(null)
                        .build())

                .customer(CustomerDTO.builder()
                        .fullName("Nguyen Van Ban")
                        .phone("0987676515")
                        .citizenId("B5234567")
                        .gender(false)
                        .dateOfBirth(LocalDate.of(2003, 7, 17))
                        .build())

                // ===== QUAN TRỌNG: 3 booking segment =====
                .bookings(bookings)

                .build();

        OrderService orderService = new OrderService();
        orderService.createOrder(order, new ArrayList<>());
    }

    private static void doInTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            transaction = entityManager.getTransaction();

            transaction.begin();

            consumer.accept(entityManager);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close(); // đóng ở đây
            }
        }
    }

    private static <T> T doInTransactionDTO(java.util.function.Function<EntityManager, T> function) {
        EntityManager entityManager = null;
        EntityTransaction transaction = null;

        try {
            entityManager = JPAUtil.getEntityManager();
            transaction = entityManager.getTransaction();

            transaction.begin();

            T result = function.apply(entityManager);

            transaction.commit();
            
            return result;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }

    private static void initRole() {
        Role roleManager = Role.builder()
                .roleId("MANAGER")
                .roleName("Nhân viên quản lí")
                .createdAt(LocalDate.now())
                .build();

        Role roleReceptionist = Role.builder()
                .roleId("RECEPTIONIST")
                .roleName("Nhân viên lễ tân")
                .createdAt(LocalDate.now())
                .build();

        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Role r", Long.class
            ).getSingleResult();

            if (count == 0) {
                em.persist(roleManager);
                em.persist(roleReceptionist);
            }
        });
    }

    private static void initShift() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Shift r", Long.class
            ).getSingleResult();
            // only create shifts when table is empty
            if (count != 0) {
                return;
            }
            em.persist(Shift.builder()
                    .name("Ca 01")
                    .startTime("00:00:00")
                    .endTime("06:00:00")
                    .createdAt(LocalDate.now())
                    .build());

            em.persist(Shift.builder()
                    .name("Ca 02")
                    .startTime("06:00:00")
                    .endTime("12:00:00")
                    .createdAt(LocalDate.now())
                    .build());

            em.persist(Shift.builder()
                    .name("Ca 03")
                    .startTime("12:00:00")
                    .endTime("18:00:00")
                    .createdAt(LocalDate.now())
                    .build());

            em.persist(Shift.builder()
                    .name("Ca 04")
                    .startTime("18:00:00")
                    .endTime("23:59:59")
                    .createdAt(LocalDate.now())
                    .build());
        });
    }

    private static void initRoomType() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from RoomType r", Long.class
            ).getSingleResult();
            // only create room types when table is empty
            if (count != 0) {
                return;
            }
            em.persist(RoomType.builder()
                    .roomTypeId("DOUBLE")
                    .name("Phòng đôi")
                    .createdAt(LocalDate.now())
                    .hourlyRate(BigDecimal.valueOf(80000))
                    .dailyRate(BigDecimal.valueOf(500000))
                    .overnightRate(BigDecimal.valueOf(300000))
                    .additionalHourRate(BigDecimal.valueOf(30000))
                    .build());

            em.persist(RoomType.builder()
                    .roomTypeId("SINGLE")
                    .name("Phòng đơn")
                    .createdAt(LocalDate.now())
                    .hourlyRate(BigDecimal.valueOf(50000))
                    .dailyRate(BigDecimal.valueOf(300000))
                    .overnightRate(BigDecimal.valueOf(250000))
                    .additionalHourRate(BigDecimal.valueOf(20000))
                    .build());
        });
    }

    private static void initOrderType() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from OrderType r", Long.class
            ).getSingleResult();
            // only create order types when table is empty
            if (count != 0) {
                return;
            }
            for (OrderBookStatus type : OrderBookStatus.values()) {
                em.persist(OrderType.builder()
                        .name(type)
                        .createdAt(LocalDate.now())
                        .build());
            }

        });
    }

    private static void initSurcharge() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Surcharge r", Long.class
            ).getSingleResult();
            // only create surcharges when table is empty
            if (count != 0) {
                return;
            }
            em.persist(Surcharge.builder().name("Phụ thu ngày lễ").price(BigDecimal.valueOf(50000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu cuối tuần").price(BigDecimal.valueOf(30000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu khách thêm").price(BigDecimal.valueOf(70000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu nhận phòng sớm").price(BigDecimal.valueOf(50000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu trả phòng trễ").price(BigDecimal.valueOf(60000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu dọn phòng đặc biệt").price(BigDecimal.valueOf(40000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu mất chìa khóa").price(BigDecimal.valueOf(150000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu thêm chăn gối").price(BigDecimal.valueOf(20000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu nâng cấp phòng").price(BigDecimal.valueOf(100000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu sử dụng hồ bơi đêm").price(BigDecimal.valueOf(50000)).createdAt(LocalDate.now()).build());
            em.persist(Surcharge.builder().name("Phụ thu giữ xe").price(BigDecimal.valueOf(10000)).createdAt(LocalDate.now()).build());

        });
    }


    private static void initAmenity() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Amenity r", Long.class
            ).getSingleResult();
            // only create amenities when table is empty
            if (count != 0) {
                return;
            }
            em.persist(Amenity.builder().nameAmenity("Nước suối").price(BigDecimal.valueOf(10000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Khăn lạnh").price(BigDecimal.valueOf(5000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Bia lon").price(BigDecimal.valueOf(20000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Nước ngọt").price(BigDecimal.valueOf(15000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Mì ly").price(BigDecimal.valueOf(12000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Bàn chải đánh răng").price(BigDecimal.valueOf(8000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Kem đánh răng").price(BigDecimal.valueOf(7000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Dầu gội").price(BigDecimal.valueOf(10000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Sữa tắm").price(BigDecimal.valueOf(10000)).createdAt(LocalDate.now()).build());
            em.persist(Amenity.builder().nameAmenity("Khăn tắm").price(BigDecimal.valueOf(15000)).createdAt(LocalDate.now()).build());

        });
    }

    private static void initRoom() {
        log.info("Initializing rooms...");
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from RoomType r", Long.class
            ).getSingleResult();
            if (count == 0) {
                return;
            }
            RoomType doubleType = em.find(RoomType.class, "DOUBLE");
            RoomType singleType = em.find(RoomType.class, "SINGLE");

            // DOUBLE 201–220
            for (int i = 201; i <= 220; i++) {
                em.persist(Room.builder()
                        .roomNumber(String.valueOf(i))
                        .roomType(doubleType)
                        .roomStatus(RoomStatus.AVAILABLE)
                        .createdAt(LocalDate.now())
                        .build());
            }

            // SINGLE 101–120
            for (int i = 101; i <= 120; i++) {
                em.persist(Room.builder()
                        .roomNumber(String.valueOf(i))
                        .roomType(singleType)
                        .roomStatus(RoomStatus.AVAILABLE)
                        .createdAt(LocalDate.now())
                        .build());
            }

        });
        log.info("Rooms initialized successfully.");
    }


    private static void initPromotion() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Promotion r", Long.class
            ).getSingleResult();
            if (count != 0) {
                return;
            }
            em.persist(Promotion.builder()
                    .promotionName("Khuyến mãi mùa đông")
                    .description("Giảm giá 10% cho đơn hàng từ 500,000đ")
                    .discountPercent(10f)
                    .minOrderAmount(BigDecimal.valueOf(500000))
                    .startDate(LocalDate.of(2025, 12, 1))
                    .endDate(LocalDate.of(2025, 12, 31))
                    .createdAt(LocalDate.of(2025, 11, 20))
                    .build());

            em.persist(Promotion.builder()
                    .promotionName("Khuyến mãi cuối tuần")
                    .description("Giảm giá 15% cho đơn hàng từ 1,000,000đ vào cuối tuần")
                    .discountPercent(15f)
                    .minOrderAmount(BigDecimal.valueOf(1000000))
                    .startDate(LocalDate.of(2025, 12, 1))
                    .endDate(LocalDate.of(2025, 12, 31))
                    .createdAt(LocalDate.of(2025, 11, 25))
                    .build());

            em.persist(Promotion.builder()
                    .promotionName("Khuyến mãi khách VIP")
                    .description("Giảm giá 20% cho đơn hàng từ 2,000,000đ")
                    .discountPercent(20f)
                    .minOrderAmount(BigDecimal.valueOf(2000000))
                    .startDate(LocalDate.of(2025, 11, 1))
                    .endDate(LocalDate.of(2025, 12, 31))
                    .createdAt(LocalDate.of(2025, 10, 30))
                    .build());
        });
    }

    private static void initCustomer() {
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from Customer r", Long.class
            ).getSingleResult();
            if (count != 0) {
                return;
            }
            em.persist(Customer.builder().fullName("Trần Minh Hoàng").phone("0911111111").email("tranminhhoang@gmail.com").citizenId("079201001234").gender(true).dateOfBirth(LocalDate.of(1990, 5, 15)).createdAt(LocalDate.of(2025, 12, 1)).build());

            em.persist(Customer.builder().fullName("Nguyễn Thị Lan").phone("0922222222").email("nguyenthilan@gmail.com").citizenId("079201001235").gender(false).dateOfBirth(LocalDate.of(1992, 8, 20)).createdAt(LocalDate.of(2025, 12, 2)).build());

            em.persist(Customer.builder().fullName("Phạm Đức Anh").phone("0933333333").email("phamducanh@gmail.com").citizenId("079201001236").gender(true).dateOfBirth(LocalDate.of(1988, 3, 10)).createdAt(LocalDate.of(2025, 12, 3)).build());

            em.persist(Customer.builder().fullName("Lê Thị Mai").phone("0944444444").email("lethimai@gmail.com").citizenId("079201001237").gender(false).dateOfBirth(LocalDate.of(1995, 11, 25)).createdAt(LocalDate.of(2025, 12, 4)).build());

            em.persist(Customer.builder().fullName("Vũ Văn Nam").phone("0955555555").email("vuvannam@gmail.com").citizenId("079201001238").gender(true).dateOfBirth(LocalDate.of(1987, 7, 18)).createdAt(LocalDate.of(2025, 12, 5)).build());

            em.persist(Customer.builder().fullName("Đặng Thị Hương").phone("0966666666").email("dangthihuong@gmail.com").citizenId("079201001239").gender(false).dateOfBirth(LocalDate.of(1993, 2, 14)).createdAt(LocalDate.of(2025, 12, 6)).build());

            em.persist(Customer.builder().fullName("Ngô Quang Huy").phone("0977777777").email("ngoquanghuy@gmail.com").citizenId("079201001240").gender(true).dateOfBirth(LocalDate.of(1991, 9, 30)).createdAt(LocalDate.of(2025, 12, 7)).build());

            em.persist(Customer.builder().fullName("Bùi Thị Thảo").phone("0988888888").email("buithithao@gmail.com").citizenId("079201001241").gender(false).dateOfBirth(LocalDate.of(1994, 6, 22)).createdAt(LocalDate.of(2025, 12, 8)).build());

            em.persist(Customer.builder().fullName("Đinh Văn Tùng").phone("0999999999").email("dinhvantung@gmail.com").citizenId("079201001242").gender(true).dateOfBirth(LocalDate.of(1989, 12, 5)).createdAt(LocalDate.of(2025, 12, 9)).build());

            em.persist(Customer.builder().fullName("Hoàng Thị Nga").phone("0910101010").email("hoangthinga@gmail.com").citizenId("079201001243").gender(false).dateOfBirth(LocalDate.of(1996, 4, 17)).createdAt(LocalDate.of(2025, 12, 10)).build());

            em.persist(Customer.builder().fullName("Trịnh Văn Kiên").phone("0920202020").email("trinhvankien@gmail.com").citizenId("079201001244").gender(true).dateOfBirth(LocalDate.of(1993, 8, 12)).createdAt(LocalDate.of(2025, 12, 11)).build());

            em.persist(Customer.builder().fullName("Võ Thị Yến").phone("0930303030").email("vothiyen@gmail.com").citizenId("079201001245").gender(false).dateOfBirth(LocalDate.of(1991, 3, 28)).createdAt(LocalDate.of(2025, 12, 12)).build());

            em.persist(Customer.builder().fullName("Dương Văn Sơn").phone("0940404040").email("duongvanson@gmail.com").citizenId("079201001246").gender(true).dateOfBirth(LocalDate.of(1989, 11, 15)).createdAt(LocalDate.of(2025, 12, 13)).build());

            em.persist(Customer.builder().fullName("Mai Văn Đức").phone("0960606060").email("phanthithu@gmail.com").citizenId("079201001248").gender(false).dateOfBirth(LocalDate.of(1994, 7, 22)).createdAt(LocalDate.of(2025, 12, 14)).build());

        });
    }
}
