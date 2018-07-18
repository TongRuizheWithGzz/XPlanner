var app = getApp();
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
Page({
  data: {
    scheduleItems: app.globalData.scheduleItems,
    height: 0,
    fix: false,
    hideFixTop: true,
    time: 0,
  },
  onLoad: function () {
    this.setData({
      height: 86.796875 * (this.data.scheduleItems.length) + 200,
    })
  },
  onPullDownRefresh:function(){
    wx.showNavigationBarLoading();
    wx.stopPullDownRefresh();
  },
  detail: function (event) {
    wx.navigateTo({
      url: '/pages/schedular/scheduleDetails/scheduleDetails?id=' +
      event.currentTarget.dataset.id
      + '&day=' +
      this.data.tabs[this.data.activeIndex],
    })
  },
  
  onPageScroll: function (e) {

    if (e.scrollTop > 228) {
      this.setData({
        hideFixTop: false,
      });
      this.setData({
        time: parseInt((e.scrollTop - 228) / 304.390625) * 3,
      })
    }
    else
      this.setData({
        hideFixTop: true,
      })
  }
})