//index.js
//获取应用实例
const app = getApp()
var api = require("../../interface/config/api.js");
Page({
  data: {
    avatarUrl: "/icons/loading.gif",
    nickName: "",
    classNumber: "",
    studentId: "",
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo')
  },
  //事件处理函数
  bindViewTap: function() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  onLoad: function() {
    var that = this;
    wx.getUserInfo({
      success: (res) => {
        that.setData({
          nickName: res.userInfo.nickName,
          avatarUrl: res.userInfo.avatarUrl,
        })
      }
    })
    if (app.globalData.openId) {
      wx.request({
        url: api.getClassAndId,
        method: 'GET',
        data: {
          openId: app.globalData.openId,
        },
        success: (res) => {
          if (res.code = 200) {
            this.setData({
              classNumber: res.data.user.classNumber,
              studentId: res.data.user.studentId,
            })
          }
        }
      })
    }
  },

})