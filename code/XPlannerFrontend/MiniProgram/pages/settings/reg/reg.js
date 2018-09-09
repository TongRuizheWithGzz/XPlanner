import moment from "moment";
var app = getApp();
var ExtensionPack = require("../../../common/extension");
var raw_extensions = require("../../../data/extensions");

Page({
  data: {
    name: "",
    password: "",
    repeated_password: "",
  },

  getName: function(e) {
    this.setData({
      name: e.detail.value
    });
  },

  getPassword: function(e) {
    this.setData({
      password: e.detail.value
    });
  },

  repeatPassword: function(e) {
    this.setData({
      repeated_password: e.detail.value
    });
  },

  register: function() {
    console.log("name ", this.data.name);
    console.log("password ", this.data.password);
    console.log("repeadted password ", this.data.repeated_password);
    if (this.data.password == this.data.repeated_password) { // 两次输入密码一致
      // 向后端发送消息，注意最好在注册过程中来个wx.showLoading，另外showLoading必须要手动调用wx.hideLoading才能停止
      wx.showLoading({
        title: "正在注册",
        mask: true
      })
      // 注册成功
      let data = {}; // 返回数据（至少要有用户信息）
      // 初始化数据
      app.globalData.date = moment().format("YYYY-MM-DD");
      app.globalData.year = (new Date()).getFullYear();
      app.globalData.month = (new Date()).getMonth() + 1;
      app.globalData.day = (new Date()).getDate();
      wx.setStorageSync("date", app.globalData.date);

      app.globalData.scheduleItems = [];
      app.globalData.dayWithItem = {};
      app.globalData.userInfo = data.userInfo; // 如果没有返回信息，则此处需要修改
      app.globalData.extensions = ExtensionPack.filterExtensions(
        ExtensionPack.warpExtensions(raw_extensions),
        [] // 指示哪些扩展是安装了的数组，注意需要按照后端的格式进行修改，查看filterExtensions如何使用查看/common/extension.js
      )
      app.globalData.userFoodEaten = [];
      app.globalData.logined = true;
      console.log(app.globalData);
      wx.showToast({
        title: "注册成功",
        icon: "success",
        duration: 3000
      })
      // 注册失败，可能有不同注册失败的理由
      wx.showModal({
        title: "",
        content: "",
        showCancel: false,
      })
    } else {
      wx.showModal({
        title: "密码不一致",
        content: "您两次输入的密码不一致，请检查后再次尝试。",
        showCancel: false,
      })
    }
  }
})