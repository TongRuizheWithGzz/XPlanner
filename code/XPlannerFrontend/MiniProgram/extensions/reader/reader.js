var api = require("../../interface/config/api.js");
var wrapper = require("../../interface/wrapper/wrapper.js");


var date_string = function() {
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
    description: "",
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
                access_token: '24.599e356ed1fa21b734907803e84cd415.2592000.1538616705.282335-11767229',
                image: base64,
                probability: true
              },
              success: function(res) {
                var result = res.data;
                var words_reslt = result["words_result"];
                var upload = "";
                for (var i = 0; i < words_reslt.length; i++) {
                  upload += words_reslt[i]["words"];
                }
              console.log("连接起来的字符创",upload)
                console.log(result)
                wx.showToast({
                  title: '图片识别成功',
                  image: "/icons/success.png",
                  duration: 1500,
                })
                setTimeout(() => {
                  wrapper.wxRequestWrapper(api.readerApi, "GET", {
                    "in": upload,
        
                  }).then((data) => {
                    var start_time = data["start_time"];
                    var end_time = data["end_time"];
                    wx.showModal({
                      title: '生成日程成功(#^.^#)',
                      content: '开始时间: ' + start_time + '\r\n 结束时间: ' + end_time,
                      confirmText: "导入日程",
                      cancelText: "放弃",
                      success: function(res) {
                        if (res.confirm) {
                          wx.navigateTo({
                            url: "/pages/schedular/addSchedule/add?readerDescription=" + upload + "&start_time=" + start_time + "&end_time=" + end_time,
                          })
                          console.log('用户点击确定')
                        } else if (res.cancel) {
                          console.log('用户点击取消')
                        }
                      }
                    })

                  }).catch((errno) => {
                    console.log("通过识图api提取日期失败：", errno);
                    wx.showModal({
                      title: 'o(╥﹏╥)o',
                      content: '请检查网络设置',
                      showCancel: false,
                    })
                  })
                },1500);
              }
            })
          }
        });

        console.log("choose image succeed");
      },
    })
  },

  // /*
  //  * showResult
  //  * 提交输入，跳转到生成的日程界面
  //  */
  // showResult: function() {
  //   console.log("hand in result");
  // },

  /*
   * getInputText
   * 获取输入的文字
   */
  getInputText: function(e) {
    this.setData({
      inputValue: e.detail.value,
      description: e.detail.value
    });
  },

  uploadstring: function() {
    var that = this;
    wrapper.wxRequestWrapper(api.readerApi, "GET", {
      "in": that.data.inputValue,
    }).then((data) => {
      var start_time = data["start_time"];
      var end_time = data["end_time"];
      wx.showModal({
        title: '生成日程成功(#^.^#)',
        content: '开始时间:    ' + start_time + ' 结束时间: ' + end_time,
        confirmText: "导入日程",
        cancelText: "放弃",
        success: function(res) {
          if (res.confirm) {
            wx.navigateTo({
              url: "/pages/schedular/addSchedule/add?readerDescription=" + that.data.description + "&start_time=" + start_time + "&end_time=" + end_time,
            })
            console.log('用户点击确定')
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })

    }).catch((errno) => {
      console.log("通过输入文字提取日期失败：", errno);
      wx.showModal({
        title: 'o(╥﹏╥)o',
        content: '请检查网络设置',
        showCancel: false,
      })
    })

  }

})