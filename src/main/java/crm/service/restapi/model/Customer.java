package crm.service.restapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "customer",
        indexes = {@Index(name = "IDX_CUSTOMER_ACTIVE", columnList = "active")})
@Where(clause="active=1")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "customer_id")
	private long id;

    @Column(name = "name", nullable = false)
	@NotBlank
	private String name;

    @Column(name = "surname", nullable = false)
	@NotBlank
	private String surname;

	@Column(name = "city")
	private String city;

	@Column(name = "address")
	private String address;

    @Column(name = "zipCode")
    private Integer zipCode;

	@OneToOne(cascade =  CascadeType.ALL)
	@JoinColumn(name = "picture_id")
	private Picture picture;

    @ManyToOne
    @JoinColumn(name = "creation_user_id", nullable = false)
	private User creationUser;

    @ManyToOne
    @JoinColumn(name = "last_mod_user_id", nullable = false)
    private User lastModUser;

	@Column(name = "active", nullable = false)
    @JsonIgnore
    private Boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public Picture getPicture() {
		return picture;
	}

	public void setPicture(Picture picture) {
		this.picture = picture;
	}

    public User getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(User creationUser) {
        this.creationUser = creationUser;
    }

    public User getLastModUser() {
        return lastModUser;
    }

    public void setLastModUser(User lastModUser) {
        this.lastModUser = lastModUser;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", picture=" + picture +
				", creationUser=" + creationUser +
				", lastModUser=" + lastModUser +
				'}';
	}
}
