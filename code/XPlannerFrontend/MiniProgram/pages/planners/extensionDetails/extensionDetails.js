var app = getApp();
var extensions = app.globalData.extensions;

Page({
  data: {
    extension: "",
  },
  onLoad: function(options) {
    this.setData({
      extension: extensions[options.id]
    })
  },

  /*
   * deleteExtension
   * 删除扩展
   */
  deleteExtension: function() {

    /* 向后端发送消息 */
    
    app.globalData.extensions[this.data.extension.id].visible = false;
    this.setData({
      extension: app.globalData.extensions[this.data.extension.id]
    });
    wx.showToast({
      title: "删除成功",
      icon: "success",
      duration: 1500
    });
  },

  /*
   * installExtension
   * 安装扩展
   */
  installExtension: function() {

    /* 向后端发送消息 */
    
    app.globalData.extensions[this.data.extension.id].visible = true;
    this.setData({
      extension: app.globalData.extensions[this.data.extension.id]
    });
    wx.showToast({
      title: "安装成功",
      icon: "success",
      duration: 2000
    });
  }
})