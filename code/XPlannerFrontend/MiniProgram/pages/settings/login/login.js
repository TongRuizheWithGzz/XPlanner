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
        return new Promise(function(resolve, reject) {
            wrapper
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
                    console.log("获得用户的设置全局变量:", data.userSettings);
                    app.globalData.extensions = extension.filterExtensions(
                        extension.warpExtensions(extensions),
                        data.userSettings
                    ); // 设置对应全局变量
                    app.globalData.userFoodEaten = [];
                    app.globalData.logined = true;
                })
                .then(errno => {
                    resolve(errno);
                })
                .catch(errno => {
                    app.globalData.logined = false;
                    console.log("Get errno when fectchUserInfo: ", errno);
                    reject(errno);
                });
        });
    },

    data: {
        qrcodeFileName: "/icons/loading.gif",
    },
    onShow: function() {
        console.log("login.onShow")
        this.setData({
            qrcodeFileName: "/icons/loading.gif",
        })
        console.log(app.globalData.hasTied)
        let that=this;
        wrapper.checkSessionWrapper().then()
        new Promise(function(resolve, reject) {
            wx.login({
                fail: function() {
                    reject(10);
                },
                success: function(res) {
                    if (res.code) {
                        resolve(res.code);
                    } else {
                        reject(10);
                    }
                }
            })
        }).then((code) => {
            return new Promise(function(resolve, reject) {
                wx.request({
                    url: api.LoginByWeixin,
                    method: "GET",
                    data: {
                        code: code
                    },
                    fail: function() {
                        reject(10);
                    },
                    success: function(res) {
                        if (res.statusCode != 200)
                            reject(10)
                        else {
                            resolve(res.data["openId"])
                        }
                    }
                })
            })
        }).then((openId) => {
            return new Promise(function(resolve, reject) {
                wx.request({
                    url: api.getQrcode,
                    method: "GET",
                    data: {
                        openId: openId,
                    },
                    fail: function() {
                        reject(11);
                    },
                    success: function(res) {
                        if (res.statusCode != 200)
                            reject(11)
                        else {
                            that.setData({
                                qrcodeFileName: api.qrCodePath + openId + ".png"
                            });
                            resolve(res.data["imgName"])
                        }
                    }
                })
            })
        }).catch((errno) => {
            let title = "";
            switch (errno) {
                case 10:
                    title = "微信登录失败";
                    break;
                case 11:
                    title = "获取二维码失败"
                    break;
            }
            wx.showModal({
                title: title,
                content: "请检查网络连接",
                showCancel: false
            });
        })


    },
    /*
     * getName
     * 获取名称
     */
    getName: function(e) {
        console.log(e.detail.value);
        tbat.setData({
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
        wrapper
            .loginByUsernamePassword("tongruizhe", "password")
            .then(errno => {
                console.log(app.globalData);
                return wrapper.wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
                    year: app.globalData.year,
                    month: app.globalData.month,
                    day: app.globalData.day
                });
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
                console.log("获得用户的设置全局变量:", data.userSettings);
                app.globalData.extensions = extension.filterExtensions(
                    extension.warpExtensions(extensions),
                    data.userSettings
                ); // 设置对应全局变量
                app.globalData.userFoodEaten = [];
                app.globalData.logined = true;
                wx.switchTab({
                    url: "/pages/schedular/schedular"
                });
            })
            .catch(errno => {
                app.globalData.logined = false;
                console.log("Get errno when login: ", errno);
                switch (errno) {
                    case 1:
                        wx.showModal({
                            title: "用户名或密码错误",
                            content: "请重新输入",
                            showCancel: false
                        });
                        break;
                    case 4:
                        wx.showModal({
                            title: "请求失败",
                            content: "请检查网络连接",
                            showCancel: false
                        });
                        break;
                    default:
                        wx.showModal({
                            title: "获取用户信息失败",
                            content: "请检查网络连接",
                            showCancel: false
                        });
                        break;
                }
            });
    },

    /**
     * register
     * 相应点击注册的连接
     */
    register: function() {
        wx.navigateTo({
            url: '/pages/settings/reg/reg',
        })
    }
});