var spider = {
    warpSpiderItems: function (spiderItems_raw) {
        var result = [];
        for (var i = 0; i < spiderItems_raw.length; i++) {
            var tmp = spiderItems_raw[i];
            tmp.start_concret_time = spiderItems_raw[i].start_time; // 由于spider应当显示日期和准确时间
            tmp.end_concret_time = spiderItems_raw[i].end_time;
            result.push(tmp);
        }
        return result;
    }
}

module.exports = spider;