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
    maxNumber: 0,
  },
  spiderRequestWrapper: function (requestPageNumber) {
    console.log("requestPageNumber: ", requestPageNumber);
    console.log("maxNumber:", this.data.maxNumber);
    //当前已缓存页面数量:maxNumber
    //已缓存大于请求页面，直接提取
    if (this.data.maxNumber >= requestPageNumber) {
      this.setData({
        crawled: app.globalData.spiderItems[requestPageNumber - 1],
        pageNumber: requestPageNumber
      })
    } else {
      console.log("准备爬取");
      console.log("requestPageNumber: ", requestPageNumber);
      console.log("maxNumber:", this.data.maxNumber);
      wx.showLoading({
        title: '拼命加载中~',
      })
      wrapper.wxRequestWrapper(api.spiderAPI, "GET", {
        pageNumber: requestPageNumber - 1,
        size: 5,
      }).then((data) => {
        app.globalData.spiderItems.push(spider.warpSpiderItems(data["notifications"]));
        wx.hideLoading();
        this.setData({
          pageNumber: requestPageNumber, //爬取后段
          maxNumber: requestPageNumber,
          crawled: data["notifications"]
        })
      }).catch((errno) => {
        this.spiderRequestErrorWrapper(errno);
      })
    }
  },
  spiderRequestErrorWrapper: function (errno) {
    wx.hideLoading();
    console.log("Spider OnShow失败: ", errno);
    wx.showModal({
      title: '爬虫失败o(╥﹏╥)o',
      content: '请检查网络连接',
      showCancel: false,
    })
  },
  onLoad: function () { },
  onShow: function () {
    if (this.data.maxNumber > 0)
      return;
    wx.showLoading({
      title: '拼命加载中(#^.^#)',
      mask: true,
    });

    this.spiderRequestWrapper(1);
    //向后端发送请求，获取5个东西
    this.slidethis();

  },
  start: function (e) {
    this.setData({
      x1: e.changedTouches[0].pageX,
    })
  },
  end: function (e) {
    this.setData({
      x2: e.changedTouches[0].pageX,
    })
    if (this.data.x1 === this.data.x2)
      return;


    if (this.data.x1 > this.data.x2) {
      this.spiderRequestWrapper(this.data.pageNumber + 1);
    } else if (this.data.x1 < this.data.x2) {
      if (this.data.pageNumber === 1){
        wx.showModal({
          title: '到第一页啦↖(^ω^)↗',
          content: '不能再翻了',
          showCancel: false,
        })
        return;
      }
      this.spiderRequestWrapper(this.data.pageNumber - 1);
    }
    this.slidethis()
  },

  //事件处理函数
  slidethis: function (e) {
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
    setTimeout(function () {
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

  refresh: function (e) {
    //清除缓存
    this.setData({
      maxNumber: 0,
    });
    wx.showLoading({
      title: '刷新ing^_^',
    })
    wrapper.wxRequestWrapper(api.spiderAPI, "GET", {
      pageNumber: 1 - 1,
      size: 5,
    }).then((data) => {

      //设置globalData
      app.globalData.spiderItems = [spider.warpSpiderItems(data["notifications"])];
      wx.hideLoading();
      this.setData({
        pageNumber: 1, //爬取后段
        crawled: data["notifications"],
        maxNumber: 1,
      })
      this.slidethis();
    }).catch((errno) => {
      this.spiderRequestErrorWrapper(errno);
    })
    wx.showModal({
      title: '到第一页啦↖(^ω^)↗',
      content: '已经刷新',
      showCancel: false,
    })
  },
  detail: function (e) {
    wx.navigateTo({
      url: '/pages/schedular/scheduleDetails/scheduleDetails?pageNumber=' + this.data.pageNumber + '&spiderIndex=' + e.currentTarget.dataset.index,
    })
  },
  addSpiderItem: function (e) {
    wx.navigateTo({
      url: '/pages/schedular/addSchedule/add?pageNumber=' + this.data.pageNumber + '&spiderIndex=' + e.currentTarget.dataset.index,
    })
  }
});