var app = getApp();
var extensions = require("../../../data/extensions");
var userInfo = require("../../../data/userInfo");
var scheduleItems = require("../../../data/scheduleItem");
var wrapper = require("../../../interface/wrapper/wrapper");

var time = require("../../../common/time");
var schedule = require("../../../common/schedule");
var extension = require("../../../common/extension");
var api = require("../../../interface/config/api");

Page({
  data: {
    name: "",
    password: "",
  },

  /*
   * getName
   * 获取名称
   */
  getName: function(e) {
    console.log(e.detail.value);
    this.setData({
      name: e.detail.value
    });
  },

  /*
   * getPassword
   * 获取密码
   */
  getPassword: function(e) {
    console.log(e.detail.value);
    this.setData({
      password: e.detail.value
    });
  },

  login: function() {
    console.log("login");
    /* 向后端发送用户名和密码 */
    wrapper.loginByUsernamePassword(this.data.name, this.data.password)
      .then((errno) => {
        var date = wx.getStorageSync('date');
        if (date) {
          console.log("Date found in Local storage");
          app.globalData.date = date;
          app.globalData.year = parseInt(date.slice(0, 4));
          app.globalData.month = parseInt(date.slice(5, 7));
          app.globalData.day = parseInt(date.slice(8, 10));
        } else {
          console.log("no date in Local storage");
          var tmp_date = new Date();
          app.globalData.year = tmp_date.getFullYear();
          app.globalData.month = tmp_date.getMonth() + 1;
          app.globalData.day = tmp_date.getDate();
          app.globalData.date = time.getDateStringWithZero(app.globalData.year, app.globalData.month, app.globalData.day);
          console.log(app.globalData.date);
          wx.setStorageSync('date', app.globalData.date);
        }
        console.log(app.globalDate);
        return wrapper.wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
          "year": app.globalData.year,
          "month": app.globalDate.month,
          "day": app.globalDate.day,
        });

      })
      .then((data) => {
        console.log("得到用户某天的数据", data, "开始请求一个月的日程")
        app.globalData.scheduleItems = schedule.warpScheduleItems(data); // 设置对应全局变量
        return wrapper.wxRequestWrapper(api.queryDaysHavingScheduletimesInMonth, "GET", {
          year: app.globalData.year,
          month: app.globalData.month
        });
      })
      .then((data) => {
        console.log("得到一月日程", data, "开始请求用户信息")
        app.globalData.dayWithItem = data; // 设置对应全局变量
        return wrapper.wxRequestWrapper(api.queryUserInfo, "GET", {});
      })
      .then((data) => {
        console.log("获得用户信息，开始请求用户的设置");
        app.globalData.userInfo = data;
        return wrapper.wxRequestWrapper(api.queryEnabledExtensionsArray, "GET", {})
      })
      .then((data) => {
        console.log("获得用户的设置全局变量");
        app.globalData.extensions = extension.warpExtensions(data); // 设置对应全局变量
        app.globalData.userFoodEaten = [];
        app.globalData.logined = true;
        wx.switchTab({
          url: '/pages/schedular/schedular',
        })
      })
      .catch((errno) => {
        app.globalData.logined = false;
        console.log("Get errno when login: ", errno);
        switch (errno) {
          case 1:
            console.log("wrong name or password");
            break;
          default:
            break;
        }
      });


  }
})