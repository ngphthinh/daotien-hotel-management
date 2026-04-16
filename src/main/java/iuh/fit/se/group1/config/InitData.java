package iuh.fit.se.group1.config;

import iuh.fit.se.group1.entity.*;
import iuh.fit.se.group1.enums.OrderBookStatus;
import iuh.fit.se.group1.enums.RoomStatus;
import iuh.fit.se.group1.infrastructure.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

public class InitData {

    public static void initAllData(){
        initAmenity();
        initRole();
        initShift();
        initRoomType();
        initOrderType();
        initSurcharge();
        initRoom();
        initPromotion();
        initCustomer();
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
        doInTransaction(em -> {
            Long count = em.createQuery(
                    "select count(r) from RoomType r", Long.class
            ).getSingleResult();
            if (count != 0) {
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
            em.persist(Customer.builder().fullName("Trần Minh Hoàng").phone("0911111111").email("tranminhhoang@gmail.com").citizenId("079201001234").gender(true).dateOfBirth(LocalDate.of(1990,5,15)).createdAt(LocalDate.of(2025,12,1)).build());

            em.persist(Customer.builder().fullName("Nguyễn Thị Lan").phone("0922222222").email("nguyenthilan@gmail.com").citizenId("079201001235").gender(false).dateOfBirth(LocalDate.of(1992,8,20)).createdAt(LocalDate.of(2025,12,2)).build());

            em.persist(Customer.builder().fullName("Phạm Đức Anh").phone("0933333333").email("phamducanh@gmail.com").citizenId("079201001236").gender(true).dateOfBirth(LocalDate.of(1988,3,10)).createdAt(LocalDate.of(2025,12,3)).build());

            em.persist(Customer.builder().fullName("Lê Thị Mai").phone("0944444444").email("lethimai@gmail.com").citizenId("079201001237").gender(false).dateOfBirth(LocalDate.of(1995,11,25)).createdAt(LocalDate.of(2025,12,4)).build());

            em.persist(Customer.builder().fullName("Vũ Văn Nam").phone("0955555555").email("vuvannam@gmail.com").citizenId("079201001238").gender(true).dateOfBirth(LocalDate.of(1987,7,18)).createdAt(LocalDate.of(2025,12,5)).build());

            em.persist(Customer.builder().fullName("Đặng Thị Hương").phone("0966666666").email("dangthihuong@gmail.com").citizenId("079201001239").gender(false).dateOfBirth(LocalDate.of(1993,2,14)).createdAt(LocalDate.of(2025,12,6)).build());

            em.persist(Customer.builder().fullName("Ngô Quang Huy").phone("0977777777").email("ngoquanghuy@gmail.com").citizenId("079201001240").gender(true).dateOfBirth(LocalDate.of(1991,9,30)).createdAt(LocalDate.of(2025,12,7)).build());

            em.persist(Customer.builder().fullName("Bùi Thị Thảo").phone("0988888888").email("buithithao@gmail.com").citizenId("079201001241").gender(false).dateOfBirth(LocalDate.of(1994,6,22)).createdAt(LocalDate.of(2025,12,8)).build());

            em.persist(Customer.builder().fullName("Đinh Văn Tùng").phone("0999999999").email("dinhvantung@gmail.com").citizenId("079201001242").gender(true).dateOfBirth(LocalDate.of(1989,12,5)).createdAt(LocalDate.of(2025,12,9)).build());

            em.persist(Customer.builder().fullName("Hoàng Thị Nga").phone("0910101010").email("hoangthinga@gmail.com").citizenId("079201001243").gender(false).dateOfBirth(LocalDate.of(1996,4,17)).createdAt(LocalDate.of(2025,12,10)).build());

            em.persist(Customer.builder().fullName("Trịnh Văn Kiên").phone("0920202020").email("trinhvankien@gmail.com").citizenId("079201001244").gender(true).dateOfBirth(LocalDate.of(1993,8,12)).createdAt(LocalDate.of(2025,12,11)).build());

            em.persist(Customer.builder().fullName("Võ Thị Yến").phone("0930303030").email("vothiyen@gmail.com").citizenId("079201001245").gender(false).dateOfBirth(LocalDate.of(1991,3,28)).createdAt(LocalDate.of(2025,12,12)).build());

            em.persist(Customer.builder().fullName("Dương Văn Sơn").phone("0940404040").email("duongvanson@gmail.com").citizenId("079201001246").gender(true).dateOfBirth(LocalDate.of(1989,11,15)).createdAt(LocalDate.of(2025,12,13)).build());

            em.persist(Customer.builder().fullName("Mai Văn Đức").phone("0960606060").email("phanthithu@gmail.com").citizenId("079201001248").gender(false).dateOfBirth(LocalDate.of(1994,7,22)).createdAt(LocalDate.of(2025,12,14)).build());

        });
    }
}
