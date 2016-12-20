angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        // $httpProvider.interceptors.push('AuthInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('MainCtrl', function ($scope, $rootScope, MainService) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });

        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
        }).then(function () {
            //首页新品推荐分页显示
            MainService.getNewProducts({pagenow: 1}).success(function (data) {
                $scope.newProductsinfo = data;

            })
        })

    })

    //分类tab
    .controller('CategoryCtrl', function ($scope, $stateParams, CategoryService) {
        CategoryService.getcatogories().success(function (data) {
            $scope.catogory = data;
        });
    })
    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })
    //购物车
    .controller('ShoppingCartCtrl', function ($scope) {
        $(".check_label").checkbox();
    })
    //会员
    .controller('MemberCtrl', function ($scope) {

    })
    //商城订单
    .controller('OrderListCtrl', function ($scope) {
        var mySwiper = new Swiper('.swiper-container', {
            pagination: '.tab',
            paginationClickable: true,
            //autoHeight: true,
            paginationBulletRender: function (index, className) {
                switch (index) {
                    case 0:
                        name = '未支付';
                        break;
                    case 1:
                        name = '已支付';
                        break;
                    case 2:
                        name = '已签收';
                        break;
                    case 3:
                        name = '退单';
                        break;
                    default:
                        name = '';
                }
                return '<span class="' + className + '">' + name + '</span>';
            }
        });
    })
    //订单详情
    .controller('OrderCtrl', function ($scope) {

    })
    //退货
    .controller('ReturnsCtrl', function ($scope) {
        $(document).ready(function () {
            $('.default').dropkick();
            theme:'black'
        });
    })
    //我的钱包
    .controller('WalletCtrl', function ($scope) {

    })
    //提现记录
    .controller('CashdetailCtrl',function($scope){

    })
    //我的收藏
    .controller('FavorCtrl', function ($scope) {

    })
    //商品列表
    .controller('GoodListCtrl', function ($scope, GoodService, $stateParams) {

        //获取产品列表
        GoodService.getGoodList({pagenow:1,categoryname: $stateParams.name, querytype: 0}).success(function (data) {
            $scope.goodList=data;
            console.log(data);

        })

        $('a.button').click(function () {
            if ($('.content').hasClass('square')) {
                $('.content').removeClass('square');
            } else {
                $('.content').addClass('square');
            }
        });


    })
    //商品详情
    .controller('GoodCtrl', function ($scope) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });
    })
    //发表评论
    .controller('AddCommentCtrl', function ($scope) {

    })
    //确认订单
    .controller('ConfirmOrderCtrl', function ($scope) {
        $(".check_label").checkbox();
    })
    //翻新
    .controller('RefurbishCtrl', function ($scope) {

    })
    //维修
    .controller('RepairCtrl', function ($scope) {

    })
    //检测
    .controller('DetectCtrl', function ($scope) {

    })
    //回收
    .controller('RecycleCtrl', function ($scope) {

    })
    //换款
    .controller('ExchangCtrl', function ($scope) {

    })

