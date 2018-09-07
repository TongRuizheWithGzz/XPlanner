package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "Weixin_user")
public class WeixinUser extends User {
    @Basic
    @Column(name = "nick_name")
    private String nickName;

    @Basic
    @Column(name = "gender")
    private String gender;



}


