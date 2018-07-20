var app = getApp();
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
// var scheduleItems = app.globalData.scheduleItems;
var time = require("../../common/time");
const MONTHS = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'June.', 'July.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.'];

const NORMAL_DAY_COLOR = 'grey';
const NORMAL_DAY_BACKGROUND = '#fff';
const SELECT_DAY_COLOR = 'white';
const SELECT_DAY_BACKGROUND = '#ffb72b';
const WORK_DAY_COLOR = 'white';
const WORK_DAY_BACKGROUND = '#46c4f3';

Page({
  data: {
    showItems: [],
    height: 0,
    fix: false,
    hideFixTop: true,
    time: 0,
    showSelect: -1,

    showYear: app.globalData.year,
    showMonth: app.globalData.month,
    monthStr: MONTHS[app.globalData.month - 1],
    // monthList: {},
    dayList: [],
  },
  onLoad: function () {
    /* 获取要显示的日程列表 */
    var tmp_show_items = app.globalData.scheduleItems;

    /* 生成本月对应的dayList */
    var tmp_day_list = this.generateDayList(app.globalData.dayWithItem, app.globalData.year, app.globalData.month, app.globalData.day);
    console.log(tmp_day_list);

    this.setData({
      dayList: tmp_day_list,
      showItems: tmp_show_items,
    })
  },
  onShow: function () {
    // console.log("on show");
    if (app.globalData.ifAddSchedule) { // 从add页面返回并且添加了日程
      if (app.globalData.ifSameDay) { // 添加的日程和目前显示的日期是相同的
        if (this.data.showItems.length == 0) { // 当前日期原来没有日程
          var tmp_day_list = this.data.dayList;
          tmp_day_list[app.globalData.day - 1].haveItems = true;
          var tmp_show_items = app.globalData.scheduleItems;
          this.setData({
            dayList: tmp_day_list,
            showItems: tmp_show_items,
          })
        } else { // 当前日期原来有日程
          var tmp_show_items = app.globalData.scheduleItems;
          this.setData({
            showItems: tmp_show_items,
          })
        }
      } else { // 添加的日程和目前显示的日期是不同的
        var new_item_date = app.globalData.scheduleItems[app.globalData.scheduleItems.length - 1].start_time;
        var tmp_day_list = this.data.dayList;
        tmp_day_list[parseInt(new_item_date.slice(8, 10)) - 1].haveItems = true;
        tmp_day_list[parseInt(new_item_date.slice(8, 10)) - 1].background = WORK_DAY_BACKGROUND;
        tmp_day_list[parseInt(new_item_date.slice(8, 10)) - 1].color = WORK_DAY_COLOR;
        // console.log(tmp_month_list);
        this.setData({
          dayList: tmp_day_list,
        })
      }
      app.globalData.ifAddSchedule = false;
      app.globalData.ifSameDay = false;
    } else { // 普通显示或者放弃添加日程
      // console.log("no thing happen after on show");
    }
  },

  bindChange: function (e) {
    var current = e.detail.current;
    this.setData({
      activeIndex: current,
      index: current,
    });
  },
  detail: function (event) {
    wx.navigateTo({
      url: '/pages/schedular/scheduleDetails/scheduleDetails?id=' +
        event.currentTarget.dataset.index,
    })
  },
  select: function (event) {
    this.setData({
      showSelect: event.currentTarget.dataset.index
    })
  },
  unselect: function () {
    this.setData({
      showSelect: -1
    })
  },
  edit: function () {
    wx.navigateTo({
      url: '/pages/schedular/add/scheduleDetails?id=' +
        event.currentTarget.dataset.index,
    })
  },

  /*
   * addSchedule
   * 添加日程
   */
  addSchedule() {
    wx.navigateTo({
      url: '/pages/schedular/addSchedule/add',
    })
  },

  /*
   * delete
   * 移除日程
   */
  delete(e) {
    /* 向后端发送请求 */

    /* 更改scheduleItems */
    var tmp_items = app.globalData.scheduleItems;
    tmp_items.splice(e.currentTarget.dataset.index, 1);
    app.globalData.scheduleItems = tmp_items;
    this.setData({
      showItems: tmp_items,
    });

    if (tmp_items.length == 0) {
      var day_list = this.data.dayList;
      day_list[app.globalData.day - 1].haveItems = 0;
      this.setData({
        dayList: day_list,
      })
    }
    return;
  },

  /*
   * generateDayList
   * 生成对应的dayList，用于控制月历的样式、对应日期日程信息的加载
   */
  generateDayList: function (date_with_item, selected_year, selected_month, selected_day) {
    /* 设置月历的样式，注意所有有日程的日子均为蓝色，选中的日子为橙色 */
    var tmp_day_list = [];
    var new_day = 0;
    const days_count = new Date(app.globalData.year, app.globalData.month, 0).getDate();
    /* 设置普通日期 */
    for (var i = 1; i <= days_count; i++) {
      tmp_day_list.push({
        month: 'current',
        day: i,
        color: NORMAL_DAY_COLOR,
        background: NORMAL_DAY_BACKGROUND,
        selected: false, // 指示是否选择
        haveItems: false, // 指示是否有日程
      });
    }
    /* 设置有日程的日期 */
    for (var item in date_with_item) {
      new_day = tmp_day_list[item - 1].day;
      tmp_day_list[item - 1] = {
        month: 'current',
        day: new_day,
        color: WORK_DAY_COLOR,
        background: WORK_DAY_BACKGROUND,
        selected: false,
        haveItems: true,
      };
    }
    /* 设置选中的日期 */
    if (selected_day == 0)
      return tmp_day_list;
    new_day = tmp_day_list[selected_day - 1].day;
    var have_items = tmp_day_list[selected_day - 1].haveItems;
    tmp_day_list[selected_day - 1] = {
      month: 'current',
      day: new_day,
      color: SELECT_DAY_COLOR,
      background: SELECT_DAY_BACKGROUND,
      selected: true,
      haveItems: have_items,
    };
    return tmp_day_list;
  },

  /*
   * prevMonth
   * 月历向前翻页触发事件
   */
  prevMonth: function () {
    var year = this.data.showYear;
    var month = this.data.showMonth;
    if (month == 1) { // 判断是否是1月
      year--;
      month = 12;
    } else {
      month--;
    }

    /* 向后端发送前一个月的信息，获取对应的有日程的日期数组 */
    var day_with_items = this.getDayWithItemsFromBackEnd(year, month);
    var tmp_day_list = this.generateDayList(day_with_items, year, month, 0);
    this.setData({
      dayList: tmp_day_list,
      showYear: year,
      showMonth: month,
    });
    console.log("change to month " + this.data.showYear + " " + this.data.showMonth);
    console.log(this.data.dayList);
  },

  /*
   * nextMonth
   * 月历向后翻页触发事件
   */
  nextMonth: function () {
    var year = this.data.showYear;
    var month = this.data.showMonth;
    if (month == 12) { // 判断是否是1月
      year++;
      month = 1;
    } else {
      month++;
    }

    /* 向后端发送后一个月的信息，获取对应的有日程的日期数组 */
    var day_with_items = this.getDayWithItemsFromBackEnd(year, month);
    var tmp_day_list = this.generateDayList(day_with_items, year, month, 0);
    this.setData({
      dayList: tmp_day_list,
      showYear: year,
      showMonth: month,
    });
    console.log("change to month " + this.data.showYear + " " + this.data.showMonth);
    console.log(this.data.dayList);
  },

  /*
   * getDayWithItems
   * 向后端发送代表某天的字符串，获取对应的月份的有日程的日子的数组
   */
  getDayWithItemsFromBackEnd: function (year, month) {
    /* 此处为模拟 */
    return {
      12: [2],
      13: [1],
      20: [1],
    };
  },

  /*
   * getScheduleItemsFromBackEndAndWarp
   * 向后端发送代表某天的字符串，获取对应的日期的日程列表
   */
  getScheduleItemsFromBackEndAndWarp: function (str) {
    /* 此处为模拟 */
    var tmp;
    if (str == "2018-07-20") {
      tmp = [
        {
          "title": "上学",
          "start_time": "2018-07-20 19:00",
          "end_time": "2018-07-20 20:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 4,
          "user_id": 1
        },
        {
          "title": "运动",
          "start_time": "2018-07-20 17:00",
          "end_time": "2018-07-20 15:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 5,
          "user_id": 1
        }
      ];
    } else if (str == "2018-07-17") {
      tmp = [
        {
          "title": "上学",
          "start_time": "2018-07-17 06:00",
          "end_time": "2018-07-17 08:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 6,
          "user_id": 1
        },
      ];
    } else if (str == "2018-06-12") {
      tmp = [
        {
          "title": "运动",
          "start_time": "2018-06-12 08:00",
          "end_time": "2018-06-12 10:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 7,
          "user_id": 1
        },
        {
          "title": "上学",
          "start_time": "2018-06-12 08:00",
          "end_time": "2018-06-12 10:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 8,
          "user_id": 1
        },
      ];
    } else if (str == "2018-06-20") {
      tmp = [
        {
          "title": "运动",
          "start_time": "2018-06-20 08:00",
          "end_time": "2018-06-20 10:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 9,
          "user_id": 1
        },
      ];
    } else if (str == "2018-06-13") {
      tmp = [
        {
          "title": "运动",
          "start_time": "2018-06-13 08:00",
          "end_time": "2018-06-13 10:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 10,
          "user_id": 1
        },
      ];
    } else if (str == "2018-07-18") {
      tmp = [
        {
          "title": "吃午饭",
          "start_time": "2018-07-18 10:00",
          "end_time": "2018-07-18 11:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 1,
          "user_id": 1
        },
        {
          "title": "上学",
          "start_time": "2018-07-18 12:00",
          "end_time": "2018-07-18 13:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 2,
          "user_id": 1
        },
        {
          "title": "运动",
          "start_time": "2018-07-18 13:00",
          "end_time": "2018-07-18 15:00",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 3,
          "user_id": 1
        }
      ];
    } else {
      console.log("no record!");
      return [];
    }

    /* 处理后端传来的scheduleItems数组 */
    for (var i = 0; i < tmp.length; i++) {
      tmp[i].start_concret_time = tmp[i].start_time.slice(11, 16);
      tmp[i].end_concret_time = tmp[i].end_time.slice(11, 16);
    }
    console.log(tmp);
    return tmp;
  },

  /*
   * getModifiedNewDay
   * 更新新日期的显示
   */
  getModifiedNewDay: function (day) {
    console.log(day);
    var day_obj = day;
    day_obj.color = SELECT_DAY_COLOR;
    day_obj.selected = true;
    day_obj.background = SELECT_DAY_BACKGROUND;
    return day_obj;
  },

  /*
   * getModifiedOldDay
   * 更新旧日期的显示
   */
  getModifiedOldDay: function (day) {
    var day_obj = day;
    if (day_obj.haveItems) { // 如果旧日期有日程
      day_obj.selected = false;
      day_obj.background = WORK_DAY_BACKGROUND;
      day_obj.color = WORK_DAY_COLOR;
    } else { // 如果旧日期没有日程
      day_obj.color = NORMAL_DAY_COLOR;
      day_obj.selected = false;
      day_obj.background = NORMAL_DAY_BACKGROUND; // ?
    }
    return day_obj;
  },

  /*
   * dayClick
   * 响应点击某天的事件
   */
  selectDay: function (e) {
    var new_year = e.detail.year;
    var new_month = e.detail.month;
    var new_day = e.detail.day;
    var old_year = app.globalData.year;
    var old_month = app.globalData.month;
    var old_day = app.globalData.day;
    var show_year = this.data.showYear;
    var show_month = this.data.showMonth;
    var new_date = time.getDateStringWithZero(new_year, new_month, new_day);
    // var tmp_month_list = this.data.monthList;

    if ((new_day == old_day && new_month == old_month && // 判断是否点击同一天
      new_year == old_year && show_month == old_month &&
      show_year == old_year) ||
      (show_month == old_month && show_year == old_year && // 判断是否点击同页上不同月份的日期
        new_month != old_month) ||
      ((show_month != old_month || show_year != old_year)) && // 判断是否点击非已选择日期所在页上非主月日期
      (show_month != new_month)) {
      console.log("Do nothing.");
      return;
    }

    app.globalData.year = new_year;
    app.globalData.month = new_month;
    app.globalData.day = new_day;
    app.globalData.date = new_date;

    /* 本地存储新日期 */

    /* 设置控制日程显示的showItems和scheduleItems */
    var tmp_items = this.getScheduleItemsFromBackEndAndWarp(new_date);
    app.globalData.scheduleItems = tmp_items;
    this.setData({
      showItems: tmp_items,
    });

    /* 设置控制月历显示的dayList */
    if (new_month == old_month && new_year == old_year) { // 在当前月内选择
      var day_list = this.data.dayList;
      day_list[old_day - 1] = this.getModifiedOldDay(day_list[old_day - 1]);
      day_list[new_day - 1] = this.getModifiedNewDay(day_list[new_day - 1]);
    } else { // 不在当前月内选择
      var day_list = this.generateDayList(this.getDayWithItemsFromBackEnd(new_year, new_day), new_year, new_month, new_day);
      console.log(day_list);
      day_list[new_day - 1] = this.getModifiedNewDay(day_list[new_day - 1]);
      console.log("flag");
    }
    this.setData({
      dayList: day_list,
    });
  }
})