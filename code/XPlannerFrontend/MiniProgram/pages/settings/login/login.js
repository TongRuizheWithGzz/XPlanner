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
        name: "",
        password: ""
    },
    onShow: function() {
        wrapper
            .checkSessionWrapper()
            .then(errno => {
                console.log("在App.js中检查登录成功");
                this.fetchUserInfoAfterLogin()
                    .then(errno => {
                        wx.switchTab({
                            url: "/pages/schedular/schedular"
                        });
                    })
                    .catch(errno => {
                        console.log(
                            "在App.js中建成登录成功，随后获取用户信息失败：",
                            errno
                        );
                        wx.showModal({
                            title: "获取用户信息失败",
                            content: "请检查网络设置",
                            showCancel: false
                        });
                    });
            })
            .catch(errno => {
                console.log("在App.js中检查登录失败,开始进行微信登录", errno);
                wx.login({
                    success: function(res) {
                        wx.request({
                            method: "GET",
                            url: api.LoginByWeixin,
                            data: {
                                code: res.code
                            },
                            success: function(res) {
                                if (res.statusCode != 200 || res.data["errMsg"] === "fail") {
                                    wx.showModal({
                                        title: "获取用户信息失败",
                                        content: "请检查网络设置",
                                        showCancel: false
                                    });
                                } else {
                                    let Cookie = res.header['Set-Cookie'].split(';')[0];
                                    wx.setStorageSync('Cookie', Cookie);
                                    
                                    wrapper.wxRequestWrapper(api.queryScheduleitemByDay, "GET", {
                                            year: app.globalData.year,
                                            month: app.globalData.month,
                                            day: app.globalData.day
                                        }).then(data => {
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
                                }
                            },
                            fail: function() {
                                wx.showModal({
                                    title: "获取用户信息失败",
                                    content: "请检查网络设置",
                                    showCancel: false
                                });
                            }
                        })
                    },
                    fail: function() {
                        wx.showModal({
                            title: "获取用户信息失败",
                            content: "请检查网络设置",
                            showCancel: false
                        });
                    }
                })
            });






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