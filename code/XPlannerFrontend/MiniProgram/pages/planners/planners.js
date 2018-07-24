var app = getApp();
var sliderWidth = 120; // 需要设置slider的宽度，用于计算中间位置
// var User_planner_setting = [0, 1, 2]; //从后端传来的已安装扩展数组
var extensions = app.globalData.extensions;
var mtabW;

Page({
  data: {
    extensions: extensions,
    move: false,
  },
  onShow: function () {
    this.setData({
      extensions: app.globalData.extensions,
    })
  },

  undo: function () {
    this.setData({
      move: false
    })
  },
  remove: function () {
    this.setData({ move: true })
  },
  rem: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '是否删除插件' + this.data.extensions[id].name,
      content: '模态弹窗',
      success: function (res) {
        app.globalData.extensions[id].visible = false;
        that.setData({
          extensions: app.globalData.extensions,
        });

        /* 写数据库 */

      }
    })
  },
  /*
   * gotoManagement
   * 前往管理扩展页面
   */
  gotoManagement: function (e) {
    wx.navigateTo({
      url: "/pages/planners/management/management"
    })
  }
})