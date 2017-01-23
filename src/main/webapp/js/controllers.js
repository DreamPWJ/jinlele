angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        $httpProvider.interceptors.push('MyInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('MainCtrl', ['$scope', '$rootScope', 'CommonService', 'MainService', 'WeiXinService', '$ionicScrollDelegate', function ($scope, $rootScope, CommonService, MainService, WeiXinService, $ionicScrollDelegate) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true,
            autoplay: 3000
        });

        //加载此页面的时候
        //自动读取网页授权接口获取用户的opendId,从而得到用户的信息，得到前台用户的id，这里暂时强制设定用户的id
        //localStorage.setItem("jinlele_userId", 1); //1应该是从数据库中查到的

        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
            localStorage.setItem("openId",localStorage.getItem("openId")?localStorage.getItem("openId"): data.openId);//缓存微信用户唯一标示openId
            localStorage.setItem("jinlele_userId",localStorage.getItem("jinlele_userId")?localStorage.getItem("jinlele_userId"): data.userId);//缓存微信用户唯一标示 userId
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
        //主页搜索
        $scope.search = '';//搜索内容
        $scope.searchquery = function (searchcontent) {
            $scope.search = searchcontent;
            $scope.getNewProducts();
        }

        //获取首页分页新产品
        $scope.newProductsinfo = [];
        $scope.page = 0;//当前页数
        $scope.total = 1;//总页数
        $scope.getNewProducts = function () {
            if ((arguments != [] && arguments[0] == 0) || $scope.search != '') {
                $scope.page = 0;
                $scope.newProductsinfo = [];
            }
            $scope.page++;
            //首页新品推荐分页显示
            MainService.getNewProducts({pagenow: $scope.page, searchcontent: $scope.search}).success(function (data) {
                $scope.search = '';//清空搜索条件
                angular.forEach(data.pagingList, function (item) {
                    $scope.newProductsinfo.push(item);
                })
                $scope.total = data.myPageCount;
                console.log(data);
                $ionicScrollDelegate.resize();//解决添加数据后页面不能及时滚动刷新造成卡顿
            }).finally(function () {
                $scope.$broadcast('scroll.refreshComplete');
                $scope.$broadcast('scroll.infiniteScrollComplete');
            })
        }
        $scope.getNewProducts();

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
                $scope.catogoryid = pid;
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
        $scope.catogoryid = $stateParams.id;
    })


    //登录页面
    .controller('LoginCtrl', function ($scope, $state, CommonService, AccountService) {

    })
    //购物车
    .controller('ShoppingCartCtrl', ['$scope', 'CartService', 'CommonService', '$state', '$rootScope', function ($scope, CartService, CommonService, $state, $rootScope) {
        $rootScope.commonService=CommonService;
        $scope.init = {
            userid: localStorage.getItem("jinlele_userId"),
            pagenow: 1
        };
        $scope.delstyle = {display: 'none'};
        CartService.getcartinfo($scope.init).success(function (data) {
            console.log(data);
            $scope.isNotData = false;
            if (data.pagingList.length == 0) {
                $scope.isNotData = true;
                return
            }
            $scope.cartlist = data;
        });
        //初始化数据
        $scope.totalnum = 0;
        $scope.totalprice = 0;
        $scope.m = [];
        $scope.checkedGcIds = [];
        $scope.checkedinfo = [];
        $scope.delFlag = false; //删除按钮默认不显示 选择了商品后才显示
        //全选
        $scope.selectAll = function ($event) {
            //去除重复，记录最后一遍数据
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            $scope.checkedinfo = [];
            var choseall = $event.target;
            if ($scope.select_all) {
                $scope.delFlag = true;
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
                $scope.delFlag = false;
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
            $scope.checkedinfo = [];
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                var f = $scope.checkedGcIds.indexOf(data.gcid);
                if (data && f !== -1) {
                    $scope.checkedinfo.push(data);
                    $scope.totalnum += parseInt(data.num);
                    $scope.totalprice += parseInt(data.num) * data.saleprice;
                }
            })
            $scope.delFlag = $scope.totalprice ?   true : false; //控制删除按钮是否显示


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
            CartService.deleteCart({userid: localStorage.getItem("jinlele_userId"), gcIdStr: $scope.checkedGcIds.join('-').trim()}).success(function (data) {
                console.log(data);
                CartService.getcartinfo($scope.init).success(function (data) {
                    $scope.cartlist = data;
                    $scope.isNotData = false;
                    if (data.pagingList.length == 0) {
                        $scope.isNotData = true;
                    }
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
            $scope.checkedinfo = [];
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    item.num = parseInt(item.num) + count;//这里可以增加上下限制
                    if (item.num < 1) {
                        //$scope.cartlist.pagingList.splice(i, 1);
                        item.num = 1;
                    }
                    if (parseInt(item.num) > item.stocknumber) {
                        item.num = item.stocknumber;
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
            $scope.checkedinfo = [];
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    if (!/^\+?[1-9][0-9]*$/.test(item.num)) {
                        item.num = 1;
                    }
                    if (parseInt(item.num) > item.stocknumber) {
                        item.num = item.stocknumber;
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
        $scope.bill = function () {
            if ($scope.checkedGcIds.length > 0) {
                $state.go("confirmorder");
                localStorage.setItem(localStorage.getItem("openId"), JSON.stringify($scope.checkedinfo));
            } else {
                CommonService.toolTip("您还没有选择要购买的商品哦！","tool-tip-message-success");
            }
        }
    }])
    //确认订单
    .controller('ConfirmOrderCtrl', ['$rootScope','$scope', '$state', 'CartService', 'AddressService', 'OrderService', 'WeiXinService', 'CommonService',function ($rootScope,$scope, $state, CartService, AddressService, OrderService, WeiXinService,CommonService) {
        $rootScope.commonService=CommonService;
        $scope.selectinfo = JSON.parse(localStorage.getItem(localStorage.getItem("openId")));
        $scope.totalprice = 0;
        $scope.totalnum = 0;
        $scope.address = {};
        $scope.show = false;
        for (var i = 0; i < $scope.selectinfo.length; i++) {
            $scope.totalnum += parseInt($scope.selectinfo[i].num);
            $scope.totalprice += parseInt($scope.selectinfo[i].num) * $scope.selectinfo[i].saleprice;
        }
        //从数据库获取最新地址展示
        AddressService.getlatestinfo({userid: localStorage.getItem("jinlele_userId")}).success(function (data) {
            $scope.address = data;
            if (data) {
                $scope.show = true;
            }
        });
        //微信内置添加地址，弹出获取地址的页面
        $scope.wxopenAddress = function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxopenAddress($scope);
            })
        }
        $scope.submitorder=function() {
            //确认信息
            $scope.confirminfo = [];
            //地址信息
            $scope.addressinfo = [];
            var address = {};
            address.userName = $scope.address.userName;
            address.postalCode = $scope.address.postalCode;
            address.provinceName = $scope.address.provinceName;
            address.cityName = $scope.address.cityName;
            address.countryName = $scope.address.countryName;
            address.detailInfo = $scope.address.detailInfo;
            address.nationalCode = $scope.address.nationalCode;
            address.telNumber = $scope.address.telNumber;
            $scope.addressinfo.push(address);
            var obj = {};
            obj.userId = localStorage.getItem("jinlele_userId");
            obj.totalnum = $scope.totalnum;
            obj.totalprice = $scope.totalprice;
            obj.storeId = 1;//后续需要根据客户选择传入
            obj.addressinfo = $scope.addressinfo;
            obj.detailinfo = JSON.parse(localStorage.getItem(localStorage.getItem("openId")));
            $scope.confirminfo.push(obj);
            //去后台生成商成订单 和 订单_商品子表的数据，返回订单信息
            CartService.saveOrder($scope.confirminfo).success(function (r) {
                if (r.errmsg == "ok") {
                    //调用微信支付服务器端接口
                    $scope.param = {
                        totalprice: 0.01, //$scope.totalprice,
                        orderNo: r.orderno,
                        descrip: '六唯壹珠宝',
                        openid: localStorage.getItem("openId"),
                        orderType:JSON.stringify({type:'006'})
                    }
                    //调用微信支付服务器端接口
                    WeiXinService.getweixinPayData($scope.param).success(function (data) {
                        WeiXinService.wxchooseWXPay(data) //调起微支付接口
                            .then(function (msg) {
                                switch (msg) {
                                    case "get_brand_wcpay_request:ok":
                                        CommonService.toolTip("支付成功","tool-tip-message-success");
                                        //支付成功，跳转订单详情
                                        sessionStorage.setItem( r.orderno,"ok");
                                        var order = {orderno: r.orderno ,orderType:'006' ,orderStatus:""}; //orderStatus为订单状态
                                        $state.go("orderdetail", {order: JSON.stringify(order)});
                                        break;
                                    default :
                                        //未支付，跳转支付进度
                                       sessionStorage.setItem(r.orderno,"");
                                        var order = {orderno: r.orderno ,orderType:'006' ,orderStatus:""}; //orderStatus为订单状态
                                        //$state.go("payresult", {orderno: r.orderno});
                                        $state.go("payresult", {order: JSON.stringify(order)});
                                        break;
                                }
                            });
                    })
                } else {
                    CommonService.toolTip("下单失败，请稍后重试！", "tool-tip-message-success");
                    $state.go("shoppingcart");
                }
            });
        }
    }])
    //支付进度
    .controller('PayResultCtrl',  ['$scope', '$stateParams','$state', 'OrderService', function ($scope, $stateParams,$state, OrderService) {
        $scope.order= JSON.parse($stateParams.order);
        console.log("$scope.order=="+$stateParams.order);
        if (sessionStorage.getItem($scope.order.orderno) == "ok" ) {
            OrderService.queryWxPutOrder({orderno:$scope.order.orderno, type:$scope.order.orderType,payresult:sessionStorage.getItem($scope.orderno)}).success(function(data){
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                switch ($scope.orderinfo.shoporderstatusCode) {//自定义支付进度展示
                    case "001":
                        $scope.process = [{value: "等待买家付款", len: 1}];
                        break;
                    case "002":
                        $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                        break;
                }
            });
        }else {
            OrderService.getOrderDetailInfo({orderno: $scope.order.orderno}).success(function (data) {
                console.log('getOrderDetailInfo==='+JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//地址信息
                var len =  $scope.orderinfo.shoporderstatusCode.length;
                var status = $scope.orderinfo.shoporderstatusCode.substring(len-3,len);
                switch (status) {//自定义支付进度展示
                    case "001":
                        $scope.process = [{value: "等待买家付款", len: 1}];
                        break;
                    case "002":
                        $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                        break;
                }
            });
        }

        //跳转到商城订单详情页
        $scope.orderdetail = function (orderno , orderType ,shoporderstatusCode) {
            var order = {orderno: orderno ,orderType:orderType ,orderStatus:shoporderstatusCode};
            console.log('orderdetail==='+JSON.stringify(order));
            $state.go("orderdetail", {order: JSON.stringify(order)});
        }
    }])

    //服务订单详情
    .controller('ServiceDetailCtrl', ['$rootScope','$scope', '$stateParams', '$state',  'OrderService', 'CommonService','WeiXinService', function ($rootScope,$scope, $stateParams, $state, OrderService,CommonService,WeiXinService) {
        $rootScope.commonService=CommonService;
        $scope.order = JSON.parse($stateParams.order);
        console.log($stateParams.order);
        if($scope.order.orderStatus){
            $scope.order.orderStatus = $scope.order.orderStatus.substring(3,6);//截取后3位
        }
        if (sessionStorage.getItem($scope.order.orderno) == "ok" || $scope.order.orderStatus == '002') {
            OrderService.queryWxPutOrder({orderno: $scope.order.orderno,type: $scope.order.orderType, payresult:sessionStorage.getItem($scope.orderno)}).success(function(data){
                console.log(JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;// 收货地址信息
                $scope.pictures = data.pictures;//图片列表
                $scope.products = data.products;//产品列表
            });
        }else {
            OrderService.getOrderDetail({orderno: $scope.order.orderno}).success(function (data) {
                console.log('else Data=='+JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                $scope.pictures = data.pictures;//图片列表
                $scope.products = data.products;//产品列表
                //$scope.orderdetail = data.orderdetail;//订单详情
            });
        }
        //微信支付调用
        $scope.weixinPay = function (orderno, totalprice) {
            //调用微信支付服务器端接口
            $scope.param = {
                totalprice: 0.01, //$scope.totalprice,
                orderNo: orderno,
                descrip: '六唯壹珠宝',
                openid: localStorage.getItem("openId"),
                orderType:JSON.stringify({type:'006'})
            }
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                //调用微信支付服务器端接口
                WeiXinService.getweixinPayData($scope.param).success(function (data) {
                    WeiXinService.wxchooseWXPay(data) //调起微支付接口
                        .then(function (msg) {
                            switch (msg) {
                                case "get_brand_wcpay_request:ok":
                                    CommonService.toolTip("支付成功","tool-tip-message-success");
                                    //调用支付后，跳转订单详情
                                    sessionStorage.setItem(orderno,"ok");
                                    $state.go("payresult", {orderno: orderno});


                                    break;
                                default :
                                    //取消或失败，停留此页面
                                    break;
                            }
                        });
                });
            })
        }

        $scope.procreceive = function () {
            var obj = {
                name: $scope.order.orderType,
                orderNo: $scope.order.orderno,
                orderTime: $scope.orderinfo.create_time
            };
            console.log('procreceive===' + JSON.stringify(obj));
            $state.go('procreceive', obj);
        }

        $scope.cancleorder=function(orderno){
            //修改后，重新请求数据
            OrderService.cancleOrder({orderno: orderno}).success(function (data) {
                if (parseInt(data.resultnumber) > 0) {
                    CommonService.toolTip("取消成功", "tool-tip-message-success");
                    $state.go("orderlist");
                }
            });
        }
    }])

    //商城订单详情
    .controller('OrderDetailCtrl', ['$rootScope','$scope', '$stateParams', '$state',  'OrderService', 'CommonService','WeiXinService', function ($rootScope,$scope, $stateParams, $state, OrderService,CommonService,WeiXinService) {
        $rootScope.commonService=CommonService;
        console.log("$stateParams.order=="+$stateParams.order);
        $scope.order = JSON.parse($stateParams.order);
        //查询物流信息
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.order.orderno}).success(function (data) {
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        if($scope.order.orderStatus){
            $scope.order.orderStatus = $scope.order.orderStatus.substring(3,6);//截取后3位
        }
        if (sessionStorage.getItem($scope.orderno) == "ok" || $scope.order.orderStatus == '002') {
            OrderService.queryWxPutOrder({orderno: $scope.order.orderno,type: $scope.order.orderType, payresult:sessionStorage.getItem($scope.orderno)}).success(function(data){
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                $scope.orderdetail = data.orderdetail;//订单详情
            });
        }else {
            OrderService.getOrderDetailInfo({orderno: $scope.order.orderno}).success(function (data) {
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                $scope.orderdetail = data.orderdetail;//订单详情
            });
        }
        //微信支付调用
        $scope.weixinPay = function (orderno, totalprice) {
            //调用微信支付服务器端接口
            $scope.param = {
                totalprice: 0.01, //$scope.totalprice,
                orderNo: orderno,
                descrip: '六唯壹珠宝',
                openid: localStorage.getItem("openId"),
                orderType:JSON.stringify({type:'006'})
            }
            //调用微信支付服务器端接口
            WeiXinService.getweixinPayData($scope.param).success(function (data) {
                WeiXinService.wxchooseWXPay(data) //调起微支付接口
                    .then(function (msg) {
                        switch (msg) {
                            case "get_brand_wcpay_request:ok":
                                CommonService.toolTip("支付成功","tool-tip-message-success");
                                //调用支付后，跳转订单详情
                                sessionStorage.setItem(orderno,"");
                                var order = {orderno:orderno ,orderType:'006' ,orderStatus:""};
                                $state.go("payresult", {order: JSON.stringify(order)});
                                break;
                            default :
                                //取消或失败，停留此页面
                                break;
                        }
                    });
            });
        }
    }])
    //发表评论
    .controller('AddCommentCtrl', ['$scope', '$stateParams', '$state','$rootScope', 'WeiXinService', 'OrderService','CommonService', function ($scope, $stateParams, $state, $rootScope,WeiXinService, OrderService,CommonService) {
        $rootScope.commonService=CommonService;
        $scope.currentId=5;
        $scope.colors = [{id:1},{id:2},{id:3},{id:4},{id:5}];
        OrderService.getOrderDetailInfo({orderno: $stateParams.orderno}).success(function (data) {
            $scope.orderinfo = data.order;//订单总信息
            $scope.orderdetail = data.orderdetail;//订单详情
        });
        $scope.jsonimg=[];
        $scope.jsonmedia=[];
        $scope.localIds = [];// 上传图片的微信路径 数组
        $scope.contents = [];// 评论内容数组
        WeiXinService.mediaIds = []; //置空媒体id数组
        $scope.wxchooseImage = function (gcid) {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxchooseImage(function (localIds) {
                    $scope.images = [];//展示的图片总数
                    $scope.jsonimg.push({"gcid":  gcid ,"localId":  localIds });
                    angular.forEach($scope.jsonimg, function (item,index) {
                        if (item.gcid == gcid) {
                            for(var i=0;i<item.localId.length;i++) {
                                $scope.images.push(item.localId[i]);
                            }
                        }
                    });
                    $scope.localIds[gcid] = $scope.images;
                    $scope.jsonmedia.push({"gcid":  gcid ,"media": WeiXinService.mediaIds });
                    $scope.$apply();
                })
                WeiXinService.mediaIds = []; //置空媒体id数组
            })
        }
        //描述等级
        $scope.paint=function(id) {
            $scope.currentId = id + 1;
        }
        //提交评论
        $scope.submitcomment = function () {
            $scope.comment = [];//评论整体信息
            $scope.itemsinfo = [];//评论实体信息
            var commentinfo = {},flag=true;
            commentinfo.orderno = $stateParams.orderno;
            commentinfo.userId = localStorage.getItem("jinlele_userId");
            commentinfo.descriplevel = $scope.currentId;
            commentinfo.type="";//业务类型
            angular.forEach($scope.orderdetail.info, function (item, index) {
                var iteminfo = {};
                iteminfo.gcid = item.gcid;
                if($scope.contents[item.gcid].length==0){
                    flag=false;
                }
                iteminfo.content = $scope.contents[item.gcid];
                $scope.mediaIds = [];// 评论图片数组
                angular.forEach($scope.jsonmedia, function (mediaitem,mediaindex) {
                    for (var i = 0; i < mediaitem.media.length; i++) {
                        if(item.gcid==mediaitem.gcid){
                            $scope.mediaIds.push(mediaitem.media[i]);
                        }
                    }
                })
                iteminfo.mediaIds = $scope.mediaIds;
                $scope.itemsinfo.push(iteminfo);
            })
            commentinfo.itemsinfo=$scope.itemsinfo;
            $scope.comment.push(commentinfo);
            if(flag) {
                OrderService.AddComment($scope.comment).success(function (data) {
                    if (parseInt(data.row) > 0) {
                        CommonService.toolTip("评论成功！", "tool-tip-message-success");
                        $state.go("orderlist");
                    } else {
                        CommonService.toolTip("评论失败！", "tool-tip-message-success");
                    }
                });
            }else{
                CommonService.toolTip("请输入评论内容！","tool-tip-message-success");
            }
        }
    }])
    //会员
    .controller('MemberCtrl', ['$scope', 'MemberService', function ($scope, MemberService) {
        var opendid = localStorage.getItem("openId");
        MemberService.getUserInfo(opendid).success(function (data) {
            $scope.user = data.userInfo;
            //console.log(JSON.stringify(data));
        });
    }])

    //订单列表
    .controller('OrderListCtrl', ['$rootScope','$scope', '$state','WeiXinService', 'OrderListService', 'OrderService','CommonService',  function ($rootScope,$scope,$state, WeiXinService, OrderListService, OrderService,CommonService) {
        //通过config接口注入权限验证配置
        $rootScope.commonService=CommonService;
        $scope.type = '006';
        $scope.orderlistsinfo = [];
        $scope.page = 0;//当前页数
        $scope.total = 1;//总页数
        $scope.moreFlag = false; //是否显示加载更多
        $scope.noDataFlag = false;  //没有数据显示
        $scope.getOrderLists = function () {
            if ((arguments != [] && arguments[0] == 0) ) {
                $scope.page = 0;
                $scope.orderlistsinfo = [];
            }
            $scope.page++;
            $scope.moreFlag = false;
            $scope.noDataFlag = false;
            //分页显示
            OrderListService.getorderLists({userid: localStorage.getItem("jinlele_userId"),pagenow: $scope.page ,type:$scope.type}).success(function (data) {
                angular.forEach(data.pagingList, function (item) {
                    $scope.orderlistsinfo.push(item);
                })
                if(data.myrows == 0) $scope.noDataFlag = true;
                $scope.total = data.myrows;
                if($scope.total > $scope.orderlistsinfo.length){
                    $scope.moreFlag = true;
                    console.log("moreFlag ==" + $scope.moreFlag );
                }
            })
        }
        $scope.getOrderLists();

        $scope.cancleorder = function (orderno) {
            //修改后，重新请求数据
            OrderService.cancleOrder({orderno: orderno}).success(function (data) {
                if (parseInt(data.resultnumber) > 0) {
                    $scope.getOrderLists();
                }
            });
        }
        //确认收货
        $scope.confirmReceive = function (type,orderno){
            var orderstatus="";
            switch (type){
                case '001':
                    orderstatus='001008';
                    break;
                case '002':
                    orderstatus='002009';
                    break;
                case '003':
                    orderstatus='003008';
                    break;
                case '004':
                    orderstatus='004007';
                    break;
                case '005':
                    orderstatus='005013';
                    break;
                default ://默认商城
                    orderstatus='004';
                    break;
            }
            OrderService.update({
                orderno: orderno,
                shoporderstatuscode:orderstatus
            }).success(function (data) {
                if (data.n == 1) {
                    CommonService.toolTip("收货成功，感谢您的购买~", "tool-tip-message");
                    $state.reload();
                }
            });
        }
        //微信支付调用
        $scope.weixinPay = function (orderno, totalprice,ordertype,orderstatus) {
            $scope.param = {
                totalprice: 0.01, //totalprice
                orderNo: orderno,
                descrip: '六唯壹珠宝',
                openid: localStorage.getItem("openId"),
                orderType:JSON.stringify({type:ordertype})
            };
            //调用微信支付服务器端接口
            WeiXinService.getweixinPayData($scope.param).success(function (data) {
                WeiXinService.wxchooseWXPay(data) //调起微支付接口
                    .then(function (msg) {
                        switch (msg) {
                            case "get_brand_wcpay_request:ok":
                                CommonService.toolTip("支付成功","tool-tip-message-success");
                                //调用支付后，跳转订单详情
                                sessionStorage.setItem(orderno,"ok");
                                var order = {orderno: orderno ,orderType:ordertype ,orderStatus:orderstatus};
                                $state.go("orderdetail", {order: JSON.stringify(order)});
                                break;
                            default :
                                //未支付
                                sessionStorage.setItem(orderno,"");
                                break;
                        }
                    });
            })
        }

        //跳转到商城订单详情页
        $scope.orderdetail = function (orderno , orderType ,shoporderstatusCode) {
            var order = {orderno: orderno ,orderType:orderType ,orderStatus:shoporderstatusCode};
            console.log('orderdetail==='+JSON.stringify(order));
            $state.go("orderdetail", {order: JSON.stringify(order)});
        }
        //跳转到服务订单详情页
        $scope.servicedetail = function (orderno , orderType ,shoporderstatusCode) {
            var order = {orderno: orderno ,orderType:orderType ,orderStatus:shoporderstatusCode};
            console.log('servicedetail==='+JSON.stringify(order));
            $state.go("servicedetail", {order: JSON.stringify(order)});
        }
        //服务订单追踪
        $scope.procreceive = function (type,orderno,createTime,shoporderstatusCode,totalprice) {
            console.log('type=='+type);
            console.log('orderno=='+orderno);
            console.log('createTime=='+createTime);
            console.log('shoporderstatusCode=='+shoporderstatusCode);
            console.log('totalprice=='+totalprice);
            //分类型=>分状态=>确定进度
            switch(type){
                case "001"://翻新
                    switch (shoporderstatusCode) {
                        case "001002":
                        case "001003":
                            $state.go('procreceive', {name: type, orderNo: orderno, orderTime: createTime});//平台收货
                            break;
                        case "001004":
                            $state.go('proctest', {type: type, orderNo: orderno, orderTime: createTime});//检测
                            break;
                        case "001005":
                            $state.go('procrefurbish', {name: 'refurbish', orderNo: orderno, orderTime: createTime});//翻新
                            break;
                        case "001006":
                            $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
                            break;
                        case "001007":
                            $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
                            break;
                        case "001008":
                            $state.go('procaddcmt', {type: type, orderno: orderno});//评论
                            break;
                    }
                    break;
                case "002"://维修
                    switch (shoporderstatusCode){
                        case "002001":
                        case "002002":
                            $state.go('procfixprice', {name: type, orderno: orderno});
                            break;
                        case "002003"://确认维修(待付款)
                            sessionStorage.setItem('jinlele_procphoto_orderno', orderno);
                            sessionStorage.setItem('jinlele_procphoto_aturalprice', totalprice);
                            sessionStorage.setItem('jinlele_procphoto_pathname', 'repair');
                            $state.go('proccommitorder');
                            break;
                        case "002004"://客户发货
                        case "002012":
                            $state.go('procreceive', {name: type, orderNo: orderno, orderTime: createTime});//平台收货
                            break;
                        case "002005":
                            $state.go('proctest', {type: type, orderNo: orderno, orderTime: createTime});//检测
                            break;
                        case "002006":
                            $state.go('procrepair', {name: 'repair', orderno: orderno, orderTime: createTime});//维修
                            break;
                        case "002007":
                            $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
                            break;
                        case "002008":
                            $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
                            break;
                        case "002009":
                            $state.go('procaddcmt', {type: type, orderno: orderno});//评论
                            break;
                    }
                    break;
                case "003"://检测
                    switch (shoporderstatusCode){
                        case "003002":
                        case "003003":
                            $state.go('procreceive', {name: type, orderNo: orderno, orderTime: createTime});//平台收货
                            break;
                        case "003004":
                            $state.go('proctest', {type: type, orderNo: orderno, orderTime: createTime});//检测
                            break;
                        case "003006":
                            $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
                            break;
                        case "003007":
                            $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
                            break;
                        case "003008":
                            $state.go('procaddcmt', {type: type, orderno: orderno});//评论
                            break;
                    }
                    break;
                case "004"://回收
                    switch (shoporderstatusCode){
                        case "":
                            break;
                    }
                    break;
                default://换款
                    switch (shoporderstatusCode){
                        case "":
                            break;
                    }
                    break;
            }
            //if(type=='002') { //如果 维修订单是带确认维修或者待定价就进入 定价的页面
            //    if (shoporderstatusCode < '002003') {
            //        $state.go('procfixprice', {name: type, orderno: orderno});
            //    }
            //    if (shoporderstatusCode == '002003') { //如果是确认维修的状态   跳转到提交订单的页面
            //        sessionStorage.setItem('jinlele_procphoto_orderno', orderno);
            //        sessionStorage.setItem('jinlele_procphoto_aturalprice', totalprice);
            //        sessionStorage.setItem('jinlele_procphoto_pathname', 'repair');
            //        $state.go('proccommitorder');
            //    }
            //}
            //if(type=='001' || type=='003' || type=='004'){   //翻新检测类服务跳转到收货的页面   :name/:orderNo/:orderTime',
            //    $state.go('procreceive' ,{name:type ,orderNo:orderno ,orderTime:createTime});
            //}
        }


    }])
    //退货
    .controller('ReturnApplyCtrl', function ($scope, $stateParams) {
        $(function () {
            $('.default').dropkick();
            theme:'black'
        });
        $scope.returnApply = {returntypeCode: "", harvestCode: "", reason: "", memo: "", orderno: $stateParams.id};
        $scope.sub = function () {
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
    .controller('FavouriteCtrl', function ($scope ,GoodService , $state) {
        $scope.rmFlag = false;
        $scope.noDataFlag = false;  //暂无数据标示
        $scope.rmFid = "";
        $scope.rmIndex = 0;
        $scope.FavArr = [];
        $scope.page = 0;//当前页数
        $scope.moreDataFlag = false; //是否显示 加载更多的点击按钮
        $scope.getFavs = function () {
            $scope.noDataFlag = false;
            $scope.page++;
            GoodService.getFavs({pagenow:$scope.page, userId:localStorage.getItem("jinlele_userId")}).success(function (data) {
                if(data.myrows == 0) {
                    $scope.noDataFlag = true;
                    return;
                }
                angular.forEach(data.pagingList, function (item) {
                    $scope.FavArr.push(item);
                })
                $scope.moreDataFlag = (data.myrows > $scope.FavArr.length) ?  true : false;
            })
        }
        $scope.getFavs();
        $scope.del = function (fid,index) {
            $scope.rmFlag = true;
            $scope.rmFid = fid;
            $scope.rmIndex = index;
        }

        $scope.cancelRemove = function () {
            $scope.rmFlag = false;
        }
        $scope.confirmRemove = function () {
            GoodService.delFavourite({fid : $scope.rmFid}).success(function (data) {
                if(data && data.n == 1){
                    $scope.FavArr.splice($scope.rmIndex ,1);
                    $scope.rmFlag = false;
                }
                if($scope.FavArr.length == 0) {
                    $scope.moreDataFlag = false;
                    $scope.noDataFlag = true;
                }
            });
        }
        $scope.detail = function (gid) {
            console.log(gid);
            $state.go("gooddetail" , {id:gid});
        }

    })
    //帮助反馈
    .controller('WishCtrl',['$rootScope','$scope','$state','MemberService','CommonService',function($rootScope,$scope,$state,MemberService,CommonService){
        $rootScope.commonService=CommonService;
        $scope.content="";
        $scope.subSuggest=function(){
            if($scope.content.length==0){
                CommonService.toolTip("请填写您的建议","tool-tip-message-success");
                return;
            }
            if($scope.content.length>200){
                CommonService.toolTip("您的建议内容过长","tool-tip-message-success");
                return;
            }
            MemberService.saveWish({suggest:$scope.content,userId:localStorage.getItem("jinlele_userId")}).success(function(data){
                if(data.n>0){
                    CommonService.toolTip("您的建议已成功提交","tool-tip-message-success");
                    $state.go("member");
                }else{
                    CommonService.toolTip("提交失败，请检查您的网络","tool-tip-message-success");
                }
            });
        }

    }])
    //绑定手机号
    .controller('BindphoneCtrl',['$rootScope','$scope','$state','$timeout','$interval','CommonService','MemberService',function($rootScope,$scope,$state,$timeout,$interval,CommonService,MemberService){
        $rootScope.commonService=CommonService;
        $scope.opendisabled = false;//关闭禁用状态
        $scope.active=true;//按钮激活样式
        $scope.paracont = "获取验证码";
        $scope.user={};
        //查询是否存在手机号码，存在则不再绑定
        MemberService.getUserInfo( localStorage.getItem("openId")).success(function(data){
            $scope.user=data.userInfo;
            if(data&&data.userInfo.phone){
                $scope.showinfo=false;
            }else{
                $scope.showinfo=true;
            }
        });
        //获取验证码
        $scope.getMsgcode=function(phoneNumber){
            //倒计时
            var timerCount=60,timePromise = undefined;
            timePromise = $interval(function(){
                if(timerCount<=0){
                    $interval.cancel(timePromise);
                    timePromise = undefined;
                    $scope.paracont = "重新获取";
                    $scope.opendisabled = false;//关闭禁用状态
                    $scope.active=true;//按钮激活样式
                }else{
                    $scope.paracont = timerCount + "s后再次获取";
                    timerCount--;
                    $scope.opendisabled = true;//打开禁用状态
                    $scope.active=false;//关闭激活样式

                }
            },1000,100);
            //验证手机号码格式，发送验证码
            var reg = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
            var flag = reg.test(phoneNumber);
            if (flag){
                MemberService.getCheckcode({phonenumber:phoneNumber}).success(function(data){
                    switch (data.result){
                        case "发送失败":
                            CommonService.toolTip("获取失败，请稍后重试","tool-tip-message-success");
                            break;
                        default :
                            $scope.randomCode=data.result;
                            CommonService.toolTip("发送成功","tool-tip-message-success");
                            break;
                    }
                });
            }else{
                CommonService.toolTip("手机号码格式错误","tool-tip-message-success");
            }
        }
        //绑定手机号码
        $scope.bindingphone=function(phoneNumber,code){
            //再次校验手机号码及验证码
            var reg = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
            var flag = reg.test(phoneNumber);
            if (flag) {
                if (code == $scope.randomCode) {
                    MemberService.bindingPhoneNumber({
                        phoneNumber: phoneNumber,
                        userId: localStorage.getItem("jinlele_userId")
                    }).success(function (data) {
                        if (data.result > 0) {
                            CommonService.toolTip("绑定成功", "tool-tip-message-success");
                            $state.go("member");
                        } else {
                            CommonService.toolTip("验证码错误", "tool-tip-message-success");
                        }
                    });
                } else {
                    CommonService.toolTip("验证码错误", "tool-tip-message-success");
                }
            }else{
                CommonService.toolTip("手机号码格式错误","tool-tip-message-success");
            }
        }
    }])
    //商品列表
    .controller('GoodListCtrl', function ($scope, GoodService, $stateParams) {

        $scope.saleFlag = 1; //综合(销售)排序方式 正序还是倒序
        $scope.timeFlag = 1; //时间
        $scope.priceFlag = 1; //价格


        function  getData(querytype , flag) {
            //获取产品列表
            GoodService.getGoodList({pagenow: 1, categoryname: $stateParams.name, querytype:querytype , flag:flag}).success(function (data) {
                $scope.goodList = data;
                console.log(data);
            })
        }

        $scope.test = function (index){
            if(index == 0){
                $scope.saleFlag = $scope.saleFlag == 1 ? 0 : 1;
                getData(index ,  $scope.saleFlag);    //暂时先按照时间吧  以后需要 用销量来分
            }
            if(index == 1){
                $scope.timeFlag = $scope.timeFlag == 1 ? 0 : 1;
                getData(index ,  $scope.timeFlag);
            }
            if(index == 2){
                $scope.priceFlag = $scope.priceFlag == 1 ? 0 : 1;
                console.log("==="+$scope.priceFlag);
                getData(index , $scope.priceFlag);
            }
        }
        //页面初始加载
        getData(1 , 1);//默认按照 时间倒序


    })
    //商品详情
    .controller('GoodDetailCtrl', function ($scope, $stateParams, $rootScope, GoodService, AddtoCartService, CommonService) {
        $rootScope.commonService = CommonService;

        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true,
            autoplay: 3000
        });
        //初始化参数
        $scope.bannerurl = "";
        $scope.stocknum = 0;//库存数
        $scope.favouriteId = "";   //收藏后的id

        $scope.gooddetail = {
            userId: localStorage.getItem("jinlele_userId"),
            goodId: $stateParams.id,
            goodchildId: "",
            num: 1
        };
        GoodService.getGoodDetail({goodId: $stateParams.id, userId: $scope.gooddetail.userId}).success(function (data) {
            console.log("getGoodDetail=="+JSON.stringify(data));
            $scope.goodDetail = data.good;
            $scope.goodChilds = data.goodchilds;
            $scope.favourites = data.favourites;
            $scope.totalnum = data.totalnum;
            $scope.bannerurl = $scope.goodChilds[0].imgurl;
            $scope.stocknum = $scope.goodChilds[0].stocknumber;
            if ($scope.goodChilds && $scope.goodChilds.length > 0) {
                angular.forEach($scope.goodChilds, function (item) {
                    item.flag = false;
                });
            }
            if($scope.favourites && $scope.favourites.length>0){
                $scope.favouriteId = $scope.favourites[0].id;
            }
            $scope.favcontent = $scope.favouriteId ? '已收藏' : '加入收藏';
            console.log("$scope.goodChilds==" + JSON.stringify($scope.goodChilds));

        });
        GoodService.getGoodCommentCount({goodId: $stateParams.id}).success(function(data){
            $scope.goodcommentcount=data.total;
        });
        GoodService.getGoodComments({goodId: $stateParams.id, pagenow: 1}).success(function(data){
            $scope.goodcomments=data.comments;
        });
        $scope.addtocart = function () {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择颜色分类", "tool-tip-message");
                return;
            }
            $scope.changeNum();
            AddtoCartService.addtocart($scope.gooddetail).success(
                function (data) {
                    console.log('data===' + data);
                    CommonService.toolTip("添加成功", "tool-tip-message-success");
                }
            )
        }
        $scope.changeNum = function () {
            $scope.totalnum =  $scope.totalnum + parseInt($scope.gooddetail.num || 0);
        }
        $scope.addNum = function () {
            if($scope.gooddetail.num<$scope.stocknum){
                $scope.gooddetail.num++;
            }
        }
        $scope.minusNum = function () {
            if ($scope.gooddetail.num > 1) {
                $scope.gooddetail.num--;
            }
        }

        $scope.fav =  function () {
            //去后台收藏表 保存或删除数据
            if($scope.favouriteId){  //
                GoodService.delFavourite({fid:$scope.favouriteId}).success(function (data) {
                    if(data && data.n == 1 ){
                        $scope.favouriteId =  "";
                        CommonService.toolTip("已取消收藏", "");
                        $scope.favcontent =  '加入收藏';
                    }
                })
            }else{
                GoodService.saveFavourite({goodId:$stateParams.id , userId: localStorage.getItem("jinlele_userId")}).success(function (data) {
                    if(data && data.favouriteId ){
                        $scope.favouriteId =  data.favouriteId;
                        CommonService.toolTip("收藏成功", "");
                        $scope.favcontent =  '已收藏';
                    }
                })
            }
        }
    })
    //流程-拍照(翻新，检测，回收业务只有拍照功能，维修业务包含拍照及下单功能)
    .controller('ProcPhotoCtrl', function ($scope,ProcCommitOrderService, $stateParams, WeiXinService, $rootScope, CommonService, ProcPhotoService, $state ,CategoryService) {
        $rootScope.commonService = CommonService;
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.localflag = false;
        $scope.localIds = [];// 上传图片的微信路径 数组
        WeiXinService.mediaIds = []; //置空媒体id数组
        $scope.type = "";//服务类型 001翻新
        $scope.service = {  //服务实体
            price:0 , //价格
            descrip: ""    //描述
        };
        switch ($scope.pagetheme) {
            case "refurbish"://翻新
                ProcPhotoService.getrefurbishPrice().success(function (data) {
                    console.log("翻新价格==" + JSON.stringify(data));
                    $scope.service.price = data.code_value;
                });
                break;
            case "repair"://维修
                $scope.secondcatagories = [];
                $scope.productArr = [];
                $scope.productArr.push(0);
                $scope.product = {
                    firstCatogoryId: [],//一级分类id
                    secondCatogoryId: [], //二级分类id
                    repairItemValue:[],//维修项目
                    num: [],
                    memo: []
                };
                //$scope.localflag = true;
                //遍历一级分类
                CategoryService.getcatogories().success(function (data) {
                    $scope.firstCatogories = data.firstList;
                    console.log(JSON.stringify($scope.firstCatogories))
                });
                //根据一级分类遍历二级分类
                $scope.getSecondCatogories = function (index) {
                    console.log("index==" + index);
                    CategoryService.getSecondCatogByPid($scope.product.firstCatogoryId[index]).success(function (data) {
                        $scope.secondcatagories["s" + index] = data;
                    });
                }
                CategoryService.getRepairItem({typename:'repairitem'}).success(function(data){
                    $scope.repairItems=data.repairitem;
                });
                //计算总数量和总价格
                $scope.numblur = function () {
                    //遍历
                    $scope.totalnum = 0;
                    if ($scope.product.num.length > 0) {
                        for (var i = 0, len = $scope.product.num.length; i < len; i++) {
                            $scope.totalnum += $scope.product.num[i] * 1;
                        }
                    }
                    console.log(" $scope.totalnum ==" + $scope.totalnum);
                }
                break;
            case "detect"://检测
                ProcPhotoService.getdetectPrice().success(function (data) {
                    console.log("检测价格==" + JSON.stringify(data));
                    $scope.service.price = data.code_value;
                });
                break;
            default :
                break;
        }
        $scope.wxchooseImage = function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxchooseImage(function (localIds) {
                    $scope.localIds = localIds;
                    $scope.$apply();
                })
            })
        }
        //进入提交订单的页面
        $scope.proccommitorder = function (pagetheme) {
            //判断参数
            var len = $scope.localIds.length;
            if (len == 0) {
                CommonService.toolTip("请上传图片" ,"");
                return;
            }
            if ($scope.service.descrip == "") {
                CommonService.toolTip("请填写商品描述" ,"");
                return;
            }
            //获取根据路由获取服务类型
            $scope.typeCode = ProcCommitOrderService.getType($scope.pagetheme).code;
            //①前台去上传图片的到微信并返回媒体Id 放入集合中
            //通过config接口注入权限验证配置

            //②后台处理:拿到mediaId去后台上传图片传到服务器本地路径
            // 然后将本地图片上传到七牛并返回七牛图片url,在后台保存数据到翻新服务表 ，照片表 ，翻新服务_照片中间表
            $scope.params = {
                userId: localStorage.getItem("jinlele_userId"),
                mediaIds: WeiXinService.mediaIds,
                descrip: $scope.service.descrip,
                storeId: 1,//暂时设定门店id为1 ，以后会根据地理位置动态获取
                type: $scope.typeCode //上传类型 翻新001维修002检测003回收004换款005
            };
            switch ($scope.typeCode){
                case "002"://如果是维修，需要传入产品信息
                    $scope.params.products = JSON.stringify($scope.product);
                    $scope.params.totalnum = $scope.totalnum;
                    console.log("$scope.pagetheme =="+ $scope.pagetheme);
                    console.log("$scope.typeCode =="+ $scope.typeCode);
                    console.log(JSON.stringify($scope.params));
                    ProcPhotoService.saveRepairOrder($scope.params).success(function (data) {
                        console.log('data=='+JSON.stringify(data));
                        if (data) {
                            // var serviceId = data.serviceId;
                            // //③后台处理成功后，跳转到下单页面
                            // sessionStorage.setItem("jinlele_procphoto_pathname", pagetheme);
                            // sessionStorage.setItem("jinlele_procphoto_serviceId", data.serviceId);
                            // sessionStorage.setItem("jinlele_procphoto_orderno", data.orderno);
                            $state.go("procfixprice",{
                                name:$scope.pagetheme,
                                orderno:data.orderno
                            });
                        }
                    });
                    break;
                default://如果是翻新和检查 需要传入价格   001,003,004,005
                    $scope.params.aturalprice = $scope.service.price;
                    console.log("$scope.pagetheme =="+ $scope.pagetheme);
                    console.log("$scope.typeCode =="+ $scope.typeCode);
                    console.log(JSON.stringify($scope.params));
                    ProcPhotoService.saveService($scope.params).success(function (data) {
                        console.log(data.serviceId);
                        if (data) {
                            var serviceId = data.serviceId;
                            //③后台处理成功后，跳转到下单页面
                            sessionStorage.setItem("jinlele_procphoto_pathname", $scope.pagetheme);
                            sessionStorage.setItem("jinlele_procphoto_serviceId", data.serviceId);
                            sessionStorage.setItem("jinlele_procphoto_aturalprice", $scope.service.price);
                            $state.go("proccommitorder");
                        }
                    });
                    break;
            }
        }
    })
    //流程- 提交订单(翻新，检测，回收业务生成订单并付款，维修业务更新订单信息)
    .controller('ProcCommitOrderCtrl', function ( $rootScope,$scope, $state, AddressService, OrderService, $stateParams, $window, ProcCommitOrderService, WeiXinService, CategoryService ,CommonService) {
        $scope.pagetheme = sessionStorage.getItem("jinlele_procphoto_pathname");
        $scope.serviceId = sessionStorage.getItem("jinlele_procphoto_serviceId");
        $scope.aturalprice = sessionStorage.getItem("jinlele_procphoto_aturalprice") || 0;
        $scope.orderno = sessionStorage.getItem("jinlele_procphoto_orderno");
        if($scope.pagetheme == 'repair'){
            $scope.totalprice =  $scope.aturalprice;
            console.log('$scope.totalprice==' + $scope.totalprice);
        }
        console.log('$scope.orderno=='+$scope.orderno);
        $rootScope.commonService = CommonService;
        $scope.type = ProcCommitOrderService.getType($scope.pagetheme); //根据路由获取服务类型
        $scope.showaddr = $scope.pagetheme == 'recycle' ? false : true;
        $scope.address = {};
        $scope.show = false; //用户控制地址显示
        $scope.order = {
            storeId: "",
            sendway: "001",
            getway: "001",
            totalprice: 0
        };
        $scope.addsendway = function (val) {
            $scope.sendwayFlag = !$scope.sendwayFlag;
            $scope.order.sendway = val;
        }
        $scope.addgetway = function (val) {
            $scope.getwayFlag = !$scope.getwayFlag;
            $scope.order.getway = val;
        }
        $scope.goback = function () {
            $window.history.back();
        }
        $scope.sendwayFlag = false;//寄件方式切换
        $scope.getwayFlag = false; //取件方式切换
        $scope.sendwayValue = ['001', '002'];//寄件取件方式值
        $scope.getwayValue = ['001', '002'];//寄件取件方式值

        $scope.secondcatagories = [];
        $scope.productArr = [];
        $scope.product = {
            firstCatogoryId: [],//一级分类id
            secondCatogoryId: [], //二级分类id
            num: [],
            memo: []
        };

        //从数据库获取地址
        AddressService.getlatestinfo({userid: localStorage.getItem("jinlele_userId")}).success(function (data) {
            $scope.address = data;
            if (data) {
                $scope.show = true;
            }
        });
        //微信内置添加地址，弹出获取地址的页面
        $scope.wxopenAddress = function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxopenAddress($scope);
            })
        }
        //遍历门店
        ProcCommitOrderService.findAllStores().success(function (data) {
            $scope.stores = data;
            console.log(JSON.stringify(data));
        });

        //遍历一级分类
        CategoryService.getcatogories().success(function (data) {
            $scope.firstCatogories = data.firstList;
            console.log(JSON.stringify($scope.firstCatogories))
        });
        //根据一级分类遍历二级分类
        $scope.getSecondCatogories = function (index) {
            console.log("index==" + index);
            CategoryService.getSecondCatogByPid($scope.product.firstCatogoryId[index]).success(function (data) {
                $scope.secondcatagories["s"+index]= data;
            });
        }
        //添加产品
        var i = 0;
        $scope.addProduct = function () {
            $scope.productArr.push(i++);
        }
        //计算总数量和总价格
        $scope.numblur = function () {
            //遍历
            $scope.totalnum = 0;
            if ($scope.product.num.length > 0) {
                for (var i = 0, len = $scope.product.num.length; i < len; i++) {
                    $scope.totalnum += $scope.product.num[i] * 1;
                }
            }
            var price = $scope.aturalprice;

            $scope.totalprice = $scope.totalnum * price;
            console.log(" $scope.totalprice ==" + $scope.totalprice);
        }

        //生成订单并付款
        $scope.procreceive = function (flag) {
            if($scope.type.code!='002'&&(!flag && ($scope.type.code!='004'))){ //如果是翻新 检测 换款 维修
                CommonService.toolTip("还有未填写的信息", "");
                return;
            }
            //提交信息
            $scope.confirminfo = [];
            //地址信息
            $scope.addressinfo = [];
            if($scope.type.code == '001' || $scope.type.code == '003' || $scope.type.code == '004' || $scope.type.code == '005'){  //如果是翻新和检测需要传入产品信息
                var address = {};
                address.userName = $scope.address.userName;
                address.postalCode = $scope.address.postalCode;
                address.provinceName = $scope.address.provinceName;
                address.cityName = $scope.address.cityName;
                address.countryName = $scope.address.countryName;
                address.detailInfo = $scope.address.detailInfo;
                address.nationalCode = $scope.address.nationalCode;
                address.telNumber = $scope.address.telNumber;
                $scope.addressinfo.push(address);
                var obj = {};
                obj.userId = localStorage.getItem("jinlele_userId");//用户id
                obj.type = $scope.type.code;    //翻新001维修002检测003回收004换款005
                obj.storeId = 1;//后续需要根据客户选择传入
                obj.sendWay=$scope.order.sendway;     //送货方式
                obj.getWay=$scope.order.getway;    //取货方式
                obj.totalprice = $scope.totalprice;//总价格
                obj.addressinfo = $scope.addressinfo;//地址信息
                obj.serviceId = $scope.serviceId;//服务id
                obj.totalnum = $scope.totalnum;//总数量
                obj.products = $scope.product;//产品集合
                $scope.confirminfo.push(obj);
                console.log(JSON.stringify($scope.confirminfo));
                //保存订单 并去支付订单
                ProcCommitOrderService.createServiceOrder($scope.confirminfo).success(function (data) {
                    if (data) {
                        //调用支付接口
                        var orderno = data.orderNo;
                        var orderTime = data.orderTime;
                        if($scope.type.code == '004'){   //如果是回收订单无需付款，直接进入平台收货页面
                            $state.go('procreceive',{name:'recycle',orderNo:data.orderNo,orderTime:data.orderTime});
                            return;
                        }
                        if($scope.type.code == '005'){   //如果是回收订单无需付款，直接进入平台收货页面
                            $state.go('procreceive',{name:'exchange',orderNo:data.orderNo,orderTime:data.orderTime});
                            return;
                        }
                        $scope.param = {
                            totalprice: 0.01, //data.totalprice
                            orderNo: data.orderNo,
                            descrip: '六唯壹珠宝',
                            openid: localStorage.getItem("openId"),
                            orderType:JSON.stringify({type:$scope.type.code})
                        }
                        //调用支付接口
                        console.log(JSON.stringify($scope.param));
                        //微信支付调用
                        WeiXinService.getweixinPayData($scope.param).success(function (data) {
                            WeiXinService.wxchooseWXPay(data)
                                .then(function (msg) {
                                    switch (msg) {
                                        case "get_brand_wcpay_request:ok":
                                            CommonService.toolTip("支付成功","tool-tip-message-success");
                                            //支付成功，跳转订单详情
                                            sessionStorage.setItem($scope.param.orderNo ,"ok");
                                            var order = {orderno: $scope.param.orderNo ,orderType:$scope.type.code ,orderStatus:""};
                                            $state.go("servicedetail", {order: JSON.stringify(order)});
                                            break;
                                        default :
                                            //未支付，跳转支付进度
                                            sessionStorage.setItem($scope.param.orderNo,"");
                                            var order = {orderno:$scope.param.orderNo ,orderType:$scope.type.code ,orderStatus:""};
                                            $state.go("payresult", {order: JSON.stringify(order)});
                                            break;
                                    }
                                });
                        });
                    }
                });
            }
            //如果是维修订单
            if($scope.type.code == '002'){
                var address = {};
                address.userName = $scope.address.userName;
                address.postalCode = $scope.address.postalCode;
                address.provinceName = $scope.address.provinceName;
                address.cityName = $scope.address.cityName;
                address.countryName = $scope.address.countryName;
                address.detailInfo = $scope.address.detailInfo;
                address.nationalCode = $scope.address.nationalCode;
                address.telNumber = $scope.address.telNumber;
                $scope.addressinfo.push(address);
                var obj = {};
                obj.userId = localStorage.getItem("jinlele_userId");//用户id
                obj.type = $scope.type.code;    //翻新001维修002检测003回收004换款005
                obj.storeId = 1;//后续需要根据客户选择传入
                obj.sendWay=$scope.order.sendway;     //送货方式
                obj.getWay=$scope.order.getway;    //取货方式
                obj.totalprice = $scope.totalprice;//总价格
                obj.addressinfo = $scope.addressinfo;//地址信息
                obj.orderno = $scope.orderno;
                $scope.confirminfo.push(obj);
                console.log(JSON.stringify($scope.confirminfo));
                //保存订单 并去支付订单
                ProcCommitOrderService.updateRepair($scope.confirminfo).success(function (data) {
                    console.log('data='+JSON.stringify(data))
                    if (data) {
                        //调用支付接口
                        var orderno = data.orderNo;
                        var orderTime = data.orderTime;
                        $scope.param = {
                            totalprice: 0.01, //data.totalprice
                            orderNo: data.orderNo,
                            descrip: '六唯壹珠宝',
                            openid: localStorage.getItem("openId"),
                            orderType:JSON.stringify({type:$scope.type.code})
                        }
                        //调用支付接口
                        console.log(JSON.stringify($scope.param));
                        //微信支付调用
                        WeiXinService.getweixinPayData($scope.param).success(function (data) {
                            WeiXinService.wxchooseWXPay(data)
                                .then(function (msg) {
                                    switch (msg) {
                                        case "get_brand_wcpay_request:ok":
                                            CommonService.toolTip("支付成功","tool-tip-message-success");
                                            //支付成功，跳转订单详情
                                            sessionStorage.setItem($scope.param.orderNo ,"ok");
                                            var order = {orderno: $scope.param.orderNo ,orderType:$scope.type.code ,orderStatus:""};
                                            $state.go("servicedetail", {order: JSON.stringify(order)});
                                            break;
                                        default :
                                            //未支付，跳转支付进度
                                            sessionStorage.setItem($scope.param.orderNo,"");
                                            var order = {orderno:$scope.param.orderNo ,orderType:$scope.type.code ,orderStatus:""};
                                            $state.go("payresult", {order: JSON.stringify(order)});
                                            break;
                                    }
                                });
                        });
                    }
                });
            }
        }
    })
    //流程-平台收货(五大类服务展示物流状态及收货证明)
    .controller('ProcReceiveCtrl', function ($scope, $stateParams , OrderService ,CommonService ,$rootScope) {
        $rootScope.commonService = CommonService;
        $scope.pagetheme = $stateParams.name;
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;

        if($stateParams.name == '001')  $scope.pagetheme = 'refurbish';
        if($stateParams.name == '002')  $scope.pagetheme = 'repair';
        if($stateParams.name == '003')  $scope.pagetheme = 'detect';
        if($stateParams.name == '004')  $scope.pagetheme = 'recycle';
        if($stateParams.name == '005')  $scope.pagetheme = 'exchange';
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="retrofit";
        $scope.jinflag=false;
        $scope.myflag=true;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        //参数
        $scope.order = {
            userlogisticsnoComp:"",//买方发货快递公司编码
            userlogisticsno:"", //买方发货单号
            orderstatus:""//订单状态
        };
        $scope.tracking = $stateParams.name == 'recycle' ? true : false;
        //去后台查询请求数据
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.initData = data.order;
            $scope.expressArr = data.express;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //客户填写物流单号保存
        $scope.saveExpress = function () {
            if (!$scope.order.userlogisticsno) {
                CommonService.toolTip("请填写物流单号", "");
                return;
            }
            //根据业务类型判断订单状态
            switch ($stateParams.name){
                case "002"://todo 维修
                    $scope.order.orderstatus="";
                    break;
                case "004"://todo 回收
                    $scope.order.orderstatus="";
                    break;
                case "005"://todo 换款
                    $scope.order.orderstatus="";
                    break;
                default://翻新、检测
                    $scope.order.orderstatus=$stateParams.name+"003";
                    break;
            }
            //'003'代表的订单状态:已发货
            OrderService.update({
                orderno: $scope.orderNo,
                userlogisticsnocomp: $scope.order.userlogisticsnoComp,
                userlogisticsno: $scope.order.userlogisticsno,
                shoporderstatuscode:$scope.order.orderstatus
            }).success(function (data) {
                console.log("data==" + JSON.stringify(data));
                if (data.n == 1) {
                    $scope.initData.userlogisticsno = $scope.order.userlogisticsno;
                }
            });
        }
    })
    //流程-检测(五大类服务检测报告)
    .controller('ProcTestCtrl',['$scope', '$stateParams','OrderService','MemberService', function ($scope, $stateParams,OrderService,MemberService) {
        console.log($stateParams.type);
        $scope.pagetheme = $stateParams.type;
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;
        switch ($stateParams.type){
            case '001':
                $scope.pagetheme = 'refurbish';
                break;
            case '002':
                $scope.pagetheme = 'repair';
                break;
            case '003':
                $scope.pagetheme = 'detect';
                break;
            case '004':
                $scope.pagetheme = 'recycle';
                break;
            case '005':
                $scope.pagetheme = 'exchange';
                break;
        }
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="hide";
        $scope.jinflag=false;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$scope.orderNo}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
    }])
    //流程-邮寄(五大类服务返回产品物流)
    .controller('ProcPostCtrl', ['$scope', '$stateParams', '$location','OrderService', function ($scope, $stateParams, $location,OrderService) {
        console.log($stateParams.type);
        $scope.pagetheme = $stateParams.type;
        if($stateParams.type == '001')  $scope.pagetheme = 'refurbish';
        if($stateParams.type == '002')  $scope.pagetheme = 'repair';
        if($stateParams.type == '003')  $scope.pagetheme = 'detect';
        if($stateParams.type == '005')  $scope.pagetheme = 'exchange';
        if ($stateParams.type == "004") {//回收
            $location.path("/");
        }
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;
        //物流样式展示
        $scope.jinlele="retrofit";
        $scope.mine="hide";
        $scope.jinflag=true;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
    }])
    //流程-验货(五大类服务用户收货验收)
    .controller('ProcCheckCtrl',['$scope', '$stateParams', '$location','OrderService', function ($scope, $stateParams, $location,OrderService) {
        console.log($stateParams.type);
        $scope.pagetheme = $stateParams.type;
        if($stateParams.type == '001')  $scope.pagetheme = 'refurbish';
        if($stateParams.type == '002')  $scope.pagetheme = 'repair';
        if($stateParams.type == '003')  $scope.pagetheme = 'detect';
        if($stateParams.type == '005')  $scope.pagetheme = 'exchange';
        if ($stateParams.type == "004") {//回收
            $location.path("/");
        }
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="hide";
        $scope.jinflag=false;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        //加载发货证明图片
        OrderService.getPostImg({orderno:$scope.orderNo}).success(function(data){
            $scope.images=data.image;
        });
    }])
    //流程-评价(五大类服务交易结束)
    .controller('ProcAddCmtCtrl',['$rootScope','$scope','$stateParams','$state','CommonService', 'WeiXinService','OrderService',function ($rootScope,$scope, $stateParams,$state,CommonService,WeiXinService,OrderService) {
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="hide";
        $scope.jinflag=false;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        $rootScope.commonService=CommonService;
        //服务名
        if($stateParams.type == '001')  $scope.pagetheme = 'refurbish';
        if($stateParams.type == '002')  $scope.pagetheme = 'repair';
        if($stateParams.type == '003')  $scope.pagetheme = 'detect';
        if($stateParams.type == '004')  $scope.pagetheme = 'recycle';
        if($stateParams.type == '005')  $scope.pagetheme = 'exchange';
        $scope.orderno = $stateParams.orderno;//订单号
        //物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderno}).success(function (data) {
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        //描述等级
        $scope.currentId=5;
        $scope.colors = [{id:1},{id:2},{id:3},{id:4},{id:5}];
        $scope.paint=function(id) {
            $scope.currentId = id + 1;
        }
        OrderService.getOrderDetailInfo({orderno: $stateParams.orderno}).success(function (data) {
            $scope.orderinfo = data.order;//订单总信息
            $scope.servicedetail = data.servicedetail;//地址详情
            $scope.address = data.address;//地址详情
        });
        $scope.content="";//评论内容
        //图片上传
        $scope.jsonimg=[];
        $scope.jsonmedia=[];
        $scope.localIds = [];// 上传图片的微信路径 数组
        WeiXinService.mediaIds = []; //置空媒体id数组
        $scope.wxchooseImage = function () {
            //通过config接口注入权限验证配置
            WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
            //通过ready接口处理成功验证
            wx.ready(function () {
                WeiXinService.wxchooseImage(function (localIds) {
                    $scope.jsonimg.push({"localId":  localIds });
                    $scope.images = [];//展示的图片总数
                    angular.forEach($scope.jsonimg, function (item,index) {
                        if (angular.isArray(item.localId)) {
                            for(var i=0;i<item.localId.length;i++) {
                                $scope.images.push(item.localId[i]);
                            }
                        }else{
                            $scope.images.push(item.localId);
                        }
                    });
                    $scope.localIds = $scope.images;
                    $scope.jsonmedia.push({"media": WeiXinService.mediaIds });
                    $scope.$apply();
                })
                WeiXinService.mediaIds = []; //置空媒体id数组
            })
        }
        //提交评论
        $scope.addcomment=function(){
            //一条评论  多张图片  orderno
            $scope.comment = [];//评论整体信息
            $scope.itemsinfo = [];//评论实体信息
            var commentinfo = {};
            commentinfo.orderno = $stateParams.orderno;
            commentinfo.userId = localStorage.getItem("jinlele_userId");
            commentinfo.descriplevel = $scope.currentId;//描述等级
            commentinfo.type=$stateParams.type;//业务类型
            var flag=true,iteminfo = {};
            iteminfo.content = $scope.content;
            if(iteminfo.content.length==0){
                flag=false;
            }
            $scope.mediaIds = [];// 评论图片数组
            angular.forEach($scope.jsonmedia, function (mediaitem,mediaindex) {
                if(angular.isArray(mediaitem.media)){
                    for (var i = 0; i < mediaitem.media.length; i++) {
                        $scope.mediaIds.push(mediaitem.media[i]);
                    }
                }else{
                    $scope.mediaIds.push(mediaitem.media);
                }
            })
            iteminfo.mediaIds = $scope.mediaIds;
            $scope.itemsinfo.push(iteminfo);
            commentinfo.itemsinfo=$scope.itemsinfo;
            $scope.comment.push(commentinfo);
            if(flag) {
                OrderService.AddComment($scope.comment).success(function (data) {
                    if (parseInt(data.row) > 0) {
                        CommonService.toolTip("评论成功！", "tool-tip-message-success");
                        $state.go("orderlist");
                    } else {
                        CommonService.toolTip("评论失败！", "tool-tip-message-success");
                    }
                });
            }else{
                CommonService.toolTip("请输入评论内容！","tool-tip-message-success");
            }
        }
    }])
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
    //翻新-翻新
    .controller('ProcRefurbishCtrl',['$scope', '$stateParams', '$location','OrderService','MemberService', function ($scope, $stateParams, $location,OrderService,MemberService) {
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        if ($stateParams.name != "refurbish") {
            $location.path("/");
        }
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="hide";
        $scope.jinflag=false;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$scope.orderNo}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$scope.orderNo}).success(function(data){
            if(data.checkreport) {
                $scope.report = data;
            }else{
                $scope.report = null;
            }
        });
    }])
    //维修-定价
    .controller('ProcFixpriceCtrl', function ($scope, $stateParams ,OrderService ,$state ,CommonService) {
        console.log('$stateParams===' + JSON.stringify($stateParams));
        $scope.pagetheme = $stateParams.name;
        if($stateParams.name == '002') $scope.pagetheme = 'repair';
        $scope.fixPrice = 0;
        //根据订单号查询是否已经定价
        OrderService.selectRepairPrice({orderNo:$stateParams.orderno}).success(function (data){
             console.log(JSON.stringify(data));
             if(data && data.fixPrice){
                 $scope.fixPrice = data.fixPrice;
             }
        });
        //放弃维修 修改订单状态，然后进入跳转到订单详情页 ， 暂时跳转到 订单列表
        $scope.drop = function () {
            OrderService.update({orderno:$stateParams.orderno,shoporderstatuscode:'002011'}).success(function (data) {
                 if(data && data.n==1){
                     CommonService.toolTip('订单已经已经取消','');
                     setTimeout(function () {
                         $state.go('orderlist');
                     },1000);
                 }
            });
        }
        //确认维修
        //href="#/proccommitorder/{{pagetheme}}"
        $scope.commit = function () {
            OrderService.update({orderno:$stateParams.orderno,shoporderstatuscode:'002003'}).success(function (data) {
                if(data && data.n==1){
                    sessionStorage.setItem('jinlele_procphoto_orderno',$stateParams.orderno);
                    sessionStorage.setItem('jinlele_procphoto_aturalprice',$scope.fixPrice);
                    sessionStorage.setItem('jinlele_procphoto_pathname','repair');
                    $state.go('proccommitorder');
                }
            });
        }
    })
    //维修-维修
    .controller('ProcRepairCtrl',['$scope', '$stateParams','OrderService','MemberService',  function ($scope, $stateParams,OrderService,MemberService) {
        $scope.pagetheme = $stateParams.name;
        $scope.orderno = $stateParams.orderno;
        $scope.orderTime = $stateParams.orderTime;
        if ($stateParams.name != "repair") {
            $location.path("/");
        }
        //物流样式展示
        $scope.jinlele="hide";
        $scope.mine="hide";
        $scope.jinflag=false;
        $scope.myflag=false;
        $scope.showwuliuInfo=function(index){
            switch (index){
                case 0:
                    $scope.jinflag=true;
                    if($scope.myflag)$scope.myflag=false;
                    $scope.jinlele="retrofit";
                    $scope.mine="hide";
                    break;
                case 1:
                    $scope.myflag=true;
                    if($scope.jinflag)$scope.jinflag=false;
                    $scope.jinlele="hide";
                    $scope.mine="retrofit";
                    break;
            }
        }

        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderno}).success(function (data) {
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$scope.orderno}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$scope.orderno}).success(function(data){
            if(data.checkreport) {
                $scope.report = data;
            }else{
                $scope.report = null;
            }
        });

    }])
    //回收-估价
    .controller('EvaluationCtrl', function ($scope ,$stateParams) {
         $scope.name = $stateParams.name;
         console.log('$scope.name ==' + $scope.name);
    })
    //回收--估价结果页面
    .controller('EvaluationresultCtrl' , function ($scope , $stateParams) {
        $scope.name = $stateParams.name;
        console.log('$scope.name ==' + $scope.name);
    })
    //换款
    .controller('ExchangCtrl', function ($scope) {

    })

