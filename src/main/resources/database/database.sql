
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
	closingTime datetime ,
	shiftDate Date,
	createdAt Date default getDate()
	foreign key (employeeId) references Employee(employeeId),
	foreign key (shiftId) references Shift(shiftId)
)
go


create table DenominaionDetail (
	denominaionDetailId bigint identity primary key,
	denominaion nvarchar(30) not null,
	quantity int not null check (quantity > 0),
	employeeShiftId bigint not null,
	createdAt Date default getDate()
	foreign key (employeeShiftId) references EmployeeShift(employeeShiftId)
)
go

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