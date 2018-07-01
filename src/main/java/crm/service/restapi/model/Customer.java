package crm.service.restapi.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;

@Entity
@Table(name = "customer")
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

	@OneToOne(cascade =  CascadeType.ALL)
	@JoinColumn(name = "picture_id")
	private Picture picture;

    @ManyToOne
    @JoinColumn(name = "creation_user_id")
	private User creationUser;

    @ManyToOne
    @JoinColumn(name = "last_mod_user_id")
    private User lastModUser;

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