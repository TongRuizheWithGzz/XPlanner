var app = getApp();
Page({
  data: {
    day:"",
    items: app.globalData.scheduleItems,
    activeIndex: 0,
  },

  onLoad: function (options) {
    this.setData({
      day:options.day,
      activeIndex:options.id,
    })
  },

  onPullDownRefresh: function () {
    
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
    
  },
  swipe:function(e){
    var current = e.detail.current;
    this.setData({
      activeIndex: current,
    });
  }
})