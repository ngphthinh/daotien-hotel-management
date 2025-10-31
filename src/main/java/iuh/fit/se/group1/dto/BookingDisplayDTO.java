package iuh.fit.se.group1.dto;

public class BookingDisplayDTO {
    private Long bookingId;
    private String roomNumber;
    private String customerName;
    private String phoneNumber;

    // Constructors
    public BookingDisplayDTO(Long bookingId, String roomNumber, String customerName, String phoneNumber) {
        this.bookingId = bookingId;
        this.roomNumber = roomNumber;
        this.customerName = customerName;
        this.phoneNumber = phoneNumber;
    }

    // Getters & setters
    public Long getBookingId() { return bookingId; }
    public String getRoomNumber() { return roomNumber; }
    public String getCustomerName() { return customerName; }
    public String getPhoneNumber() { return phoneNumber; }
}
