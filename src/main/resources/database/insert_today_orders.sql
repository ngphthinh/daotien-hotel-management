-- =============================================
-- Script: Generate ~100 paid orders for last 7 days
-- Date Range: 2025-12-15 to 2025-12-21
-- Orders per day: ~14-15 orders
-- Based on insert_sample_orders.sql pattern
-- =============================================

SET NOCOUNT ON;

DECLARE @StartDate DATE = '2025-12-15';
DECLARE @EndDate DATE = '2025-12-21';
DECLARE @CurrentDate DATE = @StartDate;
DECLARE @OrderCount INT = 0;
DECLARE @TargetOrders INT = 100;

-- Helper function values
DECLARE @MinEmployeeId INT = 1;
DECLARE @MaxEmployeeId INT = 8;
DECLARE @MinCustomerId INT = 1;
DECLARE @MaxCustomerId INT = 55;

PRINT '========================================';
PRINT 'Starting to generate ' + CAST(@TargetOrders AS VARCHAR) + ' orders';
PRINT 'Date range: ' + CAST(@StartDate AS VARCHAR) + ' to ' + CAST(@EndDate AS VARCHAR);
PRINT '========================================';

-- Declare all variables upfront
DECLARE @DailyCount INT;
DECLARE @OrdersPerDay INT;
DECLARE @EmployeeId BIGINT;
DECLARE @CustomerId BIGINT;
DECLARE @BookingTypeId BIGINT;
DECLARE @RandomBookingType INT;
DECLARE @PaymentTypeId BIGINT;
DECLARE @Hour INT;
DECLARE @Minute INT;
DECLARE @CheckInTime DATETIME;
DECLARE @CheckOutTime DATETIME;
DECLARE @RoomId BIGINT;
DECLARE @RoomTypeId BIGINT;
DECLARE @BasePrice DECIMAL(10,2);
DECLARE @Hours DECIMAL(10,2);
DECLARE @Days INT;
DECLARE @RoomPrice DECIMAL(10,2);
DECLARE @TotalPrice DECIMAL(10,2);
DECLARE @OrderId BIGINT;
DECLARE @NumAmenities INT;
DECLARE @AmenityCounter INT;
DECLARE @AmenityId BIGINT;
DECLARE @AmenityPrice DECIMAL(10,2);
DECLARE @AmenityQty INT;
DECLARE @NumSurcharges INT;
DECLARE @SurchargeCounter INT;
DECLARE @SurchargeId BIGINT;
DECLARE @SurchargePrice DECIMAL(10,2);
DECLARE @SurchargeQty INT;

-- Loop through each day
WHILE @CurrentDate <= @EndDate
BEGIN
    SET @DailyCount = 0;
    SET @OrdersPerDay = 14 + (ABS(CHECKSUM(NEWID())) % 3); -- 14-16 orders per day

    PRINT 'Processing date: ' + CAST(@CurrentDate AS VARCHAR) + ' (Target: ' + CAST(@OrdersPerDay AS VARCHAR) + ' orders)';

    -- Generate orders for this day
    WHILE @DailyCount < @OrdersPerDay AND @OrderCount < @TargetOrders
    BEGIN
        BEGIN TRY
            BEGIN TRANSACTION;

            -- Random employee and customer
            SET @EmployeeId = @MinEmployeeId + (ABS(CHECKSUM(NEWID())) % @MaxEmployeeId);
            SET @CustomerId = @MinCustomerId + (ABS(CHECKSUM(NEWID())) % @MaxCustomerId);

            -- Random booking type: 1=HOURLY (30%), 2=OVERNIGHT (50%), 3=DAILY (20%)
            SET @RandomBookingType = ABS(CHECKSUM(NEWID())) % 100;
            IF @RandomBookingType < 30
                SET @BookingTypeId = 1; -- HOURLY
            ELSE IF @RandomBookingType < 80
                SET @BookingTypeId = 2; -- OVERNIGHT
            ELSE
                SET @BookingTypeId = 3; -- DAILY

            -- Random payment type: 1=CASH (60%), 2=E_WALLET (40%)
            SET @PaymentTypeId = CASE WHEN (ABS(CHECKSUM(NEWID())) % 100) < 60 THEN 1 ELSE 2 END;

            -- Random time during the day
            SET @Hour = 8 + (ABS(CHECKSUM(NEWID())) % 14); -- 8-21h
            SET @Minute = (ABS(CHECKSUM(NEWID())) % 12) * 5; -- 0, 5, 10, ..., 55
            SET @CheckInTime = DATEADD(MINUTE, @Minute, DATEADD(HOUR, @Hour, @CurrentDate));

            -- Calculate checkout time based on booking type
            IF @BookingTypeId = 1 -- HOURLY: 2-4 hours
                SET @CheckOutTime = DATEADD(HOUR, 2 + (ABS(CHECKSUM(NEWID())) % 3), @CheckInTime);
            ELSE IF @BookingTypeId = 2 -- OVERNIGHT: 10-14 hours
                SET @CheckOutTime = DATEADD(HOUR, 10 + (ABS(CHECKSUM(NEWID())) % 5), @CheckInTime);
            ELSE -- DAILY: 1-2 days
                SET @CheckOutTime = DATEADD(DAY, 1 + (ABS(CHECKSUM(NEWID())) % 2), @CheckInTime);

            -- Get a random available room
            SET @RoomId = NULL;
            SET @RoomTypeId = NULL;
            SET @BasePrice = NULL;

            SELECT TOP 1
                @RoomId = r.roomId,
                @RoomTypeId = r.roomTypeId,
                @BasePrice = rt.price
            FROM Room r
            JOIN RoomType rt ON r.roomTypeId = rt.roomTypeId
            WHERE r.statusId = 1 -- AVAILABLE
            ORDER BY NEWID();

            -- If no room found, skip this iteration
            IF @RoomId IS NULL
            BEGIN
                ROLLBACK TRANSACTION;
                CONTINUE;
            END

            -- Calculate room price based on booking type and duration
            IF @BookingTypeId = 1 -- HOURLY
            BEGIN
                SET @Hours = DATEDIFF(MINUTE, @CheckInTime, @CheckOutTime) / 60.0;
                SET @RoomPrice = @BasePrice * @Hours;
            END
            ELSE IF @BookingTypeId = 2 -- OVERNIGHT
            BEGIN
                SET @Hours = DATEDIFF(MINUTE, @CheckInTime, @CheckOutTime) / 60.0;
                SET @RoomPrice = @BasePrice * CEILING(@Hours / 24.0);
            END
            ELSE -- DAILY
            BEGIN
                SET @Days = DATEDIFF(DAY, @CheckInTime, @CheckOutTime);
                SET @RoomPrice = @BasePrice * @Days;
            END

            -- Initialize total price with room price
            SET @TotalPrice = @RoomPrice;

            -- Insert Order (orderTypeId = 1 = PAID)
            INSERT INTO [Order] (
                employeeId, customerId, roomId, checkInTime, checkOutTime,
                bookingTypeId, totalPrice, orderTypeId, paymentTypeId, createdAt
            )
            VALUES (
                @EmployeeId, @CustomerId, @RoomId, @CheckInTime, @CheckOutTime,
                @BookingTypeId, @TotalPrice, 1, @PaymentTypeId, @CheckInTime
            );

            SET @OrderId = SCOPE_IDENTITY();

            -- Insert OrderDetail for the room
            INSERT INTO OrderDetail (orderId, roomId, checkInTime, checkOutTime, price)
            VALUES (@OrderId, @RoomId, @CheckInTime, @CheckOutTime, @RoomPrice);

            -- Add amenities (40% chance)
            IF (ABS(CHECKSUM(NEWID())) % 100) < 40
            BEGIN
                SET @NumAmenities = 1 + (ABS(CHECKSUM(NEWID())) % 3); -- 1-3 amenities
                SET @AmenityCounter = 0;

                WHILE @AmenityCounter < @NumAmenities
                BEGIN
                    SET @AmenityId = NULL;
                    SET @AmenityPrice = NULL;
                    SET @AmenityQty = 1 + (ABS(CHECKSUM(NEWID())) % 3); -- 1-3 quantity

                    SELECT TOP 1
                        @AmenityId = amenityId,
                        @AmenityPrice = price
                    FROM Amenity
                    WHERE isActive = 1
                    ORDER BY NEWID();

                    IF @AmenityId IS NOT NULL
                    BEGIN
                        INSERT INTO AmenityDetail (orderId, amenityId, quantity, usedAt)
                        VALUES (@OrderId, @AmenityId, @AmenityQty,
                               DATEADD(HOUR, 1 + (ABS(CHECKSUM(NEWID())) % 4), @CheckInTime));

                        SET @TotalPrice = @TotalPrice + (@AmenityPrice * @AmenityQty);
                    END

                    SET @AmenityCounter = @AmenityCounter + 1;
                END
            END

            -- Add surcharges (25% chance)
            IF (ABS(CHECKSUM(NEWID())) % 100) < 25
            BEGIN
                SET @NumSurcharges = 1 + (ABS(CHECKSUM(NEWID())) % 2); -- 1-2 surcharges
                SET @SurchargeCounter = 0;

                WHILE @SurchargeCounter < @NumSurcharges
                BEGIN
                    SET @SurchargeId = NULL;
                    SET @SurchargePrice = NULL;
                    SET @SurchargeQty = 1;

                    SELECT TOP 1
                        @SurchargeId = surchargeId,
                        @SurchargePrice = price
                    FROM Surcharge
                    WHERE isActive = 1
                    ORDER BY NEWID();

                    IF @SurchargeId IS NOT NULL
                    BEGIN
                        INSERT INTO SurchargeDetail (orderId, surchargeId, quantity, appliedAt)
                        VALUES (@OrderId, @SurchargeId, @SurchargeQty, @CheckInTime);

                        SET @TotalPrice = @TotalPrice + (@SurchargePrice * @SurchargeQty);
                    END

                    SET @SurchargeCounter = @SurchargeCounter + 1;
                END
            END

            -- Update order total price
            UPDATE [Order]
            SET totalPrice = @TotalPrice
            WHERE orderId = @OrderId;

            COMMIT TRANSACTION;

            SET @DailyCount = @DailyCount + 1;
            SET @OrderCount = @OrderCount + 1;

        END TRY
        BEGIN CATCH
            IF @@TRANCOUNT > 0
                ROLLBACK TRANSACTION;

            PRINT 'Error occurred: ' + ERROR_MESSAGE();
        END CATCH
    END

    PRINT '  -> Generated ' + CAST(@DailyCount AS VARCHAR) + ' orders for ' + CAST(@CurrentDate AS VARCHAR);
    SET @CurrentDate = DATEADD(DAY, 1, @CurrentDate);
END

PRINT '========================================';
PRINT 'Total orders generated: ' + CAST(@OrderCount AS VARCHAR);
PRINT '========================================';
PRINT '';

-- Display summary statistics
PRINT 'Summary by Date:';
SELECT
    CONVERT(VARCHAR(10), checkInTime, 103) as [Date],
    COUNT(*) as [Orders],
    FORMAT(SUM(totalPrice), 'N0') + ' VND' as [Revenue],
    FORMAT(AVG(totalPrice), 'N0') + ' VND' as [Avg Value],
    SUM(CASE WHEN bookingTypeId = 1 THEN 1 ELSE 0 END) as [Hourly],
    SUM(CASE WHEN bookingTypeId = 2 THEN 1 ELSE 0 END) as [Overnight],
    SUM(CASE WHEN bookingTypeId = 3 THEN 1 ELSE 0 END) as [Daily],
    SUM(CASE WHEN paymentTypeId = 1 THEN 1 ELSE 0 END) as [Cash],
    SUM(CASE WHEN paymentTypeId = 2 THEN 1 ELSE 0 END) as [E-Wallet]
FROM [Order]
WHERE CAST(checkInTime AS DATE) BETWEEN @StartDate AND @EndDate
    AND orderTypeId = 1
GROUP BY CAST(checkInTime AS DATE)
ORDER BY CAST(checkInTime AS DATE);

PRINT '';
PRINT 'Overall Statistics:';
SELECT
    COUNT(*) as [Total Orders],
    FORMAT(SUM(totalPrice), 'N0') + ' VND' as [Total Revenue],
    FORMAT(AVG(totalPrice), 'N0') + ' VND' as [Average Order],
    FORMAT(MIN(totalPrice), 'N0') + ' VND' as [Min Order],
    FORMAT(MAX(totalPrice), 'N0') + ' VND' as [Max Order]
FROM [Order]
WHERE CAST(checkInTime AS DATE) BETWEEN @StartDate AND @EndDate
    AND orderTypeId = 1;

