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
    private String login;
    private String password;
    @Transient
    private String passwordconfirmed;

    /**
     * Persisting the User entities will persist the Role as well
     * TODO: make the difference between this statements
     * @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
     * @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Transient
    public String getPasswordconfirmed() {
        return passwordconfirmed;
    }

    public void setPasswordconfirmed(String passwordconfirmed) {
        this.passwordconfirmed = passwordconfirmed;
    }

    public Set<Role> getUserroles() {
        return roles;
    }

    public void setUserroles(Set<Role> userroles) {
        this.roles = userroles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getId(), user.getId()) &&
                Objects.equals(getLogin(), user.getLogin()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getPasswordconfirmed(), user.getPasswordconfirmed()) &&
                Objects.equals(getUserroles(), user.getUserroles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLogin(), getPassword(), getPasswordconfirmed(), getUserroles());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", passwordconfirmed='" + passwordconfirmed + '\'' +
                ", userroles=" + roles +
                '}';
    }
}
