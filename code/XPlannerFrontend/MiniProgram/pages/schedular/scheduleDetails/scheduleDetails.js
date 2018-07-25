var app = getApp();
Page({
  data: {
    item: {},
    id: 0,
    ifSpider: false,
  },

  onLoad: function (options) {
    if (options.spiderIndex) {
      console.log(app.globalData.spiderItems);
      console.log(options.pageNumber);
      console.log(options.spiderIndex);
      this.setData({
        // item: app.globalData.spiderItems[options.spiderIndex],
        item: app.globalData.spiderItems[options.pageNumber - 1][options.spiderIndex],
        id: options.spiderIndex,
        ifSpider: true,
      })
    } else {
      this.setData({
        item: app.globalData.scheduleItems[options.id],
        id: options.id,
      })
    }
  },

  onShow: function (option) {
    if (app.globalData.ifChangeSchedule) { // 从add页面返回并且修改日程
      this.setData({
        item: app.globalData.scheduleItems[app.globalData.changeScheduleIndex],
      });
    }
  },

  edit: function () {
    wx.navigateTo({
      url: "/pages/schedular/addSchedule/add?id=" + this.data.id
    });
  }
})