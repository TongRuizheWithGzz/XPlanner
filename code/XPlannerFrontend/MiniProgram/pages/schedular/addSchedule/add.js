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
    showModal: true,
    msg: 'jjj',
    ifAddPage: true,
    itemIndex: 0,
    ifChangeScheduleStartDate: false,
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
    if (option.id) { // 如果是修改页面
      var tmp_item = app.globalData.scheduleItems[option.id];
      this.setData({
        startDate: tmp_item.start_time.slice(0, 10),
        startTime: tmp_item.start_time.slice(11, 16),
        endDate: tmp_item.end_time.slice(0, 10),
        endTime: tmp_item.end_time.slice(11, 16),
        title: tmp_item.title,
        description: tmp_item.description,
        address: tmp_item.address,
        ifAddPage: false,
        itemIndex: option.id,
        oldStartDate: tmp_item.start_time.slice(0, 10),
      });
    } else { // 如果是添加页面
      this.setData({
        startDate: "选择日期",
        startTime: "选择时间",
        endDate: "选择日期",
        endTime: "选择时间",
        title: "",
        description: "",
        address: ""
      });
    }
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
    if (this.data.ifAddPage) { // 如果是添加页面
      var item = {
        title: this.data.title,
        start_time: this.data.startDate + " " + this.data.startTime,
        end_time: this.data.endDate + " " + this.data.endTime,
        description: this.data.description,
        address: this.data.address,
        user_id: app.globalData.userInfo.id,
      };

      /* 向后端发送请求，获取schduleItem_id，增加对应的项目，注意可能有错误处理 */

      item.scheduleItem_id = 19; // 需要修改
      item.start_concret_time = this.data.startTime;
      item.end_concret_time = this.data.endTime;
      item.start_date = this.data.startDate;
      item.complete = false;
      item.visible = true;

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
        showModal: true,
        msg: "是否保存信息?",
      })
      return;
    } else { // 如果是修改页面
      var tmp_item = app.globalData.scheduleItems[this.data.itemIndex];
      tmp_item.start_time = this.data.startDate + " " + this.data.startTime;
      tmp_item.end_time = this.data.endDate + " " + this.data.endTime;
      tmp_item.start_concret_time = this.data.startTime;
      tmp_item.end_concret_time = this.data.endTime;
      tmp_item.title = this.data.title;
      tmp_item.description = this.data.description;
      tmp_item.address = this.data.address;
      tmp_item.start_date = this.data.startDate;
      app.globalData.scheduleItems[this.data.itemIndex] = tmp_item;

      app.globalData.ifChangeSchedule = true;
      app.globalData.changeScheduleIndex = this.data.itemIndex;
      app.globalData.ifChangeScheduleStartDate = !(this.data.oldStartDate == this.data.startDate);

      /* 向后端发送请求，注意，如果涉及开始时间点的变化而且开始日期不变，需要更新globalData */

      wx.redirectTo({
        url: "/pages/schedular/scheduleDetails/scheduleDetails?id=" + this.data.itemIndex,
      })
    }
  },

  discard: function () {
    console.log("discard");
    if (this.data.ifAddPage) { // 如果现在是添加页面
      wx.navigateBack({
        delta: 1,
      });
    } else { // 如果现在是修改页面
      wx.redirectTo({
        url: "/pages/schedular/scheduleDetails/scheduleDetails?id=" + this.data.itemIndex,
      })
    }
    this.setData({
      showModal: true,
      msg: "是否放弃信息？"
    })
  }
})