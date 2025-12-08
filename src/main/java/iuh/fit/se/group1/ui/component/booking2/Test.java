package iuh.fit.se.group1.ui.component.booking2;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Test {
    public static void main(String[] args) {

        String checkInDate = "08/12/2025 14:00";
        String checkOutDate = "12/12/2025 12:00";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime checkIn = LocalDateTime.parse(checkInDate, formatter);
        LocalDateTime checkOut = LocalDateTime.parse(checkOutDate, formatter);

        long daysBetween = ChronoUnit.DAYS.between(checkIn, checkOut) + 1;
        System.out.println(daysBetween);
    }
}
