
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
	minOrderAmount DECIMAL(18, 2) NOT NULL,
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
(N'Đặt trước',GETDATE());


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
(101, 'DOUBLE', 'AVAILABLE', GETDATE()),
(102, 'DOUBLE', 'AVAILABLE', GETDATE()),
(103, 'DOUBLE', 'AVAILABLE', GETDATE()),
(104, 'DOUBLE', 'AVAILABLE', GETDATE()),
(105, 'DOUBLE', 'AVAILABLE', GETDATE()),
(106, 'DOUBLE', 'AVAILABLE', GETDATE()),
(107, 'DOUBLE', 'AVAILABLE', GETDATE()),
(108, 'DOUBLE', 'AVAILABLE', GETDATE()),
(109, 'DOUBLE', 'AVAILABLE', GETDATE()),
(110, 'DOUBLE', 'AVAILABLE', GETDATE()),
(111, 'DOUBLE', 'AVAILABLE', GETDATE()),
(112, 'DOUBLE', 'AVAILABLE', GETDATE()),
(113, 'DOUBLE', 'AVAILABLE', GETDATE()),
(114, 'DOUBLE', 'AVAILABLE', GETDATE()),
(115, 'DOUBLE', 'AVAILABLE', GETDATE()),
(116, 'DOUBLE', 'AVAILABLE', GETDATE()),
(117, 'DOUBLE', 'AVAILABLE', GETDATE()),
(118, 'DOUBLE', 'AVAILABLE', GETDATE()),
(119, 'DOUBLE', 'AVAILABLE', GETDATE()),
(120, 'DOUBLE', 'AVAILABLE', GETDATE()),

-- SINGLE 201–220
(201, 'SINGLE', 'AVAILABLE', GETDATE()),
(202, 'SINGLE', 'AVAILABLE', GETDATE()),
(203, 'SINGLE', 'AVAILABLE', GETDATE()),
(204, 'SINGLE', 'AVAILABLE', GETDATE()),
(205, 'SINGLE', 'AVAILABLE', GETDATE()),
(206, 'SINGLE', 'AVAILABLE', GETDATE()),
(207, 'SINGLE', 'AVAILABLE', GETDATE()),
(208, 'SINGLE', 'AVAILABLE', GETDATE()),
(209, 'SINGLE', 'AVAILABLE', GETDATE()),
(210, 'SINGLE', 'AVAILABLE', GETDATE()),
(211, 'SINGLE', 'AVAILABLE', GETDATE()),
(212, 'SINGLE', 'AVAILABLE', GETDATE()),
(213, 'SINGLE', 'AVAILABLE', GETDATE()),
(214, 'SINGLE', 'AVAILABLE', GETDATE()),
(215, 'SINGLE', 'AVAILABLE', GETDATE()),
(216, 'SINGLE', 'AVAILABLE', GETDATE()),
(217, 'SINGLE', 'AVAILABLE', GETDATE()),
(218, 'SINGLE', 'AVAILABLE', GETDATE()),
(219, 'SINGLE', 'AVAILABLE', GETDATE()),
(220, 'SINGLE', 'AVAILABLE', GETDATE());

-- Order 1: Đã hoàn thành - Thuê theo giờ (3 giờ) - Phòng 201 (SINGLE)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-05 09:00:00', 180000, 1, 1, 1, NULL, 100000, N'CASH', '2025-12-05', '2025-12-05');

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (1, 21, '2025-12-05 09:00:00', '2025-12-05 12:00:00', 'HOURLY', '2025-12-05');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 21;

-- Order 2: Đã hoàn thành - Thuê theo ngày (2 ngày)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-03 14:00:00', 600000, 2, 1, 2, NULL, 300000, N'CASH', '2025-12-03', '2025-12-05');

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (2, 1, '2025-12-03 14:00:00', '2025-12-05 12:00:00', 'DAILY', '2025-12-03');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 1;

-- Order 3: Đang xử lý - Thuê theo ngày (đang ở - từ 08/12 đến 12/12 = 4 ngày)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-08 14:00:00', 1200000, 1, 2, 3, NULL, 600000, N'CASH', '2025-12-08', NULL);

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (3, 2, '2025-12-08 14:00:00', '2025-12-12 12:00:00', 'DAILY', '2025-12-08');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = 2;

-- Order 4: Đang xử lý - Thuê 2 phòng đơn theo ngày (đang ở) - Phòng 202, 203 (SINGLE)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-07 10:00:00', 1800000, 2, 2, 4, NULL, 900000, N'CASH', '2025-12-07', NULL);

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES 
(4, 22, '2025-12-07 10:00:00', '2025-12-10 12:00:00', 'DAILY', '2025-12-07'),
(4, 23, '2025-12-07 10:00:00', '2025-12-10 12:00:00', 'DAILY', '2025-12-07');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId IN (22, 23);

-- Order 5: Đặt trước - Thuê qua đêm
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-08 16:00:00', 300000, 3, 3, 5, NULL, 150000, N'E_WALLET', '2025-12-08', NULL);

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (5, 3, '2025-12-15 18:00:00', '2025-12-16 12:00:00', 'OVERNIGHT', '2025-12-08');

-- Phòng đặt trước vẫn giữ AVAILABLE cho đến ngày check-in
UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 3;

-- Order 6: Đang xử lý - Thuê theo giờ (đang ở - 4 giờ) - Phòng 204 (SINGLE)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-08 08:00:00', 110000, 2, 2, 6, NULL, 80000, N'CASH', '2025-12-08', NULL);

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (6, 24, '2025-12-08 08:00:00', '2025-12-08 12:00:00', 'HOURLY', '2025-12-08');

UPDATE Room SET roomStatus = 'OCCUPIED' WHERE roomId = 24;

-- Order 7: Đã hoàn thành - Thuê phòng đôi qua đêm
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-06 19:00:00', 300000, 3, 1, 7, NULL, 200000, N'CASH', '2025-12-06', '2025-12-07');

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (7, 4, '2025-12-06 19:00:00', '2025-12-07 11:00:00', 'OVERNIGHT', '2025-12-06');

UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 4;

-- Order 8: Đặt trước - Thuê 1 phòng đôi theo ngày (3 ngày)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate) 
VALUES ('2025-12-08 11:00:00', 1500000, 1, 3, 8, NULL, 750000, N'E_WALLET', '2025-12-08', NULL);

INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt) 
VALUES (8, 5, '2025-12-20 14:00:00', '2025-12-23 12:00:00', 'DAILY', '2025-12-08');

-- Phòng đặt trước vẫn giữ AVAILABLE cho đến ngày check-in
UPDATE Room SET roomStatus = 'AVAILABLE' WHERE roomId = 5;

-- Sample OrderDetails (Amenities)
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt) VALUES
(10000, 2, 1, 1, '2025-12-05'), -- Order 1: 2 nước suối
(15000, 2, 4, 1, '2025-12-05'), -- Order 1: 2 nước ngọt
(10000, 4, 1, 2, '2025-12-04'), -- Order 2: 4 nước suối
(20000, 3, 3, 2, '2025-12-04'), -- Order 2: 3 bia lon
(10000, 3, 1, 3, '2025-12-09'), -- Order 3: 3 nước suối
(12000, 2, 5, 3, '2025-12-09'), -- Order 3: 2 mì ly
(15000, 4, 4, 4, '2025-12-08'), -- Order 4: 4 nước ngọt
(10000, 2, 1, 6, '2025-12-08'), -- Order 6: 2 nước suối
(20000, 2, 3, 7, '2025-12-06'); -- Order 7: 2 bia lon

-- Sample SurchargeDetails
INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt) VALUES
(2, 2, 1, '2025-12-03'), -- Order 2: Phụ thu cuối tuần
(3, 4, 1, '2025-12-08'), -- Order 3: Phụ thu nhận phòng sớm
(4, 3, 2, '2025-12-07'), -- Order 4: Phụ thu khách thêm (2 phòng)
(7, 2, 1, '2025-12-06'); -- Order 7: Phụ thu cuối tuần

-- Sample Feedbacks
INSERT INTO Feedback (content, customerId, rating, createdAt) VALUES
(N'Phòng sạch sẽ, nhân viên thân thiện, giá cả hợp lý', 1, 5, '2025-12-05'),
(N'Vị trí thuận tiện, dịch vụ tốt', 2, 4, '2025-12-05'),
(N'Phòng đẹp nhưng hơi ồn vào buổi tối', 7, 3, '2025-12-07'),
(N'Rất hài lòng với dịch vụ, sẽ quay lại lần sau', 1, 5, '2025-12-06');

-- ==========================================
-- THÊM 500+ HÓA ĐƠN MẪU (THÁNG 7-12/2025)
-- ==========================================

-- Thêm khách hàng (tổng cộng 100 khách)
DECLARE @i INT = 11;
WHILE @i <= 100
BEGIN
    INSERT INTO Customer (fullName, phone, email, citizenId, gender, dateOfBirth, createdAt)
    VALUES (
        CONCAT(N'Khách hàng ', @i),
        CONCAT('09', RIGHT('00000000' + CAST(@i AS VARCHAR), 8)),
        CONCAT('khach', @i, '@email.com'),
        CONCAT('0793', RIGHT('00000000' + CAST(@i AS VARCHAR), 8)),
        @i % 2,
        DATEADD(YEAR, -(@i % 50 + 20), GETDATE()),
        DATEADD(MONTH, -(@i % 6), GETDATE())
    );
    SET @i = @i + 1;
END

-- Tạo 500 hóa đơn từ tháng 7-12/2025
DECLARE @orderNum INT = 9;
DECLARE @monthOffset INT;
DECLARE @dayOffset INT;
DECLARE @hourOffset INT;
DECLARE @customerId INT;
DECLARE @employeeId INT;
DECLARE @roomId INT;
DECLARE @bookingTypeIndex INT;
DECLARE @bookingTypeStr VARCHAR(10);
DECLARE @orderTypeId INT;
DECLARE @orderDate DATETIME;
DECLARE @checkIn DATETIME;
DECLARE @checkOut DATETIME;
DECLARE @duration INT;
DECLARE @basePrice DECIMAL(18,2);
DECLARE @totalAmount DECIMAL(18,2);
DECLARE @deposit DECIMAL(18,2);
DECLARE @paymentType NVARCHAR(20);
DECLARE @paymentDate DATE;
DECLARE @roomStatus VARCHAR(20);

WHILE @orderNum <= 508
BEGIN
    -- Random tháng (0-5 cho tháng 7-12)
    SET @monthOffset = (@orderNum % 6);
    SET @dayOffset = ((@orderNum * 7) % 28) + 1;
    SET @hourOffset = ((@orderNum * 3) % 24);
    
    -- Random customer (1-100)
    SET @customerId = ((@orderNum % 100) + 1);
    
    -- Random employee (1-5)
    SET @employeeId = ((@orderNum % 5) + 1);
    
    -- Random room (1-40)
    SET @roomId = ((@orderNum % 40) + 1);
    
    -- Random booking type (0=HOURLY, 1=DAILY, 2=OVERNIGHT)
    SET @bookingTypeIndex = (@orderNum % 3);
    SET @bookingTypeStr = CASE @bookingTypeIndex
        WHEN 0 THEN 'HOURLY'
        WHEN 1 THEN 'DAILY'
        ELSE 'OVERNIGHT'
    END;
    
    -- Order type: 80% đã hoàn thành, 15% đang xử lý, 5% đặt trước
    SET @orderTypeId = CASE 
        WHEN (@orderNum % 20) < 16 THEN 1  -- 80% completed
        WHEN (@orderNum % 20) < 19 THEN 2  -- 15% processing
        ELSE 3  -- 5% reserved
    END;
    
    -- Tính ngày order (7 tháng trước đến giờ)
    SET @orderDate = DATEADD(HOUR, @hourOffset, DATEADD(DAY, @dayOffset, DATEADD(MONTH, @monthOffset - 6, GETDATE())));
    
    -- Tính check-in/check-out và giá
    IF @bookingTypeStr = 'HOURLY'
    BEGIN
        SET @duration = ((@orderNum % 5) + 2); -- 2-6 giờ
        SET @checkIn = @orderDate;
        SET @checkOut = DATEADD(HOUR, @duration, @checkIn);
        -- Giá: 50k (giờ đầu) + 20k * (giờ thêm) cho SINGLE, 80k + 30k cho DOUBLE
        SET @basePrice = CASE WHEN @roomId <= 20 THEN 80000 + ((@duration - 1) * 30000) ELSE 50000 + ((@duration - 1) * 20000) END;
    END
    ELSE IF @bookingTypeStr = 'DAILY'
    BEGIN
        SET @duration = ((@orderNum % 7) + 1); -- 1-7 ngày
        SET @checkIn = DATEADD(HOUR, 14, CAST(CAST(@orderDate AS DATE) AS DATETIME));
        SET @checkOut = DATEADD(HOUR, 12, DATEADD(DAY, @duration, @checkIn));
        -- Giá: 500k/ngày cho DOUBLE, 300k/ngày cho SINGLE
        SET @basePrice = CASE WHEN @roomId <= 20 THEN 500000 * @duration ELSE 300000 * @duration END;
    END
    ELSE -- OVERNIGHT
    BEGIN
        SET @duration = 1;
        SET @checkIn = DATEADD(HOUR, 18, CAST(CAST(@orderDate AS DATE) AS DATETIME));
        SET @checkOut = DATEADD(HOUR, 12, DATEADD(DAY, 1, @checkIn));
        -- Giá: 300k cho DOUBLE, 250k cho SINGLE
        SET @basePrice = CASE WHEN @roomId <= 20 THEN 300000 ELSE 250000 END;
    END;
    
    SET @totalAmount = @basePrice;
    SET @deposit = @totalAmount * 0.5; -- 50% deposit
    
    -- Random payment type (70% CASH, 30% E_WALLET)
    SET @paymentType = CASE WHEN (@orderNum % 10) < 7 THEN N'CASH' ELSE N'E_WALLET' END;
    
    -- Payment date và room status
    IF @orderTypeId = 1 -- Completed
    BEGIN
        SET @paymentDate = CAST(@checkOut AS DATE);
        SET @roomStatus = 'AVAILABLE';
    END
    ELSE IF @orderTypeId = 2 -- Processing
    BEGIN
        SET @paymentDate = NULL;
        SET @roomStatus = 'OCCUPIED';
    END
    ELSE -- Reserved
    BEGIN
        SET @paymentDate = NULL;
        SET @roomStatus = 'AVAILABLE';
        -- Đặt check-in trong tương lai
        SET @checkIn = DATEADD(DAY, ((@orderNum % 20) + 5), GETDATE());
        SET @checkOut = CASE 
            WHEN @bookingTypeStr = 'HOURLY' THEN DATEADD(HOUR, @duration, @checkIn)
            WHEN @bookingTypeStr = 'DAILY' THEN DATEADD(DAY, @duration, @checkIn)
            ELSE DATEADD(DAY, 1, @checkIn)
        END;
    END;
    
    -- Insert Order
    INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
    VALUES (@orderDate, @totalAmount, @employeeId, @orderTypeId, @customerId, NULL, @deposit, @paymentType, CAST(@orderDate AS DATE), @paymentDate);
    
    -- Insert Booking
    INSERT INTO Booking (orderId, roomId, checkInDate, checkOutDate, bookingType, createdAt)
    VALUES (@orderNum, @roomId, @checkIn, @checkOut, @bookingTypeStr, CAST(@orderDate AS DATE));
    
    -- Update room status (chỉ cho completed và processing orders gần đây)
    IF @orderTypeId = 2 AND DATEDIFF(DAY, @orderDate, GETDATE()) <= 7
    BEGIN
        UPDATE Room SET roomStatus = @roomStatus WHERE roomId = @roomId;
    END
    
    -- Thêm amenities ngẫu nhiên (30% orders có amenities)
    IF (@orderNum % 10) < 3
    BEGIN
        DECLARE @amenityId INT = ((@orderNum % 10) + 1);
        DECLARE @amenityQty INT = ((@orderNum % 3) + 1);
        DECLARE @amenityPrice DECIMAL(18,2);
        
        SELECT @amenityPrice = price FROM Amenity WHERE amenityId = @amenityId;
        
        INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
        VALUES (@amenityPrice, @amenityQty, @amenityId, @orderNum, CAST(@orderDate AS DATE));
        
        -- Cập nhật tổng tiền
        UPDATE Orders SET totalAmount = totalAmount + (@amenityPrice * @amenityQty) WHERE orderId = @orderNum;
    END
    
    -- Thêm surcharge ngẫu nhiên (20% orders có surcharge)
    IF (@orderNum % 10) < 2
    BEGIN
        DECLARE @surchargeId INT = ((@orderNum % 11) + 1);
        DECLARE @surchargeQty INT = 1;
        
        INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
        VALUES (@orderNum, @surchargeId, @surchargeQty, CAST(@orderDate AS DATE));
    END
    
    SET @orderNum = @orderNum + 1;
END

PRINT N'Đã tạo 500 hóa đơn mẫu từ tháng 7-12/2025';

-- Thêm feedback cho một số orders
INSERT INTO Feedback (content, customerId, rating, createdAt)
SELECT TOP 100
    CASE (customerId % 5)
        WHEN 0 THEN N'Dịch vụ tốt, phòng sạch sẽ'
        WHEN 1 THEN N'Nhân viên thân thiện, nhiệt tình'
        WHEN 2 THEN N'Giá cả hợp lý, vị trí thuận tiện'
        WHEN 3 THEN N'Phòng đẹp, đầy đủ tiện nghi'
        ELSE N'Trải nghiệm tuyệt vời, sẽ quay lại'
    END,
    customerId,
    ((customerId % 3) + 3), -- Rating 3-5
    DATEADD(DAY, 1, paymentDate)
FROM Orders
WHERE orderTypeId = 1 AND paymentDate IS NOT NULL
ORDER BY orderId DESC;

PRINT N'Hoàn tất tạo dữ liệu mẫu!';

