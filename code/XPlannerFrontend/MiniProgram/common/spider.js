var spider = {
    warpSpiderItems: function (spiderItems_raw) {
        var result = [];
        for (var i = 0; i < spiderItems_raw.length; i++) {
            var tmp = spiderItems_raw[i];
            tmp.start_concret_time = spiderItems_raw[i].start_time.slice(11, 16);
            tmp.end_concret_time = spiderItems_raw[i].end_time.slice(11, 16);
            result.push(tmp);
        }
        return result;
    }
}

module.exports = spider;