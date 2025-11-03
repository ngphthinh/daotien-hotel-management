
use master
DROP DATABASE IF EXISTS daotien_hotel;
go

create database daotien_hotel
    go

use daotien_hotel
    go


create table Role(
                     roleId varchar(30) primary key,
                     roleName nvarchar(100) not null,
                     createdAt Date default getDate()
)
    go

create table Account(
                        accountId bigint identity primary key,
                        username varchar(50) not null unique,
                        password varchar(255) not null,
                        roleId varchar(30) not null,
                        createdAt Date default getDate()
                            foreign key (roleId) references Role(roleId)
)
    go


create table Employee(
                         employeeId bigint identity primary key,
                         fullName nvarchar(255) not null,
                         phone varchar(10) not null,
                         email varchar(255) not null,
                         hireDate Date not null,
                         citizenId varchar(12) not null,
                         gender bit not null,
                         accountId bigint not null,
                         avt VARCHAR(MAX),
	createdAt Date default getDate(),
	foreign key (accountId) references Account(accountId)
)
    go

create table Shift(
                      shiftId bigint identity primary key,
                      name varchar(50) not null,
                      startTime varchar(10) not null,
                      endTime varchar(10) not null,
                      createdAt Date default getDate()
)
    go

create table EmployeeShift(
                              employeeShiftId bigint identity primary key,
                              employeeId bigint not null,
                              shiftId bigint not null,
                              shiftDate Date,
                              createdAt Date default getDate()
                                  foreign key (employeeId) references Employee(employeeId),
                              foreign key (shiftId) references Shift(shiftId)
)
    go


create table DenominationDetail (
                                    denominationDetailId bigint identity primary key,
                                    denomination nvarchar(30) not null,
                                    quantity int not null check (quantity > 0),
                                    employeeShiftId bigint not null,
                                    createdAt Date default getDate()
                                        foreign key (employeeShiftId) references EmployeeShift(employeeShiftId)
)
    go

CREATE TABLE ShiftClose (
                            shiftCloseId BIGINT IDENTITY PRIMARY KEY,
                            employeeShiftId BIGINT NOT NULL,
                            totalRevenue DECIMAL(18,2) NULL,
                            cashInDrawer DECIMAL(18,2) NULL,
                            difference DECIMAL(18,2) NOT NULL,
                            note NVARCHAR(255) NULL,
                            createdAt DATETIME DEFAULT GETDATE(),
                            FOREIGN KEY (employeeShiftId) REFERENCES EmployeeShift(employeeShiftId)
);
GO

create table RoomType (
                          roomTypeId varchar(20) primary key,
                          name nvarchar(50) not null,
                          createdAt Date default getDate()
)
    go

create table Room (
                      roomId bigint identity primary key,
                      roomNumber nvarchar(30) not null,
                      roomTypeId varchar(20) not null,
                      roomStatus varchar(20) not null,
                      createdAt Date default getDate(),
                      foreign key (roomTypeId) references RoomType(roomTypeId)
)
    go

create table Amenity (
                         amenityId bigint identity primary key,
                         nameAmenity nvarchar(255) not null,
                         price DECIMAL(18, 2) NOT NULL,
                         createdAt Date default getDate()
)
    go



create table Promotion (
                           promotionId bigint identity primary key,
                           promotionName nvarchar(255) not null,
                           description nvarchar(1000) not null,
                           discountPercent float,
                           discountPrice DECIMAL(18, 2) NOT NULL,
                           startDate date not null,
                           endDate date not null,
                           createdAt Date default getDate()
)
    go

create table Customer(
                         customerId bigint identity primary key,
                         fullName nvarchar(255) not null,
                         phone varchar(10) not null,
                         email varchar(255) not null,
                         citizenId varchar(12) not null,
                         gender bit not null,
                         dateOfBirth date not null,
                         createdAt Date default getDate(),
)
    go

create table Feedback (
                          feedbackId bigint identity primary key,
                          content nvarchar(1000) not null,
                          customerId bigint not null,
                          rating int not null,
                          createdAt Date default getDate(),
                          foreign key (customerId) references Customer(customerId)
)
    go

create table OrderType (
                           orderTypeId bigint identity primary key,
                           name nvarchar(255) not null,
                           createdAt Date default getDate(),
)

create table Orders (
                        orderId bigint identity primary key,
                        orderDate datetime not null,
                        totalAmount Decimal(18,2) not null default 0,
                        employeeId bigint not null,
                        orderTypeId bigint not null,
                        customerId bigint not null,
                        promotionId bigint,
                        deposit Decimal(18,2) not null,
                        paymentType nvarchar(20),
                        createdAt Date default getDate(),
                        foreign key (employeeId) references Employee(employeeId),
                        foreign key (orderTypeId) references OrderType(orderTypeId),
                        foreign key (promotionId) references Promotion(promotionId),
                        foreign key (customerId) references Customer(customerId),
)
    go

create table Booking (
                         bookingId bigint identity primary key,
                         checkInDate datetime not null,
                         checkOutDate datetime not null,
                         employeeId bigint not null,
                         orderId bigint not null,
                         roomId bigint not null,
                         bookingType varchar(15),
                         totalPrice Decimal (18,2) not null,
                         createdAt Date default getDate(),
                         foreign key (roomId) references Room(roomId),
                         foreign key (employeeId) references Employee(employeeId),
                         foreign key (orderId) references Orders(orderId)
)
    go


create table OrderDetail(
                            unitPrice Decimal (18,2) not null,
                            quantity int not null check (quantity >0),
                            amenityId bigint not null,
                            orderId bigint not null,
                            createdAt Date default getDate(),
                            primary key (orderId, amenityId),
                            foreign key (orderId) references Orders(orderId),
                            foreign key (amenityId) references Amenity(amenityId)
)
    go

create table Surcharge (
                           surchargeId bigint identity primary key,
                           name nvarchar(255) not null,
                           price DECIMAL(18, 2) NOT NULL,
                           createdAt Date default getDate()
)
    go

create table SurchargeDetail (
                                 orderId bigint not null,
                                 surchargerId bigint not null,
                                 quantity int check (quantity > 0),
                                 createdAt DATE default getDate(),
                                 primary key (orderId,surchargerId),
                                 foreign key (orderId) references Orders(orderId),
                                 foreign key (surchargerId) references Surcharge(surchargeId)
)
    go

INSERT INTO Role (roleId, roleName, createdAt)
VALUES
    ('MANAGER', N'Nhân viên quản lý', '2025-10-27'),
    ('RECEPTIONIST', N'Nhân viên lễ tân', '2025-10-27');

INSERT INTO Shift (name, startTime, endTime, createdAt)
VALUES
    ('Ca 01', '00:00:00', '06:00:00', GETDATE()),
    ('Ca 02', '06:00:00', '12:00:00', GETDATE()),
    ('Ca 03', '12:00:00', '18:00:00', GETDATE()),
    ('Ca 04', '18:00:00', '23:59:59', GETDATE());

INSERT INTO RoomType(roomTypeId,name,createdAt)
VALUES
    ('DOUBLE',N'Phòng đôi',GETDATE()),
    ('SINGLE',N'Phòng đơn',GETDATE());

INSERT INTO OrderType(name,createdAt)
VALUES
(N'Đã hoàn thành',GETDATE()),
(N'Đang xử lý',GETDATE()),
(N'Đặt trước',GETDATE());


INSERT INTO Promotion (promotionName, description, discountPercent, discountPrice, startDate, endDate)
VALUES (N'Khuyến mãi Mùa Hè Rực Rỡ', N'Giảm 15% cho các đơn đặt phòng trong tháng 7', 15.00, 500000.00, '2025-07-01',
        '2025-07-31'),
       (N'Ưu đãi Quốc Khánh 2/9', N'Giảm 20% cho tất cả khách đặt phòng trong dịp lễ Quốc Khánh', 20.00, 0.00,
        '2025-08-30', '2025-09-03'),
       (N'Khuyến mãi Cuối Tuần Vui Vẻ', N'Giảm 10% cho các đơn đặt phòng vào thứ 6, 7 và chủ nhật', 10.00, 300000.00,
        '2025-01-01', '2025-12-31'),
       (N'Giảm giá Ngày Lễ Tình Nhân', N'Giảm 25% cho các cặp đôi đặt phòng vào ngày Valentine', 25.00, 400000.00,
        '2025-02-10', '2025-02-15'),
       (N'Ưu đãi Mùa Thu Lãng Mạn', N'Giảm 12% cho các đơn đặt phòng trong tháng 10', 12.00, 350000.00, '2025-10-01',
        '2025-10-31'),
       (N'Khuyến mãi Hè Cho Gia Đình', N'Giảm 200.000đ cho hóa đơn từ 1 triệu đồng trở lên', 0.00, 1000000.00,
        '2025-06-01', '2025-08-31'),
       (N'Giảm giá Ngày Quốc Tế Phụ Nữ', N'Giảm 30% cho tất cả đơn đặt phòng của khách nữ', 30.00, 0.00, '2025-03-06',
        '2025-03-10'),
       (N'Ưu đãi Sinh Viên', N'Giảm 15% cho khách hàng xuất trình thẻ sinh viên khi đặt phòng', 15.00, 200000.00,
        '2025-01-01', '2025-12-31'),
       (N'Khuyến mãi Mừng Đại Lễ 30/4', N'Giảm 20% hoặc 100.000đ cho hóa đơn từ 800.000đ trở lên', 20.00, 800000.00,
        '2025-04-25', '2025-05-02'),
       (N'Giảm giá Đặt Phòng Sớm', N'Giảm 10% cho khách đặt phòng trước ít nhất 7 ngày', 10.00, 0.00, '2025-01-01',
        '2025-12-31');

Insert into Room (roomNumber, roomTypeId, roomStatus)
values ('101', 'SINgLE', 'AVAILABLE'),
       ('102', 'SINgLE', 'AVAILABLE'),
       ('103', 'SINgLE', 'AVAILABLE'),
       ('104', 'SINgLE', 'AVAILABLE'),
       ('105', 'SINgLE', 'AVAILABLE'),
       ('106', 'SINgLE', 'AVAILABLE'),
       ('107', 'SINgLE', 'AVAILABLE'),
       ('108', 'SINgLE', 'AVAILABLE'),
       ('109', 'SINgLE', 'AVAILABLE'),
       ('110', 'SINgLE', 'AVAILABLE'),
       ('111', 'SINgLE', 'AVAILABLE'),
       ('112', 'SINgLE', 'AVAILABLE'),
       ('113', 'SINgLE', 'AVAILABLE'),
       ('114', 'SINgLE', 'AVAILABLE'),
       ('115', 'SINgLE', 'AVAILABLE'),
       ('116', 'SINgLE', 'AVAILABLE'),
       ('117', 'SINgLE', 'AVAILABLE'),
       ('118', 'SINgLE', 'AVAILABLE'),
       ('119', 'SINgLE', 'AVAILABLE'),
       ('120', 'SINgLE', 'AVAILABLE'),
       ('201', 'DOUBLE', 'AVAILABLE'),
       ('202', 'DOUBLE', 'AVAILABLE'),
       ('203', 'DOUBLE', 'AVAILABLE'),
       ('204', 'DOUBLE', 'AVAILABLE'),
       ('205', 'DOUBLE', 'AVAILABLE'),
       ('206', 'DOUBLE', 'AVAILABLE'),
       ('207', 'DOUBLE', 'AVAILABLE'),
       ('208', 'DOUBLE', 'AVAILABLE'),
       ('209', 'DOUBLE', 'AVAILABLE'),
       ('210', 'DOUBLE', 'AVAILABLE');

-- dung tieng viet
Insert into Amenity (nameAmenity, price) values
                                             (N'Bữa sáng miễn phí', 50000.00),
                                             (N'Hồ bơi ngoài trời',  80000.00),
                                             (N'Spa và phòng tập gym',  100000.00),
                                             (N'Dịch vụ đưa đón sân bay',  100000.00),
                                             (N'Giặt là và ủi đồ', 50.000),
                                             (N'Cho thuê xe đạp',  30.000),
                                             (N'Phòng họp và hội nghị',  200.000),
                                             (N'Dịch vụ ăn uống tại phòng',  200000.00),
                                             (N'Khu vui chơi trẻ em',  20000.00);

select * from Amenity

INSERT INTO Customer (fullName, phone, email, citizenId, gender, dateOfBirth)
VALUES
    (N'Nguyễn Văn An', '0901234567', 'an.nguyen@example.com', '012345678901', 0, '1995-03-12'),
    (N'Lê Thị Bích', '0912345678', 'bich.le@example.com', '023456789012', 1, '1998-07-24'),
    (N'Trần Quốc Huy', '0938765432', 'huy.tran@example.com', '034567890123', 0, '1990-11-05'),
    (N'Phạm Thu Trang', '0976543210', 'trang.pham@example.com', '045678901234', 1, '2000-01-16'),
    (N'Đỗ Minh Khang', '0981122334', 'khang.do@example.com', '056789012345', 0, '1993-09-20'),
    (N'Hoàng Mỹ Dung', '0965566778', 'dung.hoang@example.com', '067890123456', 1, '1997-12-30'),
    (N'Vũ Đức Long', '0944455667', 'long.vu@example.com', '078901234567', 0, '1989-05-18'),
    (N'Ngô Thị Hạnh', '0923344556', 'hanh.ngo@example.com', '089012345678', 1, '1996-02-10'),
    (N'Bùi Anh Tuấn', '0911223344', 'tuan.bui@example.com', '090123456789', 0, '1992-10-25'),
    (N'Phan Thảo Vy', '0909988776', 'vy.phan@example.com', '011223344556', 1, '2001-06-07');


insert into Surcharge(name, price) values (N'Phụ thu ngày lễ', 50000.00),
                                          (N'Trả phòng trễ', 30000.00),
                                          (N'NDọn phòng thêm', 40000.00);

