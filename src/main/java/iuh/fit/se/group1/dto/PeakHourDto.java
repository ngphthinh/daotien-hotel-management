package iuh.fit.se.group1.dto;

/**
 * DTO cho khung giờ cao điểm
 */
public class PeakHourDto {
    private String hour;
    private int bookingCount;

    public PeakHourDto() {
    }

    public PeakHourDto(String hour, int bookingCount) {
        this.hour = hour;
        this.bookingCount = bookingCount;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getBookingCount() {
        return bookingCount;
    }

    public void setBookingCount(int bookingCount) {
        this.bookingCount = bookingCount;
    }
}

