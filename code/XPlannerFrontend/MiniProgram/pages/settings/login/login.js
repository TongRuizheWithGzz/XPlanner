var app = getApp();
var extensions = require("../../../data/extensions");
var userInfo = require("../../../data/userInfo");
var scheduleItems = require("../../../data/scheduleItem");
var wrapper = require("../../../interface/wrapper/wrapper");

var time = require("../../../common/time");
var schedule = require("../../../common/schedule");
var extension = require("../../../common/extension");

Page({
  data: {
    name: "",
    password: "",
  },

  /*
   * getName
   * 获取名称
   */
  getName: function (e) {
    console.log(e.detail.value);
    this.setData({
      name: e.detail.value
    });
  },

  /*
   * getPassword
   * 获取密码
   */
  getPassword: function (e) {
    console.log(e.detail.value);
    this.setData({
      password: e.detail.value
    });
  },

  login: function () {
    console.log("login");
    /* 向后端发送用户名和密码 */
    // wrapper.loginByUsernamePassword(this.data.name, this.data.password).then((errno) => {
    //   wx.getStorage({
    //     key: 'date',
    //     success: function (res) {
    //       app.globalData.date = res.data;
    //       app.globalData.year = parseInt(res.data.slice(0, 4));
    //       app.globalData.month = parseInt(res.data.slice(5, 7));
    //       app.globalData.day = parseInt(res.data.slice(8, 10));
    //     },
    //     fail: function() {
    //       var tmp_date = new Date();
    //       app.globalData.year = tmp_date.getFullYear();
    //       app.globalData.month = tmp_date.getMonth() + 1;
    //       app.globalData.day = tmp_date.getDate();
    //       app.globalData.date = time.getDateStringWithZero(app.globalData.year, app.globalData.month, app.globalData.day);
    //       console.log(app.globalData.date);
    //       wx.setStorage({
    //         key: 'date',
    //         data: app.globalData.date
    //       });
    //     }
    //   })
    //   // app.globalData.date = "2018-07-18";
    //   // app.globalData.year = 2018;
    //   // app.globalData.month = 7;
    //   // app.globalData.day = 18;
    //   app.globalData.logined = true;
    // }).catch((errno) => {
    //   switch (errno) {
    //     case 1:
    //       console.log("wrong name or password");
    //       break;
    //     default:
    //       break;
    //   }
    // });

    // wx.getStorage({
    //   key: 'date',
    //   success: function (res) {
    //     app.globalData.date = res.data;
    //     app.globalData.year = parseInt(res.data.slice(0, 4));
    //     app.globalData.month = parseInt(res.data.slice(5, 7));
    //     app.globalData.day = parseInt(res.data.slice(8, 10));
    //   },
    //   fail: function () {
    //     var tmp_date = new Date();
    //     app.globalData.year = tmp_date.getFullYear();
    //     app.globalData.month = tmp_date.getMonth() + 1;
    //     app.globalData.day = tmp_date.getDate();
    //     app.globalData.date = time.getDateStringWithZero(app.globalData.year, app.globalData.month, app.globalData.day);
    //     console.log(app.globalData.date);
    //     wx.setStorage({
    //       key: 'date',
    //       data: app.globalData.date
    //     });
    //   }
    // })
    app.globalData.logined = true;
    app.globalData.date = "2018-07-18";
    app.globalData.year = 2018;
    app.globalData.month = 7;
    app.globalData.day = 18;
    app.globalData.userInfo = userInfo;
    app.globalData.extensions = extension.warpExtensions(extensions);
    app.globalData.userFoodEaten = [];
    app.globalData.scheduleItems = schedule.warpScheduleItems(scheduleItems);
    app.globalData.dayWithItem = { 17: [1], 18: [3], 20: [2] };
    app.globalData.ifAddSchedule = false;
    app.globalData.ifSameDay = false;

    wx.switchTab({
      url: '/pages/schedular/schedular',
    })
  }
})