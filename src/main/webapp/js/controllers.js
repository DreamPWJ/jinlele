angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        // $httpProvider.interceptors.push('AuthInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('IndexCtrl', function ($scope, $rootScope, MainService) {
        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
        })
        //首页新品推荐分页显示
        MainService.getNewProducts({pagenow: 1}).success(function (data) {
            $scope.newProductsinfo = data;
            console.log(data);
        })
    })

    //分类tab
    .controller('CategoryCtrl', function ($scope,$http,JinLeLe) {
        $http({
            method:"GET",
            url:JinLeLe.api+'/good/getFirstCatogotory'
        }).success(function(data){
            $scope.catogory=data;
        });
    })
    //用户中心
    .controller('UserCenterCtrl', function ($scope) {

    })
    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })

