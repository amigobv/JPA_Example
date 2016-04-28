package swt6.orm.domain.annotated;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class Address implements Serializable {
	private static final long serialVersionUID = 1L;

	private String zipCode;
	private String city;
	private String street;

	// classes persisted by Hibernate must have a default constructor
	// (newInstance of reflection API)
	public Address() {
	}

	public Address(String zipCode, String city, String street) {
		this.zipCode = zipCode;
		this.city = city;
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Override
	public boolean equals(Object obj) {
		Address adr = (Address) obj;

		return zipCode.equals(adr.zipCode) && city.equals(adr.city)
				&& street.equals(adr.street);
	}

	@Override
	public String toString() {
		return zipCode + " " + city + ", " + street;
	}
}
