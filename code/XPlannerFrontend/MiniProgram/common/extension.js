var extension = {
    warpExtensions: function (extensions_raw) {
        var result = [];
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
            result.push(tmp);
        }
        return result;
    }
}

module.exports = extension;