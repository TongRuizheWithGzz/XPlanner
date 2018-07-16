package com.codemover.xplanner.Model.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Role {
    private Integer id;
    private String status;
    private String rolename;
    private User user;

    public Role(String status, String rolename) {
        this.status = status;
        this.rolename = rolename;
    }

    @Id
    @Column(name = "role_ud")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "role_name")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (id != null ? !id.equals(role.id) : role.id != null) return false;
        if (status != null ? !status.equals(role.status) : role.status != null) return false;
        if (rolename != null ? !rolename.equals(role.rolename) : role.rolename != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (rolename != null ? rolename.hashCode() : 0);
        return result;
    }
}
