var app = getApp();
var sliderWidth = 120; // 需要设置slider的宽度，用于计算中间位置
var User_planner_setting = [0, 1, 2]; //从后端传来的已安装扩展数组
var extensions = app.globalData.extensions;
var mtabW;

/*
 * filterAndSetGlobalExtensions
 * 根据后端传来的数组settings过滤得到用户安装的扩展，并且设置globalData
 */
function filterAndSetGlobalExtensions(settings) {
  for (var i = 0; i < settings.length; i++) {
    app.globalData.extensions[settings[i]].visible = true;
  }
  return app.globalData.extensions;
}

/*
 * getExtensionsInfoFromBackEnd
 * 从后端获取用户安装扩展对应数组
 */
function getExtensionsInfoFromBackEnd() {
  var tmp = User_planner_setting;
  return tmp;
}

Page({
  data: {
    extensions: extensions,
  },
  onLoad: function() {
    var that = this;
    var settings = getExtensionsInfoFromBackEnd(); // 用户安装扩展对应的数组
    var tmp = filterAndSetGlobalExtensions(settings); // 过滤并设置全局数据
    this.setData({
      extensions: tmp,
    });
  },
  onShow: function() {
    this.setData({
      extensions: app.globalData.extensions,
    })
  },
  /*
   * gotoManagement
   * 前往管理扩展页面
   */
  gotoManagement: function(e) {
    wx.navigateTo({
      url: "/pages/planners/management/management"
    })
  }
})