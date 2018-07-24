const ApiRootUrl = 'http://127.0.0.1:8082/api/';

module.exports = {
  LoginByWeixin: ApiRootUrl + 'auth/loginByWeixin', //微信登录
  LoginByUsernamePassword: ApiRootUrl+'auth/loginByUsernamePassword', //用户名密码登录
  // LoginByJAccount:ApiRootUrl+'auth/loginByJAccount',//JAccount登录
  // queryAllscheduleitems,
  // addScheduleitem,
  // deleteScheduleitme,
  // updateScheduleitem,
  // queryScheduleitemByDay,
  // queryDaysHavingScheduletimesInMonth,
  // getUserSettings,
}

