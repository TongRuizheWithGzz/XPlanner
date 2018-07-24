var app = getApp();
var extensions = app.globalData.extensions;
var User_planner_setting = app.globalData.User_planner_setting;
/*
 * filterAndSetGlobalExtensions
 * 根据后端传来的数组settings过滤得到用户安装的扩展，并且设置globalData
 */
function filterAndSetGlobalExtensions() {
  var temp = app.globalData.extensions;
  for (var i = 0; i < temp.length; i++) {
    var k = User_planner_setting.indexOf(i);
    if (k >= 0)
      k = true;
    else
      k = false;
    temp[i].visible = k;
  }
  return temp;
}


Page({

  /**
   * 页面的初始数据
   */
  data: {
    time: app.globalData.date,
    extensions: filterAndSetGlobalExtensions(),
    User_planner_setting: User_planner_setting,
  },
  open: function(e) {
    wx.navigateTo({
      url: e.currentTarget.dataset.url,
    })
  },
  show: function(e) {
    wx.navigateTo({
      url: '/pages/store/detail/detail?id=' + e.currentTarget.dataset.id,
    })
  },
  install:function(e){
    
  }
})