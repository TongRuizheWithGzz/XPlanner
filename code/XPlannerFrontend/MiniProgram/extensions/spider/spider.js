var sedata = require("../../data/spiderItems.js")
var spider = require("../../common/spider");
var wrapper = require("../../interface/wrapper/wrapper.js");

var api = require("../../interface/config/api.js");

var app = getApp();

Page({
  data: {
    animationlist: [],
    isAnimation: false,
    zindex: [
      1, 2, 3
    ],
    animationlistyet: [],
    spiderItems: [1, 2, 3],
    pageNumber: 1,
    x1: -1,
    x2: -1,
    page: 1,
    crawled: [],

  },
  onLoad: function() {

    // app.globalData.spiderItems = [spider.warpSpiderItems(tmp)];
    // this.setData({
    //   crawled: tmp,
    // })
  },
  onShow: function() {

    //load page 1
    wx.showLoading({
      title: "正在加载",
      mask: true,
    })
    //向后端发送请求，获取5个东西
    // var tmp = sedata;
    wrapper.wxRequestWrapper(api.spiderAPI, "GET", {
      pageNumber: 0,
      size: 5,
    }).then((data) => {
      var notifications = data.notifications;
      app.globalData.spiderItems = [spider.warpSpiderItems(notifications)];
      this.setData({
        crawled: notifications,
      })
      wx.hideLoading()
    }).catch((errno) => {
      wx.hideLoading()
      console.log("Spider 加载失败", errno);
      wx.showModal({
        title: '请求失败',
        content: '请检查网络设置',
        showCancel: false,
      })
    })
  },
  start: function(e) {
    this.setData({
      x1: e.changedTouches[0].pageX,
    })
  },
  end: function(e) {
    this.setData({
      x2: e.changedTouches[0].pageX,
    })
    if (this.data.x1 === this.data.x2)
      return;
    if (this.data.x1 > this.data.x2) {
      wrapper.wxRequestWrapper(api.spiderAPI, "GET", {
        pageNumber: this.data.pageNumber + 1 - 1,
        size: 5,
      }).then((data) => {
        var notifications = data.notifications;
        app.globalData.spiderItems = [spider.warpSpiderItems(notifications)];
        this.setData({
          crawled: notifications,
        })
        this.setData({
          pageNumber: this.data.pageNumber + 1 //爬取后段
        })
        wx.hideLoading()
        this.slidethis()
      }).catch((errno) => {
        wx.hideLoading()
        console.log("Spider 加载失败", errno);
        wx.showModal({
          title: '请求失败',
          content: '请检查网络设置',
          showCancel: false,
        })
      })
      // this.setData({
      //   pageNumber: this.data.pageNumber + 1 //爬取后段
      // })
    } else if (this.data.x1 < this.data.x2) {
      if (this.data.pageNumber === 1)
        return;
      wrapper.wxRequestWrapper(api.spiderAPI, "GET", {
        pageNumber: this.data.pageNumber + 1 - 1,
        size: 5,
      }).then((data) => {
        var notifications = data.notifications;
        app.globalData.spiderItems = [spider.warpSpiderItems(notifications)];
        this.setData({
          crawled: notifications,
        })
        this.setData({
          pageNumber: this.data.pageNumber + 1 //爬取后段
        })
        wx.hideLoading()
        this.slidethis()
      }).catch((errno) => {
        wx.hideLoading()
        console.log("Spider 加载失败", errno);
        wx.showModal({
          title: '请求失败',
          content: '请检查网络设置',
          showCancel: false,
        })
        this.setData({
          pageNumber: this.data.pageNumber - 1 - 1
        });

      })
      // this.setData({
      //   pageNumber: this.data.pageNumber - 1
      // });
    }
   
  },
  //事件处理函数
  slidethis: function(e) {
    console.log("slide")

    var self = this;
    if (this.data.isAnimation) {
      return false;
    }
    this.setData({
      isAnimation: true
    });
    var animation1 = wx.createAnimation({
      duration: 300,
      timingFunction: 'cubic-bezier(.8,.2,.1,0.8)'
    });
    this.animation1 = animation1;
    this
      .animation1
      .translateY(8)
      .translateX(36)
      .rotate(0)
      .scale(1)
      .step();
    var animation2 = wx.createAnimation({
      duration: 300,
      timingFunction: 'cubic-bezier(.8,.2,.1,0.8)'
    });
    this.animation2 = animation2;
    this
      .animation2
      .translateY(42)
      .translateX(10)
      .rotate(0)
      .step();
    var animation3 = wx.createAnimation({
      duration: 300,
      timingFunction: 'cubic-bezier(.8,.2,.1,0.8)'
    });
    this.animation3 = animation3;
    this
      .animation3
      .translateY(24)
      .translateX(26)
      .rotate(0)
      .step();
    if (this.data.animationlistyet.length <= 0) {
      this.data.animationlistyet = [
        this.animation1.export(),
        this.animation2.export(),
        this.animation3.export()
      ];
    }
    this.setData({
      animationlist: this.data.animationlistyet
    });
    var animationlistyet = self.data.animationlistyet;
    var animation = self
      .data
      .animationlistyet
      .pop();
    self
      .data
      .animationlistyet
      .unshift(animation);
    self.setData({
      animationlist: [],
      animationlistyet: self.data.animationlistyet
    });
    setTimeout(function() {
      var zindex = self.data.zindex;
      var slidethis = self
        .data
        .zindex
        .shift();
      self
        .data
        .zindex
        .push(slidethis);
      self.setData({
        isAnimation: false,
        zindex: self.data.zindex
      });
    }, 100);
  },
  refresh: function(e) {
    this.slidethis();

    /* 向后端请求page1 */

    var tmp = sedata;
    var tmp_items = app.globalData.spiderItems;
    tmp_items[0] = spider.warpSpiderItems(tmp); // 更新全局数据，需要更改
    app.globalData.spiderItems = tmp_items;
    this.setData({
      crawled: app.globalData.spiderItems[0],
      pageNumber: 1
    })
  },
  detail: function(e) {
    wx.navigateTo({
      url: '/pages/schedular/scheduleDetails/scheduleDetails?pageNumber=' + this.data.pageNumber + '&spiderIndex=' + e.currentTarget.dataset.index,
    })
  },
  addSpiderItem: function(e) {
    wx.navigateTo({
      url: '/pages/schedular/addSchedule/add?pageNumber=' + this.data.pageNumber + '&spiderIndex=' + e.currentTarget.dataset.index,
    })
  }
});