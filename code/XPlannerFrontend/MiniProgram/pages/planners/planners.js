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
    tabs: ["推荐", "已安装"], //tob标题
    activeIndex: 0,
    slideOffset: 0,
    tabW: 0,
    index: 0,
    topView: 'A',
    sliderOffset: 0,
    sliderLeft: 0,
    extensions: extensions,
  },
  onLoad: function() {
    var that = this;
    wx.getSystemInfo({
      success: function(res) {
        that.setData({
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2,
          sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex
        })
      }
    });
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
   * tabClick
   * 响应点击顶部tab
   */
  tabClick: function(e) {
    console.log(e)
    var that = this;
    var index = 0;
    for (var i = 0; i < this.data.tabs.length; i++) {
      if (this.data.tabs[i] === e.currentTarget.dataset.item) {
        index = i
        break
      }
    }
    var offsetW = e.currentTarget.offsetLeft;
    this.setData({
      activeIndex: index,
      slideOffset: offsetW
    });
  },

  /*
   * bindChange
   * 响应幻灯片切换
   */
  bindChange: function(e) {
    var current = e.detail.current;
    var offsetW = current * mtabW;
    this.setData({
      activeIndex: current,
      index: current,
      slideOffset: offsetW,
      topView: this.data.tabs[current]
    });
    console.log(this.data.topView + ' ' + offsetW)
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