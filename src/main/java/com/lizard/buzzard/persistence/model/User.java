package com.lizard.buzzard.persistence.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstname;
    private String lastname;

    private String password;
    @Transient
    private String passwordconfirmed;
    private String email;

    /**
     * Persisting the User entities will persist the Role as well
     * TODO: make the difference between this statements
     * @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
     * @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lasttname) {
        this.lastname = lasttname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Transient
    public String getPasswordconfirmed() {
        return passwordconfirmed;
    }

    public void setPasswordconfirmed(String passwordconfirmed) {
        this.passwordconfirmed = passwordconfirmed;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> userroles) {
        this.roles = userroles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getFirstname(), user.getFirstname()) &&
                Objects.equals(getLastname(), user.getLastname()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getPasswordconfirmed(), user.getPasswordconfirmed()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getRoles(), user.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getPassword(), getPasswordconfirmed(), getEmail(), getRoles());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lasttname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", passwordconfirmed='" + passwordconfirmed + '\'' +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
