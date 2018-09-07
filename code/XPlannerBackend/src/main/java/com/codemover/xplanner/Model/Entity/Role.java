package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;

@Entity
public class Role {
    private Integer id;
    private String rolename;

    public Role(String rolename) {
        this.rolename = rolename;
    }

    public Role(){

    }

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    @Basic
    @Column(name = "role_name")
    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }


}
