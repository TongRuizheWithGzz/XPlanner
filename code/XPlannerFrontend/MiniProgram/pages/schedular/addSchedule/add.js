var app = getApp();

Page({
  data: {
    startDate: "选择日期",
    startTime: "选择时间",
    endDate: "选择日期",
    endTime: "选择时间",
    title: "",
    description: "",
    address: ""
  },
  onLoad: function (option) {
    this.setData({
      startDate: "选择日期",
      startTime: "选择时间",
      endDate: "选择日期",
      endTime: "选择时间",
      title: "",
      description: "",
      address: ""
    })
  },
  titleIp: function (e) {
    this.setData({
      title: e.detail.value
    })
  },
  descriptionIp: function (e) {
    this.setData({
      description: e.detail.value
    })
  },
  addressIp: function (e) {
    this.setData({
      address: e.detail.value
    })
  },
  changeSD: function (e) {
    this.setData({
      startDate: e.detail.value,
    })
  },
  changeST: function (e) {
    this.setData({
      startTime: e.detail.value,
    })
  },
  changeED: function (e) {
    this.setData({
      endDate: e.detail.value,
    })
  },
  changeET: function (e) {
    this.setData({
      endTime: e.detail.value,
    })
  },
  add: function () {
    var item = {
      title: this.data.title,
      start_time: this.data.start_time,
      end_time: this.data.end_time,
      description: this.data.description,
      address: this.data.address,
      user_id: app.globalData.userInfo.id,
    };

    /* 向后端发送请求，获取schduleItem_id */
    item.scheduleItem_id = 19;
    console.log("add");
    console.log(item);

    var tmp = app.globalData.scheduleItems;
    tmp.push(item);
    app.globalData.scheduleItems = tmp;
    wx.navigateBack({
      delta: 1,
    })
  },
  discard: function () {
    console.log("discard");
    wx.navigateBack({
      delta: 1,
    })
  }
})