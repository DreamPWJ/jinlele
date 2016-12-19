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
    .controller('CategoryCtrl', function ($scope,$stateParams,CategoryService) {
        console.log($stateParams.id);
        CategoryService.getcatogories().success(function(data){
            $scope.catogory=data;
        });
    })
    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })
    //购物车
    .controller('ShoppingCartCtrl',function($scope){

    })
    //会员
    .controller('MemberCtrl',function($scope){

    })
    //商城订单
    .controller('OrderListCtrl',function($scope){

    })
    //订单详情
    .controller('OrderCtrl',function($scope){

    })
    //我的钱包
    .controller('WalletCtrl',function($scope){

    })
    //我的收藏
    .controller('FavorCtrl',function($scope){

    })
    //商品列表
    .controller('GoodCtrl',function($scope){

    })
    //发表评论
    .controller('AddCommentCtrl',function($scope){

    })
    //确认订单
    .controller('ConfirmOrderCtrl',function($scope){

    })
    //翻新
    .controller('RefurbishCtrl',function($scope){

    })
    //维修
    .controller('RepairCtrl',function($scope){

    })
    //检测
    .controller('DetectCtrl',function($scope){

    })
    //回收
    .controller('RecycleCtrl',function($scope){

    })
    //换款
    .controller('ExchangCtrl',function($scope){

    })

