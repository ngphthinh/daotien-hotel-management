package iuh.fit.se.group1.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Amenity {
	private Long amenityId;
    private String nameAmenity;
    private BigDecimal price;
    private LocalDate createdAt;
    
    public Amenity() {
   
    }

	public Amenity(Long amenityId,BigDecimal price) {
		this.amenityId = amenityId;
		this.price = price;
	}

	public Amenity(Long amenityId, String nameAmenity, BigDecimal price) {
		this.amenityId = amenityId;
		this.nameAmenity = nameAmenity;
		this.price = price;
	}

	public Amenity(Long amenityId, String nameAmenity, BigDecimal price, LocalDate createdAt) {
		super();
		this.amenityId = amenityId;
		this.nameAmenity = nameAmenity;
		this.price = price;
		this.createdAt = createdAt;
	}

	public Amenity(String nameAmenity, BigDecimal price) {
		this.nameAmenity = nameAmenity;
		this.price = price;
	}

    public Amenity(Long amenityId) {
		this.amenityId = amenityId;
    }

    public Long getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Long amenityId) {
		this.amenityId = amenityId;
	}

	public String getNameAmenity() {
		return nameAmenity;
	}

	public void setNameAmenity(String nameAmenity) {
		this.nameAmenity = nameAmenity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	
	@Override
	public String toString() {
	    return "Amenity{" + "amenityId=" + amenityId + ", nameAmenity=" + nameAmenity + ", price=" + price + ", createdAt=" + createdAt + '}';
	}


	@Override
	public int hashCode() {
		return Objects.hash(amenityId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Amenity other = (Amenity) obj;
		return Objects.equals(amenityId, other.amenityId);
	}

    public void setAmenityId(String maDV) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setPrice(double giaDV) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
