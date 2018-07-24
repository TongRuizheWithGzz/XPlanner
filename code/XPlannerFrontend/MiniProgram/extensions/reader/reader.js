Page({
  data: {
    files: [],
    inputValue: "",
    activeIndex: 1, // 当前激活的tab序数
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
  choosePicture: function() {
    var that = this;
    wx.chooseImage({
      count: 1,
      sizeType: ["original", "compressed"],
      sourceType: ["album", "camera"],
      success: function(res) {
        var tempFilePaths = res.tempFilePaths;
        wx.request({
          url: tempFilePaths[0],
          method: 'GET',
          responseType: 'arraybuffer',
          success: function(res) {
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
              success: function(res) {
                var result = res.data.result[0];
                console.log(result);
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
   * showResult
   * 提交输入，跳转到生成的日程界面
   */
  showResult: function() {
    console.log("hand in result");
  },

  /*
   * getInputText
   * 获取输入的文字
   */
  getInputText: function(e) {
    console.log(e.detail.value);
    this.setData({
      inputValue: e.detail.value
    });
  },

  /*
   * swipeContent
   * 滑动切换文字识别和图片识别
   */
  swipeContent: function(e) {
    this.setData({
      activeIndex: e.detail.current,
    });
  },

  /*
   * tabClick
   * 点击切换文字识别和图片识别
   */
  tabClick: function(e) {
    console.log(e.currentTarget.dataset.id);
    this.setData({
      activeIndex: e.currentTarget.dataset.id,
    })
  }
})