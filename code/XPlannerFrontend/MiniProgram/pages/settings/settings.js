var app = getApp();
// var userInfo = app.globalData.userInfo;
var userInfo = require("../../data/userInfo");

Page({
  data: {
    userInfo: userInfo,
    logined: false,
    name: "",
    password: "",
    password_visible: false,
    eye_path: "/icons/eye_close.png"
  },
  onLoad: function() {
    // console.log(this.data.userInfo.avatar);
  },

  /*
   * getName
   * 获取名称
   */
  getName: function(e) {
    this.setData({
      name: e.detail.value
    });
  },

  /*
   * getPassword
   * 获取密码
   */
  getPassword: function(e) {
    this.setData({
      password: e.detail.value
    });
  },

  /*
   * changeVisiblity
   * 改变密码是否可见
   */
  changeVisiblity: function() {
    if (this.data.password_visible) {
      this.setData({
        password_visible: false,
        eye_path: "/icons/eye_close.png"
      });
    } else {
      this.setData({
        password_visible: true,
        eye_path: "/icons/eye_open.png"
      })
    }
  }
})