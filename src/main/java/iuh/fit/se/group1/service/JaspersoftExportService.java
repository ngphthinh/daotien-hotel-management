package iuh.fit.se.group1.service;

import iuh.fit.se.group1.config.AppLogger;
import iuh.fit.se.group1.dto.OrderDTO;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.util.Constants;
import iuh.fit.se.group1.util.MoneyToTextUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JaspersoftExportService {
    private static final Logger log = LoggerFactory.getLogger(JaspersoftExportService.class);
    private final OrderService orderService;
    private static final String OUTPUT_DIR = getJarDirectory() + File.separator + "hoadon";
    private static final String TEMPLATE_PATH = "info/daotien.jasper";

    /**
     * Get the directory where the JAR file is located
     *
     * @return Path to the JAR directory, or current working directory if not running from JAR
     */
    private static String getJarDirectory() {
        try {
            String jarPath = JaspersoftExportService.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath();

            File jarFile = new File(jarPath);

            // If running from JAR, get parent directory
            if (jarFile.isFile()) {
                return jarFile.getParent();
            }
            // If running from IDE (classes directory), use current working directory
            return System.getProperty("user.dir");
        } catch (Exception e) {
            AppLogger.info("Could not determine JAR location, using current directory: {}", e.getMessage());
            return System.getProperty("user.dir");
        }
    }

    // Static block to register fonts early
    static {
        registerDejaVuFonts();
    }

    public JaspersoftExportService() {
        this.orderService = new OrderService();
    }

    /**
     * Register DejaVu Sans fonts with AWT GraphicsEnvironment
     * This ensures JasperReports can measure and render text correctly
     */
    private static void registerDejaVuFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

            // Register DejaVu Sans Regular
            InputStream regularFont = JaspersoftExportService.class
                    .getClassLoader()
                    .getResourceAsStream("fonts/DejaVuSans.ttf");
            if (regularFont != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, regularFont);
                ge.registerFont(font);
                regularFont.close();
                AppLogger.info("Registered DejaVu Sans Regular font");
            }

            // Register DejaVu Sans Bold
            InputStream boldFont = JaspersoftExportService.class
                    .getClassLoader()
                    .getResourceAsStream("fonts/DejaVuSans-Bold.ttf");
            if (boldFont != null) {
                Font font = Font.createFont(Font.TRUETYPE_FONT, boldFont);
                ge.registerFont(font);
                boldFont.close();
                AppLogger.info("Registered DejaVu Sans Bold font");
            }
        } catch (Exception e) {
            AppLogger.info("Warning: Could not register DejaVu fonts: {}", e.getMessage());
            // Don't throw - let JasperReports try to use the fonts anyway
        }
    }

    public void exportOrderToPdf(Long orderId, String promotion, String paymentType, String totalPricePayment, String employeeName) {
        OrderDTO order = orderService.getOrderById(orderId);
        System.out.println(order);
        String rooms = order.getBookings().stream()
                .map(e -> e.getRoom().getRoomNumber()).collect(Collectors.joining(", "));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        String checkIn = order.getBookings().get(0).getCheckInDate().format(dtf);
        String checkOut = order.getBookings().get(0).getCheckOutDate().format(dtf);
        String bookingType = order.getBookings().get(0).getBookingType().getDisplayName();

        double totalPricePaymentDouble = Constants.parseVND(totalPricePayment);
        Map<String, Object> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("customerName", order.getCustomer().getFullName());
        params.put("phoneCustomer", order.getCustomer().getPhone());
        params.put("rooms", rooms);
        params.put("checkInDate", checkIn);
        params.put("checkOutDate", checkOut);
        params.put("bookingType", bookingType);
        params.put("paymentType", paymentType);
        params.put("promotion", promotion);
        params.put("totalPrice", Constants.VND_FORMAT.format(order.getTotalAmount()));
        params.put("deposit", Constants.VND_FORMAT.format(order.getDeposit()));
        params.put("totalPricePayment", totalPricePayment);
        params.put("paymentDate", order.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        params.put("totalPaymentStr", MoneyToTextUtil.convert(totalPricePaymentDouble));
        params.put("employeeName", employeeName);
        params.put("ITEM_DS", getListInvoice(order));

        try {

            InputStream is = getClass()
                    .getClassLoader()
                    .getResourceAsStream(TEMPLATE_PATH);

            if (is == null) {
                throw new RuntimeException("Không tìm thấy file mẫu báo cáo: " + TEMPLATE_PATH);
            }

            JasperReport report = (JasperReport) JRLoader.loadObject(is);
            JasperPrint print = JasperFillManager.fillReport(
                    report,
                    params,
                    new JREmptyDataSource()
            );

            // Create hoadon directory if it doesn't exist
            File outputDir = new File(OUTPUT_DIR);
            if (!outputDir.exists()) {
                outputDir.mkdirs();
                AppLogger.info("Created invoice directory at: {}", OUTPUT_DIR);
            }

            String fileName = "hoadon_" + orderId + "_" +
                    order.getPaymentDate().format(DateTimeFormatter.ofPattern("ddMMyyyy")) + ".pdf";
            String filePath = OUTPUT_DIR + File.separator + fileName;

            JasperExportManager.exportReportToPdfFile(print, filePath);
            AppLogger.info("Invoice exported successfully to: {}", filePath);

        } catch (Exception e) {
            AppLogger.info("Lỗi khi xuất báo cáo hóa đơn: {}", e.getMessage());
            throw new RuntimeException(e);
        }

    }

    private JRBeanCollectionDataSource getListInvoice(OrderDTO order) {
        return new JRBeanCollectionDataSource(orderService.getInvoiceItems(order));
    }


}
