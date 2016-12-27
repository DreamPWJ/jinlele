angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        // $httpProvider.interceptors.push('AuthInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('MainCtrl', ['$scope', '$rootScope', 'CommonService', 'MainService', 'WeiXinService', function ($scope, $rootScope, CommonService, MainService, WeiXinService) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });

        //加载此页面的时候
        //自动读取网页授权接口获取用户的opendId,从而得到用户的信息，得到前台用户的id，这里暂时强制设定用户的id
        localStorage.setItem("jinlele_id", 1); //1应该是从数据库中查到的


        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
        }).then(function () {
            //首页新品推荐分页显示
            MainService.getNewProducts({pagenow: 1}).success(function (data) {
                $scope.newProductsinfo = data;

            })
        }).then(function () {
            //是否是微信 初次获取签名 获取微信签名
            if (WeiXinService.isWeiXin()) {
                // 获取微信签名
                $scope.wxparams = {
                    url: location.href.split('#')[0] //当前网页的URL，不包含#及其后面部分
                }
                WeiXinService.getWCSignature($scope.wxparams).success(function (data) {
                    localStorage.setItem("timestamp", data.timestamp);//生成签名的时间戳
                    localStorage.setItem("noncestr", data.nonceStr);//生成签名的随机串
                    localStorage.setItem("signature", data.signature);//生成签名
                    //通过config接口注入权限验证配置
                    WeiXinService.weichatConfig(data.timestamp, data.nonceStr, data.signature);


                })
            }
        })

    }])

    //分类tab
    .controller('CategoryCtrl', function ($scope, $stateParams, $window, CategoryService, ResizeService) {
        ResizeService.autoHeight();
        $window.onresize = ResizeService.autoHeight;
        $scope.init = {
            pagenow: 1,
            goodArr: [],//产品列表
            //根据分类id查询所有的商品 分页展示
            findGoods: function (catogoryId) {
                CategoryService.getGoodsByCidPaging($scope.init.pagenow, catogoryId).success(function (data) {
                    $scope.goods = data.pagingList;
                });
            },
            //根据一级分类id 查询所有的二级分类
            getSecondCatogories: function (pid) {
                CategoryService.getSecondCatogories(pid).success(function (data) {
                    $scope.secondCatogories = data;
                    if ($scope.secondCatogories != null && $scope.secondCatogories.length > 0) {
                        $scope.init.findGoods($scope.secondCatogories[0].id);
                    }
                });
            }
        };
        //初始加载
        CategoryService.getcatogories().success(function (data) {
            $scope.catogory = data.firstList;
        });
        //初始加载二级分类和下面的产品列表
        $scope.init.getSecondCatogories($stateParams.id);

    })


    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })
    //购物车
    .controller('ShoppingCartCtrl', ['$scope', 'CartService', 'CommonService', '$state', '$rootScope', function ($scope, CartService, CommonService, $state, $rootScope) {
        $scope.init = {
            userid: 1,
            pagenow: 1
        };
        CartService.getcartinfo($scope.init).success(function (data) {
            $scope.cartlist = data;
        });
        $scope.totalnum = 0;
        $scope.totalprice = 0;
        $scope.m = [];
        $scope.checked = [];
        //全选
        $scope.selectAll = function ($event) {
            var choseall = $event.target;
            if ($scope.select_all) {
                $scope.select_one = true;
                $scope.checked = [];
                angular.forEach($scope.cartlist.pagingList, function (data, index) {
                    $scope.checked.push(data.gcid);
                    $scope.m[data.gcid] = true;
                    $scope.totalnum += data.num;
                    $scope.totalprice += data.num * data.saleprice;
                })
                $('#' + choseall.gcid).siblings("label").addClass("on");
                angular.forEach($scope.checked, function (i, index) {
                    $('#' + i).siblings("label").addClass("on");
                })
            } else {
                $scope.select_one = false;
                $scope.checked = [];
                $scope.m = [];
                $scope.totalnum = 0;
                $scope.totalprice = 0;
                $('.check_label').removeClass("on");
            }
            console.log($scope.checked);
        };
        //单选
        $scope.selectOne = function ($event, select) {
            var choseone = $event.target;
            angular.forEach($scope.m, function (data, id) {
                var index = $scope.checked.indexOf(id);
                if (data && index === -1) {
                    $scope.checked.push(id);
                    $('#' + choseone.id).siblings("label").addClass("on");
                } else if (!data && index !== -1) {
                    $scope.checked.splice(index, 1);
                    $('#' + choseone.id).siblings("label").removeClass("on");
                }
                ;
            })
            if ($scope.cartlist.pagingList.length === $scope.checked.length) {
                $scope.select_all = true;
                $('#all').siblings("label").addClass("on");
            } else {
                $scope.select_all = false;
                $('#all').siblings("label").removeClass("on");
            }
            $scope.totalnum = 0;//去除重复，记录最后一遍数据
            $scope.totalprice = 0;
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                var f = $scope.checked.indexOf(data.gcid);
                if (data && f !== -1) {
                    $scope.totalnum += data.num;
                    $scope.totalprice += data.num * data.saleprice;
                }
            })
            console.log($scope.checked);
        }
        //删除
        $scope.del = function () {
            console.log($scope.checked);
        }
        //数量更新(点击，手改)
        $scope.updateamount = function (id, count) {
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    item.num = item.num + count;//这里可以增加上下限制
                    if (item.num < 1) {
                        //$scope.cartlist.pagingList.splice(i, 1);
                        item.num = 1;
                    }
                }
                var f = $scope.checked.indexOf(item.gcid);
                if (item && f !== -1) {
                    $scope.totalnum += item.num;
                    $scope.totalprice += item.num * item.saleprice;
                }
            }
        }
        $scope.changenamount = function (id) {
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    if (!/^\+?[1-9][0-9]*$/.test(item.num)) {
                        item.num = 1;
                    }
                }
                var f = $scope.checked.indexOf(item.gcid);
                if (item && f !== -1) {
                    $scope.totalnum += parseInt(item.num);
                    $scope.totalprice += item.num * item.saleprice;
                }
            }
        }
        $scope.confirmorder = function () {
            console.log($scope.checked);
            if ($scope.checked.length == 0) {
                $rootScope.commonService = CommonService;
                CommonService.toolTip("请选择要购买的商品");
                return;
            }
            $scope.checkedGoodArr = [];
            $scope.checkedGoodChildArr = [];
            angular.forEach($scope.cartlist.pagingList, function (item) {
                console.log(JSON.stringify(item.gcid));
                for (var i = 0, len = $scope.checked.length; i < len; i++) {
                    var obj = {};
                    if (item.gcid == $scope.checked[i]) {
                        console.log('item.gcid ==');
                        obj.goodId = item.goodId;
                        obj.cartId = item.cartId;
                        obj.gcid = item.gcid;
                        obj.num = item.num;
                        $scope.checkedGoodChildArr.push(obj);
                        $scope.checkedGoodArr.push(item);
                    }
                }
            })

            $scope.obj = {
                totalprice: $scope.totalprice,
                totalnum: $scope.totalnum,
                userId: localStorage.getItem("jinlele_id"),
                storeId: 1,//后续需要根据客户选择传入
                chars: JSON.stringify($scope.checkedGoodChildArr)
            };
            //去后台生成商成订单 和 订单_商品子表的数据
            CartService.saveOrder($scope.obj).success(function (data) {
                console.log(JSON.stringify(data));
                if (data && data.status == "ok") {
                    //跳转到支付页面
                    $state.go("confirmorder", {
                        checkedGoodArr: JSON.stringify($scope.checkedGoodArr),
                        totalprice: $scope.totalprice,
                        totalnum: $scope.totalnum
                    });
                }
            });

        }
    }])
    //会员
    .controller('MemberCtrl', function ($scope) {

    })
    //商城订单
    .controller('OrderListCtrl', ['$scope', 'WeiXinService', 'OrderListService', function ($scope, WeiXinService, OrderListService) {
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
        $scope.init = {
            userid: 1,
            pagenow: 1
        };
        OrderListService.getorderLists($scope.init).success(function (data) {
            $scope.list = data;
        });

        //微信支付调用
        $scope.weixinPay = function () {
            //调用微信支付服务器端接口
            WeiXinService.getweixinPayData().success(function (data) {
                WeiXinService.wxchooseWXPay(data); //调起微支付接口
            })
        }


    }])
    //订单详情
    .controller('OrderDetailCtrl', function ($scope) {

    })
    //退货
    .controller('ReturnApplyCtrl', function ($scope) {
        $(document).ready(function () {
            $('.default').dropkick();
            theme:'black'
        });
    })
    //我的钱包
    .controller('WalletCtrl', function ($scope) {

    })
    //提现记录
    .controller('CashdetailCtrl', function ($scope) {

    })
    //我的收藏
    .controller('FavorCtrl', function ($scope) {

    })
    //商品列表
    .controller('GoodListCtrl', function ($scope, GoodService, $stateParams) {

        //获取产品列表
        GoodService.getGoodList({pagenow: 1, categoryname: $stateParams.name, querytype: 0}).success(function (data) {
            $scope.goodList = data;
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
    .controller('GoodDetailCtrl', function ($scope, $stateParams, $rootScope, GoodService, AddtoCartService, CommonService) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });
        //初始化参数
        $scope.bannerurl = "";
        $scope.stocknum = 0;//库存数

        $scope.gooddetail = {
            userId: localStorage.getItem("jinlele_id"),
            goodId: $stateParams.id,
            goodchildId: "",
            num: 1
        };
        GoodService.getGoodDetail({goodId: $stateParams.id, userId: $scope.gooddetail.userId}).success(function (data) {
            console.log(JSON.stringify(data));
            $scope.goodDetail = data.good;
            $scope.goodChilds = data.goodchilds;
            $scope.totalnum = data.totalnum;
            $scope.initNum = data.totalnum;
            $scope.bannerurl = $scope.goodChilds[0].imgurl;
            $scope.stocknum = $scope.goodChilds[0].stocknumber;
            if ($scope.goodChilds && $scope.goodChilds.length > 0) {
                angular.forEach($scope.goodChilds, function (item) {
                    item.flag = false;
                });
            }
            console.log("$scope.goodChilds==" + JSON.stringify($scope.goodChilds));

        });

        $scope.addtocart = function () {
            if (!$scope.gooddetail.goodchildId) {
                $rootScope.commonService = CommonService;
                CommonService.toolTip("请选择颜色分类", "tool-tip-message");
                return;
            }
            $scope.changeNum();
            AddtoCartService.addtocart($scope.gooddetail).success(
                function (data) {
                    console.log('data===' + data);
                    $rootScope.commonService = CommonService;
                    CommonService.toolTip("添加成功", "tool-tip-message-success");
                }
            )
        }
        $scope.changeNum = function () {
            $scope.totalnum = $scope.initNum + parseInt($scope.gooddetail.num || 0);
        }
        $scope.addNum = function () {
            $scope.gooddetail.num++;
            $scope.totalnum = $scope.totalnum + 1;
        }
        $scope.minusNum = function () {
            if ($scope.gooddetail.num > 1) {
                $scope.gooddetail.num--;
                $scope.totalnum = $scope.totalnum - 1;
            }
        }

    })


    //发表评论
    .controller('AddCommentCtrl', function ($scope) {

    })

    //确认订单
    .controller('ConfirmOrderCtrl', function ($scope, $stateParams) {
        $scope.goodsArr = JSON.parse($stateParams.checkedGoodArr);
        $scope.totalprice = $stateParams.totalprice;
        $scope.totalnum = $stateParams.totalnum;
        // $(".check_label").checkbox();
        console.log("--" + JSON.stringify($scope.goodsArr));
        // $scope.goodArr =    $stateParams.checkedGoodArr;
        // console.log( $scope.goodArr);

    })
    //流程-拍照
    .controller('ProcPhotoCtrl', function ($scope, $stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.localflag = false;
        if ($stateParams.name == "repair") {
            $scope.localflag = true;
        }

    })
    //流程-提交订单
    .controller('ProcCommitOrderCtrl', function ($scope, $stateParams, $window) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.showaddr = $stateParams.name == 'recycle' ? false : true;
        $scope.goback = function () {
            $window.history.back();
        }

    })
    //流程-平台收货
    .controller('ProcReceiveCtrl', function ($scope, $stateParams, $window) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.tracking = $stateParams.name == 'recycle' ? true : false;

    })
    //流程-检测
    .controller('ProcTestCtrl', function ($scope, $stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
    })
    //流程-邮寄
    .controller('ProcPostCtrl', function ($scope, $stateParams, $location) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        if ($stateParams.name == "recycle") {
            $location.path("/");
        }

    })
    //流程-验货
    .controller('ProcCheckCtrl', function ($scope, $stateParams, $location) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        if ($stateParams.name == "recycle") {
            $location.path("/");
        }

    })
    //流程-评价
    .controller('ProcAddCmtCtrl', function ($scope, $stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
    })
    //估价结果(回收、换款)
    .controller('ProcPricingCtrl', function ($scope, $stateParams, $location) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.decide = false;
        switch ($stateParams.name) {
            case "recycle":
                $scope.decide = true;
                break;
            case "exchange":
                break;
            default :
                $location.path("/");
                break;
        }
    })
    //翻新
    .controller('ProcRefurbishCtrl', function ($scope, $stateParams, $location) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        if ($stateParams.name != "refurbish") {
            $location.path("/");
        }

    })
    //维修-定价
    .controller('ProcFixpriceCtrl', function ($scope, $stateParams) {
        $scope.pagetheme = $stateParams.name;
    })
    //维修
    .controller('ProcRepairCtrl', function ($scope, $stateParams) {
        $scope.pagetheme = $stateParams.name;
        if ($stateParams.name != "repair") {
            $location.path("/");
        }
    })
    //回收-估价
    .controller('RecycleCtrl', function ($scope) {

    })
    //换款
    .controller('ExchangCtrl', function ($scope) {

    })

