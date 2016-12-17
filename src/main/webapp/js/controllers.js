angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        // $httpProvider.interceptors.push('AuthInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('IndexCtrl', function ($scope, $rootScope, MainService) {
        MainService.getMainUser().success(function (data) {
            $scope.userinfo = data;
            console.log(data);
        })

    })

    //用户中心
    .controller('UserCenterCtrl', function ($scope, AccountService) {

    })
    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })

