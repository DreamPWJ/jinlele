angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'starter.config', 'starter.directive', 'starter.filter'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {  //APP首页面
                url: '/main',
                cache: true,
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
            .state('cashdetail', {  //钱包
                url: '/cashdetail',
                templateUrl: 'html/cashdetail.html',
                controller: 'CashdetailCtrl'
            })
            .state('favourite', {  //我的收藏
                url: '/favourite',
                templateUrl: 'html/favourite.html',
                controller: 'FavorCtrl'
            })
            .state('orderlist', {  // 商城订单
                url: '/orderlist',
                templateUrl: 'html/orderlist.html',
                controller: 'OrderListCtrl'
            })
            .state('returnapply', {  //退货
                url: '/returnapply/:id',
                templateUrl: 'html/returnapply.html',
                controller: 'ReturnApplyCtrl'
            })
            .state('orderdetail', {  //订单详情
                url: '/orderdetail/:id',
                templateUrl: 'html/orderdetail.html',
                controller: 'OrderDetailCtrl'
            })
            .state('confirmorder', {  //确认订单
                url: '/confirmorder',
                templateUrl: 'html/confirmorder.html',
                controller: 'ConfirmOrderCtrl'
            })
            .state('goodlist', {  //商品列表
                url: '/goodlist/:name',
                templateUrl: 'html/goodlist.html',
                controller: 'GoodListCtrl'
            })
            .state('gooddetail', {  //商品详情
                url: '/gooddetail/:id',
                templateUrl: 'html/gooddetail.html',
                controller: 'GoodDetailCtrl'
            })
            .state('addcomment', {  //添加评论
                url: '/addcomment',
                templateUrl: 'html/addcomment.html',
                controller: 'AddCommentCtrl'
            })
            .state('refurbish', {  //翻新
                url: '/refurbish',
                templateUrl: 'html/refurbish/index.html',
                controller: 'RefurbishCtrl'
            })
            .state('refpay', {  //翻新付款
                url: '/refpay',
                templateUrl: 'html/refurbish/pay.html',
                controller: 'RefpayCtrl'
            })
            .state('refreceipt', {  //翻新收货
                url: '/refreceipt',
                templateUrl: 'html/refurbish/receipt.html',
                controller: 'RefReceiptCtrl'
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
            .state('ordercomment', {  //换款
                url: '/ordercomment/:name',
                templateUrl: 'html/common/ordercomment.html',
                controller: 'OrderCommentCtrl'
            })

        $urlRouterProvider.otherwise('main')
    })