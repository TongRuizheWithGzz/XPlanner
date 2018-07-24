var app = getApp();
var extensions = app.globalData.extensions;
var time = require("../../common/time");
var date = new Date();

Page({
  data: {
    time: time.getDateStringWithZero(date.getFullYear(), date.getMonth() + 1, date.getDate()),
    extensions: extensions,
  },
  onShow: function () {
    this.setData({
      extensions: app.globalData.extensions,
    })
  },
  open: function(e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url,
    })
  },
  show: function(e) {
    wx.navigateTo({
      url: '/pages/store/detail/detail?id=' + e.currentTarget.dataset.id,
    })
  },
  install:function(e){
    app.globalData.extensions[e.currentTarget.dataset.id].visible = true;
    this.setData({
      extensions: app.globalData.extensions
    })
    wx.showToast({
      title: "已完成",
      icon: "success",
      duration: 3000
    });
  }
})