var app = getApp();
var scheduleItems = app.globalData.scheduleItems;

Page({
  data: {
    startDate: "选择日期",
    startTime: "选择时间",
    endDate: "选择日期",
    endTime: "选择时间",
    title: "",
    description: "",
    address: "",
    start_time: "",
    end_time: "",
    showModal: true,
    msg:'jjj',
  },
  submit: function () {
    this.setData({
      showModal: true
    })
  },

  preventTouchMove: function () {

  },


  go: function () {
    this.setData({
      showModal: false
    })
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
  save: function () {
    var item = {
      title: this.data.title,
      start_time: this.data.startDate + " " + this.data.startTime,
      end_time: this.data.endDate + " " + this.data.endTime,
      description: this.data.description,
      address: this.data.address,
      user_id: app.globalData.userInfo.id,
    };

    /* 向后端发送请求，获取schduleItem_id，增加对应的项目，注意可能有错误处理 */
    item.scheduleItem_id = 19;
    item.start_concret_time = this.data.startTime;
    item.end_concret_time = this.data.endTime;

    var tmp = app.globalData.scheduleItems;
    tmp.push(item);
    app.globalData.scheduleItems = tmp;
    app.globalData.ifAddSchedule = true;
    app.globalData.ifSameDay = (app.globalData.date == item.start_time.slice(0, 10));
    // console.log(app.globalData.scheduleItems);
    wx.navigateBack({
      delta: 1,
    });
    this.setData({
      showModal:true,
      msg:"是否保存信息?",
    })
  },
  discard: function () {
    console.log("discard");
    wx.navigateBack({
      delta: 1,
    });
    this.setData({
      showModal:true,
      msg:"是否放弃信息？"
    })
  }
})