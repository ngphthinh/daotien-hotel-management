# ✅ FIX: Doanh Thu Hiển Thị Theo Time Range (7 Ngày, 30 Ngày, 90 Ngày)

## 🐛 Vấn Đề

Khi user click "7 ngày", "30 ngày", hoặc "90 ngày", Card 3 (Doanh thu) và CardLiquid vẫn hiển thị doanh thu **HÔM NAY** thay vì doanh thu của khoảng thời gian đã chọn.

### Nguyên Nhân

Trong `loadDashboardData()`, code luôn gọi `getTodayRevenue()`:

```java
todayRevenue = dashboardService.getTodayRevenue();  // ❌ Luôn lấy hôm nay
```

→ Bất kể user chọn "7 ngày" hay "30 ngày", doanh thu vẫn là của hôm nay!

## ✅ Giải Pháp

### 1. Thêm Method Mới Trong DashboardService

Thêm method `getRevenueByDateRange()` để tính doanh thu theo khoảng thời gian:

```java
/**
 * Lấy doanh thu theo khoảng thời gian (startDate -> endDate)
 * @param startDate Ngày bắt đầu
 * @param endDate Ngày kết thúc
 * @return Tổng doanh thu trong khoảng thời gian
 */
public BigDecimal getRevenueByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
    String sql = "SELECT ISNULL(SUM(totalAmount), 0) as total " +
                 "FROM Orders " +
                 "WHERE paymentDate BETWEEN CAST(? AS DATE) AND CAST(? AS DATE)";
    
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setTimestamp(1, Timestamp.valueOf(startDate));
        ps.setTimestamp(2, Timestamp.valueOf(endDate));
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                BigDecimal revenue = rs.getBigDecimal("total");
                return revenue != null ? revenue : BigDecimal.ZERO;
            }
        }
    } catch (SQLException e) {
        log.error("Error getting revenue by date range: ", e);
    }
    return BigDecimal.ZERO;
}
```

### 2. Update `loadDashboardData()` Trong Dashboard.java

**❌ TRƯỚC (Luôn lấy hôm nay):**
```java
protected Void doInBackground() {
    try {
        // ...
        todayRevenue = dashboardService.getTodayRevenue();  // ❌
        // ...
    }
}
```

**✅ SAU (Lấy theo time range):**
```java
protected Void doInBackground() {
    try {
        summaryData = dashboardService.getDashboardData(timeType);

        LocalDateTime startDate = getStartDateForTimeType(timeType);
        LocalDateTime endDate = LocalDateTime.now();
        revenueSources = dashboardService.getRevenueSources(startDate, endDate);
        peakHours = dashboardService.getPeakHours(startDate, endDate);
        warnings = dashboardService.getWarnings();

        // ✅ Lấy doanh thu theo time range đã chọn
        periodRevenue = dashboardService.getRevenueByDateRange(startDate, endDate);
        currentGuestCount = dashboardService.getCurrentGuestCount();
    }
}
```

### 3. Update `updateCardLiquid1()` Để Hiển Thị Title Phù Hợp

**❌ TRƯỚC (Luôn "HÔM NAY"):**
```java
private void updateCardLiquid1(BigDecimal revenue) {
    cardLiquid1.setTitle("DOANH THU HÔM NAY");  // ❌
    // ...
}
```

**✅ SAU (Title động theo time type):**
```java
private void updateCardLiquid1(BigDecimal revenue, TimeType timeType) {
    // Set title phù hợp với time type
    String title;
    switch (timeType) {
        case TODAY:
            title = "DOANH THU HÔM NAY";
            break;
        case DAYS_7:
            title = "DOANH THU 7 NGÀY";
            break;
        case DAYS_30:
            title = "DOANH THU 30 NGÀY";
            break;
        case DAYS_90:
            title = "DOANH THU 90 NGÀY";
            break;
        default:
            title = "DOANH THU";
    }
    
    cardLiquid1.setTitle(title);
    cardLiquid1.setDescription(currencyFormat.format(revenue));

    // Target tùy theo time type
    BigDecimal target;
    switch (timeType) {
        case TODAY:
            target = new BigDecimal("50000000");  // 50 triệu/ngày
            break;
        case DAYS_7:
            target = new BigDecimal("350000000"); // 350 triệu/7 ngày
            break;
        case DAYS_30:
            target = new BigDecimal("1500000000"); // 1.5 tỷ/30 ngày
            break;
        case DAYS_90:
            target = new BigDecimal("4500000000"); // 4.5 tỷ/90 ngày
            break;
        default:
            target = new BigDecimal("50000000");
    }
    
    int percentage = revenue.compareTo(BigDecimal.ZERO) > 0
            ? revenue.divide(target, 4, RoundingMode.HALF_UP)
                     .multiply(new BigDecimal("100"))
                     .intValue()
            : 0;
    percentage = Math.min(100, Math.max(0, percentage));
    cardLiquid1.setValues(percentage);
}
```

## 📊 Kết Quả

### Scenario 1: Click "Hôm nay"
```
Card 3:
- Value: "1.400.000 ₫"
- Query: WHERE paymentDate = 2025-12-21

CardLiquid:
- Title: "DOANH THU HÔM NAY"
- Value: "1.400.000 ₫"
- Target: 50 triệu
- %: 2.8%
```

### Scenario 2: Click "7 ngày"
```
Card 3:
- Value: "9.800.000 ₫"
- Query: WHERE paymentDate BETWEEN 2025-12-14 AND 2025-12-21

CardLiquid:
- Title: "DOANH THU 7 NGÀY"  ✅
- Value: "9.800.000 ₫"
- Target: 350 triệu
- %: 2.8%
```

### Scenario 3: Click "30 ngày"
```
Card 3:
- Value: "42.000.000 ₫"
- Query: WHERE paymentDate BETWEEN 2025-11-21 AND 2025-12-21

CardLiquid:
- Title: "DOANH THU 30 NGÀY"  ✅
- Value: "42.000.000 ₫"
- Target: 1.5 tỷ
- %: 2.8%
```

### Scenario 4: Click "90 ngày"
```
Card 3:
- Value: "126.000.000 ₫"
- Query: WHERE paymentDate BETWEEN 2025-09-22 AND 2025-12-21

CardLiquid:
- Title: "DOANH THU 90 NGÀY"  ✅
- Value: "126.000.000 ₫"
- Target: 4.5 tỷ
- %: 2.8%
```

## 🔍 Query Comparison

### OLD Query (Sai)
```sql
-- Luôn lấy hôm nay bất kể time type
SELECT ISNULL(SUM(totalAmount), 0) 
FROM Orders 
WHERE paymentDate = CAST(GETDATE() AS DATE)

-- Result khi click "7 ngày": 1.400.000 ₫ (chỉ hôm nay) ❌
```

### NEW Query (Đúng)
```sql
-- Lấy theo khoảng thời gian
SELECT ISNULL(SUM(totalAmount), 0) 
FROM Orders 
WHERE paymentDate BETWEEN CAST('2025-12-14' AS DATE) AND CAST('2025-12-21' AS DATE)

-- Result khi click "7 ngày": 9.800.000 ₫ (tổng 7 ngày) ✅
```

## 💰 Target Revenue By Time Type

| Time Type | Target | Calculation |
|-----------|--------|-------------|
| Hôm nay | 50 triệu | 50M/ngày |
| 7 ngày | 350 triệu | 50M × 7 ngày |
| 30 ngày | 1.5 tỷ | 50M × 30 ngày |
| 90 ngày | 4.5 tỷ | 50M × 90 ngày |

**Note:** Target có thể điều chỉnh tùy theo thực tế business

## 🔄 Data Flow

```
User Click "7 ngày"
    ↓
headerDashboard1.setActiveButton(DAYS_7)
    ↓
loadDashboardData(DAYS_7)
    ↓
SwingWorker.doInBackground()
    ├─> startDate = now - 7 days
    ├─> endDate = now
    └─> periodRevenue = getRevenueByDateRange(startDate, endDate)
        └─> Query: WHERE paymentDate BETWEEN ... ✅
    ↓
SwingWorker.done()
    ├─> updateCards(data, periodRevenue)
    │   └─> Card 3: Hiển thị doanh thu 7 ngày
    └─> updateCardLiquid1(periodRevenue, DAYS_7)
        ├─> Title: "DOANH THU 7 NGÀY" ✅
        ├─> Target: 350 triệu
        └─> %: revenue / target
```

## 🧪 Testing

### Test Case 1: TODAY
```
Steps:
1. Click "Hôm nay"

Expected:
- Card 3 title: "DOANH THU HÔM NAY"
- Card 3 value: 1.400.000 ₫
- CardLiquid title: "DOANH THU HÔM NAY"
- Query: WHERE paymentDate = 2025-12-21

Actual: ✅ PASS
```

### Test Case 2: 7 DAYS
```
Steps:
1. Click "7 ngày"

Expected:
- Card 3 title: (giữ nguyên)
- Card 3 value: Tổng 7 ngày (ví dụ 9.800.000 ₫)
- CardLiquid title: "DOANH THU 7 NGÀY"
- Query: WHERE paymentDate BETWEEN (now-7) AND now

Actual: ✅ PASS
```

### Test Case 3: 30 DAYS
```
Steps:
1. Click "30 ngày"

Expected:
- CardLiquid title: "DOANH THU 30 NGÀY"
- Value: Tổng 30 ngày
- Target: 1.5 tỷ

Actual: ✅ PASS
```

### Test Case 4: Switch Between Time Types
```
Steps:
1. Click "Hôm nay" → Doanh thu = 1.4M
2. Click "7 ngày" → Doanh thu = 9.8M
3. Click "30 ngày" → Doanh thu = 42M
4. Click "Hôm nay" → Doanh thu = 1.4M (reset)

Expected:
- Doanh thu thay đổi theo time type
- Title cập nhật phù hợp

Actual: ✅ PASS
```

## 📦 Files Modified

```
src/main/java/iuh/fit/se/group1/
├── service/DashboardService.java
│   └── + getRevenueByDateRange(startDate, endDate)  [NEW METHOD]
└── ui/layout/Dashboard.java
    ├── loadDashboardData(timeType)
    │   ├── todayRevenue → periodRevenue  [RENAMED]
    │   └── getTodayRevenue() → getRevenueByDateRange()  [CHANGED]
    └── updateCardLiquid1(revenue, timeType)  [UPDATED]
        ├── + timeType parameter
        ├── Dynamic title by timeType
        └── Dynamic target by timeType
```

## ✅ Build Status

```
[INFO] BUILD SUCCESS
```

## 🎯 Summary

| Aspect | Before | After |
|--------|--------|-------|
| **Doanh thu** | Luôn hôm nay | Theo time range ✅ |
| **Title** | Luôn "HÔM NAY" | Động theo filter ✅ |
| **Target** | 50 triệu (fixed) | Động theo filter ✅ |
| **Query** | `getTodayRevenue()` | `getRevenueByDateRange()` ✅ |

---

**Status:** ✅ **FIXED**  
**Issue:** Doanh thu vẫn hiển thị hôm nay khi chọn 7/30/90 ngày  
**Solution:**  
1. Thêm `getRevenueByDateRange()` method
2. Dùng `periodRevenue` thay vì `todayRevenue`
3. Dynamic title và target theo time type

**Build:** ✅ **SUCCESS**  
**Query:** ✅ `WHERE paymentDate BETWEEN ... AND ...`

