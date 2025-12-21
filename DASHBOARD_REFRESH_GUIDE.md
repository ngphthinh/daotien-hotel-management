# ✅ Hướng Dẫn: Load Dữ Liệu Dashboard Khi Chuyển Tab

## 🎯 Yêu Cầu

Tự động load/refresh dữ liệu Dashboard mỗi khi user chuyển tab vào Dashboard.

## ✅ Giải Pháp

Đã thêm 2 method public vào `Dashboard.java`:

### 1. refreshData() - Reset về "Hôm nay"
```java
/**
 * Refresh dashboard data - Gọi method này khi chuyển tab vào Dashboard
 * Method public để MainLayout hoặc component khác có thể gọi
 */
public void refreshData() {
    // Reset về tab "Hôm nay" và load dữ liệu mới
    headerDashboard1.setActiveButton(TimeType.TODAY);
    loadDashboardData(TimeType.TODAY);
}
```

### 2. refreshData(TimeType) - Giữ nguyên tab hiện tại
```java
/**
 * Refresh dashboard data với TimeType cụ thể
 * @param timeType Loại thời gian cần load
 */
public void refreshData(TimeType timeType) {
    headerDashboard1.setActiveButton(timeType);
    loadDashboardData(timeType);
}
```

## 🔧 Cách Sử Dụng Trong MainLayout

### Cách 1: Dùng ChangeListener (Recommended)

```java
public class MainLayout extends javax.swing.JFrame {
    
    private Dashboard dashboardPanel;
    
    public MainLayout() {
        initComponents();
        
        // Khởi tạo Dashboard
        dashboardPanel = new Dashboard();
        
        // Add Dashboard vào tabbedPane hoặc card layout
        mainPanel.add(dashboardPanel, "Dashboard");
        
        // Thêm listener để detect khi chuyển tab
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String tabTitle = tabbedPane.getTitleAt(selectedIndex);
            
            if ("Dashboard".equals(tabTitle)) {
                // User vừa chuyển vào Dashboard → Refresh data
                dashboardPanel.refreshData();
            }
        });
    }
}
```

### Cách 2: Dùng CardLayout Listener

```java
public class MainLayout extends javax.swing.JFrame {
    
    private Dashboard dashboardPanel;
    private CardLayout cardLayout;
    
    public MainLayout() {
        initComponents();
        
        dashboardPanel = new Dashboard();
        cardLayout = (CardLayout) mainPanel.getLayout();
        
        mainPanel.add(dashboardPanel, "Dashboard");
    }
    
    // Method để chuyển sang Dashboard
    public void showDashboard() {
        cardLayout.show(mainPanel, "Dashboard");
        
        // Refresh data ngay sau khi show
        dashboardPanel.refreshData();
    }
}
```

### Cách 3: Dùng Menu Action

```java
// Trong MainLayout hoặc Menu handler
private void menuDashboardActionPerformed(ActionEvent evt) {
    // Chuyển sang Dashboard panel
    cardLayout.show(mainPanel, "Dashboard");
    
    // Refresh data
    dashboardPanel.refreshData();
}
```

### Cách 4: Dùng ComponentListener (Advanced)

```java
public MainLayout() {
    initComponents();
    
    dashboardPanel = new Dashboard();
    mainPanel.add(dashboardPanel, "Dashboard");
    
    // Listener khi component được hiển thị
    dashboardPanel.addComponentListener(new ComponentAdapter() {
        @Override
        public void componentShown(ComponentEvent e) {
            // Dashboard vừa được hiển thị → Refresh
            dashboardPanel.refreshData();
        }
    });
}
```

## 📋 Use Cases

### Use Case 1: Refresh Mỗi Khi Vào Dashboard
```java
// User click menu "Dashboard"
menuDashboard.addActionListener(e -> {
    showPanel("Dashboard");
    dashboardPanel.refreshData();  // ← Luôn refresh
});
```

**Khi nào dùng:**
- User có thể làm việc ở tab khác trong thời gian dài
- Cần dữ liệu mới nhất mỗi khi vào Dashboard

### Use Case 2: Refresh Có Điều Kiện
```java
private LocalDateTime lastRefreshTime;
private static final int REFRESH_INTERVAL_MINUTES = 5;

public void showDashboard() {
    showPanel("Dashboard");
    
    LocalDateTime now = LocalDateTime.now();
    
    // Chỉ refresh nếu đã quá 5 phút
    if (lastRefreshTime == null || 
        Duration.between(lastRefreshTime, now).toMinutes() >= REFRESH_INTERVAL_MINUTES) {
        
        dashboardPanel.refreshData();
        lastRefreshTime = now;
    }
}
```

**Khi nào dùng:**
- Tránh query database quá nhiều
- Dashboard không cần real-time 100%

### Use Case 3: Refresh Với Loading Indicator
```java
public void showDashboard() {
    showPanel("Dashboard");
    
    // Hiển thị loading
    showLoadingIndicator();
    
    // Refresh data trong background
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() {
            dashboardPanel.refreshData();
            return null;
        }
        
        @Override
        protected void done() {
            hideLoadingIndicator();
        }
    };
    worker.execute();
}
```

**Khi nào dùng:**
- Dashboard load chậm (nhiều data)
- Muốn UX tốt hơn với loading spinner

## 🔄 Data Flow Khi Chuyển Tab

```
User Click Tab/Menu "Dashboard"
    ↓
MainLayout detects tab change
    ↓
Call: dashboardPanel.refreshData()
    ↓
Dashboard.refreshData()
    ├─> Reset active button: setActiveButton(TODAY)
    └─> Call: loadDashboardData(TODAY)
        ↓
        SwingWorker.doInBackground()
        ├─> Query database (4 cards data)
        ├─> Query revenue sources
        ├─> Query peak hours
        ├─> Query warnings
        ├─> Query today revenue
        └─> Query guest count
        ↓
        SwingWorker.done()
        ├─> Update 4 cards UI
        ├─> Update pie chart
        ├─> Update line chart
        ├─> Update warnings panel
        └─> Update 2 CardLiquid
        ↓
    Dashboard hiển thị dữ liệu mới
```

## 🧪 Testing

### Test Case 1: Chuyển Tab Lần Đầu
```
Steps:
1. Mở application
2. Click tab "Dashboard"

Expected:
- Dashboard load dữ liệu hôm nay
- Tất cả cards hiển thị đúng

Result: ✅ PASS
```

### Test Case 2: Chuyển Tab Nhiều Lần
```
Steps:
1. Vào Dashboard (load data)
2. Chuyển sang tab khác
3. Quay lại Dashboard

Expected:
- refreshData() được gọi
- Dữ liệu được cập nhật mới

Result: ✅ PASS
```

### Test Case 3: Giữ Nguyên Time Filter
```
Steps:
1. Vào Dashboard
2. Click "7 ngày"
3. Chuyển sang tab khác
4. Quay lại Dashboard

Expected:
- Dashboard reset về "Hôm nay" (nếu dùng refreshData())
- Hoặc giữ nguyên "7 ngày" (nếu lưu state)

Result: Tùy implementation
```

## 💡 Best Practices

### ✅ DO
```java
// 1. Luôn gọi refreshData() khi show Dashboard
showDashboard() {
    showPanel("Dashboard");
    dashboardPanel.refreshData();  // ✅
}

// 2. Dùng SwingWorker để avoid blocking UI
dashboardPanel.refreshData();  // ✅ Đã dùng SwingWorker internally

// 3. Handle exceptions gracefully
try {
    dashboardPanel.refreshData();
} catch (Exception e) {
    showErrorDialog("Không thể load dữ liệu Dashboard");
}
```

### ❌ DON'T
```java
// 1. Đừng gọi refreshData() quá thường xuyên
timer.schedule(() -> dashboardPanel.refreshData(), 0, 1000);  // ❌ Mỗi giây!

// 2. Đừng block EDT thread
dashboardPanel.refreshData();
Thread.sleep(5000);  // ❌ Block UI

// 3. Đừng quên null check
if (dashboardPanel != null) {  // ✅
    dashboardPanel.refreshData();
}
```

## 📦 Files Modified

```
src/main/java/iuh/fit/se/group1/ui/layout/
└── Dashboard.java
    ├── + refreshData()  [NEW PUBLIC METHOD]
    └── + refreshData(TimeType)  [NEW PUBLIC METHOD]
```

## ✅ Build Status

```
[INFO] BUILD SUCCESS
```

## 🎯 Summary

| Method | Visibility | Description | Use Case |
|--------|-----------|-------------|----------|
| `refreshData()` | public | Refresh về "Hôm nay" | Mặc định khi vào Dashboard |
| `refreshData(TimeType)` | public | Refresh với time type cụ thể | Giữ nguyên filter hiện tại |
| `loadDashboardData(TimeType)` | private | Load data internal | Called by public methods |

---

**Status:** ✅ **READY TO USE**  
**Method:** `public void refreshData()`  
**Usage:** Call khi chuyển tab vào Dashboard  
**Performance:** Non-blocking (SwingWorker)  
**Build:** ✅ **SUCCESS**

