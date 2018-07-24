var app = getApp();
var sliderWidth = 120; // 需要设置slider的宽度，用于计算中间位置
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

  /*
   * undo
   * 关闭删除模式
   */
  undo: function () {
    this.setData({
      move: false
    })
  },

  /*
   * remove
   * 开启删除模式
   */
  remove: function () {
    this.setData({ move: true })
  },

  /*
   * rem
   * 删除扩展
   */
  rem: function (e) {
    var that = this;
    var id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '是否删除插件' + this.data.extensions[id].name,
      content: '模态弹窗',
      success: function (res) {
        if (res.confirm) {
          app.globalData.extensions[id].visible = false;
          that.setData({
            extensions: app.globalData.extensions,
          });

          /* 写数据库 */

        }
      }
    })
  },

  direct: function (e) {
    console.log("sb");
    wx.navigateTo({
      url: e.currentTarget.dataset.url,
    })
  }
})