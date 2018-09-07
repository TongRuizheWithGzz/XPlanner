var time = {
    /*
     * getDateStrWithZero
     * 2019, 7, 9 -> 2019-07-09
     */
    getDateStringWithZero: function (year, month, day) {
        if (month < 10) month = "0" + month;
        if (day < 10) day = "0" + day;
        // console.log("" + year + "-" + month + "-" + day);
        return "" + year + "-" + month + "-" + day;
    },

    getMonthStringWithZero: function (year, month) {
        if (month < 10) month = "0" + month;
        return "" + year + "-" + month;
    },

    /*
     * Zero2WithoutZero
     * 2018-07 -> 2018-7
     */
    Zero2WithoutZero: function (date) {
        if (date[5] != "0") return date;
        return date.slice(0, 5) + date[6];
    },

    /*
     * ZeroGetDay
     * 2018-07-01 -> 1
     */
    ZeroGetDay: function () {

    },

    getConcreteTime: function (hour, min) {
      if (hour < 10) hour = "0" + hour;
      if (min < 10) min = "0" + min;
      console.log("" + hour + ":" + min);
      return "" + hour + ":" + min;
    }
}

module.exports = time;