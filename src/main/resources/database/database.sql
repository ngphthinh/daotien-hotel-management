
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

