var api = require("../../interface/config/api.js");
var wrapper = require("../../interface/wrapper/wrapper.js");


var date_string = function () {
  var dates = new Date();
  var years = dates.getFullYear();
  var months = dates.getMonth() + 1;
  if (months < 10)
    months = "0" + months;
  var days = dates.getDate();
  if (days < 10)
    days = "0" + days;
  var hours = dates.getHours();
  if (hours < 10)
    hours = "0" + hours;
  var mins = dates.getMinutes();
  if (mins < 10)
    mins = "0" + mins;
  return years + "." + months + "." + days + " " + hours + ":" + mins
};


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
    console.log("这个东西肯定是undefined", this.data.inputValue);
    this.setData({
      inputValue: e.detail.value
    });

    wrapper.wxRequestWrapper(api.readerApi,"GET",{
      in: e.detail.value,
    }).then((data)=>{
      var start_time=data["start_time"];
      var end_time=start_time.slice()
    })
    // console.log(e.detail.value);

    
   
  },
  
})