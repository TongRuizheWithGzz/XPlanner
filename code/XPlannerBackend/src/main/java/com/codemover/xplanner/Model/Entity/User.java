package com.codemover.xplanner.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    private Integer userId;
    private String userName;
    @JsonIgnore
    private String userPassword;
    private String avatarUrl;
    @JsonIgnore
    private Set<Scheduleitme> scheduleitmes;
    @JsonIgnore
    private Set<UserFoodEaten> foodEatens;
    @JsonIgnore
    private Set<Role> roles;
    @JsonIgnore
    private boolean enabled;
    @JsonIgnore
    private Timestamp last_keeper_fresh;

    @JsonIgnore
    private Set<KeeperRecommand> keeperRecommands;


    @JsonIgnore
    private Set<Plannerstore> plannerstores;

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

    @Basic
    @Column(name = "last_keeper_fresh")
    public Timestamp getLast_keeper_fresh() {
        return last_keeper_fresh;
    }

    public void setLast_keeper_fresh(Timestamp last_keeper_fresh) {
        this.last_keeper_fresh = last_keeper_fresh;
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


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_planner_settings",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "plannerid")}
    )
    public Set<Plannerstore> getPlannerstores() {
        return plannerstores;
    }

    public void setPlannerstores(Set<Plannerstore> plannerstores) {
        this.plannerstores = plannerstores;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    public Set<Scheduleitme> getScheduleitmes() {
        return scheduleitmes;
    }

    public void setScheduleitmes(Set<Scheduleitme> scheduleitmes) {
        this.scheduleitmes = scheduleitmes;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<KeeperRecommand> getKeeperRecommands() {
        return keeperRecommands;
    }

    public void setKeeperRecommands(Set<KeeperRecommand> keeperRecommands) {
        this.keeperRecommands = keeperRecommands;
    }

}
