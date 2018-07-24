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
          app.globalData.date = res.data;
          app.globalData.year = parseInt(res.data.slice(0, 4));
          app.globalData.month = parseInt(res.data.slice(5, 7));
          app.globalData.day = parseInt(res.data.slice(8, 10));
        } else {
          var tmp_date = new Date();
          app.globalData.year = tmp_date.getFullYear();
          app.globalData.month = tmp_date.getMonth() + 1;
          app.globalData.day = tmp_date.getDate();
          app.globalData.date = time.getDateStringWithZero(app.globalData.year, app.globalData.month, app.globalData.day);
          console.log(app.globalData.date);
          wx.setStorage('date', app.globalData.date);
        }


        /* 获取当天日程 */
        wrapper.wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
          "year": app.globalData.year,
          "month": app.globalDate.month,
          "day": app.globalDate.day,
        }).then((data) => {
          console.log("Get scheduleitems by data:", data)
          app.globalData.scheduleItems = schedule.warpScheduleItems(data); // 设置对应全局变量
        }).catch((errno) => {
          console.log("something wrong when get scheduleItems: " + errno);
        });

        /* 获取当月有日程的日期的对象 */
        wrapper.wxRequestWrapper(api.queryDaysHavingScheduletimesInMonth, "GET", {
          year: app.globalData.year,
          month: app.globalData.month
        }).then((data) => {
          console.log("Get scheduleitems in month",data)
          app.globalData.dayWithItem = data; // 设置对应全局变量
        }).catch((errno) => {
          console.log("something wrong when get days with items in selected month: " + errno);
        });

        /* 获取用户信息 */
        wrapper.wxRequestWrapper(api.queryUserInfo, "GET", {}).then((data) => {
          app.globalData.userInfo = data;
        }).catch((errno) => {
          console.log("something wrong when get user info: " + errno);
        });





        /* 获取用户安装扩展对应的数组 */
        wrapper.wxRequestWrapper(api.queryEnabledExtensionsArray, "GET", {}).then((data) => {
          app.globalData.extensions = extension.warpExtensions(data); // 设置对应全局变量
        }).catch((errno) => {
          console.log("something wrong when get installed extensions: " + errno);
        })
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