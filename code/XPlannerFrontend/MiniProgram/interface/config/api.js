const ApiRootUrl = 'http://127.0.0.1:8082/api/';

module.exports = {
  LoginByWeixin: ApiRootUrl + 'auth/loginByWeixin', //微信登录
  LoginByUsernamePassword: ApiRootUrl + 'auth/loginByUsernamePassword', //用户名密码登录
  checkSession: ApiRootUrl + 'auth/checkSession', //检查用户是否登录
  queryUserInfo: ApiRootUrl + 'me',
  // LoginByJAccount:ApiRootUrl+'auth/loginByJAccount',//JAccount登录
  // queryAllscheduleitems,
  addScheduleitem: ApiRootUrl + 'schedules',
  deleteScheduleitme: ApiRootUrl + 'schedules/',
  updateScheduleitem: ApiRootUrl + 'schedules/',
  queryScheduleitemByDay: ApiRootUrl + 'scheduleForDay',
  queryDaysHavingScheduletimesInMonth: ApiRootUrl + 'monthScheduleInfo',
  changeScheduleCompletedState: ApiRootUrl + '',
  queryEnabledExtensionsArray: ApiRootUrl + 'me/settings',
  // getUserSettings,
  //Spider: Get newset notifications
  spiderAPI: ApiRootUrl + "notifications",
  keeperAddFoodApi: ApiRootUrl + "addFood",
  keeperApi: ApiRootUrl + "keeper",
  readerApi: ApiRootUrl + "reader",
  getFoodByDinningHall: ApiRootUrl + 'food',
}