var extensions = require("/data/extensions");
var userInfo = require("/data/userInfo");
var scheduleItems = require("/data/scheduleItem");
var wrapper = require("/interface/wrapper/wrapper.js");
var schedule = require("/common/schedule");
var extension = require("/common/extension");
var time = require('/common/time.js');
var api = require("/interface/config/api.js");

//app.js
App({
    onLaunch: function() {
        var date = wx.getStorageSync('date');
        if (date) {
            console.log("Date found in Local storage");
            this.globalData.date = date;
            this.globalData.year = parseInt(date.slice(0, 4));
            this.globalData.month = parseInt(date.slice(5, 7));
            this.globalData.day = parseInt(date.slice(8, 10));
        } else {
            console.log("no date in Local storage");
            var tmp_date = new Date();
            this.globalData.year = tmp_date.getFullYear();
            this.globalData.month = tmp_date.getMonth() + 1;
            this.globalData.day = tmp_date.getDate();
            this.globalData.date = time.getDateStringWithZero(this.globalData.year, this.globalData.month, this.globalData.day);
            console.log(this.globalData.date);
            wx.setStorageSync('date', this.globalData.date);
        }


        // var openId = this.globalData.openId;
        // var that = this;
        // new Promise(function(resolve, reject) {
        //     let errorFlag = false;
        //     if (openId === "" || !openId) {
        //         new Promise(function(resolve, reject) {
        //             wx.login({
        //                 fail: function() {
        //                     reject(10);
        //                 },
        //                 success: function(res) {
        //                     if (res.code) {
        //                         resolve(res.code);
        //                     } else {
        //                         reject(10);
        //                     }
        //                 }
        //             })
        //         }).then((code) => {
        //             return new Promise(function(resolve, reject) {
        //                 wx.request({
        //                     url: api.getOpenId,
        //                     data: {
        //                         code,
        //                     },
        //                     success: function(res) {
        //                         if (res.statusCode = 200)
        //                             resolve(res.data);
        //                         else {
        //                             reject(11);
        //                         }
        //                     },
        //                     fail: function() {

        //                         reject(11);
        //                     }
        //                 })
        //             })
        //         }).then((openId) => {
        //             that.globalData.openId = openId;
        //         }).catch((errno) => {
        //             errorFlag = true;
        //         })
        //         if (errorFlag)
        //             reject(11);
        //         resolve(that.globalData.openId);
        //     } else {
        //         resolve(openId);
        //     }
        // }).then((openId) => {
        //     return new Promise(function(resolve, reject) {
        //         wx.request({
        //             url: api.checkTied,
        //             method: "GET",
        //             data: {
        //                 openId,
        //             },
                    
        //             success: function(res) {
        //                 if (res.statusCode != 200)
        //                     reject(12);
        //                 resolve(res.data)
        //             },
        //             fail: function() {
        //                 reject(12);
        //             }

        //         })
        //     })
        // }).then((res) => {
        //     return new Promise(function(resolve, reject) {
        //         if (!res) {
        //             console.log("JAccount账号未绑定");
        //             that.globalData.hasTied = false;
        //         } else {
        //             console.log("JAccount账号绑定！");
        //             that.globalData.hasTied = true;
        //             wx.request({
        //                 url: api.LoginByWeixin,
        //                 method: "GET",
        //                 data: {
        //                     openId: that.globalData.openId,
        //                 },
        //                 success: function(res) {
        //                     if (res.statusCode != 200 || res.data.errno != 0)
        //                         reject(12);
        //                     else {
        //                         let Cookie = res.header['Set-Cookie'].split(';')[0];
        //                         wx.setStorageSync('Cookie', Cookie);
        //                         resolve(0);
        //                     }
        //                 }
        //             })
        //         }
        //         console.log("app.onLaunch finish");
        //     })
        // }).catch((errno) => {
        //     console.log(errno)
        //     wx.showModal({
        //         title: "服务器连接失败",
        //         content: "请检查网络连接",
        //         showCancel: false
        //     });
        // })

    },
    onShow: function() {
        console.log("app.onShow start");
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
        logined: false, // 是否登录
        ifChangeSchedule: false, // 指示schedular显示时是否从修改事务的add页面返回
        changeScheduleIndex: 0, // 指示修改事务在全局变量里的索引
        ifChangeScheduleStartDate: false, // 指示修改事务时是否有修改开始日期
        spiderItems: [],
        keeperItems: [],
        newItemDate: "",
        ifPressSaveInAddSchedulePage: false,
        showCompletedSchedule: true,
        openId: "",
        hasTied: true,
    }
})