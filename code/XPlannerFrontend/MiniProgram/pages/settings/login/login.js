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
  fetchUserInfoAfterLogin: function() {
  return  wrapper
      .wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
        year: app.globalData.year,
        month: app.globalData.month,
        day: app.globalData.day
      })
      .then(data => {
        console.log("得到用户某天的数据", data, "开始请求一个月的日程");
        app.globalData.scheduleItems = schedule.warpScheduleItems(
          data.scheduleitems
        ); // 设置对应全局变量
        return wrapper.wxRequestWrapper(
          api.queryDaysHavingScheduletimesInMonth,
          "GET", {
            year: app.globalData.year,
            month: app.globalData.month
          }
        );
      })
      .then(data => {
        console.log("得到一月日程", data, "开始请求用户信息");
        app.globalData.dayWithItem = data.dateMap; // 设置对应全局变量
        return wrapper.wxRequestWrapper(api.queryUserInfo, "GET", {});
      })
      .then(data => {
        console.log("获得用户信息，开始请求用户的设置");
        app.globalData.userInfo = data.userInfo;
        return wrapper.wxRequestWrapper(
          api.queryEnabledExtensionsArray,
          "GET", {}
        );
      })
      .then(data => {
        return new Promise(function(resolve, reject) {
          console.log("获得用户的设置全局变量");
          // console.log(data.userSettings)
          app.globalData.extensions = extension.filterExtensions(
            extension.warpExtensions(extensions),
            data.userSettings
          ); // 设置对应全局变量
          app.globalData.userFoodEaten = [];
          app.globalData.logined = true;
          resolve(true);
        })
      })

  },


  data: {
    qrcodeFileName: "/icons/loading.gif",
  },
  onShow: function() {
    console.log("login.js.onshow STARTs!")

    this.setData({
      qrcodeFileName: "/icons/loading.gif",
    })


    let that = this;
    if (app.globalData.openId && app.globalData.hasTied) {
      wx.request({
        url: api.LoginByWeixin,
        method: "GET",
        data: {
          openId: app.globalData.openId,
        },
        success: function(res) {
          if (res.statusCode != 200 || res.data.errno != 0)
            reject("确认绑定后获取cookie信息失败");
          else {
            let Cookie = res.header['Set-Cookie'].split(';')[0];
            wx.setStorageSync('Cookie', Cookie);
            new Promise(function(resolve, reject) {
              that.fetchUserInfoAfterLogin();
            }).then(() => {
              wx.switchTab({
                url: "pages/schedular/schedular",
              })
            }).catch((errMsg) => {
              console.log(errMsg);
              wx.showModal({
                title: "请求失败",
                content: "请检查网络连接",
                showCancel: false
              });
            })
          }
        }
      })
    } else {
      new Promise(function(resolve, reject) {
        new Promise(function(resolve, reject) {
          wx.login({
            fail: function() {
              reject("获取code失败");
            },
            success: function(res) {
              if (res.code) {
                resolve(res.code);
              } else {
                reject("获取code失败");
              }
            }
          })
        }).then((code) => {
          return new Promise(function(resolve, reject) {
            wx.request({
              url: api.getOpenId,
              data: {
                code,
              },
              success: function(res) {
                if (res.statusCode = 200)
                  resolve(res.data);
                else {
                  reject("获取openId失败");
                }
              },
              fail: function() {
                reject("获取openId失败");
              }
            })
          })
        }).then((openId) => {
          app.globalData.openId = openId;
          return new Promise(function(resolve, reject) {
            wx.request({
              url: api.checkTied,
              method: "GET",
              data: {
                openId,
              },
              success: function(res) {
                if (res.statusCode != 200)
                  reject("获取绑定信息失败");
                resolve(res.data)
              },
              fail: function() {
                reject("获取绑定信息失败");
              }
            })
          })
        }).then((res) => {
          return new Promise(function(resolve, reject) {
            if (!res) {
              console.log("JAccount账号未绑定");
              app.globalData.hasTied = false;
              wx.request({
                url: api.getQrcode,
                method: "GET",
                data: {
                  openId: app.globalData.openId,
                },
                fail: function() {
                  reject("获取二维码失败");
                },
                success: function(res) {
                  if (res.statusCode != 200)
                    reject("获取二维码失败")
                  else {
                    that.setData({
                      qrcodeFileName: api.qrCodePath + app.globalData.openId + ".png"
                    });
                    resolve(res.data["imgName"])
                  }
                }
              })
            } else {
              console.log("JAccount账号绑定！");
              app.globalData.hasTied = true;
              wx.request({
                url: api.LoginByWeixin,
                method: "GET",
                data: {
                  openId: app.globalData.openId,
                },
                success: function(res) {
                  if (res.statusCode != 200 || res.data.errno != 0)
                    reject("确认绑定后获取cookie信息失败");
                  else {
                    let Cookie = res.header['Set-Cookie'].split(';')[0];
                    wx.setStorageSync('Cookie', Cookie);
                    that.fetchUserInfoAfterLogin()
                      .then(() => {

                        wx.switchTab({
                          url: "/pages/schedular/schedular",
                        })
                      }).catch((errMsg) => {
                        console.log(errMsg);
                        wx.showModal({
                          title: "请求失败",
                          content: "请检查网络连接",
                          showCancel: false
                        });
                      })
                  }
                }
              })
            }
          })
        }).catch((errMsg) => {
          console.log(errMsg);
          wx.showModal({
            title: "服务器连接失败",
            content: "请检查网络连接",
            showCancel: false
          });
        })
      })
    }
  },
});