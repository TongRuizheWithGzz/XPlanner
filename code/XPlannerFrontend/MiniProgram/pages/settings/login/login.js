var app = getApp();
var userInfo = require("../../../data/userInfo");

Page({
  data: {
    name: "",
    password: "",
  },

  /*
   * getName
   * 获取名称
   */
  getName: function (e) {
    console.log(e.detail.value);
    this.setData({
      name: e.detail.value
    });
  },

  /*
   * getPassword
   * 获取密码
   */
  getPassword: function (e) {
    console.log(e.detail.value);
    this.setData({
      password: e.detail.value
    });
  },

  login: function () {
    console.log("login");
    /* 向后端发送用户名和密码 */
    /* 设置全局的userInfo */
    /* 跳转到schedular */

    wx.redirectTo({
      url: '/pages/schedular/schedular',
    })
  }
})