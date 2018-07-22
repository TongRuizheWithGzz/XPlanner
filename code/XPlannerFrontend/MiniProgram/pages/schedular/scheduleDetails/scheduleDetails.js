var app = getApp();
Page({
  data: {
    day:"",
    items: app.globalData.scheduleItems,
    item:'',
  },

  onLoad: function (options) {
    this.setData({
      day:"options.day,",
      item:this.data.items[0],
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
  
})