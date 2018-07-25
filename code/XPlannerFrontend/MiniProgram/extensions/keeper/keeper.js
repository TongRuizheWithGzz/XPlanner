var app = getApp();
var keeperItems = require("../../data/scheduleItem"); //后端加载推荐事项
var test_data_food = require("../../data/food"); //food 测试数据
var food_types = require("../../data/food_type"); //food_type测试数据
var keeper = app.globalData.extensions[1] //keeper 插件信息
var addresses = require("../../data/addresses.js"); //所有地址，写死在前端
var userinfo = app.globalData.userInfo; //用户信息
var api = require("../../interface/config/api.js");
var wrapper = require("../../interface/wrapper/wrapper.js");


//加入是否勾选属性
var food_wrap = function(food_list) {
  for (var k = 0; k < food_list.length; k++)
    food_list[k].selected = false;
  return food_list;
}

//获取格式化时间
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
}

var get_eaten = function(foodmap) {
  var res = [];
  for (var address in foodmap) {
    var temp = foodmap[address]
    for (var food = 0; food < temp.length; ++food)
      if (temp[food].selected === true)
        res.push({
          food_name: temp[food].food_name,
          calorie: temp[food].calorie,
        })
  }
  return res;
}

Page({
  data: {
    keeperItems: [], //从后端请求的推荐事项
    keeper: keeper, //插件信息
    icons: ['/icons/aa.png', '/icons/bb.png', '/icons/cc.png'], //运动事项图标
    addresses: addresses, //所有地址
    address: '第一餐厅', //当前地址
    foodmap: {}, //地址到食物的映射，
    user_food_eaten: [] // 传回后端的饮食统计信息,保存在本页面
  },


  //后端有关,后端加载此页面推荐数据和一餐食物
  onShow: function() {

    var tmp = 'foodmap.' + this.data.address
    if (this.data.foodmap != undefined &&
      this.data.foodmap[this.data.address] != undefined) {
      return;
    }
    wrapper.wxRequestWrapper(api.keeperApi, "POST", {}).then((data) => {
      var temp = data["recommands"];
      console.log("recommand:",temp);
      this.setData({
        keeperItems: temp,
        [tmp]: food_wrap(test_data_food)
      })
      app.globalData.keeperItems = this.data.keeperItems;
    }).catch((errno) => {
      console.log("获取用户推荐运动事项失败: ", errno);
      wx.showModal({
        title: '推荐失败',
        content: '请检查网络设置',
        showCancel: false,

      })
    })

    // app.globalData.keeperItems = this.data.keeperItems; // 向全局写入keeperItems, 方便跳转传参
    //后端加载第一餐厅食物
    //后端加载推荐运动事项
  },



  //后端无关, 推荐事项加入scheduler
  addSchedule: function(e) {
    wx.navigateTo({
      url: '/pages/schedular/addSchedule/add?keeperIndex=' +
        e.currentTarget.dataset.id,
    })
  },


  //后端有关，通过照片识别得到user-food-eaten数据，向后端发回user_food_eaten
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

                //图片识别失败， 不发回后端user_food_eaten数据
                if ((result.calorie <= 0) || (result.probability < 0.1)) {
                  wx.showToast({
                    title: '不知道这是什么食物',
                    image: "/icons/q.png",
                    duration: 1500
                  })
                }
                //图片识别成功，向后端发送user_food_eaten
                else {
                  //向后端返回user_food_eaten
                  //设置页面数据
                  var temp = that.data.user_food_eaten;

                  temp.push({
                    food_name: result.name,
                    calorie: result.calorie,
                  })
                  wx.showToast({
                    title: '成功，已统计',
                    image: "/icons/success.png",
                    duration: 1500
                  })
                  that.setData({
                    user_food_eaten: temp
                  })
                }
              }
            })
          }
        });
      },
    })
  },

  //后端有关
  bindPickerChange: function(e) {
    this.setData({
      address: addresses[parseInt(e.detail.value)]
    })
    /*从后端请求该餐厅食物*/
    var tmp = 'foodmap.' + this.data.address

    //已请求此地址过食物
    if (this.data.foodmap[this.data.address] != undefined)
      return;
    //未请求过此地址食物,
    else
      this.setData({
        [tmp]: food_wrap(test_data_food) //后端返回该地址食物， 向foodmap加入此地址到食物的映射
      })
  },

  //此处与后端无关！
  select: function(e) {
    var target = e.currentTarget.dataset;
    var add = target.add;
    var index = target.index;
    var k = 'foodmap.' + add + '[' + index + '].selected';
    var t = this.data.foodmap[add][index].selected;

    this.setData({ //选择|不选择，改变对钩
      [k]: !t
    })
  },

  //后端有关，向后端发送user_food_eaten
  additems: function() {
    var that = this;
    wx.showModal({
      title: '保存饮食统计信息？',
      success: function(res) {

        // console.log(that);
        var temp = get_eaten(that.data.foodmap);
        // console.log(temp)

        if (res.confirm) { // 向后端发送user_food_eaten, 并清空对钩
          wrapper.wxRequestWrapper(api.keeperAddFoodApi, "POST", {
            foodPOJOS: temp,
          }).then((data) => {
            var temp = that.data.user_food_eaten;
            that.setData({
              user_food_eaten: temp.concat(get_eaten(that.data.foodmap))
            });
            var temp = that.data.foodmap;
            // console.log(that.data.foodmap);
            for (var address in temp)
              temp[address] = food_wrap(temp[address])
            that.setData({
              foodmap: temp
            })
          }).catch((errno) => {
            console.log("用户添加食物失败:", errno);
            wx.showModal({
              title: '添加食物失败',
              content: '请检查网络连接',
              showCancel: false,
            });
          })
          // that.setData({
          //   user_food_eaten: temp.concat(get_eaten(that.data.foodmap))
          // })
          //向后端发送user_food_eaten

          //清空对钩
          // var temp = that.data.foodmap;
          // for(var address in temp)
          //   temp[address] = food_wrap(temp[address])

          // that.setData({
          //   foodmap:temp
          // })
        }
      }
    })
  }
})