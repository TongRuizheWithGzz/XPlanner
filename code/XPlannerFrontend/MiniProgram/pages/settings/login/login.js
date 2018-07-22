var app = getApp();
var extensions = require("../../../data/extensions");
var userInfo = require("../../../data/userInfo");
var scheduleItems = require("../../../data/scheduleItem");

function warpExtensions(extensions_raw) {
  var result = [];
  for (var i = 0; i < extensions_raw.length; i++) {
    var tmp = new Object();
    tmp.id = extensions_raw[i].planner_id;
    tmp.name = extensions_raw[i].planner_name;
    tmp.description = extensions_raw[i].description;
    tmp.content = "尚无内容。";
    tmp.visible = false;
    tmp.messages = extensions_raw[i].planner_name == "Spider" ? 8 : -1;
    tmp.icon = extensions_raw[i].picture_path_name;
    var name = tmp.name.toLowerCase();
    tmp.extension_path = "/extensions/" + name + "/" + name;
    result.push(tmp);
  }
  return result;
}

function warpScheduleItems(scheduleItem_raw) {
  var array = [];
  for (var i = 0; i < scheduleItem_raw.length; i++) {
    var tmp = new Object();
    tmp.title = scheduleItem_raw[i].title;
    tmp.start_time = scheduleItem_raw[i].start_time;
    tmp.end_time = scheduleItem_raw[i].end_time;
    tmp.description = scheduleItem_raw[i].description;
    tmp.address = scheduleItem_raw[i].address;
    tmp.scheduleItem_id = scheduleItem_raw[i].scheduleItem_id;
    tmp.user_id = scheduleItem_raw[i].user_id;
    tmp.start_concret_time = tmp.start_time.slice(11, 16);
    tmp.end_concret_time = tmp.end_time.slice(11, 16);
    tmp.start_date = tmp.start_time.slice(0, 10);
    tmp.completed = false;
    tmp.visible = true;
    array.push(tmp);
  }
  return array;
}

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
    // /* 向后端发送用户名和密码 */
    // wx.request({
    //   url: "",
    //   success: function (res) {
    //     /* 设置全局的userInfo */
    //     /* 跳转到schedular */

    //     wx.redirectTo({
    //       url: '/pages/schedular/schedular',
    //     })
    //   }
    // });
    app.globalData.date = "2018-07-18";
    app.globalData.year = 2018;
    app.globalData.month = 7;
    app.globalData.day = 18;
    app.globalData.userInfo = userInfo;
    app.globalData.extensions = warpExtensions(extensions);
    app.globalData.userFoodEaten = [];
    app.globalData.scheduleItems = warpScheduleItems(scheduleItems);
    app.globalData.dayWithItem = { 17: [1], 18: [3], 20: [2] };
    app.globalData.ifAddSchedule = false;
    app.globalData.ifSameDay = false;

    wx.switchTab({
      url: '/pages/schedular/schedular',
    })
  }
})