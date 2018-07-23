//index.js 获取应用实例
var sedata = require("../../data/tongquitems.js")
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
    crawled: sedata,
  },
  onShow: function() {
    console.log(this.data.crawled);
    //load page 1
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
      this.setData({
        pageNumber: this.data.pageNumber + 1
      })
    } else if (this.data.x1 < this.data.x2) {
      if (this.data.pageNumber === 1)
        return;
      this.setData({
        pageNumber: this.data.pageNumber - 1
      });
    }
    this.slidethis()
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
    this.slidethis()
    this.setData({
      pageNumber: 1
    })
  },
  detail:function(){
    wx.navigateTo({
      url: '/pages/schedular/scheduleDetails/scheduleDetails',
    })
  }
});