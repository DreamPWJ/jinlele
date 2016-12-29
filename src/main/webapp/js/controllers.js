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
        //localStorage.setItem("jinlele_userId", 1); //1应该是从数据库中查到的

        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
            localStorage.setItem("openId", data.openId);//缓存微信用户唯一标示openId
            localStorage.setItem("jinlele_userId", data.userId);//缓存微信用户唯一标示 userId
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
                $scope.catogoryid=pid;
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
        $scope.catogoryid=$stateParams.id;
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
        $scope.delstyle = {display: 'none'};
        CartService.getcartinfo($scope.init).success(function (data) {
            $scope.cartlist = data;
        });
        //初始化数据
        $scope.totalnum = 0;
        $scope.totalprice = 0;
        $scope.m = [];
        $scope.checkedGcIds = [];
        $scope.checkedinfo = [];
        //全选
        $scope.selectAll = function ($event) {
            //去除重复，记录最后一遍数据
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            $scope.checkedinfo = [];
            var choseall = $event.target;
            if ($scope.select_all) {
                $scope.select_one = true;
                $scope.checkedGcIds = [];
                angular.forEach($scope.cartlist.pagingList, function (data, index) {
                    $scope.checkedGcIds.push(data.gcid);
                    $scope.m[data.gcid] = true;
                    $scope.checkedinfo.push(data);
                    $scope.totalnum += parseInt(data.num);
                    $scope.totalprice += parseInt(data.num) * data.saleprice;
                })
                $('#' + choseall.id).siblings("label").addClass("on");
                angular.forEach($scope.checkedGcIds, function (i, index) {
                    $('#' + i).siblings("label").addClass("on");
                })
            } else {
                $scope.select_one = false;
                $scope.checkedGcIds = [];
                $scope.checkedinfo = [];
                $scope.m = [];
                $scope.totalnum = 0;
                $scope.totalprice = 0;
                $('.check_label').removeClass("on");
            }
        };
        //单选
        $scope.selectOne = function ($event, select) {
            var choseone = $event.target;
            angular.forEach($scope.m, function (data, id) {
                var index = $scope.checkedGcIds.indexOf(id);
                if (data && index === -1) {
                    $scope.checkedGcIds.push(id);
                    $('#' + choseone.id).siblings("label").addClass("on");
                } else if (!data && index !== -1) {
                    $scope.checkedGcIds.splice(index, 1);
                    $('#' + choseone.id).siblings("label").removeClass("on");
                }
            })
            if ($scope.cartlist.pagingList.length === $scope.checkedGcIds.length) {
                $scope.select_all = true;
                $('#all').siblings("label").addClass("on");
            } else {
                $scope.select_all = false;
                $('#all').siblings("label").removeClass("on");
            }
            $scope.totalnum = 0;//去除重复，记录最后一遍数据
            $scope.totalprice = 0;
            $scope.checkedinfo=[];
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                var f = $scope.checkedGcIds.indexOf(data.gcid);
                if (data && f !== -1) {
                    $scope.checkedinfo.push(data);
                    $scope.totalnum += parseInt(data.num);
                    $scope.totalprice += parseInt(data.num) * data.saleprice;
                }
            })
        }
        //删除
        $scope.del = function () {
            if ($scope.checkedGcIds.length > 0) {
                $scope.delstyle = {};
            }
        }
        //确认删除
        $scope.confirm = function () {
            console.log($scope.checkedGcIds);
            $scope.delstyle = {display: 'none'};
            CartService.deleteCart({userid: 1, gcIdStr: $scope.checkedGcIds.join('-').trim()}).success(function (data) {
                console.log(data);
                CartService.getcartinfo($scope.init).success(function (data) {
                    $scope.cartlist = data;
                });
            })
        }
        //取消删除
        $scope.cancle = function () {
            $scope.delstyle = {display: 'none'};
        }
        //点击更新数量
        $scope.updateamount = function (id, count) {
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            $scope.checkedinfo=[];
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    item.num = item.num + count;//这里可以增加上下限制
                    if (item.num < 1) {
                        //$scope.cartlist.pagingList.splice(i, 1);
                        item.num = 1;
                    }
                }
                var f = $scope.checkedGcIds.indexOf(item.gcid);//判断是否存在选中的gcid
                if (item && f !== -1) {
                    $scope.checkedinfo.push(item);
                    $scope.totalnum += parseInt(item.num);
                    $scope.totalprice += parseInt(item.num) * item.saleprice;
                }
            }
        }
        //手改更新数量
        $scope.changenamount = function (id) {
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            $scope.checkedinfo=[];
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    if (!/^\+?[1-9][0-9]*$/.test(item.num)) {
                        item.num = 1;
                    }
                }
                var f = $scope.checkedGcIds.indexOf(item.gcid);
                if (item && f !== -1) {
                    $scope.checkedinfo.push(item);
                    $scope.totalnum += parseInt(item.num);
                    $scope.totalprice += parseInt(item.num) * item.saleprice;
                }
            }
        }
        //结算
        $scope.bill=function(){
            if($scope.checkedGcIds.length>0){
                $state.go("confirmorder",{selectinfo:JSON.stringify($scope.checkedinfo)});
            }else{
                CommonService.toolTip("您还没有选择要购买的商品哦！");
            }
        }
        $scope.confirmorder = function () {
            console.log($scope.checkedGcIds);
            if ($scope.checkedGcIds.length == 0) {
                $rootScope.commonService = CommonService;
                CommonService.toolTip("请选择要购买的商品");
                return;
            }
            $scope.checkedGoodArr = [];
            $scope.checkedGoodChildArr = [];
            angular.forEach($scope.cartlist.pagingList, function (item) {
                console.log(JSON.stringify(item.gcid));
                for (var i = 0, len = $scope.checkedGcIds.length; i < len; i++) {
                    var obj = {};
                    if (item.gcid == $scope.checkedGcIds[i]) {
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
                userId: localStorage.getItem("jinlele_userId"),
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
    //确认订单
    .controller('ConfirmOrderCtrl',['$scope','$stateParams','$state','CartService', function ($scope, $stateParams,$state,CartService) {
        $scope.selectinfo = JSON.parse($stateParams.selectinfo);
        $scope.totalprice = 0;
        $scope.totalnum =0;
        for(var i=0;i<$scope.selectinfo.length;i++) {
            $scope.totalnum += parseInt($scope.selectinfo[i].num);
            $scope.totalprice += parseInt($scope.selectinfo[i].num) * $scope.selectinfo[i].saleprice;
        }
        //1.获取地址2.保存订单，返回订单号（保存到shoporder，shoporder_good，删除购物车相应数据）  3.支付(付与未付)->订单详情页
        $scope.submitorder=function(){
            $scope.obj = {
                totalprice: $scope.totalprice,
                totalnum: $scope.totalnum,
                userId: 1,//localStorage.getItem("jinlele_userId"),
                storeId: 1,//后续需要根据客户选择传入
                chars: $stateParams.selectinfo
            };
            //去后台生成商成订单 和 订单_商品子表的数据
            CartService.saveOrder($scope.obj).success(function (data) {
                //调用支付
                $state.go("orderdetail",{orderno:data.orderno});

            });
        }
    }])
    //订单详情
    .controller('OrderDetailCtrl', ['$scope','$stateParams','OrderService',function ($scope, $stateParams,OrderService) {
        OrderService.getOrderDetailInfo({orderno:$stateParams.orderno}).success(function(data){
            $scope.orderinfo=data.order;//订单总信息
            $scope.orderdetail=data.orderdetail;//订单详情
        })
    }])
    //发表评论
    .controller('AddCommentCtrl', ['$scope','$stateParams','OrderService',function ($scope, $stateParams,OrderService) {
        OrderService.getOrderDetailInfo({orderno:$stateParams.orderno}).success(function(data){
            $scope.orderinfo=data.order;//订单总信息
            $scope.orderdetail=data.orderdetail;//订单详情
        })
    }])
    //会员
    .controller('MemberCtrl', ['$scope', 'MemberService', function ($scope, MemberService) {
        var opendid =localStorage.getItem("openId");
        MemberService.getUserInfo(opendid).success(function (data) {
            $scope.user = data.userInfo;
            //console.log(JSON.stringify(data));
        });
    }])

    //商城订单
    .controller('OrderListCtrl', ['$scope', 'WeiXinService', 'OrderListService', 'OrderService', function ($scope, WeiXinService, OrderListService, OrderService) {
        var mySwiper = new Swiper('.swiper-container', {
            pagination: '.tab',
            paginationClickable: true,
            //autoHeight: true,
            paginationBulletRender: function (index, className) {
                switch (index) {
                    case 0:
                        name = '待付款';
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
        $scope.cancleorder = function (orderno) {
            //修改后，重新请求数据
            OrderService.cancleOrder({orderno: orderno}).success(function (data) {
                if (parseInt(data.resultnumber) > 0) {
                    OrderListService.getorderLists($scope.init).success(function (data) {
                        $scope.list = data;
                    });
                }
            });
        }
        //微信支付调用
        $scope.weixinPay = function () {
            //调用微信支付服务器端接口
            WeiXinService.getweixinPayData().success(function (data) {
                WeiXinService.wxchooseWXPay(data); //调起微支付接口
            })
        }


    }])
    //退货
    .controller('ReturnApplyCtrl', function ($scope, $stateParams) {
        $(function () {
            $('.default').dropkick();
            theme:'black'
        });
        $scope.returnApply={returntypeCode:"",harvestCode:"",reason:"",memo:"",orderno:$stateParams.id};
        $scope.sub=function(){
            console.log($scope.returnApply);
        }
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
            userId:1,// localStorage.getItem("jinlele_userId"),
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

    //流程-拍照
    .controller('ProcPhotoCtrl', function ($scope, $stateParams ,WeiXinService ,$rootScope ,CommonService ,ProcPhotoService ,$state) {
        $rootScope.commonService = CommonService;
        WeiXinService.mediaIds = []; //置空媒体id数组
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.localflag = false;
        $scope.localIds = [];// 上传图片的微信路径 数组
        $scope.type = "";//服务类型 001翻新
        $scope.service = {  //服务实体
            price:1980, //价格
            descrip:""    //描述
        };
        if ($stateParams.name == "repair") {
            $scope.localflag = true;
        }

        $scope.wxchooseImage=function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxchooseImage(function (localIds) {
                    $scope.localIds =localIds;
                    $scope.$apply();
                })
            })
        }

        //进入提交订单的页面
        $scope.proccommitorder = function (pagetheme) {
            $state.go("proccommitorder",{name:pagetheme});

            //判断参数
            // var len=$scope.localIds.length;
            // if(len==0){
            //      CommonService.toolTip("请上传图片");
            //      return;
            // }
            // if($scope.service.descrip==""){
            //      CommonService.toolTip("请填写商品描述");
            //      return;
            // }
            // if(pagetheme == "refurbish"){
            //     $scope.type = '001';
            // }
            // if(pagetheme == "repair"){
            //     $scope.type = '002';
            // }
            // if(pagetheme == "detect"){
            //     $scope.type = '003';
            // }
            // if(pagetheme == "recycle"){
            //     $scope.type = '004';
            // }
            //
            // //①前台去上传图片的到微信并返回媒体Id 放入集合中
            // //通过config接口注入权限验证配置
            //
            // //②后台处理:拿到mediaId去后台上传图片传到服务器本地路径 //然后将本地图片上传到七牛并返回七牛图片url,在后台保存数据到翻新服务表 ，照片表 ，翻新服务_照片中间表
            //  $scope.params = {
            //      userId:localStorage.getItem("jinlele_userId"),
            //      mediaIds: WeiXinService.mediaIds,
            //      price:$scope.service.price,
            //      descrip:$scope.service.descrip,
            //      storeId:1,//暂时设定门店id为1 ，以后会根据地理位置动态获取
            //      type:$scope.type //上传类型 翻新001维修002检测003回收004服务信息表买方005卖方收货006
            //  };
            //
            // ProcPhotoService.saveService($scope.params).success(function (data) {
            //     if(data && data.status == 'ok'){
            //         $state.go("proccommitorder",{name:pagetheme});
            //     }
            // })

        }

        //$scope.proccommitorder = function (pagetheme) {}
    })

//③后台处理成功后，跳转到下单页面



//流程-提交订单
.controller('ProcCommitOrderCtrl', function ($scope, $stateParams, $window ,ProcCommitOrderService ,WeiXinService) {

    console.log($stateParams.name);
    $scope.pagetheme = $stateParams.name;
    $scope.showaddr = $stateParams.name == 'recycle' ? false : true;
    $scope.order = {
        storeId:"",
        sendway:"",
        getway:""
    };
    $scope.sendwayFlag = false;//寄件方式切换
    $scope.getwayFlag = false; //取件方式切换
    $scope.wayValue=['001' ,'002'];//寄件取件方式值

    $scope.goback = function () {
        $window.history.back();
    }
    //遍历门店
    ProcCommitOrderService.findAllStores().success(function (data) {
        $scope.stores = data;
        console.log(JSON.stringify(data));
    });
    //弹出获取地址的页面
    $scope.wxopenAddress=function () {
        //通过config接口注入权限验证配置
        WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
        //通过ready接口处理成功验证
        wx.ready(function () {
            WeiXinService.wxopenAddress();
            if(WeiXinService.address)alert(JSON.stringify(WeiXinService.address));
            //新增收货地址
        })
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

        $scope.wxchooseImage=function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxchooseImage()
            })
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

