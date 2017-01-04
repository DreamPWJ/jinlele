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
                controller: 'FavouriteCtrl'
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
                url: '/orderdetail/:orderno',
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
            .state('addcomment', {  //添加评价
                url: '/addcomment/:orderno',
                templateUrl: 'html/addcomment.html',
                controller: 'AddCommentCtrl'
            })
            .state('procphoto', {  //流程-拍照
                url: '/procphoto/:name',
                templateUrl: 'html/common/procphoto.html',
                controller: 'ProcPhotoCtrl'
            })
            .state('proccommitorder', {  //流程-提交订单
                url: '/proccommitorder',
                templateUrl: 'html/common/proccommitorder.html',
                controller: 'ProcCommitOrderCtrl'
            })
            .state('procreceive', {  //流程-平台收货
                url: '/procreceive/:name/:orderNo/:orderTime',
                templateUrl: 'html/common/procreceive.html',
                controller: 'ProcReceiveCtrl'
            })
            .state('proctest', {  //流程-检测
                url: '/proctest/:name',
                templateUrl: 'html/common/proctest.html',
                controller: 'ProcTestCtrl'
            })
            .state('procpost', {  //流程-邮寄
                url: '/procpost/:name',
                templateUrl: 'html/common/procpost.html',
                controller: 'ProcPostCtrl'
            })
            .state('proccheck', {  //流程-验货
                url: '/proccheck/:name',
                templateUrl: 'html/common/proccheck.html',
                controller: 'ProcCheckCtrl'
            })
            .state('procaddcmt', {  //流程-评论
                url: '/procaddcmt/:name',
                templateUrl: 'html/common/procaddcmt.html',
                controller: 'ProcAddCmtCtrl'
            })
            .state('procpricing', {  //流程-估价结果
                url: '/procpricing/:name',
                templateUrl: 'html/common/procpricing.html',
                controller: 'ProcPricingCtrl'
            })
            .state('procrefurbish', {  //翻新
                url: '/procrefurbish/:name',
                templateUrl: 'html/refurbish/procrefurbish.html',
                controller: 'ProcRefurbishCtrl'
            })
            .state('procfixprice', {  //定价
                url: '/procfixprice/:name',
                templateUrl: 'html/repair/procfixprice.html',
                controller: 'ProcFixpriceCtrl'
            })
            .state('procrepair', {  //维修
                url: '/procrepair/:name',
                templateUrl: 'html/repair/procrepair.html',
                controller: 'ProcRepairCtrl'
            })
            .state('recycle', {  //回收-估价
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