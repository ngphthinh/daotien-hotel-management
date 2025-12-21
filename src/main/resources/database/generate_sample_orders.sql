-- =============================================
-- SCRIPT TẠO DỮ LIỆU MẪU HÓA ĐƠN ĐÃ THANH TOÁN
-- Tạo 150+ hóa đơn trong 6 tháng gần nhất (07/2025 - 12/2025)
--
-- GIÁ PHÒNG:
-- SINGLE (roomId 21-40): HOURLY=50k, OVERNIGHT=250k, DAILY=300k
-- DOUBLE (roomId 1-20): HOURLY=80k, OVERNIGHT=300k, DAILY=500k
--
-- TIỀN CỌC: 30% của tổng tiền phòng
-- =============================================

USE daotien_hotel;
GO

BEGIN Transaction;

DECLARE @counter INT = 1;
DECLARE @maxOrders INT = 150;
DECLARE @orderId BIGINT;
DECLARE @orderDate DATETIME;
DECLARE @employeeId BIGINT;
DECLARE @customerId BIGINT;
DECLARE @roomCount INT;
DECLARE @bookingTypeIdx INT;
DECLARE @paymentTypeIdx INT;
DECLARE @promotionId BIGINT;
DECLARE @roomId BIGINT;
DECLARE @checkInDate DATETIME;
DECLARE @checkOutDate DATETIME;
DECLARE @bookingType VARCHAR(15);
DECLARE @paymentType NVARCHAR(20);
DECLARE @roomPrice DECIMAL(18,2);
DECLARE @totalRoomPrice DECIMAL(18,2);
DECLARE @amenityPrice DECIMAL(18,2);
DECLARE @surchargePrice DECIMAL(18,2);
DECLARE @totalAmount DECIMAL(18,2);
DECLARE @deposit DECIMAL(18,2);
DECLARE @month INT;
DECLARE @day INT;
DECLARE @hour INT;
DECLARE @isDouble BIT;
DECLARE @amenityCount INT;
DECLARE @surchargeCount INT;
DECLARE @usePromotion BIT;

-- Bắt đầu tạo hóa đơn
WHILE @counter <= @maxOrders
BEGIN
    -- Random tháng (7-12)
    SET @month = 7 + (@counter % 6);
    SET @day = 1 + ((@counter * 7) % 28);
    SET @hour = 8 + ((@counter * 3) % 14);

    SET @orderDate = DATETIMEFROMPARTS(2025, @month, @day, @hour, 0, 0, 0);

    -- Random employee (3-8)
    SET @employeeId = 3 + (@counter % 6);

    -- Random customer (1-55)
    SET @customerId = 1 + (@counter % 55);

    -- Random số phòng (1-4 phòng)
    SET @roomCount = 1 + (@counter % 4);

    -- Random booking type (0=HOURLY, 1=OVERNIGHT, 2=DAILY)
    SET @bookingTypeIdx = @counter % 3;

    -- Random payment type (0=CASH, 1=E_WALLET)
    SET @paymentTypeIdx = @counter % 2;
    SET @paymentType = CASE WHEN @paymentTypeIdx = 0 THEN 'CASH' ELSE 'E_WALLET' END;

    -- Random promotion (10% có promotion)
    SET @usePromotion = CASE WHEN (@counter % 10) = 0 THEN 1 ELSE 0 END;
    SET @promotionId = CASE WHEN @usePromotion = 1 THEN 1 + (@counter % 3) ELSE NULL END;

    -- Tính toán thời gian check-in/out và giá phòng
    SET @totalRoomPrice = 0;

    -- Tạo hóa đơn
    INSERT INTO Orders (orderDate, totalAmount, employeeId, orderTypeId, customerId, promotionId, deposit, paymentType, createdAt, paymentDate)
    VALUES (@orderDate, 0, @employeeId, 1, @customerId, @promotionId, 0, @paymentType, CAST(@orderDate AS DATE), CAST(@orderDate AS DATE));

    SET @orderId = SCOPE_IDENTITY();

    -- Thêm các phòng
    DECLARE @roomIdx INT = 1;
    WHILE @roomIdx <= @roomCount
    BEGIN
        -- Random loại phòng (SINGLE hoặc DOUBLE)
        SET @isDouble = CASE WHEN (@counter + @roomIdx) % 2 = 0 THEN 1 ELSE 0 END;

        -- Chọn roomId
        IF @isDouble = 1
            SET @roomId = 1 + ((@counter + @roomIdx) % 20); -- DOUBLE: 1-20
        ELSE
            SET @roomId = 21 + ((@counter + @roomIdx) % 20); -- SINGLE: 21-40

        -- Tính thời gian và giá dựa trên booking type
        IF @bookingTypeIdx = 0 -- HOURLY
        BEGIN
            SET @bookingType = 'HOURLY';
            SET @checkInDate = @orderDate;
            SET @checkOutDate = DATEADD(HOUR, 3 + (@counter % 3), @orderDate);
            SET @roomPrice = CASE WHEN @isDouble = 1 THEN 80000 ELSE 50000 END;
        END
        ELSE IF @bookingTypeIdx = 1 -- OVERNIGHT
        BEGIN
            SET @bookingType = 'OVERNIGHT';
            SET @checkInDate = DATEADD(HOUR, 12, @orderDate);
            SET @checkOutDate = DATEADD(HOUR, 23, @orderDate);
            SET @roomPrice = CASE WHEN @isDouble = 1 THEN 300000 ELSE 250000 END;
        END
        ELSE -- DAILY
        BEGIN
            SET @bookingType = 'DAILY';
            SET @checkInDate = DATEADD(HOUR, 6, @orderDate);
            SET @checkOutDate = DATEADD(DAY, 1 + (@counter % 3), @checkInDate);
            -- Tính số ngày
            DECLARE @days INT = DATEDIFF(DAY, @checkInDate, @checkOutDate);
            SET @roomPrice = CASE WHEN @isDouble = 1 THEN 500000 * @days ELSE 300000 * @days END;
        END

        SET @totalRoomPrice = @totalRoomPrice + @roomPrice;

        -- Insert booking
        INSERT INTO Booking (orderId, roomId, createdAt, checkInDate, checkOutDate, bookingType)
        VALUES (@orderId, @roomId, CAST(@orderDate AS DATE), @checkInDate, @checkOutDate, @bookingType);

        SET @roomIdx = @roomIdx + 1;
    END

    -- Tính tiền cọc = 30% tổng tiền phòng
    SET @deposit = ROUND(@totalRoomPrice * 0.3, 0);

    -- Thêm amenity (random 0-3 loại)
    SET @amenityCount = @counter % 4;
    SET @amenityPrice = 0;

    IF @amenityCount >= 1
    BEGIN
        INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
        VALUES (10000, 2 + (@counter % 5), 1, @orderId, CAST(@orderDate AS DATE));
        SET @amenityPrice = @amenityPrice + 10000 * (2 + (@counter % 5));
    END

    IF @amenityCount >= 2
    BEGIN
        INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
        VALUES (20000, 1 + (@counter % 4), 3, @orderId, CAST(@orderDate AS DATE));
        SET @amenityPrice = @amenityPrice + 20000 * (1 + (@counter % 4));
    END

    IF @amenityCount >= 3
    BEGIN
        INSERT INTO OrderDetail (unitPrice, quantity, amenityId, orderId, createdAt)
        VALUES (15000, 1 + (@counter % 3), 4, @orderId, CAST(@orderDate AS DATE));
        SET @amenityPrice = @amenityPrice + 15000 * (1 + (@counter % 3));
    END

    -- Thêm surcharge (random 0-2 loại)
    SET @surchargeCount = @counter % 3;
    SET @surchargePrice = 0;

    IF @surchargeCount >= 1
    BEGIN
        INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
        VALUES (@orderId, 1 + (@counter % 5), 1, CAST(@orderDate AS DATE));
        SET @surchargePrice = @surchargePrice + 50000;
    END

    IF @surchargeCount >= 2
    BEGIN
        INSERT INTO SurchargeDetail (orderId, surchargerId, quantity, createdAt)
        VALUES (@orderId, 3, @roomCount, CAST(@orderDate AS DATE));
        SET @surchargePrice = @surchargePrice + 70000 * @roomCount;
    END

    -- Tính tổng tiền
    SET @totalAmount = @totalRoomPrice + @amenityPrice + @surchargePrice;

    -- Áp dụng giảm giá nếu có promotion
    IF @promotionId IS NOT NULL
    BEGIN
        IF @promotionId = 1 AND @totalAmount >= 500000
            SET @totalAmount = @totalAmount * 0.9; -- Giảm 10%
        ELSE IF @promotionId = 2 AND @totalAmount >= 1000000
            SET @totalAmount = @totalAmount * 0.85; -- Giảm 15%
        ELSE IF @promotionId = 3 AND @totalAmount >= 2000000
            SET @totalAmount = @totalAmount * 0.8; -- Giảm 20%
    END

    -- Cập nhật tổng tiền và tiền cọc
    UPDATE Orders
    SET totalAmount = @totalAmount,
        deposit = @deposit
    WHERE orderId = @orderId;

    SET @counter = @counter + 1;
END

PRINT N'✓ Đã tạo thành công ' + CAST(@maxOrders AS NVARCHAR) + N' hóa đơn đã thanh toán';
PRINT N'✓ Thời gian: 07/2025 - 12/2025';
PRINT N'✓ Giá phòng tính đúng theo RoomType';
PRINT N'✓ Tiền cọc = 30% tổng tiền phòng';
PRINT N'✓ Mix đủ: HOURLY, OVERNIGHT, DAILY';
PRINT N'✓ Mix đủ: CASH, E_WALLET';
PRINT N'✓ Có Amenity và Surcharge đa dạng';

-- Hiển thị thống kê
SELECT
    COUNT(*) AS TotalOrders,
    SUM(totalAmount) AS TotalRevenue,
    AVG(totalAmount) AS AvgOrderValue,
    SUM(deposit) AS TotalDeposit
FROM Orders
WHERE orderTypeId = 1 AND paymentDate IS NOT NULL;

COMMIT Transaction;