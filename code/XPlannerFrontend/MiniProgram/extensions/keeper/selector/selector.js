var food = require("../../../data/food");
var food_types = require("../../../data/food_type");
var app = getApp();

/*
 * getFoodByTypeId
 * 根据food_type_id从all_food数组中获取相应的所有食物的数组
 */
function getFoodByTypeId(food_type_id, all_food) {
  var tmp_food = [];
  for (var i = 0; i < all_food.length; i++) {
    if (all_food[i].food_type_id === food_type_id)
      tmp_food.push(all_food[i]);
  }
  return tmp_food;
}

/*
 * warpFoodInfo
 * 在每个food的信息中加入选择次数这一项
 */
function warpFoodInfo(array) {
  for (var i = 0; i < array.length; i++) {
    array[i].selected_number = 0;
  }
  return array;
}

/*
 * warpFoodTypesInfo
 * 向每个food_type信息中加入是否已加载这一项
 */
function warpFoodTypesInfo(array) {
  for (var i = 0; i < array.length; i++) {
    array[i].loaded = false;
  }
  array[0].loaded = true;
  return array;
}

Page({
  data: {
    food: [],
    food_types: [],
    active_type_index: 0,
    files: [],
    location: "地址"
  },
  onLoad: function() {
    /* 获取地址 */
    var self = this;
    wx.getLocation({
      success: function(res) {
        self.setData({
          location: "(" + res.latitude + "," + res.longitude + ")"
        })
      },
    })

    var first_food_type_id = 0; // 显示在第一项的food_type对应的id

    /* 使用first_food_type_id从后端获取对应的food信息和所有的food_type的信息 */

    /* 模拟从后端获取的第一组food信息并加入选择次数的信息项 */
    var tmp_food = getFoodByTypeId(0, food);
    tmp_food = warpFoodInfo(tmp_food);

    /* 处理food_types信息，使之包括“相应type的食物是否加载到前端”的信息 */
    var tmp_food_types = warpFoodTypesInfo(food_types);

    this.setData({
      food_types: tmp_food_types,
      food: tmp_food,
    });
  },

  tabClick: function(e) {
    if (this.data.food_types[e.currentTarget.dataset.id].loaded == true) {
      this.setData({
        active_type_index: e.currentTarget.dataset.id
      })
      console.log(this.data.food_types);
      console.log(this.data.food);
      return;
    }

    /* 向后端发送e.currentTarget.dataset.id，对应food_type_id，获取包含所有相应种类的食物的数组 */

    /* 模拟上述数组 */
    var tmp_food = getFoodByTypeId(e.currentTarget.dataset.id, food);
    tmp_food = warpFoodInfo(tmp_food);
    tmp_food = tmp_food.concat(this.data.food);

    /* 改变相应food_type，表明前端已经拥有该type对应的食物的数据 */
    var tmp_food_types = this.data.food_types;
    tmp_food_types[e.currentTarget.dataset.id].loaded = true;

    /* 注意，此处的id是food_type_id，这样的用法意味着后端传输的foot_type数组必须按id大小排序，而且id间隔为1 */
    this.setData({
      food: tmp_food,
      food_types: tmp_food_types,
      active_type_index: e.currentTarget.dataset.id
    });
  },

  /*
   * addFood
   * 增加选中的食物一次
   */
  addFood: function(e) {
    var tmp_food = this.data.food;
    for (var i = 0; i < this.data.food.length; i++) {
      if (this.data.food[i].food_id === e.currentTarget.dataset.id) {
        tmp_food[i].selected_number++;
        break;
      }
    }
    this.setData({
      food: tmp_food,
    })
  },

  /*
   * removeFood
   * 减少选中的食物一次
   */
  removeFood: function(e) {
    var tmp_food = this.data.food;
    for (var i = 0; i < this.data.food.length; i++) {
      if (this.data.food[i].food_id === e.currentTarget.dataset.id) {
        tmp_food[i].selected_number--;
        break;
      }
    }
    this.setData({
      food: tmp_food,
    })
  },

  /*
   * confirmSelection
   * 确认当前选择结果，跳转到output
   */
  confirmSelection: function() {
    app.globalData.userFoodEaten = this.data.food;
    wx.navigateTo({
      url: "/extensions/keeper/output/output",
    })
  },
})