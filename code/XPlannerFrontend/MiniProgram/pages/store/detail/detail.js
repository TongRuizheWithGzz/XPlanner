// pages/store/detail/detail.js
var app = getApp();
var extensions = app.globalData.extensions;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    extension:{},
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      extension:extensions[options.id]
    })
  },
  back:function(){
    wx.navigateBack({})
  },
  open: function (e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url,
    })
  },
  install: function (e) {

  }
})