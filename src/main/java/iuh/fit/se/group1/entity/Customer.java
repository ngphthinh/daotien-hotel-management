package iuh.fit.se.group1.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Customer {
    private Long customerId;
    private String fullName;
    private String phone;
    private String email;
    private String citizenId;
    private boolean gender; 
    private LocalDate dateOfBirth;
    private LocalDate createdAt;
    
    public Customer() {
    }

	public Customer(Long customerId, String fullName, String phone, String email, String citizenId, boolean gender,
			LocalDate dateOfBirth,  LocalDate createdAt) {
		this.customerId = customerId;
		this.fullName = fullName;
		this.phone = phone;
		this.email = email;
		this.citizenId = citizenId;
		this.gender = gender;
		this.dateOfBirth = dateOfBirth;
		this.createdAt = createdAt;
	}


	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}



	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}
	@Override
	public String toString() {
	    return "Customer{" + "customerId=" + customerId + ", fullName=" + fullName + ", phone=" + phone + ", email=" + email + ", citizenId=" + citizenId + ", gender=" + (gender ? "Female" : "Male") + ", dateOfBirth=" + dateOfBirth + ", createdAt=" + createdAt + '}';
	}

	@Override
	public int hashCode() {
		return Objects.hash(citizenId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return Objects.equals(citizenId, other.citizenId);
	}
    
  
}
