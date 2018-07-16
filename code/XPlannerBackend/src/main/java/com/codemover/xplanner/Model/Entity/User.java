package com.codemover.xplanner.Model.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String avatarUrl;
    private Collection<Scheduleitme> scheduleitmesByUserId;
    private Collection<UserFoodEaten> userFoodEatensByUserId;
    private Set<Role> roles;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column(name = "user_id")

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "user_password")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "avatar_url")
    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId &&
                Objects.equals(userName, user.userName) &&
                Objects.equals(userPassword, user.userPassword);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId, userName, userPassword);
    }

    @OneToMany(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    public Collection<Scheduleitme> getScheduleitmesByUserId() {
        return scheduleitmesByUserId;
    }

    public void setScheduleitmesByUserId(Collection<Scheduleitme> scheduleitmesByUserId) {
        this.scheduleitmesByUserId = scheduleitmesByUserId;
    }

    @OneToMany(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    public Collection<UserFoodEaten> getUserFoodEatensByUserId() {
        return userFoodEatensByUserId;
    }

    public void setUserFoodEatensByUserId(Collection<UserFoodEaten> userFoodEatensByUserId) {
        this.userFoodEatensByUserId = userFoodEatensByUserId;
    }
}
