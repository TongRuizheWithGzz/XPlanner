var app = getApp();
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
var moment = require("../../component/moment");
var selectedDate = "2018-1-10";
var scheduleItems = require("../../data/scheduleItem");
const MONTHS = ['Jan.', 'Feb.', 'Mar.', 'Apr.', 'May.', 'June.', 'July.', 'Aug.', 'Sept.', 'Oct.', 'Nov.', 'Dec.'];

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
    scheduleItems: [],
    height: 0,
    fix: false,
    hideFixTop: true,
    time: 0,
    year: 2018,
    month: 7,
    day: 18,
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
    var selected_date = selectedDate;
    var selected_year = 2018;
    var selected_month = 7;
    var selected_day = 18;
    var date_with_item = [3, 4, 5, 7, 17, 18, 19, 20, 21, 22, 28];
    var tmp_schedule = scheduleItems;

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
      scheduleItems: tmp_schedule,
      height: 86.796875 * (that.data.scheduleItems.length) + 540,
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
  addSchedule() {
    wx.navigateTo({
      url: '/pages/schedular/addSchedule/add',
    })
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
    for (var i = 0; i < date_with_item.length; i++) {
      new_day = tmp_day_list[date_with_item[i] - 1].day;
      tmp_day_list[date_with_item[i] - 1] = {
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
    new_day = tmp_day_list[selected_day - 1].day;
    var have_items = tmp_day_list[selected_day - 1].haveItems;
    tmp_day_list[selected_day - 1] = {
      month: 'current',
      day: new_day,
      color: 'white',
      background: '#ffb72b',
      loaded: false,
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

  },

  /*
   * nextMonth
   * 月历向后翻页触发事件
   */
  nextMonth: function () {

  },

  /*
   * dayClick
   * 响应点击某天的事件
   */
  dayClick: function (e) {
    var tmp_year = e.detail.year;
    var tmp_month = e.detail.month;
    var tmp_day = e.detail.day;
    if (tmp_month == this.data.month) { // 如果点击的是本月的日期
      if (this.data.dayList[tmp_day - 1].selected) { // 如果点击已经选中的日期
        console.log("点击本月已经选中的日期");
        return;
      } else if (!this.data.dayList[tmp_day - 1].loaded) { // 如果点击尚未加载的日期
        console.log("点击本月尚未加载日程的日期");
        /* 向后端请求相应日程 */
        var tmp_items = this.getScheduleItemsByDay(tmp_year + "-" + tmp_month + "-" + tmp_day);
        tmp_items = tmp_items.concat(this.data.scheduleItems); // 合并新旧日程
        console.log("得到新日程列表");

        /* 更新月历显示和日程 */
        var tmp_day_list = this.data.dayList;
        /* 更新旧日期的显示 */
        tmp_day_list[this.data.day - 1] = this.getModifiedOldDay(tmp_day_list[this.data.day - 1]);
        /* 更新新日期的显示 */
        tmp_day_list[tmp_day - 1] = this.getModifiedNewDay(tmp_day_list[tmp_day - 1]);
        this.setData({
          day: tmp_day,
          dayList: tmp_day_list,
          scheduleItems: tmp_items,
        });
      } else { // 如果点击已经加载、未选中的日期
        var tmp_day_list = this.data.dayList;
        console.log("点击已经加载、未选中的日期");
        /* 更新旧日期的显示 */
        tmp_day_list[this.data.day - 1] = this.getModifiedOldDay(tmp_day_list[this.data.day - 1]);
        /* 更新新日期的显示 */
        tmp_day_list[tmp_day - 1] = this.getModifiedNewDay(tmp_day_list[tmp_day - 1]);
        this.setData({
          day: tmp_day,
          dayList: tmp_day_list,
        });
      }
    } else { // 如果点击的不是本月的日期
      console.log("点击的不是本月的日期");
    }
  },

  /*
   * getDayWithItems
   * 向后端发送代表某天的字符串，获取对应的月份的有日程的日子的数组，更新月历
   */
  getDayWithItemsByDay: function (str) {
    /* 此处为模拟 */
    return [];
  },

  /*
   * getScheduleItemsByDay
   * 向后端发送代表某天的字符串，获取对应的日期的日程列表，更新日程和月历
   */
  getScheduleItemsByDay: function (str) {
    /* 此处为模拟 */
    return [];
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
  }
})