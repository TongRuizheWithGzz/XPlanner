var app = getApp();
Page({
  data: {
    item: {},
    id: 0,
  },

  onLoad: function (options) {
    this.setData({
      item: app.globalData.scheduleItems[options.id],
      id: options.id,
    })
  },

  onShow: function (option) {
    if (app.globalData.ifChangeSchedule) { // 从add页面返回并且修改日程
      this.setData({
        item: app.globalData.scheduleItems[app.globalData.changeScheduleIndex],
      });
    }
  },

  edit: function () {
    wx.redirectTo({
      url: "/pages/schedular/addSchedule/add?id=" + this.data.id
    });
  }
})