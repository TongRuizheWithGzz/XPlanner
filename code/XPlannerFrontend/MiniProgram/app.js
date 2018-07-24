// var extensions = require("/data/extensions");
// var userInfo = require("/data/userInfo");
// var scheduleItems = require("/data/scheduleItem");

//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    // var logs = wx.getStorageSync('logs') || []
    // logs.unshift(Date.now())
    // wx.setStorageSync('logs', logs)

    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //   }
    // });
    // // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           // 可以将 res 发送给后台解码出 unionId
    //           this.globalData.userInfo = res.userInfo

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (this.userInfoReadyCallback) {
    //             this.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }
    //   }
    // });

    /* 向后端发送选择的日期，获取当月有日程的日期的数组和当天的日期，设置globalData，注意要使用包装函数 */
    /* 另外，使用wx.getStorage获取存储的选区的日期，也需要设置globalData */

    // this.globalData.date = "2018-07-18";
    // this.globalData.year = 2018;
    // this.globalData.month = 7;
    // this.globalData.day = 18;
    // this.globalData.userInfo = userInfo;
    // this.globalData.extensions = warpExtensions(extensions);
    // this.globalData.userFoodEaten = [];
    // this.globalData.scheduleItems = warpScheduleItems(scheduleItems);
    // this.globalData.dayWithItem = { 17: [1], 18: [3], 20: [2] };
    // this.globalData.ifAddSchedule = false;
    // this.globalData.ifSameDay = false;
  },
  globalData: {
    date: "",
    year: new Date().getFullYear(),
    month: new Date().getMonth() + 1,
    day: new Date().getDate(),
    userInfo: {},
    extensions: [],
    userFoodEaten: [],
    scheduleItems: [],
    dayWithItem: {}, // 似乎只要用一次
    ifAddSchedule: false, // 指示schedular显示时是否从添加事务的add页面返回
    ifSameDay: false, // 指示添加的日程是否和当天是否是一天
    logined: false,
    ifChangeSchedule: false, // 指示schedular显示时是否从修改事务的add页面返回
    changeScheduleIndex: 0,
    ifChangeScheduleStartDate: false,
  }
})