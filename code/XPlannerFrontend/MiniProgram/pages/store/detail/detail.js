// pages/store/detail/detail.js
var app = getApp();
var ex_tools = require("../../../common/extension.js")

var extensions = ex_tools.warpExtensions(require("../../../data/extensions.js"))
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
      // extension: extensions[options.id],
      extension:app.globalData.extensions[options.id],
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
    console.log("dsa",this.data.extension);
  }
})