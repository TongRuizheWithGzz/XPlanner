var app = getApp();

Page({
  data: {
    user_food_eaten: []
  },
  onLoad: function() {
    this.setData({
      user_food_eaten: app.globalData.userFoodEaten
    })
    console.log(this.data.user_food_eaten);
  },

  /*
   * confirm
   * 确认提交的食物信息，并且发送给后端
   */
  confirm: function() {

    /* 向后端发送食物信息 */

    wx.navigateBack({
      delta: 2
    });
  },

  /*
   * giveUpInfo
   * 放弃当前食物信息
   */
  giveUpInfo: function() {
    wx.navigateBack({
      delta: 2
    });
  }
})