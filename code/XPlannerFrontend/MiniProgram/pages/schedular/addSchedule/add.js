Page({

  /**
   * 页面的初始数据
   */
  data: {
    startDate:"choose date",
    startTime: "choose time",
    endDate: "choose date",
    endTime: "choose time",
    title:"",
    description:"",
    address:""
  },
  titleIp:function(e){
    this.setData({
      title:e.detail.value
    })
  },
  descriptionIp: function (e) {
    this.setData({
      description: e.detail.value
    })
  },
  addressIp: function (e) {
    this.setData({
      address: e.detail.value
    })
  },
  changeSD:function(e){
    this.setData({
      startDate:e.detail.value,
    })
  },
  changeST: function (e) {
    this.setData({
      startTime: e.detail.value,
    })
  },
  changeED: function (e) {
    this.setData({
      endDate: e.detail.value,
    })
  },
  changeET: function (e) {
    this.setData({
      endTime: e.detail.value,
    })
  },
  add:function(){
    wx.navigateBack({
      
    })
  }
})