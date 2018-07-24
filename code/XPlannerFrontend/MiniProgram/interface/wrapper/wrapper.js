const api = require('../config/api.js');

var wrapper = {
  loginByUsernamePassword: function(username, password) {
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
  },

  wxRequestWrapper: function(apiUrl, method, data) {
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
}

module.exports = wrapper;