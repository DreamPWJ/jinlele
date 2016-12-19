
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'starter.config', 'starter.directive'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {  //APP首页面
                url: '/main',
                templateUrl: 'html/main.html',
                controller: 'IndexCtrl'
            })
            .state('category', {  //分类
                url: '/category/:id',
                templateUrl: 'html/category.html',
                controller: 'CategoryCtrl'
            })
            .state('shoppingcart', {  //购物车
                url: '/shoppingcart',
                templateUrl: 'html/shoppingcart.html',
                controller: 'ShoppingCartCtrl'
            })
            .state('member', {  //会员
                url: '/member',
                templateUrl: 'html/member.html',
                controller: 'MemberCtrl'
            })
            .state('wallet', {  //钱包
                url: '/wallet',
                templateUrl: 'html/wallet.html',
                controller: 'WalletCtrl'
            })
            .state('favourite', {  //我的收藏
                url: '/favourite',
                templateUrl: 'html/favourite.html',
                controller: 'FavorCtrl'
            })
            .state('orderlist', {  //订单详情
                url: '/orderlist',
                templateUrl: 'html/order.html',
                controller: 'OrderListCtrl'
            })
            .state('order', {  //订单详情
                url: '/order/:id',
                templateUrl: 'html/orderdetail.html',
                controller: 'OrderCtrl'
            })
            .state('confirm', {  //确认订单
                url: '/confirm',
                templateUrl: 'html/confirmorder.html',
                controller: 'ConfirmOrderCtrl'
            })
            .state('good', {  //商品详情
                url: '/good/:id',
                templateUrl: 'html/gooddetail.html',
                controller: 'GoodCtrl'
            })
            .state('repair', {  //翻新
                url: '/repair',
                templateUrl: 'html/repair/index.html',
                controller: 'RepairCtrl'
            })

        $urlRouterProvider.otherwise('main')
    })