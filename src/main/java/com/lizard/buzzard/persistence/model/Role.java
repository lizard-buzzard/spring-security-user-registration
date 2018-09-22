package com.lizard.buzzard.persistence.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * model has a unique business key which is marked with the Hibernate-specific @NaturalId annotation
     */
    @NaturalId
    private String rolename;
    /**
     * in this bidirectional relationship, the User model own the association. This is needed since only one side can own
     * a relationship, and changes are only propagated to the database from this particular side
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId()) &&
                Objects.equals(getRolename(), role.getRolename()) &&
                Objects.equals(getUsers(), role.getUsers());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRolename(), getUsers());
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
                ", users=" + users +
                '}';
    }
}
