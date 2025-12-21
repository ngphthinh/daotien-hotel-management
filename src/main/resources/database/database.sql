
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
	isDeleted bit default 0,
	foreign key (accountId) references Account(accountId),
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

CREATE TABLE ShiftClose (
                            shiftCloseId BIGINT IDENTITY PRIMARY KEY,
                            employeeShiftId BIGINT NOT NULL,
                            totalRevenue DECIMAL(18,2) NULL,
                            cashInDrawer DECIMAL(18,2) NULL,
                            difference DECIMAL(18,2) NOT NULL,
                            note NVARCHAR(255) NULL,
                            managerId BIGINT NOT NULL,
                            createdAt DATETIME DEFAULT GETDATE(),
                            FOREIGN KEY (employeeShiftId) REFERENCES EmployeeShift(employeeShiftId),
                            FOREIGN KEY (managerId) REFERENCES Employee(employeeId)
);
GO

create table DenominationDetail (
                                    denominationDetailId bigint identity primary key,
                                    denomination nvarchar(30) not null,
                                    quantity int not null check (quantity > 0),
                                    employeeShiftId bigint not null,
                                    createdAt Date default getDate()
                                        foreign key (employeeShiftId) references EmployeeShift(employeeShiftId)
)
    go


create table RoomType (
                          roomTypeId varchar(20) primary key,
                          name nvarchar(50) not null,
                          createdAt Date default getDate(),
                          hourlyRate DECIMAL(18, 2),
                          dailyRate DECIMAL(18, 2),
                          overnightRate DECIMAL(18, 2),
                          additionalHourRate DECIMAL(18, 2)
)
    go

create table Room (
                      roomId bigint identity primary key,
                      roomNumber nvarchar(30) not null,
                      roomTypeId varchar(20) not null,
                      roomStatus varchar(20) not null,
                      createdAt Date default getDate(),
                      isDeleted bit default 0,
                      foreign key (roomTypeId) references RoomType(roomTypeId)
)
    go

create table Amenity (
                         amenityId bigint identity primary key,
                         nameAmenity nvarchar(255) not null,
                         price DECIMAL(18, 2) NOT NULL,
                         isDeleted bit default 0,
                         createdAt Date default getDate()
)
    go



create table Promotion (
                           promotionId bigint identity primary key,
                           promotionName nvarchar(255) not null,
                           description nvarchar(1000) not null,
                           discountPercent float,
                           minOrderAmount DECIMAL(18, 2) NOT NULL,
                           startDate date not null,
                           endDate date not null,
                           isDeleted bit default 0,
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
                         isDeleted bit default 0,
                         createdAt Date default getDate(),
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
                        paymentDate date,
                        foreign key (employeeId) references Employee(employeeId),
                        foreign key (orderTypeId) references OrderType(orderTypeId),
                        foreign key (promotionId) references Promotion(promotionId),
                        foreign key (customerId) references Customer(customerId),
)
    go

create table Booking (
                         bookingId bigint identity primary key,
                         orderId bigint not null,
                         roomId bigint not null,
                         createdAt Date default getDate(),
                         checkInDate datetime not null,
                         checkOutDate datetime not null,
                         bookingType varchar(15),
                         foreign key (roomId) references Room(roomId),
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
                           isDeleted bit default 0,
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

INSERT INTO RoomType(roomTypeId,name,createdAt,hourlyRate,dailyRate,overnightRate,additionalHourRate)
VALUES
    ('DOUBLE',N'Phòng đôi',GETDATE(),80000,500000,300000,30000),
    ('SINGLE',N'Phòng đơn',GETDATE(),50000,300000,250000,20000);

INSERT INTO OrderType(name,createdAt)
VALUES
    (N'Đã hoàn thành',GETDATE()),
    (N'Đang xử lý',GETDATE()),
    (N'Đặt trước',GETDATE()),
    (N'Đã hủy',GETDATE());


INSERT INTO Surcharge(name,price,createdAt) VALUES (N'Phụ thu ngày lễ',50000,getDate())
    INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu cuối tuần', 30000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu khách thêm', 70000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu nhận phòng sớm', 50000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu trả phòng trễ', 60000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu dọn phòng đặc biệt', 40000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu mất chìa khóa', 150000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu thêm chăn gối', 20000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu nâng cấp phòng', 100000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu sử dụng hồ bơi đêm', 50000, GETDATE());
INSERT INTO Surcharge(name, price, createdAt) VALUES (N'Phụ thu giữ xe', 10000, GETDATE());

INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Nước suối', 10000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Khăn lạnh', 5000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Bia lon', 20000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Nước ngọt', 15000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Mì ly', 12000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Bàn chải đánh răng', 8000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Kem đánh răng', 7000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Dầu gội', 10000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Sữa tắm', 10000, GETDATE());
INSERT INTO Amenity(nameAmenity, price, createdAt) VALUES (N'Khăn tắm', 15000, GETDATE());

INSERT INTO Room (roomNumber, roomTypeId, roomStatus, createdAt) VALUES
-- DOUBLE 101–120
(201, 'DOUBLE', 'AVAILABLE', GETDATE()),
(202, 'DOUBLE', 'AVAILABLE', GETDATE()),
(203, 'DOUBLE', 'AVAILABLE', GETDATE()),
(204, 'DOUBLE', 'AVAILABLE', GETDATE()),
(205, 'DOUBLE', 'AVAILABLE', GETDATE()),
(206, 'DOUBLE', 'AVAILABLE', GETDATE()),
(207, 'DOUBLE', 'AVAILABLE', GETDATE()),
(208, 'DOUBLE', 'AVAILABLE', GETDATE()),
(209, 'DOUBLE', 'AVAILABLE', GETDATE()),
(210, 'DOUBLE', 'AVAILABLE', GETDATE()),
(211, 'DOUBLE', 'AVAILABLE', GETDATE()),
(212, 'DOUBLE', 'AVAILABLE', GETDATE()),
(213, 'DOUBLE', 'AVAILABLE', GETDATE()),
(214, 'DOUBLE', 'AVAILABLE', GETDATE()),
(215, 'DOUBLE', 'AVAILABLE', GETDATE()),
(216, 'DOUBLE', 'AVAILABLE', GETDATE()),
(217, 'DOUBLE', 'AVAILABLE', GETDATE()),
(218, 'DOUBLE', 'AVAILABLE', GETDATE()),
(219, 'DOUBLE', 'AVAILABLE', GETDATE()),
(220, 'DOUBLE', 'AVAILABLE', GETDATE()),

-- SINGLE 201–220
(101, 'SINGLE', 'AVAILABLE', GETDATE()),
(102, 'SINGLE', 'AVAILABLE', GETDATE()),
(103, 'SINGLE', 'AVAILABLE', GETDATE()),
(104, 'SINGLE', 'AVAILABLE', GETDATE()),
(105, 'SINGLE', 'AVAILABLE', GETDATE()),
(106, 'SINGLE', 'AVAILABLE', GETDATE()),
(107, 'SINGLE', 'AVAILABLE', GETDATE()),
(108, 'SINGLE', 'AVAILABLE', GETDATE()),
(109, 'SINGLE', 'AVAILABLE', GETDATE()),
(110, 'SINGLE', 'AVAILABLE', GETDATE()),
(111, 'SINGLE', 'AVAILABLE', GETDATE()),
(112, 'SINGLE', 'AVAILABLE', GETDATE()),
(113, 'SINGLE', 'AVAILABLE', GETDATE()),
(114, 'SINGLE', 'AVAILABLE', GETDATE()),
(115, 'SINGLE', 'AVAILABLE', GETDATE()),
(116, 'SINGLE', 'AVAILABLE', GETDATE()),
(117, 'SINGLE', 'AVAILABLE', GETDATE()),
(118, 'SINGLE', 'AVAILABLE', GETDATE()),
(119, 'SINGLE', 'AVAILABLE', GETDATE()),
(120, 'SINGLE', 'AVAILABLE', GETDATE());

INSERT INTO Promotion (promotionName, description, discountPercent, minOrderAmount, startDate, endDate, createdAt) VALUES
                                                                                                                       (N'Khuyến mãi mùa đông', N'Giảm giá 10% cho đơn hàng từ 500,000đ', 10, 500000, '2025-12-01', '2025-12-31', '2025-11-20'),
                                                                                                                       (N'Khuyến mãi cuối tuần', N'Giảm giá 15% cho đơn hàng từ 1,000,000đ vào cuối tuần', 15, 1000000, '2025-12-01', '2025-12-31', '2025-11-25'),
                                                                                                                       (N'Khuyến mãi khách VIP', N'Giảm giá 20% cho đơn hàng từ 2,000,000đ', 20, 2000000, '2025-11-01', '2025-12-31', '2025-10-30');


-- 1. TẠO TÀI KHOẢN VÀ NHÂN VIÊN

-- 2. PHÂN CA LÀM VIỆC CHO NHÂN VIÊN (2 NHÂN VIÊN/CA)

-- Lịch làm việc từ 15/12 đến 21/12/2024
-- Mỗi ca có 2 nhân viên
INSERT INTO EmployeeShift (employeeId, shiftId, shiftDate, createdAt) VALUES
-- Ngày 15/12
(3, 1, '2025-12-15', '2025-12-15'),
(4, 1, '2025-12-15', '2025-12-15'),
(5, 2, '2025-12-15', '2025-12-15'),
(6, 2, '2025-12-15', '2025-12-15'),
(7, 3, '2025-12-15', '2025-12-15'),
(8, 3, '2025-12-15', '2025-12-15'),
(3, 4, '2025-12-15', '2025-12-15'),
(4, 4, '2025-12-15', '2025-12-15'),

-- Ngày 16/12
(5, 1, '2025-12-16', '2025-12-16'),
(6, 1, '2025-12-16', '2025-12-16'),
(7, 2, '2025-12-16', '2025-12-16'),
(8, 2, '2025-12-16', '2025-12-16'),
(3, 3, '2025-12-16', '2025-12-16'),
(4, 3, '2025-12-16', '2025-12-16'),
(5, 4, '2025-12-16', '2025-12-16'),
(6, 4, '2025-12-16', '2025-12-16'),

-- Ngày 17/12
(7, 1, '2025-12-17', '2025-12-17'),
(8, 1, '2025-12-17', '2025-12-17'),
(3, 2, '2025-12-17', '2025-12-17'),
(4, 2, '2025-12-17', '2025-12-17'),
(5, 3, '2025-12-17', '2025-12-17'),
(6, 3, '2025-12-17', '2025-12-17'),
(7, 4, '2025-12-17', '2025-12-17'),
(8, 4, '2025-12-17', '2025-12-17'),

-- Ngày 18/12
(3, 1, '2025-12-18', '2025-12-18'),
(4, 1, '2025-12-18', '2025-12-18'),
(5, 2, '2025-12-18', '2025-12-18'),
(6, 2, '2025-12-18', '2025-12-18'),
(7, 3, '2025-12-18', '2025-12-18'),
(8, 3, '2025-12-18', '2025-12-18'),
(3, 4, '2025-12-18', '2025-12-18'),
(4, 4, '2025-12-18', '2025-12-18'),

-- Ngày 19/12
(5, 1, '2025-12-19', '2025-12-19'),
(6, 1, '2025-12-19', '2025-12-19'),
(7, 2, '2025-12-19', '2025-12-19'),
(8, 2, '2025-12-19', '2025-12-19'),
(3, 3, '2025-12-19', '2025-12-19'),
(4, 3, '2025-12-19', '2025-12-19'),
(5, 4, '2025-12-19', '2025-12-19'),
(6, 4, '2025-12-19', '2025-12-19'),

-- Ngày 20/12
(7, 1, '2025-12-20', '2025-12-20'),
(8, 1, '2025-12-20', '2025-12-20'),
(3, 2, '2025-12-20', '2025-12-20'),
(4, 2, '2025-12-20', '2025-12-20'),
(5, 3, '2025-12-20', '2025-12-20'),
(6, 3, '2025-12-20', '2025-12-20'),
(7, 4, '2025-12-20', '2025-12-20'),
(8, 4, '2025-12-20', '2025-12-20'),

-- Ngày 21/12
(3, 1, '2025-12-21', '2025-12-21'),
(4, 1, '2025-12-21', '2025-12-21'),
(5, 2, '2025-12-21', '2025-12-21'),
(6, 2, '2025-12-21', '2025-12-21'),
(7, 3, '2025-12-21', '2025-12-21'),
(8, 3, '2025-12-21', '2025-12-21'),
(3, 4, '2025-12-21', '2025-12-21'),
(4, 4, '2025-12-21', '2025-12-21'),

-- Ngày 22/12
(5, 1, '2025-12-22', '2025-12-22'),
(6, 1, '2025-12-22', '2025-12-22'),
(7, 2, '2025-12-22', '2025-12-22'),
(8, 2, '2025-12-22', '2025-12-22'),
(3, 3, '2025-12-22', '2025-12-22'),
(4, 3, '2025-12-22', '2025-12-22'),
(5, 4, '2025-12-22', '2025-12-22'),
(6, 4, '2025-12-22', '2025-12-22'),

-- Ngày 23/12
(7, 1, '2025-12-23', '2025-12-23'),
(8, 1, '2025-12-23', '2025-12-23'),
(3, 2, '2025-12-23', '2025-12-23'),
(4, 2, '2025-12-23', '2025-12-23'),
(5, 3, '2025-12-23', '2025-12-23'),
(6, 3, '2025-12-23', '2025-12-23'),
(7, 4, '2025-12-23', '2025-12-23'),
(8, 4, '2025-12-23', '2025-12-23'),

-- Ngày 24/12
(3, 1, '2025-12-24', '2025-12-24'),
(4, 1, '2025-12-24', '2025-12-24'),
(5, 2, '2025-12-24', '2025-12-24'),
(6, 2, '2025-12-24', '2025-12-24'),
(7, 3, '2025-12-24', '2025-12-24'),
(8, 3, '2025-12-24', '2025-12-24'),
(3, 4, '2025-12-24', '2025-12-24'),
(4, 4, '2025-12-24', '2025-12-24'),

-- Ngày 25/12
(5, 1, '2025-12-25', '2025-12-25'),
(6, 1, '2025-12-25', '2025-12-25'),
(7, 2, '2025-12-25', '2025-12-25'),
(8, 2, '2025-12-25', '2025-12-25'),
(3, 3, '2025-12-25', '2025-12-25'),
(4, 3, '2025-12-25', '2025-12-25'),
(5, 4, '2025-12-25', '2025-12-25'),
(6, 4, '2025-12-25', '2025-12-25'),

-- Ngày 26/12
(7, 1, '2025-12-26', '2025-12-26'),
(8, 1, '2025-12-26', '2025-12-26'),
(3, 2, '2025-12-26', '2025-12-26'),
(4, 2, '2025-12-26', '2025-12-26'),
(5, 3, '2025-12-26', '2025-12-26'),
(6, 3, '2025-12-26', '2025-12-26'),
(7, 4, '2025-12-26', '2025-12-26'),
(8, 4, '2025-12-26', '2025-12-26'),

-- Ngày 27/12
(3, 1, '2025-12-27', '2025-12-27'),
(4, 1, '2025-12-27', '2025-12-27'),
(5, 2, '2025-12-27', '2025-12-27'),
(6, 2, '2025-12-27', '2025-12-27'),
(7, 3, '2025-12-27', '2025-12-27'),
(8, 3, '2025-12-27', '2025-12-27'),
(3, 4, '2025-12-27', '2025-12-27'),
(4, 4, '2025-12-27', '2025-12-27'),

-- Ngày 28/12
(5, 1, '2025-12-28', '2025-12-28'),
(6, 1, '2025-12-28', '2025-12-28'),
(7, 2, '2025-12-28', '2025-12-28'),
(8, 2, '2025-12-28', '2025-12-28'),
(3, 3, '2025-12-28', '2025-12-28'),
(4, 3, '2025-12-28', '2025-12-28'),
(5, 4, '2025-12-28', '2025-12-28'),
(6, 4, '2025-12-28', '2025-12-28'),

-- Ngày 29/12
(7, 1, '2025-12-29', '2025-12-29'),
(8, 1, '2025-12-29', '2025-12-29'),
(3, 2, '2025-12-29', '2025-12-29'),
(4, 2, '2025-12-29', '2025-12-29'),
(5, 3, '2025-12-29', '2025-12-29'),
(6, 3, '2025-12-29', '2025-12-29'),
(7, 4, '2025-12-29', '2025-12-29'),
(8, 4, '2025-12-29', '2025-12-29'),

-- Ngày 30/12
(3, 1, '2025-12-30', '2025-12-30'),
(4, 1, '2025-12-30', '2025-12-30'),
(5, 2, '2025-12-30', '2025-12-30'),
(6, 2, '2025-12-30', '2025-12-30'),
(7, 3, '2025-12-30', '2025-12-30'),
(8, 3, '2025-12-30', '2025-12-30'),
(3, 4, '2025-12-30', '2025-12-30'),
(4, 4, '2025-12-30', '2025-12-30');

-- 3. KHÁCH HÀNG

INSERT INTO Customer (fullName, phone, email, citizenId, gender, dateOfBirth, createdAt) VALUES
                                                                                             (N'Trần Minh Hoàng', '0911111111', 'tranminhhoang@gmail.com', '079201001234', 1, '1990-05-15', '2025-12-01'),
                                                                                             (N'Nguyễn Thị Lan', '0922222222', 'nguyenthilan@gmail.com', '079201001235', 0, '1992-08-20', '2025-12-02'),
                                                                                             (N'Phạm Đức Anh', '0933333333', 'phamducanh@gmail.com', '079201001236', 1, '1988-03-10', '2025-12-03'),
                                                                                             (N'Lê Thị Mai', '0944444444', 'lethimai@gmail.com', '079201001237', 0, '1995-11-25', '2025-12-04'),
                                                                                             (N'Vũ Văn Nam', '0955555555', 'vuvannam@gmail.com', '079201001238', 1, '1987-07-18', '2025-12-05'),
                                                                                             (N'Đặng Thị Hương', '0966666666', 'dangthihuong@gmail.com', '079201001239', 0, '1993-02-14', '2025-12-06'),
                                                                                             (N'Ngô Quang Huy', '0977777777', 'ngoquanghuy@gmail.com', '079201001240', 1, '1991-09-30', '2025-12-07'),
                                                                                             (N'Bùi Thị Thảo', '0988888888', 'buithithao@gmail.com', '079201001241', 0, '1994-06-22', '2025-12-08'),
                                                                                             (N'Đinh Văn Tùng', '0999999999', 'dinhvantung@gmail.com', '079201001242', 1, '1989-12-05', '2025-12-09'),
                                                                                             (N'Hoàng Thị Nga', '0910101010', 'hoangthinga@gmail.com', '079201001243', 0, '1996-04-17', '2025-12-10'),
                                                                                             (N'Trịnh Văn Kiên', '0920202020', 'trinhvankien@gmail.com', '079201001244', 1, '1993-08-12', '2025-12-11'),
                                                                                             (N'Võ Thị Yến', '0930303030', 'vothiyen@gmail.com', '079201001245', 0, '1991-03-28', '2025-12-12'),
                                                                                             (N'Dương Văn Sơn', '0940404040', 'duongvanson@gmail.com', '079201001246', 1, '1989-11-15', '2025-12-13'),
                                                                                             (N'Phan Thị Thu', '0950505050', 'phanthithu@gmail.com', '079201001247', 0, '1994-07-22', '2025-12-14'),
                                                                                             (N'Mai Văn Đức', '0960606060', 'maivanduc@gmail.com', '079201001248', 1, '1992-01-30', '2025-12-15');

-- 4. ĐƠN HÀNG ĐÃ HOÀN THÀNH (orderTypeId = 1)

-- Order 1: Phòng đơn qua đêm (đã checkout)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-15 14:00:00', 285000, 3, 1, 1, NULL, 100000, 'CASH', '2025-12-15', '2025-12-16');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (1, 21, '2025-12-15', '2025-12-15 14:00:00', '2025-12-16 10:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
                                                                                 (10000, 2, 1, 1, '2025-12-15'), -- Nước suối x2
                                                                                 (15000, 1, 4, 1, '2025-12-15'); -- Nước ngọt x1

-- Order 2: Phòng đôi theo giờ (đã checkout)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-16 09:00:00', 240000, 4, 1, 2, NULL, 80000, 'E_WALLET', '2025-12-16', '2025-12-16');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (2, 1, '2025-12-16', '2025-12-16 09:00:00', '2025-12-16 12:00:00', 'HOURLY');

-- Order 3: Phòng đơn cả ngày với phụ thu cuối tuần (đã checkout)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-17 06:00:00', 408000, 5, 1, 3, NULL, 150000, 'CASH', '2025-12-17', '2025-12-17');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (3, 22, '2025-12-17', '2025-12-17 06:00:00', '2025-12-17 22:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
                                                                                 (12000, 2, 5, 3, '2025-12-17'), -- Mì ly x2
                                                                                 (20000, 2, 3, 3, '2025-12-17'); -- Bia lon x2

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt) VALUES
    (3, 2, 1, '2025-12-17'); -- Phụ thu cuối tuần

-- Order 4: Phòng đôi qua đêm (đã checkout)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-16 15:00:00', 355000, 6, 1, 4, NULL, 150000, 'CASH', '2025-12-16', '2025-12-17');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (4, 2, '2025-12-16', '2025-12-16 15:00:00', '2025-12-17 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
                                                                                 (10000, 2, 1, 4, '2025-12-16'), -- Nước suối x2
                                                                                 (15000, 1, 4, 4, '2025-12-16'), -- Nước ngọt x1
                                                                                 (12000, 2, 5, 4, '2025-12-16'); -- Mì ly x2

-- Order 5: Phòng đơn theo giờ (đã checkout)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-17 10:00:00', 165000, 3, 1, 5, NULL, 50000, 'E_WALLET', '2025-12-17', '2025-12-17');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (5, 23, '2025-12-17', '2025-12-17 10:00:00', '2025-12-17 13:00:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
                                                                                 (10000, 1, 1, 5, '2025-12-17'), -- Nước suối x1
                                                                                 (15000, 1, 4, 5, '2025-12-17'); -- Nước ngọt x1

-- 5. ĐƠN ĐANG XỬ LÝ (orderTypeId = 2) - CHECKED-IN

-- Order 6: Phòng đôi đang sử dụng (cả ngày)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 08:00:00', 0, 4, 2, 6, NULL, 200000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (6, 3, '2025-12-18', '2025-12-18 08:00:00', '2025-12-18 20:00:00', 'DAILY');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = 3;

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
                                                                                 (10000, 2, 1, 6, '2025-12-18'), -- Nước suối x2
                                                                                 (20000, 1, 3, 6, '2025-12-18'); -- Bia lon x1

-- Order 7: Phòng đơn đang sử dụng (theo giờ)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 10:00:00', 0, 5, 2, 7, NULL, 100000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (7, 24, '2025-12-18', '2025-12-18 10:00:00', '2025-12-18 14:00:00', 'HOURLY');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = 24;

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
    (10000, 1, 1, 7, '2025-12-18'); -- Nước suối x1

-- Order 8: Phòng đôi đang sử dụng (qua đêm)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 14:00:00', 0, 6, 2, 8, NULL, 150000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (8, 4, '2025-12-18', '2025-12-18 14:00:00', '2025-12-19 11:00:00', 'OVERNIGHT');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = 4;

-- 6. ĐƠN ĐẶT TRƯỚC (orderTypeId = 3)

-- Order 9: Đặt phòng đôi cho ngày mai
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 15:00:00', 0, 3, 3, 9, NULL, 250000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (9, 5, '2025-12-18', '2025-12-19 14:00:00', '2025-12-20 12:00:00', 'OVERNIGHT');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 5;

-- Order 10: Đặt phòng đơn cho cuối tuần
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 16:00:00', 0, 4, 3, 10, NULL, 150000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (10, 25, '2025-12-18', '2025-12-21 10:00:00', '2025-12-21 22:00:00', 'DAILY');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 25;

-- Order 11: Đặt phòng đôi với khuyến mãi
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 17:00:00', 0, 5, 3, 11, 1, 300000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (11, 6, '2025-12-18', '2025-12-22 14:00:00', '2025-12-23 12:00:00', 'OVERNIGHT');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 6;

-- Order 12: Đặt phòng đơn theo giờ
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 18:00:00', 0, 6, 3, 12, NULL, 50000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (12, 26, '2025-12-18', '2025-12-20 09:00:00', '2025-12-20 12:00:00', 'HOURLY');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 26;

-- Order 13: Đặt phòng đôi cả ngày
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-18 19:00:00', 0, 3, 3, 13, NULL, 200000, NULL, '2025-12-18', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (13, 7, '2025-12-18', '2025-12-23 08:00:00', '2025-12-23 20:00:00', 'DAILY');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 7;

-- 7. ĐƠN ĐÃ HỦY (orderTypeId = 4)

-- Order 14: Đơn bị hủy do khách không đến
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-16 10:00:00', 0, 3, 4, 14, NULL, 100000, NULL, '2025-12-16', NULL);

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (14, 27, '2025-12-16', '2025-12-17 14:00:00', '2025-12-18 12:00:00', 'OVERNIGHT');

-- Order 15: Đơn bị hủy do khách yêu cầu
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) VALUES
    ('2025-12-17 11:00:00', 0, 4, 4, 15, NULL, 150000, 'CASH', '2025-12-17', '2025-12-17');

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType) VALUES
    (15, 28, '2025-12-17', '2025-12-19 10:00:00', '2025-12-19 22:00:00', 'DAILY');

-- 8. PHẢN HỒI TỪ KHÁCH HÀNG


-- 9. CHI TIẾT MỆNH GIÁ TIỀN (DENOMINATION DETAIL)

-- Cho EmployeeShift ngày 15/12 - Ca 02 (Admin1 + Dung4)
-- EmployeeShift ID sẽ là 3 (Admin1, Ca 02, 15/12)
INSERT INTO DenominationDetail (denomination, quantity, employeeShiftId, createdAt) VALUES
                                                                                        (N'500,000đ', 2, 3, '2025-12-15'),
                                                                                        (N'200,000đ', 5, 3, '2025-12-15'),
                                                                                        (N'100,000đ', 8, 3, '2025-12-15'),
                                                                                        (N'50,000đ', 15, 3, '2025-12-15'),
                                                                                        (N'20,000đ', 25, 3, '2025-12-15'),
                                                                                        (N'10,000đ', 40, 3, '2025-12-15');

-- Cho EmployeeShift ngày 16/12 - Ca 02 (Admin1 + Dung4)
-- EmployeeShift ID sẽ là 11 (Admin1, Ca 02, 16/12)
INSERT INTO DenominationDetail (denomination, quantity, employeeShiftId, createdAt) VALUES
                                                                                        (N'500,000đ', 1, 11, '2025-12-16'),
                                                                                        (N'200,000đ', 4, 11, '2025-12-16'),
                                                                                        (N'100,000đ', 6, 11, '2025-12-16'),
                                                                                        (N'50,000đ', 12, 11, '2025-12-16'),
                                                                                        (N'20,000đ', 20, 11, '2025-12-16'),
                                                                                        (N'10,000đ', 30, 11, '2025-12-16');

-- 10. ĐÓNG CA (SHIFT CLOSE)

-- Đóng ca ngày 15/12 - Ca 02 (Admin1 + Dung4)
-- EmployeeShift ID = 3 (Admin1, Ca 02, 15/12)
INSERT INTO ShiftClose (employeeShiftId, totalRevenue, cashInDrawer, difference, note, managerId, createdAt) VALUES
    (3, 500000, 500000, 0, N'Ca làm việc bình thường, không có vấn đề gì', 1, '2025-12-15 12:00:00');

-- Đóng ca ngày 16/12 - Ca 02 (Admin1 + Dung4)
-- EmployeeShift ID = 11 (Admin1, Ca 02, 16/12)
INSERT INTO ShiftClose (employeeShiftId, totalRevenue, cashInDrawer, difference, note, managerId, createdAt) VALUES
    (11, 480000, 485000, 5000, N'Thừa 5,000đ - đã ghi nhận', 1, '2025-12-16 12:00:00');

-- 11. CẬP NHẬT PHÒNG HỎNG

UPDATE Room SET roomStatus = 'OUT_OF_ORDER' WHERE roomId IN (29, 30);

GO

BEGIN TRANSACTION;



COMMIT TRANSACTION;