package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    private Integer userId;
    private String userName;
    private String userPassword;
    private String avatarUrl;
    private Set<Scheduleitme> scheduleitmes;
    private Set<UserFoodEaten> foodEatens;
    private Set<Role> roles;
    private boolean enabled;

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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public Set<Scheduleitme> getScheduleitmes() {
        return scheduleitmes;
    }

    public void setScheduleitmes(Set<Scheduleitme> scheduleitmes) {
        this.scheduleitmes = scheduleitmes;
    }

    @OneToMany(mappedBy = "userByUserId", fetch = FetchType.LAZY)
    public Set<UserFoodEaten> getFoodEatens() {
        return foodEatens;
    }

    public void setFoodEatens(Set<UserFoodEaten> foodEatens) {
        this.foodEatens = foodEatens;
    }

    @Basic
    @Column(name = "enabled")
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
