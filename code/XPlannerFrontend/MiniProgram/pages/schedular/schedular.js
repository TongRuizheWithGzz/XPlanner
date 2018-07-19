var app = getApp();
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
var scheduleItems = app.globalData.scheduleItems;
const MONTHS = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'June.', 'July.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.'];

function getDateStr(year, month, day) {
  if (month < 10) month = "0" + month;
  if (day < 10) day = "0" + day;
  console.log("" + year + "-" + month + "-" + day);
  return "" + year + "-" + month + "-" + day;
}

Page({
  data: {
    showCheck: true,
    tabs: ["今天", "明天"],
    pageData: [{
      "data": "pageA"
    }, {
      "data": "pageB"
    }], //page数据
    activeIndex: 0,
    slideOffset: 0,
    index: 0,
    sliderOffset: 0,
    sliderLeft: 0,
    showItems: [],
    height: 0,
    fix: false,
    hideFixTop: true,
    time: 0,

    year: 2018,
    month: 7,
    day: 18,
    selectedDate: "2018-07-18",
    showYear: 2018,
    showMonth: 7,
    monthStr: MONTHS[new Date().getMonth()],
    dayList: [],
    monthList: {},
  },
  onLoad: function () {
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2,
          sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex
        })
      }
    });

    /* 获取选择的日期，向后端发送选择的日期，要求获取有日程的日子的数组和当天的日程列表 */
    var selected_date = "2018-07-18";
    var selected_year = 2018;
    var selected_month = 7;
    var selected_day = 18;
    var date_with_item = {
      17: [1, 0],
      18: [3, 0],
      20: [2, 0]
    }
    var tmp_show_items = this.filterScheduleItems(app.globalData.scheduleItems, selected_date);

    /* 生成本月对应的dayList */
    var tmp_day_list = this.generateDayList(date_with_item, selected_year, selected_month, selected_day);

    /* 生成monthList */
    var tmp_month_list = new Object();
    tmp_month_list[selected_year + "-" + selected_month] = tmp_day_list;
    console.log(tmp_month_list);

    this.setData({
      dayList: tmp_day_list,
      monthList: tmp_month_list,
      year: selected_year,
      month: selected_month,
      day: selected_day,
      selectedDate: selected_date,
      showItems: tmp_show_items,
      height: 86.796875 * (app.globalData.scheduleItems.length) + 540,
    })
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
        event.currentTarget.dataset.id +
        '&day=' +
        this.data.tabs[this.data.activeIndex],
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
   * removeSchedule
   * 移除日程
   */
  removeSchedule(e) {
    /* 更改scheduleItems */
    var tmp_items = app.globalData.scheduleItems;
    var tmp_show_items = this.data.showItems;
    console.log(e.currentTarget.dataset.scheduleItemId);
    for (var i = 0; i < tmp_items.length; i++) {
      if (tmp_items[i].scheduleItem_id == e.currentTarget.dataset.scheduleItemId) {
        console.log("Removing ");
        console.log(tmp_items[i]);
        tmp_items.splice(i, 1);
      }
    }
    app.globalData.scheduleItems = tmp_items;

    /* 更改removeSchedule */
    for (var i = 0; i < tmp_show_items.length; i++) {
      if (tmp_show_items[i].scheduleItem_id == e.currentTarget.dataset.scheduleItemId) {
        tmp_show_items.splice(i, 1);
        if (tmp_show_items.length == 0) {
          /* 设置monthList */
          var tmp_month_list = this.data.monthList;
          var tmp_day_list = tmp_month_list[this.data.year + "-" + this.data.month];
          tmp_day_list[this.data.day - 1].haveItems = false;
          tmp_month_list[this.data.year + "-" + this.data.month] = tmp_day_list;
          this.setData({
            showItems: tmp_show_items,
            monthList: tmp_month_list,
          });
        } else {
          this.setData({
            showItems: tmp_show_items
          })
        }
      }
    }
    return;
  },

  onPageScroll: function (e) {
    if (e.scrollTop > 228) {
      this.setData({
        hideFixTop: false,
        time: parseInt((e.scrollTop - 228) / 304.390625) * 3,
      });
    } else
      this.setData({
        hideFixTop: true,
      })
  },

  unfold: function () {
    console.log("sb");
  },

  /*
   * generateDayList
   * 生成对应的dayList，用于控制月历的样式、对应日期日程信息的加载
   */
  generateDayList: function (date_with_item, selected_year, selected_month, selected_day) {
    /* 设置月历的样式，注意所有有日程的日子均为蓝色，选中的日子为橙色 */
    var tmp_day_list = [];
    var new_day = 0;
    const days_count = new Date(selected_year, selected_month, 0).getDate();
    /* 设置普通日期 */
    for (var i = 1; i <= days_count; i++) {
      tmp_day_list.push({
        month: 'current',
        day: i,
        color: "grey",
        loaded: false, // 指示是否已加载相应日程数据
        selected: false, // 指示是否选择
        haveItems: false, // 指示是否有日程
      });
    }
    /* 设置有日程的日期 */
    console.log(date_with_item);
    for (var item in date_with_item) {
      console.log(item);
      new_day = tmp_day_list[item - 1].day;
      tmp_day_list[item - 1] = {
        month: 'current',
        day: new_day,
        color: 'white',
        background: '#46c4f3',
        loaded: false,
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
      color: 'white',
      background: '#ffb72b',
      loaded: true,
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

    if (!this.data.monthList[year + "-" + month]) { // 判断前端是否已经加载对应月的信息
      /* 向后端发送前一个月的信息，获取对应的有日程的日期数组 */
      var day_with_items = this.getDayWithItemsByMonth(year, month);
      var tmp_month_list = this.data.monthList;
      tmp_month_list[year + "-" + month] = this.generateDayList(day_with_items, year, month, 0);
      this.setData({
        monthList: tmp_month_list,
        showYear: year,
        showMonth: month,
      });
      console.log(this.data.showYear + " " + this.data.showMonth);
      console.log(this.data.monthList);
    } else {
      this.setData({
        showYear: year,
        showMonth: month,
      });
      console.log(this.data.showYear + " " + this.data.showMonth);
      console.log(this.data.monthList);
    }
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

    if (!this.data.monthList[year + "-" + month]) { // 判断前端是否已经加载对应月的信息
      /* 向后端发送后一个月的信息，获取对应的有日程的日期数组 */
      var day_with_items = this.getDayWithItemsByMonth(year, month);
      var tmp_month_list = this.data.monthList;
      tmp_month_list[year + "-" + month] = this.generateDayList(day_with_items, year, month, 0);
      this.setData({
        monthList: tmp_month_list,
        showYear: year,
        showMonth: month,
      });
      console.log(this.data.showYear + " " + this.data.showMonth);
      console.log(this.data.monthList);
    } else {
      this.setData({
        showYear: year,
        showMonth: month,
      });
      console.log(this.data.showYear + " " + this.data.showMonth);
      console.log(this.data.monthList);
    }
  },

  /*
   * getDayWithItems
   * 向后端发送代表某天的字符串，获取对应的月份的有日程的日子的数组
   */
  getDayWithItemsByMonth: function (year, month) {
    /* 此处为模拟 */
    return {
      12: [2, 0],
      13: [1, 0],
      20: [1, 0],
    };
  },

  /*
   * getScheduleItemsByDay
   * 向后端发送代表某天的字符串，获取对应的日期的日程列表
   */
  getScheduleItemsByDay: function (str) {
    /* 此处为模拟 */
    if (str == "2018-07-20") {
      return [
        {
          "title": "上学",
          "start_time": "2018-07-20",
          "end_time": "2018-07-20",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 4,
          "user_id": 1
        },
        {
          "title": "运动",
          "start_time": "2018-07-20",
          "end_time": "2018-07-20",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 5,
          "user_id": 1
        }
      ];
    } else if (str == "2018-07-17") {
      return [
        {
          "title": "上学",
          "start_time": "2018-07-17",
          "end_time": "2018-07-17",
          "description": "Eat more and more and more Eat more and more and more Eat more and more and more",
          "address": "二餐",
          "scheduleItem_id": 6,
          "user_id": 1
        },
      ];
    } else if (str == "2018-06-12") {
      return [
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
      return [
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
      return [
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
    } else {
      console.log("no record!");
      return [];
    }
  },

  /*
   * getModifiedNewDay
   * 更新新日期的显示
   */
  getModifiedNewDay: function (day) {
    var day_obj = day;
    day_obj.color = 'white';
    day_obj.selected = true;
    day_obj.loaded = true;
    day_obj.background = '#ffb72b';
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
      day_obj.background = '#46c4f3';
    } else { // 如果旧日期没有日程
      day_obj.color = 'grey';
      day_obj.selected = false;
      day_obj.background = '#fff'; // ?
    }
    return day_obj;
  },

  /*
   * filterScheduleItems
   * 从日程的列表中获得相应日期的日程数组
   */
  filterScheduleItems: function (items, date) {
    var result = [];
    for (var i = 0; i < items.length; i++) {
      if (items[i].start_time.slice(0, 10) == date) {
        console.log(items[i].start_time.slice(0, 10) + "%");
        result.push(items[i]);
      }
    }
    return result;
  },

  /*
   * dayClick
   * 响应点击某天的事件
   */
  selectDay: function (e) {
    var new_year = e.detail.year;
    var new_month = e.detail.month;
    var new_day = e.detail.day;
    var old_year = this.data.year;
    var old_month = this.data.month;
    var old_day = this.data.day;
    var show_year = this.data.showYear;
    var show_month = this.data.showMonth;
    var new_date = getDateStr(new_year, new_month, new_day);
    var tmp_month_list = this.data.monthList;

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

    this.setData({
      year: new_year,
      month: new_month,
      day: new_day,
      selectedDate: new_date,
    });

    /* 设置控制日程显示的showItems和scheduleItems */
    var new_day_list = this.data.monthList[new_year + "-" + new_month];
    if (!new_day_list[new_day - 1].loaded) { // 判断是否已经加载
      var tmp_items = this.getScheduleItemsByDay(new_date);
      var tmp_show_items = tmp_items;
      tmp_items = tmp_items.concat(app.globalData.scheduleItems);
      app.globalData.scheduleItems = tmp_items;
    } else {
      var tmp_show_items = this.filterScheduleItems(app.globalData.scheduleItems, new_date);
    }
    this.setData({
      showItems: tmp_show_items,
    })

    /* 设置控制月历显示的monthList */
    if (new_month == old_month && new_year == old_year) { // 判断是否在当前月内选择
      new_day_list[old_day - 1] = this.getModifiedOldDay(new_day_list[old_day - 1]);
      new_day_list[new_day - 1] = this.getModifiedNewDay(new_day_list[new_day - 1]);
      tmp_month_list[old_year + "-" + old_month] = new_day_list;
    } else {
      var old_day_list = this.data.monthList[old_year + "-" + old_month];
      old_day_list[old_day - 1] = this.getModifiedOldDay(old_day_list[old_day - 1]);
      new_day_list[new_day - 1] = this.getModifiedNewDay(new_day_list[new_day - 1]);
      tmp_month_list[old_year + "-" + old_month] = old_day_list;
      tmp_month_list[new_year + "-" + new_month] = new_day_list;
    }
    this.setData({
      monthList: tmp_month_list,
    });
  }
})