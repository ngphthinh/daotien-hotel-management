-- =============================================
-- SCRIPT TẠO DỮ LIỆU MẪU HÓA ĐƠN ĐÃ THANH TOÁN
-- Tạo hóa đơn trong 5-6 tháng gần nhất (từ tháng 7/2025 đến 12/2025)
-- Nhân viên: 3-8 (6 nhân viên)
-- Khách hàng: 1-55
-- BookingType: HOURLY, OVERNIGHT, DAILY
-- PaymentType: CASH, E_WALLET
-- OrderType: 1 (Đã hoàn thành)
--
-- GIÁ PHÒNG:
-- SINGLE (1-20): HOURLY=50k, OVERNIGHT=250k, DAILY=300k
-- DOUBLE (21-40): HOURLY=80k, OVERNIGHT=300k, DAILY=500k
--
-- TIỀN CỌC: 30% của tổng tiền phòng
-- =============================================

USE daotien_hotel;
GO
GO

Begin Transaction;
-- Hóa đơn tháng 7/2025
-- Hóa đơn 1: HOURLY - CASH - 1 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-07-05 10:30:00', 180000, 3, 1, 1, NULL, 50000, 'CASH', '2025-07-05', '2025-07-05');
DECLARE @orderId1 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId1, 21, '2025-07-05', '2025-07-05 10:30:00', '2025-07-05 14:30:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 2, 1, @orderId1, '2025-07-05'),
       (15000, 1, 4, @orderId1, '2025-07-05');

-- Hóa đơn 2: DAILY - E_WALLET - 2 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-07-08 14:00:00', 1050000, 4, 1, 2, NULL, 300000, 'E_WALLET', '2025-07-08', '2025-07-08');
DECLARE @orderId2 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId2, 1, '2025-07-08', '2025-07-08 14:00:00', '2025-07-09 12:00:00', 'DAILY'),
       (@orderId2, 2, '2025-07-08', '2025-07-08 14:00:00', '2025-07-09 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 4, 3, @orderId2, '2025-07-08'),
       (10000, 2, 1, @orderId2, '2025-07-08');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId2, 2, 1, '2025-07-08');

-- Hóa đơn 3: OVERNIGHT - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-07-12 20:00:00', 650000, 5, 1, 3, NULL, 200000, 'CASH', '2025-07-12', '2025-07-13');
DECLARE @orderId3 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId3, 3, '2025-07-12', '2025-07-12 20:00:00', '2025-07-13 11:00:00', 'OVERNIGHT'),
       (@orderId3, 4, '2025-07-12', '2025-07-12 20:00:00', '2025-07-13 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 3, 1, @orderId3, '2025-07-12'),
       (12000, 2, 5, @orderId3, '2025-07-12');

-- Hóa đơn tháng 8/2025
-- Hóa đơn 4: DAILY - CASH - Có promotion
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-08-03 09:00:00', 1350000, 6, 1, 5, 1, 500000, 'CASH', '2025-08-03', '2025-08-05');
DECLARE @orderId4 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId4, 21, '2025-08-03', '2025-08-03 14:00:00', '2025-08-05 12:00:00', 'DAILY'),
       (@orderId4, 22, '2025-08-03', '2025-08-03 14:00:00', '2025-08-05 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 5, 3, @orderId4, '2025-08-03'),
       (15000, 4, 4, @orderId4, '2025-08-03'),
       (10000, 3, 1, @orderId4, '2025-08-03');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId4, 3, 2, '2025-08-03');

-- Hóa đơn 5: HOURLY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-08-10 16:30:00', 110000, 7, 1, 8, NULL, 50000, 'E_WALLET', '2025-08-10', '2025-08-10');
DECLARE @orderId5 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId5, 5, '2025-08-10', '2025-08-10 16:30:00', '2025-08-10 19:30:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 2, 1, @orderId5, '2025-08-10'),
       (15000, 1, 4, @orderId5, '2025-08-10');

-- Hóa đơn 6: OVERNIGHT - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-08-15 22:00:00', 320000, 8, 1, 10, NULL, 100000, 'CASH', '2025-08-15', '2025-08-16');
DECLARE @orderId6 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId6, 23, '2025-08-15', '2025-08-15 22:00:00', '2025-08-16 10:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 2, 1, @orderId6, '2025-08-15');

-- Hóa đơn tháng 9/2025
-- Hóa đơn 7: DAILY - E_WALLET - 3 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-09-05 13:00:00', 1850000, 3, 1, 12, 2, 600000, 'E_WALLET', '2025-09-05', '2025-09-07');
DECLARE @orderId7 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId7, 6, '2025-09-05', '2025-09-05 14:00:00', '2025-09-07 12:00:00', 'DAILY'),
       (@orderId7, 7, '2025-09-05', '2025-09-05 14:00:00', '2025-09-07 12:00:00', 'DAILY'),
       (@orderId7, 24, '2025-09-05', '2025-09-05 14:00:00', '2025-09-07 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 10, 3, @orderId7, '2025-09-05'),
       (15000, 6, 4, @orderId7, '2025-09-05'),
       (10000, 5, 1, @orderId7, '2025-09-05');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId7, 1, 1, '2025-09-05'),
       (@orderId7, 3, 3, '2025-09-05');

-- Hóa đơn 8: HOURLY - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-09-12 11:00:00', 95000, 4, 1, 15, NULL, 50000, 'CASH', '2025-09-12', '2025-09-12');
DECLARE @orderId8 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId8, 8, '2025-09-12', '2025-09-12 11:00:00', '2025-09-12 14:00:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 1, 1, @orderId8, '2025-09-12'),
       (15000, 1, 4, @orderId8, '2025-09-12');

-- Hóa đơn 9: OVERNIGHT - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-09-18 21:00:00', 580000, 5, 1, 18, NULL, 200000, 'E_WALLET', '2025-09-18', '2025-09-19');
DECLARE @orderId9 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId9, 25, '2025-09-18', '2025-09-18 21:00:00', '2025-09-19 11:00:00', 'OVERNIGHT'),
       (@orderId9, 9, '2025-09-18', '2025-09-18 21:00:00', '2025-09-19 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 3, 3, @orderId9, '2025-09-18'),
       (10000, 2, 1, @orderId9, '2025-09-18');

-- Hóa đơn tháng 10/2025
-- Hóa đơn 10: DAILY - CASH - 4 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-10-02 12:00:00', 2400000, 6, 1, 20, 3, 800000, 'CASH', '2025-10-02', '2025-10-05');
DECLARE @orderId10 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId10, 26, '2025-10-02', '2025-10-02 14:00:00', '2025-10-05 12:00:00', 'DAILY'),
       (@orderId10, 27, '2025-10-02', '2025-10-02 14:00:00', '2025-10-05 12:00:00', 'DAILY'),
       (@orderId10, 10, '2025-10-02', '2025-10-02 14:00:00', '2025-10-05 12:00:00', 'DAILY'),
       (@orderId10, 11, '2025-10-02', '2025-10-02 14:00:00', '2025-10-05 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 15, 3, @orderId10, '2025-10-02'),
       (15000, 10, 4, @orderId10, '2025-10-02'),
       (10000, 8, 1, @orderId10, '2025-10-02'),
       (12000, 5, 5, @orderId10, '2025-10-02');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId10, 1, 2, '2025-10-02'),
       (@orderId10, 3, 4, '2025-10-02'),
       (@orderId10, 2, 1, '2025-10-02');

-- Hóa đơn 11: HOURLY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-10-08 15:00:00', 185000, 7, 1, 22, NULL, 80000, 'E_WALLET', '2025-10-08', '2025-10-08');
DECLARE @orderId11 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId11, 28, '2025-10-08', '2025-10-08 15:00:00', '2025-10-08 19:00:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 2, 3, @orderId11, '2025-10-08'),
       (15000, 2, 4, @orderId11, '2025-10-08'),
       (10000, 1, 1, @orderId11, '2025-10-08');

-- Hóa đơn 12: OVERNIGHT - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-10-15 23:00:00', 880000, 8, 1, 25, NULL, 300000, 'CASH', '2025-10-15', '2025-10-16');
DECLARE @orderId12 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId12, 29, '2025-10-15', '2025-10-15 23:00:00', '2025-10-16 11:00:00', 'OVERNIGHT'),
       (@orderId12, 30, '2025-10-15', '2025-10-15 23:00:00', '2025-10-16 11:00:00', 'OVERNIGHT'),
       (@orderId12, 12, '2025-10-15', '2025-10-15 23:00:00', '2025-10-16 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 4, 3, @orderId12, '2025-10-15'),
       (10000, 3, 1, @orderId12, '2025-10-15');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId12, 5, 1, '2025-10-15');

-- Hóa đơn 13: DAILY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-10-20 10:00:00', 1120000, 3, 1, 28, NULL, 400000, 'E_WALLET', '2025-10-20', '2025-10-22');
DECLARE @orderId13 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId13, 31, '2025-10-20', '2025-10-20 14:00:00', '2025-10-22 12:00:00', 'DAILY'),
       (@orderId13, 13, '2025-10-20', '2025-10-20 14:00:00', '2025-10-22 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 6, 3, @orderId13, '2025-10-20'),
       (15000, 4, 4, @orderId13, '2025-10-20');

-- Hóa đơn tháng 11/2025
-- Hóa đơn 14: HOURLY - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-03 13:30:00', 145000, 4, 1, 30, NULL, 50000, 'CASH', '2025-11-03', '2025-11-03');
DECLARE @orderId14 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId14, 32, '2025-11-03', '2025-11-03 13:30:00', '2025-11-03 17:30:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 2, 3, @orderId14, '2025-11-03'),
       (10000, 1, 1, @orderId14, '2025-11-03');

-- Hóa đơn 15: OVERNIGHT - E_WALLET - 2 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-08 20:30:00', 750000, 5, 1, 32, NULL, 250000, 'E_WALLET', '2025-11-08', '2025-11-09');
DECLARE @orderId15 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId15, 33, '2025-11-08', '2025-11-08 20:30:00', '2025-11-09 11:00:00', 'OVERNIGHT'),
       (@orderId15, 14, '2025-11-08', '2025-11-08 20:30:00', '2025-11-09 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 5, 3, @orderId15, '2025-11-08'),
       (15000, 3, 4, @orderId15, '2025-11-08'),
       (10000, 2, 1, @orderId15, '2025-11-08');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId15, 2, 1, '2025-11-08'),
       (@orderId15, 3, 1, '2025-11-08');

-- Hóa đơn 16: DAILY - CASH - 3 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-12 11:00:00', 1650000, 6, 1, 35, 1, 500000, 'CASH', '2025-11-12', '2025-11-14');
DECLARE @orderId16 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId16, 34, '2025-11-12', '2025-11-12 14:00:00', '2025-11-14 12:00:00', 'DAILY'),
       (@orderId16, 35, '2025-11-12', '2025-11-12 14:00:00', '2025-11-14 12:00:00', 'DAILY'),
       (@orderId16, 15, '2025-11-12', '2025-11-12 14:00:00', '2025-11-14 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 8, 3, @orderId16, '2025-11-12'),
       (15000, 6, 4, @orderId16, '2025-11-12'),
       (10000, 4, 1, @orderId16, '2025-11-12'),
       (12000, 3, 5, @orderId16, '2025-11-12');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId16, 3, 2, '2025-11-12');

-- Hóa đơn 17: HOURLY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-18 16:00:00', 120000, 7, 1, 38, NULL, 50000, 'E_WALLET', '2025-11-18', '2025-11-18');
DECLARE @orderId17 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId17, 16, '2025-11-18', '2025-11-18 16:00:00', '2025-11-18 19:00:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (15000, 2, 4, @orderId17, '2025-11-18'),
       (10000, 1, 1, @orderId17, '2025-11-18');

-- Hóa đơn 18: OVERNIGHT - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-22 22:00:00', 380000, 8, 1, 40, NULL, 150000, 'CASH', '2025-11-22', '2025-11-23');
DECLARE @orderId18 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId18, 36, '2025-11-22', '2025-11-22 22:00:00', '2025-11-23 10:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 2, 3, @orderId18, '2025-11-22'),
       (10000, 2, 1, @orderId18, '2025-11-22');

-- Hóa đơn tháng 12/2025
-- Hóa đơn 19: DAILY - E_WALLET - 2 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-01 09:00:00', 1280000, 3, 1, 42, 2, 400000, 'E_WALLET', '2025-12-01', '2025-12-03');
DECLARE @orderId19 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId19, 37, '2025-12-01', '2025-12-01 14:00:00', '2025-12-03 12:00:00', 'DAILY'),
       (@orderId19, 17, '2025-12-01', '2025-12-01 14:00:00', '2025-12-03 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 7, 3, @orderId19, '2025-12-01'),
       (15000, 5, 4, @orderId19, '2025-12-01'),
       (10000, 3, 1, @orderId19, '2025-12-01');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId19, 2, 1, '2025-12-01');

-- Hóa đơn 20: HOURLY - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-05 14:30:00', 155000, 4, 1, 44, NULL, 80000, 'CASH', '2025-12-05', '2025-12-05');
DECLARE @orderId20 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId20, 38, '2025-12-05', '2025-12-05 14:30:00', '2025-12-05 18:30:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 2, 3, @orderId20, '2025-12-05'),
       (15000, 2, 4, @orderId20, '2025-12-05');

-- Hóa đơn 21: OVERNIGHT - E_WALLET - 3 phòng
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-08 21:00:00', 1050000, 5, 1, 46, NULL, 350000, 'E_WALLET', '2025-12-08', '2025-12-09');
DECLARE @orderId21 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId21, 39, '2025-12-08', '2025-12-08 21:00:00', '2025-12-09 11:00:00', 'OVERNIGHT'),
       (@orderId21, 40, '2025-12-08', '2025-12-08 21:00:00', '2025-12-09 11:00:00', 'OVERNIGHT'),
       (@orderId21, 18, '2025-12-08', '2025-12-08 21:00:00', '2025-12-09 11:00:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 6, 3, @orderId21, '2025-12-08'),
       (15000, 4, 4, @orderId21, '2025-12-08'),
       (10000, 3, 1, @orderId21, '2025-12-08');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId21, 3, 3, '2025-12-08');

-- Hóa đơn 22: DAILY - CASH - 4 phòng (lớn)
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-10 10:00:00', 2850000, 6, 1, 48, 3, 1000000, 'CASH', '2025-12-10', '2025-12-13');
DECLARE @orderId22 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId22, 19, '2025-12-10', '2025-12-10 14:00:00', '2025-12-13 12:00:00', 'DAILY'),
       (@orderId22, 20, '2025-12-10', '2025-12-10 14:00:00', '2025-12-13 12:00:00', 'DAILY'),
       (@orderId22, 1, '2025-12-10', '2025-12-10 14:00:00', '2025-12-13 12:00:00', 'DAILY'),
       (@orderId22, 2, '2025-12-10', '2025-12-10 14:00:00', '2025-12-13 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 20, 3, @orderId22, '2025-12-10'),
       (15000, 15, 4, @orderId22, '2025-12-10'),
       (10000, 10, 1, @orderId22, '2025-12-10'),
       (12000, 8, 5, @orderId22, '2025-12-10');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId22, 1, 2, '2025-12-10'),
       (@orderId22, 3, 4, '2025-12-10'),
       (@orderId22, 2, 1, '2025-12-10');

-- Hóa đơn 23: HOURLY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-14 12:00:00', 135000, 7, 1, 50, NULL, 50000, 'E_WALLET', '2025-12-14', '2025-12-14');
DECLARE @orderId23 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId23, 3, '2025-12-14', '2025-12-14 12:00:00', '2025-12-14 16:00:00', 'HOURLY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 1, 3, @orderId23, '2025-12-14'),
       (15000, 2, 4, @orderId23, '2025-12-14'),
       (10000, 1, 1, @orderId23, '2025-12-14');

-- Hóa đơn 24: OVERNIGHT - CASH
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-16 23:30:00', 450000, 8, 1, 52, NULL, 150000, 'CASH', '2025-12-16', '2025-12-17');
DECLARE @orderId24 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId24, 4, '2025-12-16', '2025-12-16 23:30:00', '2025-12-17 10:30:00', 'OVERNIGHT');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 3, 3, @orderId24, '2025-12-16'),
       (10000, 2, 1, @orderId24, '2025-12-16'),
       (15000, 2, 4, @orderId24, '2025-12-16');

INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId24, 5, 1, '2025-12-16');

-- Hóa đơn 25: DAILY - E_WALLET
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-12-18 08:00:00', 980000, 3, 1, 54, NULL, 300000, 'E_WALLET', '2025-12-18', '2025-12-19');
DECLARE @orderId25 BIGINT = SCOPE_IDENTITY();

INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId25, 5, '2025-12-18', '2025-12-18 14:00:00', '2025-12-19 12:00:00', 'DAILY'),
       (@orderId25, 6, '2025-12-18', '2025-12-18 14:00:00', '2025-12-19 12:00:00', 'DAILY');

INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 5, 3, @orderId25, '2025-12-18'),
       (15000, 4, 4, @orderId25, '2025-12-18'),
       (10000, 3, 1, @orderId25, '2025-12-18');

-- Thêm nhiều hóa đơn nhỏ hơn để đa dạng dữ liệu
-- Hóa đơn 26-30: Mix các loại
INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-07-20 15:00:00', 90000, 4, 1, 7, NULL, 50000, 'CASH', '2025-07-20', '2025-07-20');
DECLARE @orderId26 BIGINT = SCOPE_IDENTITY();
INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId26, 7, '2025-07-20', '2025-07-20 15:00:00', '2025-07-20 18:00:00', 'HOURLY');
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 1, 1, @orderId26, '2025-07-20');

INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-08-25 19:00:00', 560000, 5, 1, 11, NULL, 200000, 'E_WALLET', '2025-08-25', '2025-08-26');
DECLARE @orderId27 BIGINT = SCOPE_IDENTITY();
INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId27, 8, '2025-08-25', '2025-08-25 19:00:00', '2025-08-26 10:00:00', 'OVERNIGHT'),
       (@orderId27, 9, '2025-08-25', '2025-08-25 19:00:00', '2025-08-26 10:00:00', 'OVERNIGHT');
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 2, 1, @orderId27, '2025-08-25');

INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-09-28 11:00:00', 720000, 6, 1, 16, NULL, 250000, 'CASH', '2025-09-28', '2025-09-29');
DECLARE @orderId28 BIGINT = SCOPE_IDENTITY();
INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId28, 10, '2025-09-28', '2025-09-28 14:00:00', '2025-09-29 12:00:00', 'DAILY');
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 3, 3, @orderId28, '2025-09-28'),
       (15000, 2, 4, @orderId28, '2025-09-28');

INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-10-30 17:00:00', 175000, 7, 1, 23, NULL, 80000, 'E_WALLET', '2025-10-30', '2025-10-30');
DECLARE @orderId29 BIGINT = SCOPE_IDENTITY();
INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId29, 11, '2025-10-30', '2025-10-30 17:00:00', '2025-10-30 21:00:00', 'HOURLY');
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (20000, 3, 3, @orderId29, '2025-10-30');

INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
VALUES ('2025-11-25 20:00:00', 410000, 8, 1, 37, NULL, 150000, 'CASH', '2025-11-25', '2025-11-26');
DECLARE @orderId30 BIGINT = SCOPE_IDENTITY();
INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
VALUES (@orderId30, 12, '2025-11-25', '2025-11-25 20:00:00', '2025-11-26 09:00:00', 'OVERNIGHT');
INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
VALUES (10000, 3, 1, @orderId30, '2025-11-25'),
       (20000, 2, 3, @orderId30, '2025-11-25');
INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
VALUES (@orderId30, 4, 1, '2025-11-25');

PRINT N'✓ Đã tạo thành công 30 hóa đơn đã thanh toán từ tháng 7/2025 đến 12/2025';
PRINT N'✓ Mix đủ các loại: HOURLY, OVERNIGHT, DAILY';
PRINT N'✓ Mix đủ PaymentType: CASH, E_WALLET';
PRINT N'✓ Đã thêm Amenity và Surcharge cho các hóa đơn';
PRINT N'✓ Sử dụng nhân viên 3-8, khách hàng 1-55';
GO

COMMIT TRANSACTION;



