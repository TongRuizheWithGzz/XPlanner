var keeperItems = require("../../data/scheduleItem");
var sliderWidth = 96;

Page({
  data: {
    showCheckbox: true,
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
    scheduleItems: keeperItems,
    height: 0,
    fix: false,
    hideFixTop: true,
    time: 0,
  },
  onLoad: function() {
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        that.setData({
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2,
          sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex
        })
      }
    });
    that.setData({
      height: 86.796875 * (that.data.scheduleItems.length) + 540,
    })
  },
  bindChange: function(e) {
    var current = e.detail.current;
    this.setData({
      activeIndex: current,
      index: current,
    });
  },
  detail: function(event) {
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
  onPageScroll: function(e) {

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

  unfold: function() {
    console.log("sb")
  },
  showCheck: function() {
    this.setData({
      showCheckbox: false,
    })
  },

  gotoSelector: function() {
    wx.navigateTo({
      url: "/extensions/keeper/selector/selector",
    })
  },
  gotoCamera: function() {
    wx.navigateTo({
      url: "/extensions/keeper/camera/camera",
    })
  }
})