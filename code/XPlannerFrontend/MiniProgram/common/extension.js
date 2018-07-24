var extension = {
  /*
   * warpExtensions
   * 包装extensions
   */
  warpExtensions: function (extensions_raw) {
    var result = [];
    var shortmsg = ["轻松获取各个网站信息", "定制健康的运动饮食计划", "识别各处的事项安排", "即将推出"];
    var bk = ["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532373805713&di=56f13a5ad75559c7db59292e92986fa9&imgtype=0&src=http%3A%2F%2F1842.img.pp.sohu.com.cn%2Fimages%2F2012%2F4%2F23%2F9%2F6%2Fu130567646_1379e2310ceg213.jpg"]
    var icon = ["https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1532238329349&di=0d99521b5ee795b5df00824d90f949f2&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3Dc1673089aec27d1eb12b338773bcc71b%2Fd31b0ef41bd5ad6ec89ce6c78bcb39dbb6fd3cb1.jpg"]
    for (var i = 0; i < extensions_raw.length; i++) {
      var tmp = new Object();
      tmp.id = extensions_raw[i].planner_id;
      tmp.name = extensions_raw[i].planner_name;
      tmp.description = extensions_raw[i].description;
      tmp.content = "尚无内容。";
      tmp.visible = false;
      tmp.messages = extensions_raw[i].planner_name == "Spider" ? 8 : -1;
      tmp.icon = extensions_raw[i].picture_path_name;
      var name = tmp.name.toLowerCase();
      tmp.extension_path = "/extensions/" + name + "/" + name;
      tmp.background = bk[i];
      tmp.icon = icon[i];
      tmp.msg = shortmsg[i];
      result.push(tmp);
    }
    return result;
  },

  /*
   * filterExtensions
   * 根据array设置用户安装扩展情况
   */
  filterExtensions: function (extensions, array) {
    var tmp = extensions;
    for (var i = 0; i < array.length; i++) {
      extensions[array[i]].visible = true;
    }
    return tmp;
  }
}

module.exports = extension;