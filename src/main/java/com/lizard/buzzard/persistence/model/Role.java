package com.lizard.buzzard.persistence.model;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.List;
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

    @ManyToMany
    @JoinTable(name="role_privilege",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "priv_id", referencedColumnName = "id"))
    private List<RolesPrivilege> privileges;

    public Role() {
        super();
    }

    public Role(String rolename, List<RolesPrivilege> privileges) {
        super();
        this.rolename = rolename;
        this.privileges = privileges;
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

    public List<RolesPrivilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<RolesPrivilege> privileges) {
        this.privileges = privileges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return Objects.equals(getRolename(), role.getRolename());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRolename());
    }

    /**
     * SEE: https://github.com/rzwitserloot/lombok/issues/1007
     * @return
     */
    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", rolename='" + rolename + '\'' +
//                ", users=" + users +
//                ", privileges=" + privileges +
                '}';
    }
}
