angular.module('starter.services', [])
//service在使用this指针，而factory直接返回一个对象
    .service('CommonService', function ($rootScope) {
        return {

            toolTip: function (msg, type) { //全局tooltip提示
                this.message = msg;
                this.type = type;
                //提示框显示最多3秒消失
                var _self = this;
                $timeout(function () {
                    _self.message = null;
                    _self.type = null;
                }, 3000);
            }


        }
    })
    .service('MainService', function ($q, $http, JinLeLe) { //首页服务定义
        return {
            getIndexInfo: function (params) { //获取首页信息
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/index/getIndexInfo"
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getNewProducts: function (params) { //首页新品推荐分页显示
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/index/getNewProducts/" + params.pagenow
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('CategoryService', function ($q, $http, JinLeLe) {
        return {
            getcatogories: function (params) { //获取首页信息
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getFirstCatogotory'
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getSecondCatogories:function (pid) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getSecondCatogaryByPid/'+pid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getGoodsByCidPaging:function (pagenow , catogoryId) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getGoodsByCidPaging/'+pagenow + '/' + catogoryId
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('GoodService', function ($q, $http, JinLeLe) { //商品相关的服务
        return {
                getGoodList: function (params) { //获取产品列表
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getGoodList/'+params.pagenow+'/'+params.categoryname+'/'+params.querytype
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getGoodDetail: function (params) { //获取产品详情
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getGoodDetail/'+params.goodId
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('AccountService', function ($q, $http, JinLeLe, $state, $interval, $timeout, $ionicLoading) {
        return {
            login: function (datas) { //登录
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'POST',
                    url: JinLeLe.api + "/user/login",
                    data: datas
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            addFilenames: function ($scope, params, imageUrl) {//上传附件

            }

        }
    })
    .service('ResizeService',function(){
        return {
            autoHeight: function () {
                if (window.innerHeight) {//FF
                    nowHeight = window.innerHeight;
                } else {
                    nowHeight = document.documentElement.clientHeight;
                }
                var jianHeight = 50;
                if (nowHeight > jianHeight) {
                    document.getElementById('fenlei').style.height = nowHeight - jianHeight + 'px';
                } else {
                    document.getElementById('fenlei').style.height = jianHeight + 'px';
                }
                var jianHeight = 50;
                if (nowHeight > jianHeight) {
                    document.getElementById('leibie').style.height = nowHeight - jianHeight + 'px';
                } else {
                    document.getElementById('leibie').style.height = jianHeight + 'px';
                }
            }
        }
    })
    .service('WeiXinService', function ($q, $http, JinLeLe) { //微信 JS SDK 接口服务定义

    })
    .factory('AuthInterceptor', function () {//设置请求头信息的地方是$httpProvider.interceptors。也就是为请求或响应注册一个拦截器。使用这种方式首先需要定义一个服务

        return {
            request: function (config) {
                config.headers = config.headers || {};
                var token = localStorage.getItem('token');
                if (token) {
                    config.headers.authorization = token;
                }
                return config;
            },
            response: function (response) {

            },
            responseError: function (response) {
                // ...
            }
        };
    })
