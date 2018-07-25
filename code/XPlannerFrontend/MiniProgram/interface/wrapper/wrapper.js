const api = require('../config/api.js');
var extension = require('../../common/extension.js')
var schedule = require('../../common/schedule.js')
var app = getApp();

function loginByUsernamePassword(username, password) {
  return new Promise(function(resolve, reject) {
    wx.request({
      url: api.LoginByUsernamePassword,
      data: {
        username,
        password,
      },
      header: {
        'content-type': 'application/x-www-form-urlencoded',
      },
      method: 'POST',
      success: function(res) {
        switch (res.data.errno) {
          case 0:
            //登录成功，存储Cookie
            let Cookie = res.header['Set-Cookie'].split(';')[0];
            wx.setStorageSync('Cookie', Cookie);
            resolve(0);
            break;
          case 1:
            //认证失败,用户名密码错误
            reject(1);
            break;
        }
      },
      fail: function() {
        //wx.request() failed! 可能由于网络错误等原因
        reject(4);
      }
    })
  })
}

function wxRequestWrapper(apiUrl, method, data) {
  return new Promise(function(resolve, reject) {
    let Cookie = wx.getStorageSync("Cookie");
    if (!Cookie) {
      //本地存储没有Cookie 用户尚未登录
      reject(5);
    }
    wx.request({
      url: apiUrl,
      method,
      data,
      header: {
        'Cookie': Cookie,
        'content-Type': 'application/json;charset=UTF-8',

      },
      success: function(res) {

        switch (res.data.errno) {
          case (0):
            //接口调用成功
            resolve(res.data);
            break;
          case (1):
            //用户权限认证过期(Cookie无效)
          case (2):
            //没有权限访问该接口
            resolve(res.data.errno);
            break;
        }
      },
      fail: function(res) {
        switch (res.statusCode) {
          case 400:
            reject(7);
          case 404:
            reject(8);
        }
        //wx.request() failed! 可能由于网络错误等原因
        resolve(4);
      }
    })
  })
}

function checkSessionWrapper() {
  return new Promise(function(resolve, reject) {
    let Cookie = wx.getStorageSync("Cookie");
    if (!Cookie) {
      //本地存储没有Cookie 用户尚未登录
      reject(9);
    }
    wx.request({
      url: api.checkSession,
      method: "GET",
      header: {
        'Cookie': Cookie,
      },
      success: function(res) {
        switch (res.data.errno) {
          case (0):
            //成功检查Session
            resolve(res.data);
            break;
          case (1):
            //用户权限认证过期(Cookie无效)
          case (2):
            //没有权限访问该接口
            reject(res.data.errno);
            break;
        }
      },
      fail: function(res) {
        switch (res.statusCode) {
          case 400:
            reject(7);
          case 404:
            reject(8);
        }
        //wx.request() failed! 可能由于网络错误等原因
        resolve(4);
      }
    })
  })
}

// function fetchAfterUserLogin() {
//   return wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
//       "year": app.globalData.year,
//       "month": app.globalData.month,
//       "day": app.globalData.day,
//     }).then((data) => {
//       console.log("得到用户某天的数据", data, "开始请求一个月的日程")
//       app.globalData.scheduleItems = schedule.warpScheduleItems(data.scheduleitems); // 设置对应全局变量
//       return wxRequestWrapper(api.queryDaysHavingScheduletimesInMonth, "GET", {
//         year: app.globalData.year,
//         month: app.globalData.month
//       });
//     }).then((data) => {
//       console.log("得到一月日程", data, "开始请求用户信息")
//       app.globalData.dayWithItem = data.dateMap; // 设置对应全局变量
//       return wxRequestWrapper(api.queryUserInfo, "GET", {});
//     })
//     .then((data) => {
//       console.log("获得用户信息，开始请求用户的设置");
//       app.globalData.userInfo = data.userInfo;
//       return wxRequestWrapper(api.queryEnabledExtensionsArray, "GET", {})
//     })
//     .then((data) => {
//       console.log("获得用户的设置全局变量:", data.userSettings);
//       app.globalData.extensions = extension.filterExtensions(extension.warpExtensions(extensions), data.userSettings); // 设置对应全局变量
//       app.globalData.userFoodEaten = [];
//       app.globalData.logined = true;
//     }).then((errno) => {
//       resolve(errno);
//     })
//     .catch((errno) => {
//       app.globalData.logined = false;
//       console.log("Get errno when fectchUserInfo: ", errno);
//       reject(errno);
//     })
// }


module.exports = {
  loginByUsernamePassword,
  wxRequestWrapper,
  checkSessionWrapper,
};