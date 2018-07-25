Page({
  data: {
    inputValue: "",
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
              url: 'https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic',
              method: 'POST',
              header: {
                'Content-Type': 'application/x-www-form-urlencoded'
              },
              data: {
                access_token: '24.8cc9ce430d27729ba7d725b2b3e4245e.2592000.1535118010.282335-11588118',
                image: base64,
                probability: true
              },
              success: function(res) {
                var result = res.data;
                console.log(result)
                wx.showToast({
                  title: '成功，已统计',
                  image: "/icons/success.png",
                  duration: 1500
                })
              }
            })
          }
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
  
})