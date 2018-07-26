var app = getApp();
var scheduleItems = app.globalData.scheduleItems;
var wrapper = require("../../../interface/wrapper/wrapper");
var api = require("../../../interface/config/api");
var schedule = require("../../../common/schedule");
var time = require("../../../common/time.js");
Page({
  data: {
    startDate: "选择日期",
    startTime: "选择时间",
    endDate: "选择日期",
    endTime: "选择时间",
    title: "",
    description: "",
    address: "",
    showModal: true,
    msg: 'jjj',
    ifAddPage: true,
    ifSpiderPage: false,
    itemIndex: 0,
    ifChangeScheduleStartDate: false,
  },
  submit: function() {
    this.setData({
      showModal: true
    })
  },

  preventTouchMove: function() {

  },


  go: function() {
    this.setData({
      showModal: false
    })
  },

  onLoad: function(options) {
    if (options.id) { // 如果是修改页面
      var tmp_item = app.globalData.scheduleItems[options.id];
      this.setData({
        startDate: tmp_item.start_time.slice(0, 10),
        startTime: tmp_item.start_time.slice(11, 16),
        endDate: tmp_item.end_time.slice(0, 10),
        endTime: tmp_item.end_time.slice(11, 16),
        title: tmp_item.title,
        description: tmp_item.description,
        address: tmp_item.address,
        ifAddPage: false,
        itemIndex: options.id,
        oldStartDate: tmp_item.start_time.slice(0, 10),
      });
    } else if (options.spiderIndex) { // 如果是添加spider项目
      console.log(options.spiderIndex);
      console.log("add spider item");
      console.log(options.pageNumber);
      console.log(app.globalData.spiderItems);
      var tmp_item = app.globalData.spiderItems[options.pageNumber - 1][options.spiderIndex];
      console.log(tmp_item);
      this.setData({
        startDate: tmp_item.start_time.slice(0, 10),
        startTime: tmp_item.start_time.slice(11, 16),
        endDate: tmp_item.end_time.slice(0, 10),
        endTime: tmp_item.end_time.slice(11, 16),
        title: tmp_item.title,
        description: tmp_item.description,
        address: tmp_item.address,
        ifAddPage: true, // 添加spider项目本质上和添加普通项目相同
      });
    } else if (options.readerDescription) {
      console.log("从Reader进入到了add界面");
      this.setData({
        startDate: options.start_time.slice(0, 10),
        startTime: options.start_time.slice(11, 16),
        endDate: options.end_time.slice(0, 10),
        endTime: options.end_time.slice(11, 16),
        title: "",
        description: options.readerDescription,
        address: "",
        ifAddPage: true,
      });
    } else if (options.keeperIndex) {
      console.log("从Keeper进入了add界面");
      var tmp_item = app.globalData.keeperItems[options.keeperIndex];
      this.setData({
        startDate: tmp_item.start_time.slice(0, 10),
        startTime: tmp_item.start_time.slice(11, 16),
        endDate: tmp_item.end_time.slice(0, 10),
        endTime: tmp_item.end_time.slice(11, 16),
        title: tmp_item.title,
        description: tmp_item.description,
        address: tmp_item.address,
        ifAddPage: true,
      });
    } else { // 如果是添加页面
      this.setData({
        startDate: "选择日期",
        startTime: "选择时间",
        endDate: "选择日期",
        endTime: "选择时间",
        title: "",
        description: "",
        address: ""
      });
    }
  },
  titleIp: function(e) {
    this.setData({
      title: e.detail.value
    })
  },
  descriptionIp: function(e) {
    this.setData({
      description: e.detail.value
    })
  },
  addressIp: function(e) {
    this.setData({
      address: e.detail.value
    })
  },
  changeSD: function(e) {
    this.setData({
      startDate: e.detail.value,
    })
  },
  changeST: function(e) {
    this.setData({
      startTime: e.detail.value,
    })
  },
  changeED: function(e) {
    this.setData({
      endDate: e.detail.value,
    })
  },
  changeET: function(e) {
    this.setData({
      endTime: e.detail.value,
    })
  },
  save: function() {
    if (this.data.ifAddPage) { // 如果是添加页面
      var tmp_start_date = this.data.startDate === "选择日期" ? app.globalData.date : this.data.startDate;
      var tmp_start_concret_time = this.data.startTime === "选择时间" ? time.getConcreteTime(new Date().getHours(), new Date().getMinutes()) : this.data.startTime;
      var tmp_end_date = this.data.endDate === "选择日期" ? app.globalData.date : this.data.endDate;
      var tmp_end_concret_time = this.data.endTime === "选择时间" ? time.getConcreteTime(new Date().getHours(), new Date().getMinutes()) : this.data.endTime;
      this.setData({
        startDate: tmp_start_date,
        startTime: tmp_start_concret_time,
        endDate: tmp_end_date,
        endTime: tmp_end_concret_time,
      });

      var tmp_start_time = tmp_start_date + " " + tmp_start_concret_time;
      var tmp_end_time = tmp_end_date + " " + tmp_end_concret_time;
      if (tmp_end_time < tmp_start_time) {
        wx.showModal({
          title: '时间错误',
          content: '请设置正确的时间',
          showCancel: false,
        })
        return;
      }
      var tmp_title = this.data.title == "" ? "无标题" : this.data.title;
      // var item = schedule.generateScheduleItem(
      //   tmp_title,
      //   tmp_start_time,
      //   tmp_end_time,
      //   this.data.description,
      //   this.data.address
      // );

      /* 向后端发送请求，获取schduleItem_id，增加对应的项目，注意可能有错误处理 */
      wrapper.wxRequestWrapper(api.addScheduleitem, "POST", {
        start_time: tmp_start_time,
        end_time: tmp_end_time,
        title: tmp_title,
        description: this.data.description,
        address: this.data.address,
        completed: false,
        year: app.globalData.year,
        month: app.globalData.month,
        day: app.globalData.day,
      }).then((data) => {
        var newScheduleitem = data["newScheduleitem"];
        var newScheduleitmesForDay = data["newScheduleitmesForDay"];
        console.log("得到服务器返回的新scheduleitem", newScheduleitem);
        console.log("得到服务器返回的当天scheduleitems", newScheduleitmesForDay);
        // item.scheduleItem_id = scheduleitem.scheduleItem_id;
        newScheduleitem.start_concret_time = this.data.startTime;
        newScheduleitem.end_concret_time = this.data.endTime;
        newScheduleitem.start_date = this.data.startDate;
        newScheduleitem.start_date = this.data.startDate;
        newScheduleitem.complete = false;
        newScheduleitem.visible = true;

        app.globalData.scheduleItems = schedule.warpScheduleItems(newScheduleitmesForDay);
        console.log(app.globalData.scheduleItems);
        app.globalData.ifAddSchedule = true;
        app.globalData.newItemDate = this.data.startDate;
        app.globalData.ifSameDay = (app.globalData.date == this.data.startDate.slice(0, 10));
        wx.showToast({
          title: '日程添加成功',
          image: "/icons/success.png",
          duration: 1000,
          success: function() {
            setTimeout(() => {
              (wx.navigateBack({
                delta: 1,
              }))
            }, 1000)

          }
        })


      }).catch((errno) => {
        console.log("添加日程服务器返回错误：", errno);
        wx.showModal({
          title: '添加失败',
          content: '请检查网络连接',
          showCancel: false,
        })

      });
    } else { // 如果是修改页面
      /* 检测错误输入 */
      var tmp_start_date = this.data.startDate === "选择日期" ? app.globalData.date : this.data.startDate;
      var tmp_start_concret_time = this.data.startTime === "选择时间" ? time.getConcreteTime(new Date().getHours(), new Date().getMinutes()) : this.data.startTime;
      var tmp_end_date = this.data.endDate === "选择日期" ? app.globalData.date : this.data.endDate;
      var tmp_end_concret_time = this.data.endTime === "选择时间" ? time.getConcreteTime(new Date().getHours(), new Date().getMinutes()) : this.data.endTime;
      this.setData({
        startDate: tmp_start_date,
        startTime: tmp_start_concret_time,
        endDate: tmp_end_date,
        endTime: tmp_end_concret_time,
      });

      var tmp_start_time = tmp_start_date + " " + tmp_start_concret_time;
      var tmp_end_time = tmp_end_date + " " + tmp_end_concret_time;
      if (tmp_end_time < tmp_start_time) {
        wx.showModal({
          title: '时间错误',
          content: '请设置正确的时间',
          showCancel: false,
        })
        return;
      }

      /* 向后端发送请求，无论如何都需要更新globalData中的日程数组 */
      console.log("试图修改日程");
      console.log(app.globalData.scheduleItems[this.data.itemIndex]);
      wrapper.wxRequestWrapper(api.updateScheduleitem + app.globalData.scheduleItems[this.data.itemIndex].scheduleItem_id, "PUT", {
        //在这里把修改传的参数写上
        start_time: tmp_start_time,
        end_time: tmp_end_time,
        description: this.data.description,
        address: this.data.address,
        title: this.data.title === "" ? "无标题" : this.data.title,
        completed: false,
        year: app.globalData.year,
        month: app.globalData.month,
        day: app.globalData.day,
      }).then((data) => {
        //处理成功 修改一些变量，如一些全局变量
        console.log(data);
        app.globalData.scheduleItems = schedule.warpScheduleItems(data["scheduleItems"]); // ?
        console.log("修改后返回当天日程", app.globalData.scheduleItems);
        app.globalData.ifChangeSchedule = true;
        // app.globalData.changeScheduleIndex = this.data.itemIndex;
        app.globalData.ifChangeScheduleStartDate = !(this.data.oldStartDate == this.data.startDate);
        app.globalData.newItemDate = this.data.startDate;
        wx.showToast({
          title: '日程修改成功',
          image: "/icons/success.png",
          duration: 1500,
          success: function () {
            setTimeout(() => {
              (wx.navigateBack({
                delta: 1,
              }))
            }, 1000)

          }
        })

      }).catch((errno) => {
        console.log("修改日程服务器返回错误：", errno);
        wx.showModal({
          title: '修改失败',
          content: '请检查网络连接',
          showCancel: false,
        })
      });


    }
  },

  discard: function() {
    console.log("discard");
    if (this.data.ifAddPage) { // 如果现在是添加页面
      wx.navigateBack({
        delta: 1,
      });
    } else { // 如果现在是修改页面
      wx.redirectTo({
        url: "/pages/schedular/scheduleDetails/scheduleDetails?id=" + this.data.itemIndex,
      })
    }
    this.setData({
      showModal: true,
      msg: "是否放弃信息？"
    })
  }
})