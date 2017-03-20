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
            .state('cashApply', {  //钱包
                url: '/cashApply/:balance',
                templateUrl: 'html/cashApply.html',
                controller: 'cashApplyCtrl'
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
            .state('wish', {  //帮助反馈
                url: '/wish',
                templateUrl: 'html/wish.html',
                controller: 'WishCtrl'
            })
            .state('bindphone', {  //绑定手机号
                url: '/bindphone',
                templateUrl: 'html/bindphone.html',
                controller: 'BindphoneCtrl'
            })
            .state('orderlist', {  // 商城订单
                url: '/orderlist{openId}',
                templateUrl: 'html/orderlist.html',
                controller: 'OrderListCtrl'
            })
            .state('returnapply', {  //退货
                url: '/returnapply/:orderno/:type',
                templateUrl: 'html/returnapply.html',
                controller: 'ReturnApplyCtrl'
            })
            .state('orderdetail', {  //商城订单详情
                url: '/orderdetail{order}',
                templateUrl: 'html/orderdetail.html',
                controller: 'OrderDetailCtrl'
            })
            .state('servicedetail', {  //服务订单详情
                url: '/servicedetail{order}',
                templateUrl: 'html/servicedetail.html',
                controller: 'ServiceDetailCtrl'
            })
            .state('payresult', {  //支付进度
                url: '/payresult{order}',
                templateUrl: 'html/payresult.html',
                controller: 'PayResultCtrl'
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
                url: '/procreceive/:type/:orderNo/:orderTime',
                templateUrl: 'html/common/procreceive.html',
                controller: 'ProcReceiveCtrl'
            })
            .state('proctest', {  //流程-检测
                url: '/proctest/:type/:orderNo/:orderTime',
                templateUrl: 'html/common/proctest.html',
                controller: 'ProcTestCtrl'
            })
            .state('procpost', {  //流程-邮寄
                url: '/procpost/:type/:orderNo/:orderTime',
                templateUrl: 'html/common/procpost.html',
                controller: 'ProcPostCtrl'
            })
            .state('proccheck', {  //流程-验货
                url: '/proccheck/:type/:orderNo/:orderTime',
                templateUrl: 'html/common/proccheck.html',
                controller: 'ProcCheckCtrl'
            })
            .state('procaddcmt', {  //流程-评论
                url: '/procaddcmt/:type/:orderno',
                templateUrl: 'html/common/procaddcmt.html',
                controller: 'ProcAddCmtCtrl'
            })
            .state('procrefurbish', {  //翻新
                url: '/procrefurbish/:name/:orderNo/:orderTime',
                templateUrl: 'html/refurbish/procrefurbish.html',
                controller: 'ProcRefurbishCtrl'
            })
            .state('procfixprice', {  //定价
                url: '/procfixprice/:name/:orderno',
                templateUrl: 'html/repair/procfixprice.html',
                controller: 'ProcFixpriceCtrl'
            })
            .state('procrepair', {  //维修
                url: '/procrepair/:name/:orderno/:orderTime',
                templateUrl: 'html/repair/procrepair.html',
                controller: 'ProcRepairCtrl'
            })
            .state('evaluate', {  //估价(回收、换款)
                url: '/evaluate/:name/:id',
                templateUrl: 'html/shared/evaluate.html',
                controller: 'EvaluateCtrl'
            })
            .state('evaluationresult', {  //估计结果(回收、换款)
                url: '/{name}evaluationresult/:result',
                templateUrl: 'html/shared/evaluationresult.html',
                controller: 'EvaluationResultCtrl'
            })
            .state('actualprice', {  //实际定价(回收、换款)
                url: '/actualprice/:type/:orderno',
                templateUrl: 'html/shared/actualprice.html',
                controller: 'ActualPriceCtrl'
            })
            .state('cfmrecycle', {  //回收-确认回收
                url: '/cfmrecycle/:orderno/:orderstatus',
                templateUrl: 'html/recycle/cfmrecycle.html',
                controller: 'CfmRecycleCtrl'
            })
            .state('cfmexchange', {  //换款-确认换款
                url: '/cfmexchange/:orderno/:orderstatus',
                templateUrl: 'html/exchange/cfmexchange.html',
                controller: 'CfmExchangeCtrl'
            })
            .state('barterlist', {  //换款列表
                url: '/barterlist',
                templateUrl: 'html/exchange/barterList.html',
                controller: 'BarterListCtrl'
            })
            .state('barterdetail', {  //换款详情
                url: '/barterdetailfor{goodId}',
                templateUrl: 'html/exchange/barterDetail.html',
                controller: 'BarterDetailCtrl'
            })
            .state('recharge', {  //充值
                url: '/recharge',
                templateUrl: 'html/recharge.html',
                controller: 'RechargeCtrl'
            })
            .state('rechargeOK', {  //充值成功
                url: '/rechargeOK/:orderno',
                templateUrl: 'html/rechargeOK.html',
                controller: 'RechargeOKCtrl'
            })
            .state('walletdetail', {  //账户明细
                url: '/walletdetail',
                templateUrl: 'html/walletdetail.html',
                controller: 'WalletdetailCtrl'
            })

        $urlRouterProvider.otherwise('main')
    })