
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'starter.config', 'starter.directive','starter.filter'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {  //APP首页面
                url: '/main',
                cache:true,
                templateUrl: 'html/main.html',
                controller: 'MainCtrl'
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
            .state('orderlist', {  // 商城订单
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
            .state('goodlist', {  //商品列表
                url: '/goodlist/:id',
                templateUrl: 'html/goodlist.html',
                controller: 'GoodListCtrl'
            })
            .state('good', {  //商品详情
                url: '/good/:id',
                templateUrl: 'html/gooddetail.html',
                controller: 'GoodCtrl'
            })
            .state('addcomment', {  //商品评论
                url: '/addcomment',
                templateUrl: 'html/comment.html',
                controller: 'AddCommentCtrl'
            })
            .state('refurbish', {  //翻新
                url: '/refurbish',
                templateUrl: 'html/refurbish/index.html',
                controller: 'RefurbishCtrl'
            })
            .state('repair', {  //维修
                url: '/repair',
                templateUrl: 'html/repair/index.html',
                controller: 'RepairCtrl'
            })
            .state('detect', {  //检测
                url: '/detect',
                templateUrl: 'html/detect/index.html',
                controller: 'DetectCtrl'
            })
            .state('recycle', {  //回收
                url: '/recycle',
                templateUrl: 'html/recycle/index.html',
                controller: 'RecycleCtrl'
            })
            .state('exchange', {  //换款
                url: '/exchange',
                templateUrl: 'html/exchange/index.html',
                controller: 'ExchangCtrl'
            })

        $urlRouterProvider.otherwise('main')
    })