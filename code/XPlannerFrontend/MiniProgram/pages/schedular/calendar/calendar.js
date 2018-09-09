const NORMAL_DAY = "calendar-normal-day";
const SELECTED_DAY = "calendar-selected-day";
const HAVE_ITEM_DAY = "calendar-have-item-day";
const NOT_CURRENT_MONTH_DAY = "calendar-not-current-month-day";
const DAY_TYPE_LIST = [
  NORMAL_DAY,
  SELECTED_DAY,
  HAVE_ITEM_DAY,
  NOT_CURRENT_MONTH_DAY
];
const MONTH_LENGTH_LIST = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
const WEEK_LIST = ["日", "一", "二", "三", "四", "五", "六"];

import moment from "moment";

Component({
  properties: {
    date: {
      type: String,
      default: moment().format("YYYY-MM-DD")
    },
    year: {
      type: Number,
      default: parseInt(moment().format("YYYY"))
    },
    month: {
      type: Number,
      default: parseInt(moment().format("MM"))
    },
    typeList: {
      type: Array,
      default: [],
      observer: function(newVal, oldVal, changedPath) {
        if (oldVal.length === 0) {
          let tmp_month =
            "" +
            this.properties.year +
            "-" +
            (this.properties.month < 10
              ? "0" + this.properties.month
              : this.properties.month);
          this.setData({
            content: {
              a: this.getSpecifiedMonth(tmp_month, -1),
              b: this.getSelectedMonthDayList(
                this.getSpecifiedMonth(tmp_month, 0),
                this.properties.typeList
              ),
              c: this.getSpecifiedMonth(tmp_month, 1)
            },
            showMonth: tmp_month
          });
          console.log("asd",newVal);
          console.log("sdf",oldVal);
          return;
        }
        console.log("new val :: =----",newVal)
        console.log("old val :: =----", oldVal)
        
        switch (this.data.index) {
          case 0: {
            this.setData({
              "content.a": this.getSelectedMonthDayList(
                this.data.content.a,
                newVal
              )
            });
            break;
          }
          case 1: {
            this.setData({
              "content.b": this.getSelectedMonthDayList(
                this.data.content.b,
                newVal
              )
            });
            break;
          }
          case 2: {
            this.setData({
              "content.c": this.getSelectedMonthDayList(
                this.data.content.c,
                newVal
              )
            });
            break;
          }
          default:
            break;
        }

      }
    }
  },
  data: {
    showMonth: "",
    weekTypes: WEEK_LIST,
    index: 1,
    prevIndex: 1,
    content: {
      a: {},
      b: {},
      c: {}
    }
  },
  lifetimes: {
    attached: function() {
      console.log("Launching calendar.");
      // let tmp_month =
      //   "" +
      //   this.properties.year +
      //   "-" +
      //   (this.properties.month < 10
      //     ? "0" + this.properties.month
      //     : this.properties.month);
      // this.setData({
      //   content: {
      //     a: this.getSpecifiedMonth(tmp_month, -1),
      //     b: this.getSelectedMonthDayList(
      //       this.getSpecifiedMonth(tmp_month, 0),
      //       this.properties.typeList
      //     ),

      //     c: this.getSpecifiedMonth(tmp_month, 1)
      //   },
      //   showMonth: tmp_month
      // });
      console.log(this.data.content);
    }
  },
  methods: {
    /**
     * getSpecifiedMonth
     * 获取特定月份的对象，offset取值仅能为0,1,-1
     * @param {String} month
     * @param {int} offset
     */
    getSpecifiedMonth: function(month, offset) {
      let newMonth, yearStr, monthStr;
      switch (offset) {
        case 0: {
          newMonth = month;
          break;
        }
        case 1: {
          yearStr = month.slice(0, 4);
          monthStr = month.slice(5, 7);
          if (monthStr === "12") {
            monthStr = "01";
            yearStr = parseInt(yearStr) + 1 + "";
          } else {
            monthStr = parseInt(monthStr) + 1;
            monthStr = monthStr < 10 ? "0" + monthStr : monthStr;
          }
          newMonth = yearStr + "-" + monthStr;
          break;
        }
        case -1: {
          yearStr = month.slice(0, 4);
          monthStr = month.slice(5, 7);
          if (monthStr === "01") {
            monthStr = "12";
            yearStr = parseInt(yearStr) - 1 + "";
          } else {
            monthStr = parseInt(monthStr) - 1 + "";
            monthStr = monthStr < 10 ? "0" + monthStr : monthStr;
          }
          newMonth = yearStr + "-" + monthStr;
          break;
        }
        default:
          break;
      }
      return {
        dayList: this.getDayList(newMonth),
        month: newMonth
      };
    },

    /**
     * getDayList
     * 获取制定月份的描述dayList
     * @param {String} monthStr
     */
    getDayList: function(monthStr) {
      let result = new Array(42);
      let year = parseInt(monthStr.slice(0, 4));
      let month = parseInt(monthStr.slice(5, 7));
      let startDay = new Date(
        Date.parse((monthStr + "-01").replace(/-/g, "/"))
      ).getDay(); // 1号对应的星期

      let monthLength = MONTH_LENGTH_LIST[month - 1];
      let prevMonthLength = MONTH_LENGTH_LIST[month === 1 ? 11 : month - 2];
      if ((year % 4 === 0 && year % 100 !== 0) || year % 400 === 0) {
        // 判定是闰年
        monthLength = month === 2 ? 29 : 28;
        prevMonthLength = month === 3 ? 29 : 28;
      }

      for (let i = 0; i < startDay; i++) {
        result[i] = {
          day: prevMonthLength - startDay + i + 1,
          type: NOT_CURRENT_MONTH_DAY
        };
      }
      for (let i = 0; i < monthLength; i++) {
        result[i + startDay] = {
          day: i + 1,
          type: NORMAL_DAY
        };
      }
      for (let i = monthLength + startDay; i < 42; i++) {
        result[i] = {
          day: i - (monthLength + startDay - 1),
          type: NOT_CURRENT_MONTH_DAY
        };
      }
      return result;
    },

    /**
     * getSelectedMonthDayList
     * 根据monthObj和givenTypeList，得到选中月份的新dayList
     * @param {Object} monthObj
     * @param {Array} givenTypeList
     */
    getSelectedMonthDayList: function(monthObj, givenTypeList) {
      // let result = monthObj;
      // let i = 0;
      // for (; i < result.dayList.length; i++) {
      //   if (result.dayList[i].day === 1) break;
      // }

      // for (let j = 0; j < givenTypeList.length; j++) {
      //   if (givenTypeList[j].selected) {
      //     result.dayList[i + j].type = SELECTED_DAY;
      //   } else if (givenTypeList[j].haveItems) {
      //     result.dayList[i + j].type = HAVE_ITEM_DAY;
      //   } else {
      //     result.dayList[i + j].type = NORMAL_DAY;
      //   }
      // }
      // return result;
      console.log("given typelist:---",givenTypeList);
      let result = monthObj;
      for (let i = 0; i < result.dayList.length; i++) {
        if (result.dayList[i].day === 1) {
          for (let j = 0; j < givenTypeList.length; j++) {
            if (givenTypeList[j].selected) {
              result.dayList[i + j].type = SELECTED_DAY;
            } else if (givenTypeList[j].haveItems) {
              result.dayList[i + j].type = HAVE_ITEM_DAY;
            } else {
              result.dayList[i + j].type = NORMAL_DAY;
            }
          }
          console.log("changed monthObj ", result);
          return result;
        }
      }
    },

    /**
     * prevMonth
     * 响应点击前一个月的事件
     */
    prevMonth: function() {
      this.triggerEvent("prevMonth", {}, {});
      switch (this.data.index) {
        case 1: {
          this.setData({
            showMonth: this.data.content.a.month,
            content: {
              c: this.data.content.b,
              b: this.data.content.a,
              a: this.getSpecifiedMonth(this.data.content.a.month, -1)
            }
          });
          break;
        }
        case 0: {
          this.setData({
            showMonth: this.data.content.c.month,
            content: {
              b: this.data.content.a,
              a: this.data.content.c,
              c: this.getSpecifiedMonth(this.data.content.c.month, -1)
            }
          });
          break;
        }
        case 2:
          this.setData({
            showMonth: this.data.content.b.month,
            content: {
              a: this.data.content.c,
              c: this.data.content.b,
              b: this.getSpecifiedMonth(this.data.content.b.month, -1)
            }
          });
          break;
        default:
          break;
      }
    },

    /**
     * nextMonth
     * 响应点击后一个月的事件
     */
    nextMonth: function() {
      this.triggerEvent("nextMonth", {}, {});
      switch (this.data.index) {
        case 1: {
          this.setData({
            showMonth: this.data.content.c.month,
            content: {
              a: this.data.content.b,
              b: this.data.content.c,
              c: this.getSpecifiedMonth(this.data.content.c.month, 1)
            }
          });
          break;
        }
        case 0: {
          this.setData({
            showMonth: this.data.content.b.month,
            content: {
              c: this.data.content.a,
              a: this.data.content.b,
              b: this.getSpecifiedMonth(this.data.content.b.month, 1)
            }
          });
          break;
        }
        case 2:
          this.setData({
            showMonth: this.data.content.a.month,
            content: {
              b: this.data.content.c,
              c: this.data.content.a,
              a: this.getSpecifiedMonth(this.data.content.a.month, 1)
            }
          });
          break;
        default:
          break;
      }
    },

    /**
     * changeMonth
     * 响应滑动改变月份的事件
     */
    changeMonth: function(e) {
      if (e.detail.source === "touch") {
        if (e.detail.current === 2 && this.data.index === 1) {
          this.triggerEvent("nextMonth", {}, {});
          this.setData({
            "content.a": this.getSpecifiedMonth(this.data.content.c.month, 1)
          });
        } else if (e.detail.current === 0 && this.data.index === 1) {
          this.triggerEvent("prevMonth", {}, {});
          this.setData({
            "content.c": this.getSpecifiedMonth(this.data.content.a.month, -1)
          });
        } else if (e.detail.current === 2 && this.data.index === 0) {
          this.triggerEvent("prevMonth", {}, {});
          this.setData({
            "content.b": this.getSpecifiedMonth(this.data.content.c.month, -1)
          });
        } else if (e.detail.current === 1 && this.data.index === 0) {
          this.triggerEvent("nextMonth", {}, {});
          this.setData({
            "content.c": this.getSpecifiedMonth(this.data.content.b.month, 1)
          });
        } else if (e.detail.current === 0 && this.data.index === 2) {
          this.triggerEvent("nextMonth", {}, {});
          this.setData({
            "content.b": this.getSpecifiedMonth(this.data.content.a.month, 1)
          });
        } else if (e.detail.current === 1 && this.data.index === 2) {
          this.triggerEvent("prevMonth", {}, {});
          this.setData({
            "content.a": this.getSpecifiedMonth(this.data.content.b.month, -1)
          });
        }

        switch (e.detail.current) {
          case 0: {
            this.setData({
              showMonth: this.data.content.a.month
            });
            break;
          }
          case 1: {
            this.setData({
              showMonth: this.data.content.b.month
            });
            break;
          }
          case 2: {
            this.setData({
              showMonth: this.data.content.c.month
            });
            break;
          }
          default:
            break;
        }

        this.setData({
          prevIndex: this.data.index,
          index: e.detail.current
        });
      }
    },

    selectDay: function(e) {
      let detail = {
        year: parseInt(this.data.showMonth.slice(0, 4)),
        month: parseInt(this.data.showMonth.slice(5, 7)),
        day: e.currentTarget.dataset.day
      };
      this.triggerEvent("dayClick", detail, {});
    }
  }
});
