package iuh.fit.se.group1;

import iuh.fit.se.group1.enums.BookingType;
import iuh.fit.se.group1.enums.PaymentType;
import iuh.fit.se.group1.service.JaspersoftExportService;
import iuh.fit.se.group1.util.Constants;
import iuh.fit.se.group1.util.InvoiceItem;
import iuh.fit.se.group1.util.MoneyToTextUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestPrint {

    public static void main(String[] args) throws Exception {
        JaspersoftExportService exportService = new JaspersoftExportService();
        Long orderId = 23L;
        String promotion = "-10%";
        String paymentType = PaymentType.CASH.getName();
        String totalPricePayment = Constants.VND_FORMAT.format(900_000);
        String employeeName = "Nguyễn Phước Thịnh";
        exportService.exportOrderToPdf(orderId, promotion, paymentType, totalPricePayment, employeeName);
    }
}
