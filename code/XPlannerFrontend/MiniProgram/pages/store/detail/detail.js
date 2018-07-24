// pages/store/detail/detail.js
var app = getApp();
var extensions = app.globalData.extensions;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    extension: {},
    id: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      extension: extensions[options.id],
      id: options.id
    })
  },
  back: function () {
    wx.navigateBack({})
  },
  open: function (e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url,
    })
  },
  install: function (e) {
    console.log(app.globalData.extensions);
    var id = this.data.id;
    app.globalData.extensions[id].visible = true;
    this.setData({
      extension: app.globalData.extensions[id]
    })
    wx.showToast({
      title: "已完成",
      icon: "success",
      duration: 3000
    });
  }
})