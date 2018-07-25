var user_food_eaten = require("../../../data/user_food_eaten");
var app = getApp();

/*
 * warpEatenFood
 * 将选择的食物个数均设置为1，注意如果拍多个相同的食物会导致出错，需要修改此处逻辑
 */
function warpEatenFood(array) {
  for (var i = 0; i < array.length; i++) {
    array[i].selected_number = 1;
  }
  return array;
}

Page({
  data: {
    files: [],
    location: "地址"
  },
  onLoad: function(options) {
    var self = this;
    wx.getLocation({
      success: function(res) {
        self.setData({
          location: "(" + res.latitude + "," + res.longitude + ")"
        })
      },
    })
  },

  /*
   * previewPicture
   * 控制图片预览
   */
  previewPicture: function(e) {
    wx.previewImage({
      current: e.currentTarget.id, // 当前显示图片的http链接
      urls: this.data.files // 需要预览的图片http链接列表
    })
  },

  /*
   * choosePicture
   * 选择图片
   */
  choosePicture: function () {
    var that = this;
    wx.chooseImage({
      count: 1,
      sizeType: ["original", "compressed"],
      sourceType: ["album", "camera"],
      success: function (res) {
        var tempFilePaths = res.tempFilePaths;
        wx.request({
          url: tempFilePaths[0],
          method: 'GET',
          responseType: 'arraybuffer',
          success: function (res) {
            var base64 = wx.arrayBufferToBase64(res.data);
            wx.request({
              url: 'https://aip.baidubce.com/rest/2.0/image-classify/v2/dish',
              method: 'POST',
              header: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              data: {
                access_token: '24.fe56be76c54ed3a23516812df8559fbd.2592000.1534314608.282335-11541012',
                image: base64,
                filter_threshold: 0.95,
              },
              success: function (res) {
                var result = res.data.result[0];
                console.log(result); // 从百度返回的检测结果
                if ((result.calorie <= 0) || (result.probability < 0.1)) {
                  /* do something for error */
                } else {
                  /* do something for ok */
                }
              }
            })
          }
        });
        that.setData({
          files: that.data.files.concat(res.tempFilePaths),
        });
        console.log("choose image succeed");
      },
    })
  },

  /*
   * sendPictures
   * 
   */
  sendPictures: function() {

    /* 向百度api发送消息，获得相应数据。*/

    /* 根据返回信息设置全局数据并跳转到output */
    var tmp = user_food_eaten;
    app.globalData.userFoodEaten = warpEatenFood(tmp);
    wx.navigateTo({
      url: "/extensions/keeper/output/output",
    });
  }
})