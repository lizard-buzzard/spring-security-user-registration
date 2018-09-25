package com.lizard.buzzard.persistence.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    @Column(length = 60)
    private String password;

    @Transient
    private String passwordconfirmed;

    @Column(length = 60)
    private String newPassword;

    @NotNull
    private String email;

    @Column
    private boolean enabled;

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
    private List<Role> roles;

    public User() {
        super();
        this.enabled = false;
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

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> userroles) {
        this.roles = userroles;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getEmail(), user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEmail());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lasttname='" + lastname + '\'' +
                ", password='" + password + '\'' +
//                ", passwordconfirmed='" + passwordconfirmed + '\'' +
                ", email='" + email + '\'' +
//                ", roles=" + roles +
                '}';
    }
}
