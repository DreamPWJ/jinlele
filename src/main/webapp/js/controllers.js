angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        $httpProvider.interceptors.push('MyInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('MainCtrl', ['$scope', '$rootScope', 'CommonService', 'MainService', 'WeiXinService', '$ionicScrollDelegate', 'CartService','$state',function ($scope, $rootScope, CommonService, MainService, WeiXinService, $ionicScrollDelegate,CartService,$state) {
        $scope.rightFlag = false;//右侧栏控制，true显示 false不显示
        function getBanners(arr) {
            var html = "";
            if(arr){
                for(var i=0,len=arr.length;i<len;i++){
                    html += "<li class='swiper-slide'><a href=''><img src='"+arr[i].imgurl+"'  height='188'></a></li>";
                }
            }
            $(".banner .swiper-wrapper").html(html);
            var swiper = new Swiper('.banner', {
                pagination: '.spot',
                paginationClickable: true,
                autoplay: 3000
            });
        }

        //加载此页面的时候
        //自动读取网页授权接口获取用户的opendId,从而得到用户的信息，得到前台用户的id，这里暂时强制设定用户的id
        //localStorage.setItem("jinlele_userId", 1); //1应该是从数据库中查到的

        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
            getBanners(data.banners);
            //console.log(JSON.stringify(data.banners));
            localStorage.setItem("openId",localStorage.getItem("openId")?localStorage.getItem("openId"): data.openId);//缓存微信用户唯一标示openId
            localStorage.setItem("jinlele_userId",localStorage.getItem("jinlele_userId")?localStorage.getItem("jinlele_userId"): data.userId);//缓存微信用户唯一标示 userId
        }).then(function () {
            //是否是微信 初次获取签名 获取微信签名
            if (WeiXinService.isWeiXin()) {
                // 获取微信签名
                $scope.wxparams = {
                    url: location.href.split('#')[0] //当前网页的URL，不包含#及其后面部分
                };
                WeiXinService.getWCSignature($scope.wxparams).success(function (data) {
                    localStorage.setItem("timestamp", data.timestamp);//生成签名的时间戳
                    localStorage.setItem("noncestr", data.nonceStr);//生成签名的随机串
                    localStorage.setItem("signature", data.signature);//生成签名
                    //通过config接口注入权限验证配置
                    WeiXinService.weichatConfig(data.timestamp, data.nonceStr, data.signature);
                })
            }
        });
        //主页搜索
        $scope.search = '';//搜索内容
        $scope.searchquery = function (searchcontent) {
            $scope.search = searchcontent;
            $scope.getNewProducts();
        };

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
                console.log("首页新品推荐");
                console.log(data);
                $scope.search = '';//清空搜索条件
                var index = 0;
                angular.forEach(data.pagingList, function (item) {
                    if($scope.page==1) index++;
                    if(($scope.page==1 && index > 3) || $scope.page!=1 ) {
                        $scope.newProductsinfo.push(item);
                    }
                });
                $scope.total = data.myPageCount;
                console.log(data);
                $ionicScrollDelegate.resize();//解决添加数据后页面不能及时滚动刷新造成卡顿
            }).finally(function () {
                $scope.$broadcast('scroll.refreshComplete');
                $scope.$broadcast('scroll.infiniteScrollComplete');
            })
        };
        $scope.getNewProducts();
        CartService.getCartTotalNum({userid: localStorage.getItem("jinlele_userId")}).success(function(data){
            $scope.cartTotalNum=data.totalnum;
        });

        //跳转到goodlist页面
        $scope.goolist = function (name) {
            $state.go("goodlist",{name:name});
        }
    }])


    .controller('AuthCtrl', ['$scope',function ($scope){

    }])

    //分类tab
    .controller('CategoryCtrl',['$scope','$state', '$stateParams', '$window', 'CategoryService', 'ResizeService','CartService', function ($scope,$state, $stateParams, $window, CategoryService, ResizeService,CartService) {
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
        CategoryService.getCategories().success(function (data) {
            $scope.catogory = data.firstList;
        });
        //初始加载二级分类和下面的产品列表
        $scope.init.getSecondCatogories($stateParams.id);
        $scope.catogoryid = $stateParams.id;
        CartService.getCartTotalNum({userid: localStorage.getItem("jinlele_userId")}).success(function(data){
            $scope.cartTotalNum=data.totalnum;
        });
        $scope.detail=function(gid){
            $state.go("gooddetail" , {id:gid});
        }
    }])


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
            //console.log(data);
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
                    $scope.totalprice += parseInt(data.num) * data.price;
                });
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
            });
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
                    $scope.totalprice += parseInt(data.num) * data.price;
                }
            });
            $scope.delFlag = $scope.totalprice ?   true : false; //控制删除按钮是否显示


        };
        //删除
        $scope.del = function () {
            if ($scope.checkedGcIds.length > 0) {
                $scope.delstyle = {};
            }
        };
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
        };
        //取消删除
        $scope.cancle = function () {
            $scope.delstyle = {display: 'none'};
        };
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
                    $scope.totalprice += parseInt(item.num) * item.price;
                }
            }
        };


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
                    $scope.totalprice += parseInt(item.num) * item.price;
                }
            }
        };
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
                    $scope.totalprice += parseInt(item.num) * item.price;
                }
            }
        };
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
    //换款购物车
    .controller('BarterCartCtrl', ['$scope', 'CartService', 'CommonService', '$state', '$rootScope','WalletService', 'EvaluateService',function ($scope, CartService, CommonService, $state, $rootScope,WalletService,EvaluateService) {
        $rootScope.commonService=CommonService;
        $scope.init = {
            serviceid: localStorage.getItem("barterServiceId"),
            pagenow: 1
        };
        $scope.barterprice = 0;
        $scope.balance = 0;//账户余额
        WalletService.getBalance({userId: localStorage.getItem("jinlele_userId")}).success(function (data) {
            $scope.balance = data.balance;
        });
        $scope.delstyle = {display: 'none'};
        $scope.checkflag = false;//默认复选框不选中
        $scope.checkAllflag = false;
        var selectAllCount  = 0; //控制全选的计数器
        CartService.getBarterCartInfo($scope.init).success(function (data) {
            console.log(data);
            $scope.isNotData = false;
            if (data.pagingList.length == 0) {
                $scope.isNotData = true;
                return;
            }
            $scope.cartlist = data;
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                if(data.checked == 1){
                    selectAllCount++;
                    data.checkflag = true;  //该子商品初始是否选中状态
                }else{
                    data.checkflag = false; //该子商品初始是否选中状态
                }
            });
            if(selectAllCount == $scope.cartlist.pagingList.length){
                $scope.checkAllflag = true;
            }
            console.log('$scope.checkAllflag==' +  $scope.checkAllflag);
            $scope.barterprice =data.pagingList[0].price; //估价价格

            $scope.totalprice = -$scope.barterprice;
        });
        //初始化数据
        $scope.totalnum = 0;
        $scope.totalprice = 0;
        $scope.m = [];
        $scope.checkedGcIds = [];
        $scope.checkedinfo = [];
        $scope.delFlag = false; //删除按钮默认不显示 选择了商品后才显示

        $scope.cartotalprice =0;

        EvaluateService.getShopcharTotal({serviceId: localStorage.getItem("barterServiceId")}).success(function (data) {
            if(data.echeck){
                $scope.cartotalprice = data.echeck.cartotalprice; // 选中的商品的总价格
                $scope.totalnum = data.echeck.cartotalnum; // 选中的商品的总价格
                if($scope.totalnum >0) $scope.delFlag = true;
            }
            $scope.totalprice =  $scope.cartotalprice - $scope.barterprice;//预选合计总金额
            console.log( $scope.totalprice);
        });


        //全选
        $scope.selectAll = function () {
            $scope.checkAllflag = !$scope.checkAllflag;
            // //去除重复，记录最后一遍数据
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            if ($scope.checkAllflag) {
                angular.forEach($scope.cartlist.pagingList, function (data, index) {
                    data.checkflag = true;//选中
                    $scope.totalnum += parseInt(data.num);
                    $scope.totalprice += parseInt(data.num) * data.exprice;
                });
                $scope.totalprice=$scope.totalprice-$scope.barterprice;
                $scope.delFlag = true;
            } else {
                angular.forEach($scope.cartlist.pagingList, function (data, index) {
                    data.checkflag = false;//不选中
                });
                $scope.delFlag = false;
                $scope.totalnum = 0;
                $scope.totalprice= 0 - $scope.barterprice;
            }
        };

        //单选事件
        $scope.selectOne = function (id) {
            $scope.totalnum = 0;//去除重复，记录最后一遍数据
            $scope.totalprice = 0;
            $scope.delFlag = false;//  //控制删除按钮是否显示
            var selectAllCount  = 0; //控制全选的计数器
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                if(data.gcid == id) data.checkflag = !data.checkflag ;
                if (data && data.checkflag) {
                    $scope.totalnum += parseInt(data.num);
                    $scope.totalprice += parseInt(data.num) * data.exprice;
                    $scope.delFlag = true;
                    selectAllCount++;
                }
            });
             $scope.totalprice=$scope.totalprice-$scope.barterprice;
            if(selectAllCount == $scope.cartlist.pagingList.length){
                $scope.checkAllflag = true;
            }else{
                $scope.checkAllflag = false;
            }

        };

        //点击更新数量
        $scope.updateamount = function (id,count) {
            console.log(1);
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    item.num = parseInt(item.num) + count;//这里可以增加上下限制
                    if (item.num < 1) item.num = 1;
                    if (parseInt(item.num) > item.stocknumber){
                        item.num = item.stocknumber;
                        CommonService.toolTip("已经是该商品最大库存数了奥！","");

                    }
                }
                if (item && item.checkflag) {
                    $scope.totalnum += parseInt(item.num);
                    $scope.totalprice += parseInt(item.num) * item.exprice;
                }
            }
            $scope.totalprice=$scope.totalprice-$scope.barterprice;
        };


        //手改更新数量
        $scope.changenamount = function (id) {
            console.log(22);
            $scope.totalnum = 0;
            $scope.totalprice = 0;
            for (var i = 0; i < $scope.cartlist.pagingList.length; i++) {
                var item = $scope.cartlist.pagingList[i];
                if (item.gcid == id) {
                    if (!/^\+?[1-9][0-9]*$/.test(item.num)) {
                        item.num = 1;
                    }
                    if (parseInt(item.num) > item.stocknumber) {
                        item.num = item.stocknumber;
                        CommonService.toolTip("已经是该商品最大库存数了哦！","");

                    }
                }
                if (item && item.checkflag) {
                    $scope.totalnum += parseInt(item.num);
                    $scope.totalprice += parseInt(item.num) * item.exprice;
                }
            }
            if($scope.totalprice>0){
                $scope.totalprice=$scope.totalprice-$scope.barterprice;
            }
        };

        //结算
        $scope.bill = function () {
            if($scope.totalnum<=0){
                CommonService.toolTip("还没有要结算的商品！","");
                return;
            }
            //将选中的商品id传到后台，更改checked为1
            $scope.checkedinfo = [];
            angular.forEach($scope.cartlist.pagingList, function (data, index) {
                var obj = {};
                if (data) {
                    obj.checked = data.checkflag ? 1 : 0 ;
                    obj.id = data.id;
                    obj.num = data.num;
                    obj.serviceId = localStorage.getItem("barterServiceId");
                }
                $scope.checkedinfo.push(obj);
            });
            CartService.selectBarterCartInfo($scope.checkedinfo).success(function(data) {
                console.log("结算返回数据");
                console.log(data);
                if (data ) {
                    if(data.errmsg == "ok"){
                        $state.go("procphoto", {name: "exchange"});//跳转拍照页
                    }else{
                        CommonService.toolTip("结算失败！","");
                    }

                }
            });

        };
        //删除
        $scope.del = function () {
            if ($scope.checkedGcIds.length > 0) {
                $scope.delstyle = {};
            }
        };
        //确认删除
        $scope.confirm = function () {
            console.log($scope.checkedGcIds);
            $scope.delstyle = {display: 'none'};
            CartService.delBarterCartInfo($scope.checkedinfo).success(function (data) {
                CartService.getBarterCartInfo($scope.init).success(function (data) {
                    $scope.isNotData = false;
                    if (data.pagingList.length == 0) {
                        $scope.isNotData = true;
                        return;
                    }
                    $scope.cartlist = data;
                });
            })
        };
        //取消删除
        $scope.cancle = function () {
            $scope.delstyle = {display: 'none'};
        };
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
            $scope.totalprice += parseInt($scope.selectinfo[i].num) * $scope.selectinfo[i].price;
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
        };
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
                    };
                    //调用微信支付服务器端接口
                    WeiXinService.getweixinPayData($scope.param).success(function (data) {
                        WeiXinService.wxchooseWXPay(data) //调起微支付接口
                            .then(function (msg) {
                                switch (msg) {
                                    case "get_brand_wcpay_request:ok":
                                        CommonService.toolTip("支付成功", "tool-tip-message-success");
                                        //支付成功，跳转订单详情
                                        sessionStorage.setItem(r.orderno, "ok");
                                        $state.go("orderdetail", {orderNo: r.orderno, orderType: '006'});
                                        break;
                                    default :
                                        //未支付，跳转支付进度
                                        sessionStorage.setItem(r.orderno, "");
                                        var order = {orderno: r.orderno, orderType: '006', orderStatus: ""}; //orderStatus为订单状态
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
                var len =  $scope.orderinfo.shoporderstatusCode.length;
                var status ="";
                if(len>3){
                    status = $scope.orderinfo.shoporderstatusCode.substring(len-3,len);
                }
                switch($scope.order.orderType){
                    case "001":
                    case "003":
                        switch (status) {//自定义支付进度展示
                            case "001":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "002":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    case "002":
                        switch (status) {//自定义支付进度展示
                            case "003":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "004":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    case "006":
                        switch ($scope.orderinfo.shoporderstatusCode) {//自定义支付进度展示
                            case "001":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "002":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    default :
                        break;
                }
            });
        }else {
            OrderService.getOrderDetailInfo({orderno: $scope.order.orderno}).success(function (data) {
                console.log('getOrderDetailInfo==='+JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//地址信息
                var len =  $scope.orderinfo.shoporderstatusCode.length;
                var status ="";
                if(len>3){
                    status = $scope.orderinfo.shoporderstatusCode.substring(len-3,len);
                }
                switch($scope.order.orderType){
                    case "001":
                    case "003":
                        switch (status) {//自定义支付进度展示
                            case "001":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "002":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    case "002":
                        switch (status) {//自定义支付进度展示
                            case "003":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "004":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    case "006":
                        switch ($scope.orderinfo.shoporderstatusCode) {//自定义支付进度展示
                            case "001":
                                $scope.process = [{value: "等待买家付款", len: 1}];
                                break;
                            case "002":
                                $scope.process = [{value: "等待买家付款", len: 1}, {value: "买家已付款", len: 2}];
                                break;
                        }
                        break;
                    default :
                        break;
                }
            });
        }
        //跳转到订单详情页
        $scope.orderdetail = function (orderno , orderType ,shoporderstatusCode) {
            var order = {orderno: orderno ,orderType:orderType ,orderStatus:shoporderstatusCode};
            console.log('detail==='+JSON.stringify(order));
            switch (orderType) {
                case '006':
                    $state.go("orderdetail", {orderNo: orderno, orderType: orderType});
                    break;
                default :
                    $state.go("servicedetail", {orderNo: orderno, orderType: orderType});
                    break;
            }
        }
    }])
    //服务订单详情
    .controller('ServiceDetailCtrl', ['$rootScope','$scope', '$stateParams', '$state',  'OrderService', 'CommonService','WeiXinService','ServeCommonService', function ($rootScope,$scope, $stateParams, $state, OrderService,CommonService,WeiXinService,ServeCommonService) {
        $rootScope.commonService=CommonService;
        $scope.serviceName=ServeCommonService.getName($stateParams.orderType).name;
        if (sessionStorage.getItem($stateParams.orderNo) == "ok") {
            OrderService.queryWxPutOrder({orderno: $stateParams.orderNo,type:$stateParams.orderType, payresult:sessionStorage.getItem($scope.orderno)}).success(function(data){
                console.log(JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;// 收货地址信息
                $scope.pictures = data.pictures;//图片列表
                $scope.products = data.products;//产品列表
            });
        }else {
            OrderService.getOrderDetail({orderno: $stateParams.orderNo}).success(function (data) {
                console.log('else Data=='+JSON.stringify(data));
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                $scope.pictures = data.pictures;//图片列表
                $scope.products = data.products;//产品列表
                $scope.buyinfo = data.buyinfo;//产品列表
                //$scope.orderdetail = data.orderdetail;//订单详情
            });
        }
        //微信支付调用
        $scope.weixinPay = function (orderno, totalprice) {
            console.log(totalprice);
            //调用微信支付服务器端接口
            $scope.param = {
                totalprice: 0.01, //$scope.totalprice,
                orderNo: orderno,
                descrip: '六唯壹珠宝',
                openid: localStorage.getItem("openId"),
                orderType:JSON.stringify({type:$scope.order.orderType})
            };
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
                                    sessionStorage.setItem(orderno,"");
                                    var order = {orderno:orderno ,orderType:$stateParams.orderType ,orderStatus:""};
                                    $state.go("payresult", {order: JSON.stringify(order)});
                                    break;
                                default :
                                    //取消或失败，停留此页面
                                    break;
                            }
                        });
                });
            })
        };
        //物流页面
        $scope.addLogisticsInfo = function () {
            var obj = {
                type: $stateParams.orderType,
                orderNo: $stateParams.orderNo
            };
            console.log('procreceive===' + JSON.stringify(obj));
            $state.go('procreceive', obj);
        };
        //服务追踪
        $scope.traceProgress = function (type,orderno,createTime,shoporderstatusCode,totalprice) {
            console.log('type=='+type);
            console.log('orderno=='+orderno);
            console.log('createTime=='+createTime);
            console.log('shoporderstatusCode=='+shoporderstatusCode);
            console.log('totalprice=='+totalprice);
            //分类型=>分状态=>确定进度
            $state.go("serviceProgress",{type:type,orderno:orderno});
            //switch(type){
            //    case "001"://翻新
            //        switch (shoporderstatusCode) {
            //            case "001002":
            //            case "001003":
            //                $state.go('procreceive', {type: type, orderNo: orderno});//平台收货
            //                break;
            //            case "001004":
            //                $state.go('proctest', {type: type, orderNo: orderno});//检测
            //                break;
            //            case "001005":
            //                $state.go('procrefurbish', {type: type, orderNo: orderno});//翻新
            //                break;
            //            case "001006":
            //                $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
            //                break;
            //            case "001007":
            //                $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
            //                break;
            //            case "001008":
            //                $state.go('procaddcmt', {type: type, orderno: orderno});//评论
            //                break;
            //            case "001009":
            //                CommonService.toolTip("当前订单交易完成","tool-tip-message-success");
            //                break;
            //            case "001010":
            //                CommonService.toolTip("当前订单交易关闭","tool-tip-message-success");
            //                break;
            //        }
            //        break;
            //    case "002"://维修
            //        switch (shoporderstatusCode){
            //            case "002001":
            //            case "002002":
            //                $state.go('procfixprice', {name: 'repair', orderno: orderno});
            //                break;
            //            case "002003"://确认维修(待付款)
            //                sessionStorage.setItem('jinlele_procphoto_orderno', orderno);
            //                sessionStorage.setItem('jinlele_procphoto_aturalprice', totalprice);
            //                sessionStorage.setItem('jinlele_procphoto_pathname', 'repair');
            //                $state.go('proccommitorder');
            //                break;
            //            case "002004"://客户发货
            //            case "002012"://平台收货
            //                $state.go('procreceive', {type: type, orderNo: orderno});//平台收货
            //                break;
            //            case "002005":
            //                $state.go('proctest', {type: type, orderNo: orderno});//检测
            //                break;
            //            case "002006":
            //                $state.go('procrepair', {type: type,orderNo: orderno});//维修
            //                break;
            //            case "002007":
            //                $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
            //                break;
            //            case "002008":
            //                $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
            //                break;
            //            case "002009":
            //                $state.go('procaddcmt', {type: type, orderno: orderno});//评论
            //                break;
            //            case "002010":
            //                CommonService.toolTip("当前订单交易完成","tool-tip-message-success");
            //                break;
            //            case "002011":
            //                CommonService.toolTip("当前订单交易关闭","tool-tip-message-success");
            //                break;
            //        }
            //        break;
            //    case "003"://检测
            //        switch (shoporderstatusCode){
            //            case "003002":
            //            case "003003":
            //                $state.go('procreceive', {type: type, orderNo: orderno});//平台收货
            //                break;
            //            case "003004":
            //                $state.go('proctest', {type: type, orderNo: orderno});//检测
            //                break;
            //            case "003006":
            //                $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
            //                break;
            //            case "003007":
            //                $state.go('proccheck', {type: type, orderNo: orderno, orderTime: createTime});//验收
            //                break;
            //            case "003008":
            //                $state.go('procaddcmt', {type: type, orderno: orderno});//评论
            //                break;
            //            case "003009":
            //                CommonService.toolTip("当前订单交易完成","tool-tip-message-success");
            //                break;
            //            case "003010":
            //                CommonService.toolTip("当前订单交易关闭","tool-tip-message-success");
            //                break;
            //        }
            //        break;
            //    case "004"://回收
            //        switch (shoporderstatusCode){
            //            case "004001":
            //            case "004002":
            //                $state.go('procreceive', {type: type, orderNo: orderno});//平台收货
            //                break;
            //            case "004003":
            //                $state.go('proctest', {type: type, orderNo: orderno});//检测
            //                break;
            //            case "004004":
            //                $state.go('actualprice', {type: type, orderno: orderno});//实际定价
            //                break;
            //            case "004005":
            //            case "004006":
            //                $state.go('cfmrecycle', {orderno: orderno,orderstatus:shoporderstatusCode});//确认回收
            //                break;
            //            case "004007":
            //                $state.go('procaddcmt', {type: type, orderno: orderno});//评论
            //                break;
            //            case "004008":
            //                CommonService.toolTip("当前订单交易完成","tool-tip-message-success");
            //                break;
            //            case "004009":
            //                CommonService.toolTip("等待物流筛检中，请稍后查看","tool-tip-message-success");
            //                break;
            //            case "004010":
            //                $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
            //                break;
            //            case "004011":
            //                CommonService.toolTip("当前订单交易关闭","tool-tip-message-success");
            //                break;
            //        }
            //        break;
            //    case "005"://换款
            //        switch (shoporderstatusCode){
            //            case "005001":
            //            case "005002":
            //                $state.go('procreceive', {type: type, orderNo: orderno});//平台收货
            //                break;
            //            case "005003":
            //                $state.go('proctest', {type: type, orderNo: orderno});//检测
            //                break;
            //            case "005004":
            //                $state.go('actualprice', {type: type, orderno: orderno});//实际定价
            //                break;
            //            case "005005":
            //                $state.go('cfmexchange', {orderno: orderno,orderstatus:shoporderstatusCode});//确认换款
            //                break;
            //            case "005006"://待选款
            //                localStorage.setItem("exchangeorderno",orderno);
            //                //根据订单号查询实际定价金额
            //                OrderService.selectActualPrice({orderNo:orderno}).success(function (data){
            //                    if(data && data.fixPrice){
            //                        localStorage.setItem("actualprice",data.fixPrice);
            //                    }
            //                });
            //                if(localStorage.getItem("toExchangeGoodId")==""||localStorage.getItem("toExchangeGoodId")==null) {
            //                    $state.go('barterlist');
            //                }else {
            //                    //具体商品详情
            //                    $state.go('barterdetail', {goodId: localStorage.getItem("toExchangeGoodId")});
            //                }
            //                break;
            //            case "005007"://待付款
            //                localStorage.setItem("exchangeorderno",orderno);
            //                //根据订单号查询实际定价金额
            //                OrderService.selectActualPrice({orderNo:orderno}).success(function (data){
            //                    if(data && data.fixPrice){
            //                        localStorage.setItem("actualprice",data.fixPrice);
            //                    }
            //                });
            //                //根据订单号查询goodid
            //                OrderService.getBarterGoodId({orderno:orderno}).success(function(data){
            //                    if(data.exchangeGood){
            //                        //具体商品详情
            //                        $state.go('barterdetail', {goodId: data.exchangeGood.goodId});
            //                    }
            //                });
            //                break;
            //            case "005009"://待收货
            //                $state.go('procpost', {type: type, orderNo: orderno, orderTime: createTime});//拍照邮寄
            //                break;
            //            case "005010":
            //                $state.go('procaddcmt', {type: type, orderno: orderno});//评论
            //                break;
            //            case "005011":
            //                CommonService.toolTip("当前订单交易已完成","tool-tip-message-success");
            //                break;
            //            case "005012":
            //            case "005013":
            //                $state.go('cfmrecycle', {orderno: orderno,orderstatus:shoporderstatusCode});//确认回收
            //                break;
            //            case "005008"://已付款
            //            case "005014":
            //                CommonService.toolTip("等待物流筛检中，请稍后查询","tool-tip-message-success");
            //                break;
            //            case "005015":
            //                CommonService.toolTip("当前订单交易已关闭","tool-tip-message-success");
            //                break;
            //        }
            //        break;
            //}
        }
    }])
    //服务进度
    .controller('ServiceProgressCtrl',['$scope','$stateParams','OrderService',function($scope,$stateParams,OrderService){
        $scope.orderType=$stateParams.type;
        $scope.orderno=$stateParams.orderno;
        $scope.normalflag=true;//正常订单
        OrderService.getProgressInfo({orderno:$stateParams.orderno}).success(function(data){
            console.log(data);
            $scope.orderInfo=data;
            if(data.orderstatus=="001010"||data.orderstatus=="002012"||data.orderstatus=="003010"||data.orderstatus=="004011"||data.orderstatus=="005018"){
                $scope.normalflag=false;
            }
        });
    }])
    //商城订单详情
    .controller('OrderDetailCtrl', ['$rootScope','$scope', '$stateParams', '$state',  'OrderService', 'CommonService','WeiXinService', function ($rootScope,$scope, $stateParams, $state, OrderService,CommonService,WeiXinService) {
        $rootScope.commonService=CommonService;
        //查询物流信息
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        if (sessionStorage.getItem($scope.orderno) == "ok") {
            OrderService.queryWxPutOrder({orderno: $stateParams.orderNo,type: $stateParams.orderType, payresult:sessionStorage.getItem($scope.orderno)}).success(function(data){
                $scope.orderinfo = data.order;//订单总信息
                $scope.address = data.address;//订单总信息
                $scope.orderdetail = data.orderdetail;//订单详情
            });
        }else {
            OrderService.getOrderDetailInfo({orderno: $stateParams.orderNo}).success(function (data) {
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
            };
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
                });
                WeiXinService.mediaIds = []; //置空媒体id数组
            })
        };
        //描述等级
        $scope.paint=function(id) {
            $scope.currentId = id + 1;
        };
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
                });
                iteminfo.mediaIds = $scope.mediaIds;
                $scope.itemsinfo.push(iteminfo);
            });
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
    .controller('MemberCtrl', ['$scope', 'MemberService','WalletService','CartService', function ($scope, MemberService ,WalletService,CartService) {
        var opendid = localStorage.getItem("openId");
        MemberService.getUserInfo(opendid).success(function (data) {
            $scope.user = data.userInfo;
            //console.log(JSON.stringify(data));
        });
        $scope.balance = 0;
        WalletService.getBalance({userId:localStorage.getItem("jinlele_userId")}).success(function (data) {
            console.log('data==='+JSON.stringify(data));
            $scope.balance = data.balance;
        });
        CartService.getCartTotalNum({userid: localStorage.getItem("jinlele_userId")}).success(function(data){
            $scope.cartTotalNum=data.totalnum;
        });
    }])
    //订单列表
    .controller('OrderListCtrl', ['$rootScope','$scope', '$state','$stateParams','WeiXinService', 'OrderListService', 'OrderService','CommonService','MemberService','ServeCommonService', function ($rootScope,$scope,$state,$stateParams, WeiXinService, OrderListService, OrderService,CommonService,MemberService,ServeCommonService) {
        //通过config接口注入权限验证配置
        $rootScope.commonService=CommonService;
        $scope.config = {
            data: [{id: 'ALL', text: '全部订单'},{id: '001', text: '翻新订单'},{id: '002', text: '维修订单'},{id: '003', text: '检测订单'},{id: '004', text: '回收订单'},{id: '005', text: '换款订单'},{id: '006', text: '商城订单'}],
            placeholder: '商城订单',
            minimumResultsForSearch:-1
        };
        $scope.type = $stateParams.typeName? ServeCommonService.getType($stateParams.typeName).code : 'ALL';
        $scope.orderlistsinfo = [];
        $scope.page = 0;//当前页数
        $scope.total = 1;//总页数
        $scope.moreFlag = false; //是否显示加载更多
        $scope.noDataFlag = false;  //没有数据显示
        $scope.getOrderLists = function () {
            if ((arguments != [] && arguments[0] == 0)) {
                $scope.page = 0;
                $scope.orderlistsinfo = [];
            }
            $scope.page++;
            $scope.moreFlag = false;
            $scope.noDataFlag = false;
            //分页显示
            OrderListService.getorderLists({
                userid: localStorage.getItem("jinlele_userId"),
                pagenow: $scope.page,
                type: $scope.type
            }).success(function (data) {
                angular.forEach(data.pagingList, function (item) {
                    $scope.orderlistsinfo.push(item);
                });
                if (data.myrows == 0) $scope.noDataFlag = true;
                $scope.total = data.myrows;
                if ($scope.total > $scope.orderlistsinfo.length) {
                    $scope.moreFlag = true;
                    console.log("moreFlag ==" + $scope.moreFlag);
                }
            })
        };
        $scope.getOrderLists();
        //取消订单
        $scope.cancelOrder = function (orderno,typeCode) {
            OrderService.cancelOrder({orderno: orderno, typeCode: typeCode}).success(function (data) {
                if (parseInt(data.resultnumber) > 0) {
                    CommonService.toolTip("取消成功", "tool-tip-message");
                    $scope.type = typeCode;
                    $scope.getOrderLists(0);
                }
            });
        };
        //确认收货
        $scope.confirmReceive = function (shoporderstatus,orderno){
            var orderStatus="";
            switch (shoporderstatus){
                case '001007':
                    orderStatus='001008';
                    break;
                case '002008':
                    orderStatus='002009';
                    break;
                case '003007':
                    orderStatus='003008';
                    break;
                case '004010':
                    orderStatus='004012';//交易关闭
                    break;
                case '005009'://待收货
                    orderStatus='005010';
                    break;
                case '005019'://返回待收货
                    orderStatus='005018';//交易关闭
                    break;
                default ://默认商城
                    orderStatus='004';
                    break;
            }
            OrderService.update({
                orderno: orderno,
                shoporderstatuscode:orderStatus
            }).success(function (data) {
                if (data.n == 1) {
                    CommonService.toolTip("收货成功，感谢您的光顾~", "tool-tip-message");
                    $state.reload();
                }
            });
        };
        //微信支付调用
        $scope.weixinPay = function (orderno, totalprice,ordertype) {
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
                                CommonService.toolTip("支付成功", "tool-tip-message-success");
                                //调用支付后，跳转订单详情
                                sessionStorage.setItem(orderno, "ok");
                                $state.go("orderdetail", {orderNo: orderno, orderType: ordertype});
                                break;
                            default :
                                //未支付
                                sessionStorage.setItem(orderno, "");
                                break;
                        }
                    });
            })
        };

        //跳转到商城订单详情页
        $scope.orderdetail = function (orderno , orderType) {
            $state.go("orderdetail", {orderNo: orderno, orderType: orderType});
        };
        //跳转到服务订单详情页
        $scope.servicedetail = function (orderno , orderType) {
            $state.go("servicedetail", {orderNo: orderno, orderType: orderType});
        };
        //图片预览
        $scope.previewImg=function(src){
            var imgArray = [];
            if(angular.isArray(src)){
                angular.forEach(src,function(item,index){
                    imgArray.push(item.url);
                });
                WeiXinService.wxpreviewImage(imgArray[0],imgArray);
            }else {
                imgArray.push(src);
                WeiXinService.wxpreviewImage(src, imgArray);
            }
        }
    }])
    //退款
    .controller('ReturnApplyCtrl', ['$rootScope','$scope','$state', '$stateParams','CategoryService','OrderService','CommonService',function ($rootScope,$scope,$state, $stateParams,CategoryService,OrderService,CommonService) {
        $rootScope.commonService=CommonService;
        //退款原因
        $scope.reasonConfig= {
            data: [],
            minimumResultsForSearch:-1
        };
        CategoryService.getItems({typename: 'returnReason'}).success(function (data) {
            angular.forEach(data.selectedItems,function(item,index){
                var obj={};
                obj.id=item.codevalue;
                obj.text=item.dictname;
                $scope.reasonConfig.data.push(obj);
                if(index==0){
                    $scope.reasonItemValue=item.codevalue;
                }
            })
        });
        $scope.opendisabled=false;//按钮禁用状态
        $scope.commitApply = function () {
            $scope.opendisabled=true;
            OrderService.applyReturn({type:$stateParams.type,orderno:$stateParams.orderno,userId:localStorage.getItem("jinlele_userId"),reasonCode:$scope.reasonItemValue,memo:$scope.memo}).success(function(data){
                if(data&&data.result=="ok"){
                    CommonService.toolTip("退款申请提交成功！", "tool-tip-message-success");
                }else{
                    CommonService.toolTip("您已提交过此订单的退款申请！", "");
                }
                $state.go("orderlist");
            });
        }
    }])
    //物流追踪
    .controller('LogisticsTrackCtrl',['$rootScope','$scope','$state', '$stateParams','OrderService','CommonService',function($rootScope,$scope,$state, $stateParams,OrderService,CommonService){
        $rootScope.commonService = CommonService;
        $scope.orderNo = $stateParams.orderNo;
        //物流样式展示
        switch ($stateParams.who){
            case "M":
                $scope.jinlele="hide";
                $scope.mine="retrofit";
                $scope.jinflag=false;
                $scope.myflag=true;
                break;
            case "T":
                $scope.jinlele="retrofit";
                $scope.mine="hide";
                $scope.jinflag=true;
                $scope.myflag=false;
                break;
        }
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
        };
        //参数
        $scope.order = {
            userlogisticsnoComp:"",//买方发货快递公司编码
            userlogisticsno:"", //买方发货单号
            orderstatus:""//订单状态
        };
        //物流公司
        $scope.userlogisticsnoConfig= {
            data: [],
            placeholder: '请选择物流公司'
        };
        //去后台查询物流数据
        OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
            $scope.orderinfo = data.order;
            angular.forEach(data.express,function(item,index){
                var obj={};
                obj.id=item.number;
                obj.text=item.company;
                $scope.userlogisticsnoConfig.data.push(obj);
            });
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        //保存客户填写的物流信息
        $scope.saveExpress = function () {
            if (!$scope.order.userlogisticsnoComp) {
                CommonService.toolTip("请选择物流公司", "");
                return;
            }
            if (!$scope.order.userlogisticsno) {
                CommonService.toolTip("请填写快递单号", "");
                return;
            }
            OrderService.update({
                orderno: $scope.orderNo,
                userlogisticsnocomp: $scope.order.userlogisticsnoComp,
                userlogisticsno: $scope.order.userlogisticsno,
                shoporderstatuscode:'009'
            }).success(function (data) {
                if (data.n == 1) {
                    //重新更新数据
                    OrderService.findReceiptServiceByOrderno({orderNo:$scope.orderNo}).success(function (data) {
                        $scope.orderinfo = data.order;
                        angular.forEach(data.express,function(item,index){
                            var obj={};
                            obj.id=item.number;
                            obj.text=item.company;
                            $scope.userlogisticsnoConfig.data.push(obj);
                        });
                        if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
                        if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
                    });
                }
            });
        };
        //修改物流信息
        $scope.editUsrLogisticsInfo=function(){
            $scope.orderinfo.userlogisticsno=null;
        }
    }])
    //虚拟账户明细
    .controller('WalletdetailCtrl', function ($scope, WalletService) {
        console.log(1);
        $scope.noDataFlag = false;  //暂无数据标示
        // $scope.rmFid = "";
        // $scope.rmIndex = 0;
        $scope.walletdetailArr = [];
        $scope.page = 0;//当前页数
        $scope.moreDataFlag = false; //是否显示 加载更多的点击按钮
        $scope.getData = function () {
            $scope.noDataFlag = false;
            $scope.page++;
            WalletService.getWalletdetail({pagenow:$scope.page, userId:localStorage.getItem("jinlele_userId")}).success(function (data) {
                if(data.myrows == 0) {
                    $scope.noDataFlag = true;
                    return;
                }
                angular.forEach(data.pagingList, function (item) {
                    $scope.walletdetailArr.push(item);
                });
                $scope.moreDataFlag = (data.myrows > $scope.walletdetailArr.length) ?  true : false;
            })
        };
        $scope.getData();
    })
    //充值成功页面
    .controller('RechargeOKCtrl', function ($scope, $stateParams, $rootScope, CommonService, WalletService) {
        $scope.resultFlag = false;//充值结果
        $scope.orderno =  $stateParams.orderno;
        console.log('$stateParams.orderno=='+$stateParams.orderno);
        //{"totalprice":50.0,"pay_result":"001","shoporderstatusCode":"007001"}


        WalletService.getRechargeResult({orderno:$scope.orderno})
            .success(function (data) {
                console.log("DATA=="+JSON.stringify(data));
                if(data.pay_result=='003'){
                    $scope.resultFlag = true;
                    $scope.price = data.actualpayprice;
                }else{
                    $scope.resultFlag = false;
                }
            })
    })
    //充值页面
    .controller('RechargeCtrl', function ($scope, $stateParams, $rootScope, CommonService, WalletService, $state, WeiXinService) {
        $rootScope.commonService=CommonService;
        $scope.rechargeMoney = "";
        $scope.submit = function () {
            if(!$scope.rechargeMoney){
                CommonService.toolTip("充值金额不能为空", "");
                return;
            }
            WalletService.saveRechargeOrder({userId:localStorage.getItem("jinlele_userId"),rechargeMoney:$scope.rechargeMoney})
                .success(function (data) {
                    if(data && data.n==1){
                        console.log("充值data=="+JSON.stringify(data));
                        $scope.weixinPay(data.orderno, data.price, data.type, data.orderstatus);
                    }else{
                        CommonService.toolTip("网络异常", "");

                    }
                }).error(function (res) {
                console.log(JSON.stringify(res));
                CommonService.toolTip("网络异常", "");
            });
        };

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
                                break;
                            default :
                                break;
                        }
                        $state.go("rechargeOK", {orderno: orderno});
                    });
            })
        }
    })
    //我的钱包
    .controller('WalletCtrl', function ($scope ,WalletService , $state ,$rootScope ,CommonService) {
        $rootScope.commonService=CommonService;
        $scope.balance = 0;
        WalletService.getBalance({userId:localStorage.getItem("jinlele_userId")}).success(function (data) {
            console.log('data==='+JSON.stringify(data));
            $scope.balance = data.balance;
        });
        $scope.cashApply = function () {
            if(!$scope.balance){
                CommonService.toolTip("您没有可提现的金额", "");
                return;
            }
            $state.go('cashApply',{balance:$scope.balance});

        }
    })
    //提现记录
    .controller('CashdetailCtrl', function ($scope ,WalletService) {
        $scope.noDataFlag = false;
        WalletService.getAllcashApply({userId:localStorage.getItem("jinlele_userId")})
            .success(function (data) {
                console.log("data==" + JSON.stringify(data));
                $scope.arr = data.records;
                if($scope.arr.length == 0)$scope.noDataFlag = true;
                console.log('$scope.noDataFlag=='+$scope.noDataFlag);
            });
    })
    //提现申请
    .controller('cashApplyCtrl', function ($scope , $stateParams ,$rootScope ,CommonService ,WalletService ,$state) {
        $rootScope.commonService=CommonService;
        $scope.balance = $stateParams.balance;
        $scope.applyMoney = '';
        $scope.submit = function () {
            if(!$scope.applyMoney){
                CommonService.toolTip("提现金额不能为空", "");
                return;
            }
            if($scope.applyMoney > $scope.balance){
                CommonService.toolTip("提现金额不能超过余额", "");
                return;
            }
            if($scope.applyMoney <1){
                CommonService.toolTip("提现金额不能低于1元", "");
                return;
            }
            WalletService.saveCashApply({userId:localStorage.getItem("jinlele_userId"),applyMoney:$scope.applyMoney})
                .success(function (data) {
                    console.log("data=="+JSON.stringify(data));
                    if(data.n == 1){
                        $state.go("cashdetail");
                    }
                });

        }
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
                });
                $scope.moreDataFlag = (data.myrows > $scope.FavArr.length) ?  true : false;
            })
        };
        $scope.getFavs();
        $scope.del = function (fid,index) {
            $scope.rmFlag = true;
            $scope.rmFid = fid;
            $scope.rmIndex = index;
        };

        $scope.cancelRemove = function () {
            $scope.rmFlag = false;
        };
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
        };
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
        $scope.btninfo = "获取验证码";
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
            //验证手机号码格式，发送验证码
            var reg = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
            var flag = reg.test(phoneNumber);
            if (flag){
                //倒计时
                var timerCount=60,timePromise = undefined;
                timePromise = $interval(function(){
                    if(timerCount<=0){
                        $interval.cancel(timePromise);
                        timePromise = undefined;
                        $scope.btninfo = "重新获取";
                        $scope.opendisabled = false;//关闭禁用状态
                        $scope.active=true;//按钮激活样式
                    }else{
                        $scope.btninfo = timerCount + "s后再次获取";
                        timerCount--;
                        $scope.opendisabled = true;//打开禁用状态
                        $scope.active=false;//关闭激活样式

                    }
                },1000,100);
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
        };
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
        //属性分别为:  数据集合 页号 是否更多标示  是否无数据标示  查询条件标示  正序（0）倒序（1）标示
        // sale   time  price
        $scope.arr = [
            {data:[],page:0,moreDataFlag:false,noDataFlag:false,querytype:0,flag:1},
            {data:[],page:0,moreDataFlag:false,noDataFlag:false,querytype:1,flag:1},
            {data:[],page:0,moreDataFlag:false,noDataFlag:false,querytype:2,flag:0}
        ];
        $scope.getData=function(index) {
            $scope.arr[index].noDataFlag  = false;
            $scope.arr[index].page++;

            //获取产品列表
            GoodService.getGoodList({pagenow: $scope.arr[index].page, categoryname: encodeURIComponent($stateParams.name), querytype:$scope.arr[index].querytype , flag:$scope.arr[index].flag}).success(function (data) {
                console.log(data.myrows);
                console.log(data.pagingList);
                if(data.myrows == 0) {
                    $scope.arr[index].noDataFlag = true;
                    return;
                }
                angular.forEach(data.pagingList, function (item) {
                    $scope.arr[index].data.push(item);
                });
                $scope.arr[index].moreDataFlag = (data.myrows > $scope.arr[index].data.length) ?  true : false;
            })
        }

        //页面初始加载
        $scope.getData(0);
        $scope.getData(1);
        $scope.getData(2);


    })
    //商品详情
    .controller('GoodDetailCtrl',['$scope','$state','$stateParams', '$rootScope', 'GoodService', 'AddtoCartService', 'CommonService', function ($scope,$state,$stateParams, $rootScope, GoodService, AddtoCartService, CommonService) {
        $rootScope.commonService = CommonService;
        $scope.rightFlag = false;
        function getBanners(arr) {
            var html = "";
            if (arr) {
                for (var i = 0, len = arr.length; i < len; i++) {
                    html += "<li class='swiper-slide'><img src='" + arr[i].imgurl + "' style='height:360px;'></li>";
                }
            }
            $(".banner .swiper-wrapper").html(html);
            var swiper = new Swiper('.banner', {
                pagination: '.spot',
                paginationClickable: true,
                autoplay: false
            });
        }
        //初始化参数
        $scope.bannerurl = "";
        $scope.stocknum = 0;//库存数
        $scope.favouriteId = "";   //收藏后的id
        $scope.menuWidth = {"width": "33.333%"};
        $scope.gooddetail = {
            userId: localStorage.getItem("jinlele_userId"),
            goodId: $stateParams.id,
            goodchildId: "",
            num: 1
        };
        GoodService.getGoodDetail({goodId: $stateParams.id, userId: $scope.gooddetail.userId}).success(function (data) {
            console.log("getGoodDetail==" + JSON.stringify(data));
            $scope.goodDetail = data.good;
            $scope.goodChilds = data.goodchilds;
            $scope.favourites = data.favourites;
            $scope.totalnum = data.totalnum;
            $scope.bannerurl = data.imgurls;
            $scope.bannerurl.splice(0, 0, {"imgurl": data.good.bannerurl});
            getBanners($scope.bannerurl);
            $scope.price = data.good.minprice;
            $scope.stocknum = data.good.stocknum;
            if (data.good.canchange == 0) {
                $scope.menuWidth = {"width": "25%"};
            }
            if ($scope.goodChilds && $scope.goodChilds.length > 0) {
                angular.forEach($scope.goodChilds, function (item) {
                    item.flag = false;
                });
            }
            if ($scope.favourites && $scope.favourites.length > 0) {
                $scope.favouriteId = $scope.favourites[0].id;
            }
            $scope.favcontent = $scope.favouriteId ? '已收藏' : '加入收藏';
            console.log("$scope.goodChilds==" + JSON.stringify($scope.goodChilds));

        });
        GoodService.getGoodCommentCount({goodId: $stateParams.id}).success(function (data) {
            $scope.goodcommentcount = data.total;
        });
        GoodService.getGoodComments({goodId: $stateParams.id, pagenow: 1}).success(function (data) {
            $scope.goodcomments = data.comments;
        });
        $scope.addtocart = function () {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $scope.totalnum += parseInt($scope.gooddetail.num || 0);
            AddtoCartService.addtocart($scope.gooddetail).success(
                function (data) {
                    console.log('data===' + data);
                    CommonService.toolTip("添加成功", "tool-tip-message-success");
                }
            )
        };
        $scope.changeNum = function () {
            if (!/^\+?[1-9][0-9]*$/.test($scope.gooddetail.num)) {
                $scope.gooddetail.num = 1;
            }
            if ($scope.gooddetail.num > $scope.stocknum) {
                $scope.gooddetail.num = $scope.stocknum;
            }
        };
        $scope.addNum = function () {
            if ($scope.gooddetail.num < $scope.stocknum) {
                $scope.gooddetail.num++;
            }
        };
        $scope.minusNum = function () {
            if ($scope.gooddetail.num > 1) {
                $scope.gooddetail.num--;
            }
        };

        $scope.fav = function () {
            //去后台收藏表 保存或删除数据
            if ($scope.favouriteId) {  //
                GoodService.delFavourite({fid: $scope.favouriteId}).success(function (data) {
                    if (data && data.n == 1) {
                        $scope.favouriteId = "";
                        CommonService.toolTip("已取消收藏", "");
                        $scope.favcontent = '加入收藏';
                    }
                })
            } else {
                GoodService.saveFavourite({
                    goodId: $stateParams.id,
                    userId: localStorage.getItem("jinlele_userId")
                }).success(function (data) {
                    if (data && data.favouriteId) {
                        $scope.favouriteId = data.favouriteId;
                        CommonService.toolTip("收藏成功", "");
                        $scope.favcontent = '已收藏';
                    }
                })
            }
        };
        $scope.changThis=function() {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $state.go("evaluate", {name: "exchange", gid: $stateParams.id, gcid: $scope.gooddetail.goodchildId});
        }
    }])
    //流程-拍照(翻新，检测，回收业务只有拍照功能，维修业务包含拍照及下单功能)
    .controller('ProcPhotoCtrl', function ($scope,ProcCommitOrderService, $stateParams, WeiXinService, $rootScope, CommonService, ProcPhotoService, $state ,CategoryService) {
        $rootScope.commonService = CommonService;
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.localIds = [];// 上传图片的微信路径 数组
        WeiXinService.mediaIds = []; //媒体id数组
        $scope.imgSrcs=[];//显示的图片src数组
        $scope.count=0;//记录图片src总数
        $scope.perUploadNumber=5;//每次上传数量
        //上传图片
        $scope.wxchooseImage = function () {
            if($scope.count<5) {
                //通过config接口注入权限验证配置
                WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
                //通过ready接口处理成功验证
                wx.ready(function () {
                    WeiXinService.wxchooseImage(function (localIds) {
                        for (var i = 0; i < localIds.length; i++) {
                            $scope.imgSrcs.push(localIds[i]);
                        }
                        $scope.count = $scope.imgSrcs.length;
                        $scope.perUploadNumber = 5 - $scope.imgSrcs.length;
                        $scope.localIds = $scope.imgSrcs;
                        $scope.$apply();
                    }, $scope.perUploadNumber)
                })
            }else{
                CommonService.toolTip("最多上传五张图片", "");
            }
        };
        //删除图片
        $scope.delthisImage=function(index){
            $scope.imgSrcs.splice(index,1);
            WeiXinService.mediaIds.splice(index,1);
            $scope.count = $scope.imgSrcs.length;
            $scope.perUploadNumber = 5 - $scope.imgSrcs.length;
        };
        //根据路由获取服务类型代码
        $scope.typeCode = ProcCommitOrderService.getType($scope.pagetheme).code;
        //服务类实体
        $scope.service = {
            price: 0  //价格
        };
        switch ($scope.pagetheme) {
            case "refurbish"://翻新
                ProcPhotoService.getrefurbishPrice().success(function (data) {
                    console.log("翻新价格==" + JSON.stringify(data));
                    $scope.service.price = data.code_value;
                });
                break;
            case "repair"://维修
                $scope.product = {
                    firstCatogoryId: "",//一级分类id
                    secondCatogoryId: "", //二级分类id
                    repairItemValue: "",//维修项目
                    num: "",
                    memo: ""
                };
                //材质
                $scope.stuffConfig= {
                    data: [],
                    minimumResultsForSearch:-1
                };
                //类别
                $scope.typeConfig= {
                    data: [],
                    minimumResultsForSearch:-1
                };
                //维修项目
                $scope.repairConfig= {
                    data: [],
                    minimumResultsForSearch:-1
                };
                //一级分类
                CategoryService.getCategories().success(function (data) {
                    console.log(JSON.stringify(data.firstList));
                    angular.forEach(data.firstList,function(item,index){
                        var obj={};
                        obj.id=item.id;
                        obj.text=item.name;
                        $scope.stuffConfig.data.push(obj);
                        if(index==0){
                            $scope.product.firstCatogoryId=item.id;
                        }
                    });
                    CategoryService.getSecondCatogByPid($scope.product.firstCatogoryId).success(function (data) {
                        $scope.typeConfig.data = [];
                        angular.forEach(data, function (item, index) {
                            var obj = {};
                            obj.id = item.id;
                            obj.text = item.name;
                            $scope.typeConfig.data.push(obj);
                            if(index==0){
                                $scope.product.secondCatogoryId=item.id;
                            }
                        })
                    });
                });
                //根据一级分类获取二级分类
                $scope.getSecondCategories = function (firstCatogoryId) {
                    CategoryService.getSecondCatogByPid(firstCatogoryId).success(function (data) {
                        $scope.typeConfig.data = [];
                        angular.forEach(data, function (item, index) {
                            var obj = {};
                            obj.id = item.id;
                            obj.text = item.name;
                            $scope.typeConfig.data.push(obj);
                            if(index==0){
                                $scope.product.secondCatogoryId=item.id;
                            }
                        })
                    });
                };
                //维修项目
                CategoryService.getItems({typename: 'repairitem'}).success(function (data) {
                    angular.forEach(data.selectedItems,function(item,index){
                        var obj={};
                        obj.id=item.codevalue;
                        obj.text=item.dictname;
                        $scope.repairConfig.data.push(obj);
                        if(index==0){
                            $scope.product.repairItemValue=item.codevalue;
                        }
                    })
                });
                //计算总数量和总价格
                $scope.getTotal = function (number) {
                    //遍历
                    $scope.totalnum = number;
                    console.log(" $scope.totalnum ==" + $scope.totalnum);
                };
                $scope.errorFlag=false;
                $scope.checkNum=function(number){
                    if(/^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/.test(number)){
                        $scope.errorFlag=false;
                    }else {
                        $scope.errorFlag = true;
                        $scope.errorInfo="请输入数字";
                    }
                };
                break;
            case "detect"://检测
                ProcPhotoService.getdetectPrice().success(function (data) {
                    console.log("检测价格==" + JSON.stringify(data));
                    $scope.service.price = data.code_value;
                });
                break;
            default ://回收或换款
                $scope.service.price = localStorage.getItem("evaluationPrice");
                break;
        }
        //进入提交订单的页面
        $scope.commitServiceOrder = function (pagetheme) {
            if($scope.typeCode=='002') {
                if(/^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/.test($scope.product.num)){
                    $scope.errorFlag=false;
                }else {
                    $scope.errorFlag = true;
                    $scope.errorInfo="请输入数字";
                    return;
                }
            }
            //判断参数
            if ($scope.localIds.length == 0) {
                switch ($scope.typeCode) {
                    case "005":
                        CommonService.toolTip("请上传旧款的图片", "");
                        break;
                    default :
                        CommonService.toolTip("请上传图片", "");
                        break;
                }
                return;
            }
            $scope.params = {
                userId: localStorage.getItem("jinlele_userId"),
                mediaIds: WeiXinService.mediaIds,
                type: $scope.typeCode //上传类型 翻新001维修002检测003回收004换款005
            };
            switch ($scope.typeCode){
                case "002"://如果是维修，需要传入产品信息
                    $scope.repairInfo=[];
                    $scope.params.products =[];
                    $scope.params.products.push($scope.product);
                    $scope.params.totalnum = $scope.totalnum;
                    $scope.repairInfo.push($scope.params);
                    console.log("$scope.pagetheme =="+ $scope.pagetheme);
                    console.log("$scope.typeCode =="+ $scope.typeCode);
                    console.log(JSON.stringify($scope.repairInfo));
                    ProcPhotoService.saveRepairOrder($scope.repairInfo).success(function (data) {
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
                case "005"://换款
                    //将serviceId与图片关联
                    $scope.params.serviceId = localStorage.getItem("barterServiceId");
                    console.log(JSON.stringify($scope.params));
                    ProcPhotoService.updateService($scope.params).success(function (data) {
                        if (data) {
                            //③后台处理成功后，跳转到下单页面
                            sessionStorage.setItem("jinlele_procphoto_pathname", $scope.pagetheme);
                            sessionStorage.setItem("jinlele_procphoto_serviceId", localStorage.getItem("barterServiceId"));
                            $state.go("proccommitorder");
                        }
                    });
                    break;
                default://如果是翻新和检查 需要传入价格   001,003,004,005
                    $scope.params.totalprice = $scope.service.price;
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
        WeiXinService.wxgetLocation();
        //console.log('latitude:'+localStorage.getItem('latitude'));
        //console.log('longitude:'+localStorage.getItem('longitude'));
        //console.log(typeof localStorage.getItem('latitude'));
        //console.log(typeof localStorage.getItem('longitude'));
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
            address:"",
            sendway: "001",
            getway: "001",
            totalprice: 0
        };
        $scope.addsendway = function (val) {
            $scope.sendwayFlag = !$scope.sendwayFlag;
            $scope.order.sendway = val;
        };
        $scope.addgetway = function (val) {
            $scope.getwayFlag = !$scope.getwayFlag;
            $scope.order.getway = val;
        };
        $scope.goback = function () {
            $window.history.back();
        };
        $scope.sendwayFlag = false;//寄件方式切换
        $scope.getwayFlag = false; //取件方式切换
        $scope.sendwayValue = ['001', '002'];//寄件取件方式值
        $scope.getwayValue = ['001', '002'];//寄件取件方式值
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
        };
        //门店
        $scope.storeConfig= {
            data: [],
            minimumResultsForSearch:-1
        };
        $scope.order.storeId=1;
        //所有门店数据
        ProcCommitOrderService.findAllStores({latitude:angular.isString(localStorage.getItem('latitude'))?localStorage.getItem('latitude'):0,longitude:angular.isString(localStorage.getItem('longitude'))?localStorage.getItem('longitude'):0}).success(function (data) {
            $scope.stores = data;
            angular.forEach(data,function(item,index){
                var obj={};
                obj.id=item.id;
                obj.text=item.name;
                obj.address=item.address;
                $scope.storeConfig.data.push(obj);
                if(index==0){
                    $scope.order.storeId=item.id;
                    $scope.order.address=item.address;
                }
            });
        });
        $scope.getStoreAddress=function(storeid){
            angular.forEach($scope.storeConfig.data,function(item,index){
                if(item.id==storeid){
                    $scope.order.address=item.address;
                }
            });
        };
        $scope.product = {
            firstCatogoryId: "",//一级分类id
            secondCatogoryId: "", //二级分类id
            num: "",
            memo: ""
        };
        //产品材质
        $scope.stuffConfig= {
            data: [],
            minimumResultsForSearch:-1
        };
        //产品类别
        $scope.typeConfig= {
            data: [],
            minimumResultsForSearch:-1
        };
        //一级分类
        CategoryService.getCategories().success(function (data) {
            console.log(JSON.stringify(data.firstList));
            angular.forEach(data.firstList,function(item,index){
                var obj={};
                obj.id=item.id;
                obj.text=item.name;
                $scope.stuffConfig.data.push(obj);
                if(index==0){
                    $scope.product.firstCatogoryId=item.id;
                }
            });
            CategoryService.getSecondCatogByPid($scope.product.firstCatogoryId).success(function (data) {
                $scope.typeConfig.data = [];
                angular.forEach(data, function (item, index) {
                    var obj = {};
                    obj.id = item.id;
                    obj.text = item.name;
                    $scope.typeConfig.data.push(obj);
                    if(index==0){
                        $scope.product.secondCatogoryId=item.id;
                    }
                })
            });
        });
        //根据一级分类获取二级分类
        $scope.getSecondCategories = function (firstCatogoryId) {
            CategoryService.getSecondCatogByPid(firstCatogoryId).success(function (data) {
                $scope.typeConfig.data = [];
                angular.forEach(data, function (item, index) {
                    var obj = {};
                    obj.id = item.id;
                    obj.text = item.name;
                    $scope.typeConfig.data.push(obj);
                    if(index==0){
                        $scope.product.secondCatogoryId=item.id;
                    }
                })
            });
        };
        //计算总价格
        $scope.calcTotalPrice=function(num){
            $scope.totalprice = num * $scope.aturalprice;
            $scope.totalnum = num;
            console.log(" $scope.totalprice ==" + $scope.totalprice);
        };
        //生成订单并付款
        $scope.useful=false;//控制下单按钮,防止重复下单
        $scope.errorFlag=false;
        $scope.procreceive = function (flag) {
            if($scope.type.code!='002'&&$scope.type.code!='005'&&!flag){ //如果是翻新 检测 回收  换款
                //1.验证格式
                if(/^(0|[1-9][0-9]{0,9})(\.[0-9]{1,2})?$/.test($scope.product.num)){
                    $scope.errorFlag=false;
                }else {
                    $scope.errorFlag = true;
                    $scope.errorInfo="请输入数字";
                    return;
                }
            }
            $scope.useful=true;
            //提交信息
            $scope.confirminfo = [];
            //地址信息
            $scope.addressinfo = [];
            //产品信息
            $scope.products = [];
            if($scope.type.code == '001' || $scope.type.code == '003' || $scope.type.code == '004' ){  //如果是翻新和检测需要传入产品信息
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
                $scope.products.push($scope.product);
                var obj = {};
                obj.userId = localStorage.getItem("jinlele_userId");//用户id
                obj.type = $scope.type.code;    //翻新001维修002检测003回收004换款005
                obj.storeId = $scope.order.storeId;//后续需要根据客户选择传入
                obj.sendWay=$scope.order.sendway;     //送货方式
                obj.getWay=$scope.order.getway;    //取货方式
                obj.totalprice = $scope.totalprice;//总价格
                obj.addressinfo = $scope.addressinfo;//地址信息
                obj.serviceId = $scope.serviceId;//服务id
                obj.totalnum = $scope.totalnum;//总数量
                obj.products = $scope.products;//产品集合
                $scope.confirminfo.push(obj);
                console.log(JSON.stringify($scope.confirminfo));
                //保存订单 并去支付订单
                ProcCommitOrderService.createServiceOrder($scope.confirminfo).success(function (data) {
                    if (data) {
                        $scope.useful=false;
                        //调用支付接口
                        if($scope.type.code == '004') {   //如果是回收订单无需付款，直接进入平台收货页面
                            $state.go('procreceive', {
                                type: $scope.type.code,
                                orderNo: data.orderNo
                            });
                            return;
                        }
                        $scope.param = {
                            totalprice: 0.01, //data.totalprice
                            orderNo: data.orderNo,
                            descrip: '六唯壹珠宝',
                            openid: localStorage.getItem("openId"),
                            orderType:JSON.stringify({type:$scope.type.code})
                        };
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
                                            $state.go("servicedetail", {orderNo: $scope.param.orderNo ,orderType:$scope.type.code});
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
                obj.storeId = $scope.order.storeId;//后续需要根据客户选择传入
                obj.sendWay=$scope.order.sendway;     //送货方式
                obj.getWay=$scope.order.getway;    //取货方式
                obj.totalprice = $scope.totalprice;//总价格
                obj.addressinfo = $scope.addressinfo;//地址信息
                obj.orderno = $scope.orderno;
                $scope.confirminfo.push(obj);
                console.log(JSON.stringify($scope.confirminfo));
                //保存订单 并去支付订单
                ProcCommitOrderService.updateRepairOrder($scope.confirminfo).success(function (data) {
                    console.log('data='+JSON.stringify(data));
                    if (data) {
                        $scope.useful=false;
                        //调用支付接口
                        $scope.param = {
                            totalprice: 0.01, //data.totalprice
                            orderNo: data.orderNo,
                            descrip: '六唯壹珠宝',
                            openid: localStorage.getItem("openId"),
                            orderType: JSON.stringify({type: $scope.type.code})
                        };
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
                                            $state.go("servicedetail", {orderNo: $scope.param.orderNo ,orderType:$scope.type.code});
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
            if($scope.type.code =='005'){
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
                obj.storeId = $scope.order.storeId;//后续需要根据客户选择传入
                obj.sendWay=$scope.order.sendway;     //送货方式
                obj.getWay=$scope.order.getway;    //取货方式
                obj.addressinfo = $scope.addressinfo;//地址信息
                obj.serviceId = localStorage.getItem("barterServiceId");//服务id
                $scope.confirminfo.push(obj);
                console.log(JSON.stringify($scope.confirminfo));
                OrderService.addBarterInfo($scope.confirminfo).success(function(data){
                    $scope.useful=false;
                    if(data&&data.errmsg=="ok") {
                        $state.go("serviceProgress", {type: "005", orderno: data.orderNo});
                    }
                });
            }
        }
    })
    //流程-平台收货(五大类服务展示物流状态及收货证明)
    .controller('ProcReceiveCtrl', ['$rootScope','$scope', '$stateParams' , 'OrderService' ,'CommonService' ,'ServeCommonService',function ($rootScope,$scope, $stateParams , OrderService ,CommonService ,ServeCommonService) {
        $rootScope.commonService = CommonService;
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;//页面呈现主题
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
        };
        //参数
        $scope.order = {
            userlogisticsnoComp:"",//买方发货快递公司编码
            userlogisticsno:"", //买方发货单号
            orderstatus:""//订单状态
        };
        //物流公司
        $scope.userlogisticsnoConfig= {
            data: [],
            placeholder: '请选择物流公司'
        };
        //去后台查询物流数据
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            angular.forEach(data.express,function(item,index){
                var obj={};
                obj.id=item.number;
                obj.text=item.company;
                $scope.userlogisticsnoConfig.data.push(obj);
            });
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //保存客户填写的物流信息
        $scope.saveExpress = function () {
            if (!$scope.order.userlogisticsnoComp) {
                CommonService.toolTip("请选择物流公司", "");
                return;
            }
            if (!$scope.order.userlogisticsno) {
                CommonService.toolTip("请填写快递单号", "");
                return;
            }
            //根据业务类型判断订单状态
            switch ($stateParams.type) {
                case "002":
                    $scope.order.orderstatus = $stateParams.type + "005";
                    break;
                case "004":
                case "005":
                    $scope.order.orderstatus = $stateParams.type + "002";
                    break;
                default://翻新、检测
                    $scope.order.orderstatus = $stateParams.type + "003";
                    break;
            }
            //'003'代表的订单状态:已发货
            OrderService.update({
                orderno: $stateParams.orderNo,
                userlogisticsnocomp: $scope.order.userlogisticsnoComp,
                userlogisticsno: $scope.order.userlogisticsno,
                shoporderstatuscode:$scope.order.orderstatus
            }).success(function (data) {
                if (data.n == 1) {
                    //重新更新数据
                    OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
                        $scope.orderInfo = data.order;
                        angular.forEach(data.express,function(item,index){
                            var obj={};
                            obj.id=item.number;
                            obj.text=item.company;
                            $scope.userlogisticsnoConfig.data.push(obj);
                        });
                        if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
                    });
                }
            });
        };
        //修改物流信息
        $scope.editUsrLogisticsInfo=function(){
            $scope.orderInfo.userlogisticsno=null;
        }
    }])
    //流程-检测(五大类服务检测报告)
    .controller('ProcTestCtrl',['$scope', '$stateParams','OrderService','MemberService','ServeCommonService', function ($scope, $stateParams,OrderService,MemberService,ServeCommonService) {
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$stateParams.orderNo}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
    }])
    //流程-检测报告
    .controller('CheckReportCtrl',['$scope', '$stateParams', '$location','OrderService','MemberService','ServeCommonService', function ($scope, $stateParams, $location,OrderService,MemberService,ServeCommonService) {
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;//页面呈现主题
        $scope.orderType=$stateParams.type;
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderno}).success(function (data) {
            $scope.orderInfo = data.order;console.log(data);
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$stateParams.orderno}).success(function(data){
            if(data.checkreport) {
                $scope.report = data;
            }else{
                $scope.report = null;
            }
        });
    }])
    //流程-邮寄(五大类服务返回产品物流)
    .controller('ProcPostCtrl', ['$scope', '$stateParams', '$location','OrderService','ServeCommonService', function ($scope, $stateParams, $location,OrderService,ServeCommonService) {
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;
        $scope.type =$stateParams.type;
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
    }])
    //流程-验货(五大类服务用户收货验收)
    .controller('ProcCheckCtrl',['$scope', '$stateParams', '$location','OrderService','ServeCommonService', function ($scope, $stateParams, $location,OrderService,ServeCommonService) {
        if ($stateParams.type == "004") {//回收
            $location.path("/");
        }
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        //加载发货证明图片
        OrderService.getPostImg({orderno:$stateParams.orderNo}).success(function(data){
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
        };
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
            $scope.initData = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
            if(data.storeLogistc)$scope.sellerLogistc = data.storeLogistc.Traces;
        });
        //描述等级
        $scope.currentId=5;
        $scope.colors = [{id:1},{id:2},{id:3},{id:4},{id:5}];
        $scope.paint=function(id) {
            $scope.currentId = id + 1;
        };
        OrderService.getOrderDetailInfo({orderno: $stateParams.orderno}).success(function (data) {
            $scope.orderinfo = data.order;//订单总信息
            $scope.servicedetail = data.servicedetail;//地址详情
            $scope.address = data.address;//地址详情
        });
        $scope.content="";//评论内容

        $scope.localIds = [];// 上传图片的微信路径 数组
        WeiXinService.mediaIds = []; //媒体id数组
        $scope.imgSrcs=[];//显示的图片src数组
        $scope.count=0;//记录图片src总数
        $scope.perUploadNumber=5;//每次上传数量
        //上传图片
        $scope.wxchooseImage = function () {
            if($scope.count<5) {
                //通过config接口注入权限验证配置
                WeiXinService.weichatConfig(localStorage.getItem("timestamp"), localStorage.getItem("noncestr"), localStorage.getItem("signature"));
                //通过ready接口处理成功验证
                wx.ready(function () {
                    WeiXinService.wxchooseImage(function (localIds) {
                        for (var i = 0; i < localIds.length; i++) {
                            $scope.imgSrcs.push(localIds[i]);
                        }
                        $scope.count = $scope.imgSrcs.length;
                        $scope.perUploadNumber = 5 - $scope.imgSrcs.length;
                        $scope.localIds = $scope.imgSrcs;
                        $scope.$apply();
                    }, $scope.perUploadNumber)
                })
            }else{
                CommonService.toolTip("最多上传五张图片", "");
            }
        };
        //删除图片
        $scope.delthisImage=function(index){
            $scope.imgSrcs.splice(index,1);
            WeiXinService.mediaIds.splice(index,1);
            $scope.count = $scope.imgSrcs.length;
            $scope.perUploadNumber = 5 - $scope.imgSrcs.length;
        };
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
            iteminfo.mediaIds = WeiXinService.mediaIds;
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
    //翻新-翻新
    .controller('ProcRefurbishCtrl',['$scope', '$stateParams', '$location','OrderService','MemberService','ServeCommonService', function ($scope, $stateParams, $location,OrderService,MemberService,ServeCommonService) {
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;//页面呈现主题
        if ($stateParams.type != "001") {
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$stateParams.orderNo}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$stateParams.orderNo}).success(function(data){
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
        $scope.fixPrice = 0;
        //根据订单号查询是否已经定价
        OrderService.selectActualPrice({orderNo:$stateParams.orderno}).success(function (data){
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
        };
        //确认维修
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
    .controller('ProcRepairCtrl',['$scope', '$stateParams','$location','OrderService','MemberService','ServeCommonService',  function ($scope, $stateParams,$location,OrderService,MemberService,ServeCommonService) {
        $scope.pagetheme = ServeCommonService.getName($stateParams.type).name;//页面呈现主题
        if ($stateParams.type != "002") {
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
        };
        //获取买方地址信息及物流进度
        OrderService.findReceiptServiceByOrderno({orderNo:$stateParams.orderNo}).success(function (data) {
            $scope.orderInfo = data.order;
            if(data.userLogistc)$scope.userLogistc = data.userLogistc.Traces;
        });
        //收货证明
        OrderService.getCertifyInfo({orderno:$stateParams.orderNo}).success(function (data) {
            $scope.certificationInfo = data;
        });
        //用户信息
        MemberService.getUserInfo(localStorage.getItem("openId")).success(function(data) {
            $scope.user = data.userInfo;
        });
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$stateParams.orderNo}).success(function(data){
            if(data.checkreport) {
                $scope.report = data;
            }else{
                $scope.report = null;
            }
        });
    }])
    //估价(回收、换款)
    .controller('EvaluateCtrl', ['$rootScope','$scope','$state','$stateParams','EvaluateService','CommonService',function ($rootScope,$scope ,$state,$stateParams,EvaluateService,CommonService) {
        $rootScope.commonService = CommonService;
        $scope.pagetheme = $stateParams.name;
        localStorage.setItem("toBarterGoodId", $stateParams.gid ? $stateParams.gid : 0);
        localStorage.setItem("toBarterGoodChildId", $stateParams.gcid ? $stateParams.gcid : 0);
        var mySwiper = new Swiper('.metal', {
            pagination: '.product_tab',
            paginationClickable: true,
            //autoHeight: true,
            paginationBulletRender: function (index, className) {
                switch (index) {
                    case 0:
                        name = '黄金';
                        break;
                    case 1:
                        name = '铂金';
                        break;
                    case 2:
                        name = 'K金';
                        break;
                    case 3:
                        name = '钯金';
                        break;
                    case 4:
                        name = '白银';
                        break;
                    default:
                        name = '';
                }
                return '<a href="javascript:" class="' + className + '">' + name + '</a>';
            }
        });
        EvaluateService.getCurrentPrice().success(function (data) {
            $scope.dayprice = data.dayprice;
        });
        //黄金
        $scope.goldTypeConfig = {
            data: [{id: 120, text: '首饰'}, {id: 125, text: '金条'}],
            minimumResultsForSearch: -1
        };
        $scope.goldType = 120;
        $scope.goldPurityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "metalCode", pid: $scope.goldType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.goldPurityConfig.data.push(obj);
                if (index == 0) {
                    $scope.goldPurity = item.codevalue;
                }
            })
        });
        $scope.getGoldPurity = function (type) {
            EvaluateService.getSubSet({category: "metalCode", pid: type}).success(function (data) {
                $scope.goldPurityConfig.data = [];
                angular.forEach(data.result, function (item, index) {
                    var obj = {};
                    obj.id = item.codevalue;
                    obj.text = item.dictname;
                    $scope.goldPurityConfig.data.push(obj);
                    if (index == 0) {
                        $scope.goldPurity = item.codevalue;
                    }
                })
            })
        };
        //铂金
        $scope.boPurityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "metalCode", pid: 121}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.boPurityConfig.data.push(obj);
                if (index == 0) {
                    $scope.boPurity = item.codevalue;
                }
            })
        });
        //K金
        $scope.kPurityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "metalCode", pid: 122}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.kPurityConfig.data.push(obj);
                if (index == 0) {
                    $scope.kPurity = item.codevalue;
                }
            })
        });
        //钯金
        $scope.baPurityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "metalCode", pid: 123}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.baPurityConfig.data.push(obj);
                if (index == 0) {
                    $scope.baPurity = item.codevalue;
                }
            })
        });
        //白银
        $scope.silverTypeConfig = {
            data: [{id: 124, text: '首饰'}, {id: 126, text: '银条'}],
            minimumResultsForSearch: -1
        };
        $scope.silverType = 124;
        $scope.silverPurityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "metalCode", pid: $scope.silverType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.silverPurityConfig.data.push(obj);
                if (index == 0) {
                    $scope.silverPurity = item.codevalue;
                }
            })
        });
        $scope.getSilverPurity = function (type) {
            EvaluateService.getSubSet({category: "metalCode", pid: type}).success(function (data) {
                $scope.silverPurityConfig.data = [];
                angular.forEach(data.result, function (item, index) {
                    var obj = {};
                    obj.id = item.codevalue;
                    obj.text = item.dictname;
                    $scope.silverPurityConfig.data.push(obj);
                    if (index == 0) {
                        $scope.silverPurity = item.codevalue;
                    }
                })
            })
        };
        $scope.checkMetal = function (metalType) {
            switch (metalType) {
                case 1:
                    if (/^\d+(\.\d{1,3})?$/.test($scope.goldWeight)) {
                        $scope.gerrorFlag = false;
                    } else {
                        $scope.gerrorFlag = true;
                        $scope.gerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 2:
                    if (/^\d+(\.\d{1,3})?$/.test($scope.boWeight)) {
                        $scope.boerrorFlag = false;
                    } else {
                        $scope.boerrorFlag = true;
                        $scope.boerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 3:
                    if (/^\d+(\.\d{1,3})?$/.test($scope.kWeight)) {
                        $scope.kerrorFlag = false;
                    } else {
                        $scope.kerrorFlag = true;
                        $scope.kerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 4:
                    if (/^\d+(\.\d{1,3})?$/.test($scope.baWeight)) {
                        $scope.berrorFlag = false;
                    } else {
                        $scope.berrorFlag = true;
                        $scope.berrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 5:
                    if (/^\d+(\.\d{1,3})?$/.test($scope.silverWeight)) {
                        $scope.serrorFlag = false;
                    } else {
                        $scope.serrorFlag = true;
                        $scope.serrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
            }
        };
        //贵金属估价
        $scope.calcPMPrice = function (metalType) {
            var obj = {};
            obj.goodId = localStorage.getItem("toBarterGoodId");
            obj.goodChildId = localStorage.getItem("toBarterGoodChildId");
            switch (metalType) {
                case 1:
                    obj.purity = $scope.goldPurity;
                    obj.weight = $scope.goldWeight;
                    if (/^\d+(\.\d{1,3})?$/.test($scope.goldWeight)) {
                        $scope.gerrorFlag = false;
                    } else {
                        $scope.gerrorFlag = true;
                        $scope.gerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 2:
                    obj.purity = $scope.boPurity;
                    obj.weight = $scope.boWeight;
                    if (/^\d+(\.\d{1,3})?$/.test($scope.boWeight)) {
                        $scope.boerrorFlag = false;
                    } else {
                        $scope.boerrorFlag = true;
                        $scope.boerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 3:
                    obj.purity = $scope.kPurity;
                    obj.weight = $scope.kWeight;
                    if (/^\d+(\.\d{1,3})?$/.test($scope.kWeight)) {
                        $scope.kerrorFlag = false;
                    } else {
                        $scope.kerrorFlag = true;
                        $scope.kerrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 4:
                    obj.purity = $scope.baPurity;
                    obj.weight = $scope.baWeight;
                    if (/^\d+(\.\d{1,3})?$/.test($scope.baWeight)) {
                        $scope.berrorFlag = false;
                    } else {
                        $scope.berrorFlag = true;
                        $scope.berrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
                case 5:
                    obj.purity = $scope.silverPurity;
                    obj.weight = $scope.silverWeight;
                    if (/^\d+(\.\d{1,3})?$/.test($scope.silverWeight)) {
                        $scope.serrorFlag = false;
                    } else {
                        $scope.serrorFlag = true;
                        $scope.serrorInfo = "请输入正确范围的数字(最多三位小数)";
                        return;
                    }
                    break;
            }
            console.log(JSON.stringify(obj));
            EvaluateService.getPMPrice({
                weight: obj.weight,
                purity: obj.purity,
                goodId: obj.goodId,
                goodChildId: obj.goodChildId
            }).success(function (data) {
                if (data) {
                    if ($stateParams.name == "exchange") {
                        $state.go("showResult");
                        localStorage.setItem("evaluationPrice", data.result);
                        localStorage.setItem("barterServiceId", data.evaluateServiceId);
                    } else {
                        $state.go('evaluationresult', {name: $stateParams.name, result: JSON.stringify(data)});
                    }
                }
            });
        };
        //钻石
        $scope.certificateType = 217;
        $scope.certificateConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.certificateType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.certificateConfig.data.push(obj);
                if (index == 0) {
                    $scope.certificate = item.codevalue;
                }
            })
        });
        $scope.colorType = 218;
        $scope.colorConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.colorType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.colorConfig.data.push(obj);
                if (index == 0) {
                    $scope.color = item.codevalue;
                }
            })
        });
        $scope.cleanessType = 219;
        $scope.cleanessConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.cleanessType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.cleanessConfig.data.push(obj);
                if (index == 0) {
                    $scope.cleaness = item.codevalue;
                }
            })
        });
        $scope.florescenceType = 223;
        $scope.florescenceConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.florescenceType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.florescenceConfig.data.push(obj);
                if (index == 0) {
                    $scope.florescence = item.codevalue;
                }
            })
        });
        $scope.cutType = 220;
        $scope.cutConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.cutType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.cutConfig.data.push(obj);
                if (index == 0) {
                    $scope.cut = item.codevalue;
                }
            })
        });
        $scope.symmetryType = 222;
        $scope.symmetryConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.symmetryType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.symmetryConfig.data.push(obj);
                if (index == 0) {
                    $scope.symmetry = item.codevalue;
                }
            })
        });
        $scope.polishType = 221;
        $scope.polishConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getSubSet({category: "dwparam", pid: $scope.polishType}).success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.polishConfig.data.push(obj);
                if (index == 0) {
                    $scope.polish = item.codevalue;
                }
            })
        });
        $scope.materialConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getMaterial().success(function (data) {
            angular.forEach(data.result, function (item, index) {
                if (item.dictname == "Pt950" || item.dictname == "Pt900" || item.dictname == "Pd950" || item.dictname == "Pd900" || item.dictname == "18K" || item.dictname == "14K" || item.dictname == "9K") {
                    var obj = {};
                    obj.id = item.codevalue;
                    obj.text = item.dictname;
                    $scope.materialConfig.data.push(obj);
                    $scope.material = $scope.materialConfig.data[0].id;
                }
            })
        });
        $scope.qualityConfig = {
            data: [],
            minimumResultsForSearch: -1
        };
        EvaluateService.getQuality().success(function (data) {
            angular.forEach(data.result, function (item, index) {
                var obj = {};
                obj.id = item.codevalue;
                obj.text = item.dictname;
                $scope.qualityConfig.data.push(obj);
                if (index == 0) {
                    $scope.quality = item.codevalue;
                }
            })
        });
        $scope.choice = true;
        $scope.change = function ($event) {
            var choose = $event.target;
            $("input[name='" + choose.name + "']").siblings("label").removeClass("on");
            $("#" + choose.id).siblings("label").addClass("on");
            switch (choose.value) {
                case "1":
                    $scope.choice = true;
                    break;
                case "2":
                    $scope.choice = false;
                    break;
            }
        };

        $scope.checkmainWeight = function (index,x) {
            if(index == 0){
                if (/^\d+(\.\d{1,3})?$/.test(x)) {
                    $scope.merrorFlag = false;
                    $scope.errorFlag = false;
                } else {
                    $scope.merrorFlag = true;
                    $scope.errorFlag = true;
                    $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                    return;
                }
                if (x >= 10.999 || x <0.001) {
                    $scope.merrorFlag = true;
                    $scope.errorFlag = true;
                    $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                    return;
                } else {
                    $scope.merrorFlag = false;
                    $scope.errorFlag = false;
                }
            }
            if(index == 1){
                if (/^\d+(\.\d{1,3})?$/.test(x)) {
                    $scope.serrorFlag = false;
                    $scope.errorFlag = false;
                } else {
                    $scope.serrorFlag = true;
                    $scope.errorFlag = true;
                    $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                    return;
                }

                if (x >= 10.999 || x <0.001) {
                    $scope.serrorFlag = true;
                    $scope.errorFlag = true;
                    $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                    return;
                } else {
                    $scope.serrorFlag = false;
                    $scope.errorFlag = false;
                }
            }
            if(index == 2){
                if (/^\d+(\.\d{1,3})?$/.test(x)) {
                    $scope.terrorFlag = false;
                    $scope.errorFlag = false;
                } else {
                    $scope.terrorFlag = true;
                    $scope.errorFlag = true;
                    $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                    return;
                }
            }

        }

        //钻石估价
        $scope.calcDiamondPrice = function () {
            $scope.paras = [];
            if (/^\d+(\.\d{1,3})?$/.test($scope.mainWeight)) {
                $scope.merrorFlag = false;
                $scope.errorFlag = false;
            } else {
                $scope.merrorFlag = true;
                $scope.errorFlag = true;
                $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                return;
            }
            if ($scope.mainWeight >= 10.999 || $scope.mainWeight <0.001) {
                $scope.merrorFlag = true;
                $scope.errorFlag = true;
                $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                return;
            } else {
                $scope.merrorFlag = false;
                $scope.errorFlag = false;
            }
            if (/^\d+(\.\d{1,3})?$/.test($scope.secWeight)) {
                $scope.serrorFlag = false;
                $scope.errorFlag = false;
            } else {
                $scope.serrorFlag = true;
                $scope.errorFlag = true;
                $scope.errorInfo = "请输入正确范围的数字(最多三位小数)";
                return;
            }
            if ($scope.secWeight >= 10.999 || $scope.secWeight <0.001) {
                $scope.merrorFlag = true;
                $scope.errorFlag = true;
                $scope.errorInfo = "请输入正确范围的数字(0.001~10.999)";
                return;
            } else {
                $scope.merrorFlag = false;
                $scope.errorFlag = false;
            }
            if (/^\d+(\.\d{1,3})?$/.test($scope.totalWeight)) {
                $scope.terrorFlag = false;
                $scope.errorFlag = false;
            } else {
                $scope.terrorFlag = true;
                $scope.errorFlag = true;
                $scope.errorInfo = "请输入正确范围的数字(最多三位小数)";
                return;
            }
            if ($scope.choice) {
                //主石+副石+镶嵌材质
                var obj = {};
                obj.mainWeight = $scope.mainWeight;
                obj.certificate = $scope.certificate;
                obj.color = $scope.color;
                obj.cleaness = $scope.cleaness;
                obj.florescence = $scope.florescence;
                obj.cut = $scope.cut;
                obj.symmetry = $scope.symmetry;
                obj.polish = $scope.polish;
                obj.material = $scope.material;
                obj.secWeight = $scope.secWeight;
                obj.quality = $scope.quality;
                obj.totalWeight = $scope.totalWeight;
                obj.flag = $scope.choice;
                obj.src = $stateParams.name;
                obj.goodId = localStorage.getItem("toBarterGoodId");
                obj.goodChildId = localStorage.getItem("toBarterGoodChildId");
                $scope.paras.push(obj);
                EvaluateService.getDiamondPrice($scope.paras).success(function (data) {
                    console.log(data);
                    if (data) {
                        if(data.status=="error"){
                            CommonService.toolTip('该重量的估价没有数据','');
                            return;
                        }
                        if ($stateParams.name == "exchange") {
                            $state.go("showResult");
                            localStorage.setItem("evaluationPrice", data.result);
                            localStorage.setItem("barterServiceId", data.evaluateServiceId);
                        } else {
                            $state.go('evaluationresult', {name: $stateParams.name, result: JSON.stringify(data)});
                        }
                    }
                });
            } else {
                //副石+镶嵌材质
                var obj = {};
                obj.mainWeight = $scope.mainWeight;
                obj.material = $scope.material;
                obj.secWeight = $scope.secWeight;
                obj.quality = $scope.quality;
                obj.totalWeight = $scope.totalWeight;
                obj.flag = $scope.choice;
                $scope.paras.push(obj);
                EvaluateService.getDiamondPrice($scope.paras).success(function (data) {
                    console.log(data);
                    if (data) {
                        if ($stateParams.name == "exchange") {
                            $state.go("showResult");
                            localStorage.setItem("evaluationPrice", data.result);
                            localStorage.setItem("barterServiceId", data.evaluateServiceId);
                        } else {
                            $state.go('evaluationresult', {name: $stateParams.name, result: JSON.stringify(data)});
                        }
                    }
                });
            }
        }
    }])
    //估价结果(回收、换款)
    .controller('EvaluationResultCtrl' , ['$scope' , '$stateParams',function ($scope , $stateParams) {
        $scope.pagetheme = $stateParams.name;
        $scope.result=JSON.parse($stateParams.result);
        //这里要带入的是 估价价格
        localStorage.setItem("evaluationPrice" , $scope.result.result);
        console.log('$scope.name ==' + $scope.pagetheme);
    }])
    //实际定价(回收、换款)
    .controller('ActualPriceCtrl',['$scope','$state','$stateParams','OrderService',function ($scope,$state, $stateParams,OrderService) {
        switch ($stateParams.type){
            case '004':
                $scope.pagetheme = 'recycle';
                break;
            case '005':
                $scope.pagetheme = 'exchange';
                break;
            default :
                $state.go("main");
                break;
        }
        //检测报告
        OrderService.getServiceDetailInfo({orderno:$stateParams.orderno}).success(function(data){
            console.log(data);
            if(data.checkreport) {
                $scope.report = data;
            }else{
                $scope.report = null;
            }
        });
    }])
    //确认回收
    .controller('CfmRecycleCtrl', ['$scope' ,'$state','$stateParams','OrderService','CommonService',function ($scope ,$state, $stateParams,OrderService,CommonService) {
        $scope.orderstatus = $stateParams.orderstatus;
        switch ($scope.orderstatus){
            case '004005':
            case '005012':
                //根据订单号查询实际定价金额
                OrderService.selectActualPrice({orderNo:$stateParams.orderno}).success(function (data){
                    if(data && data.fixPrice){
                        $scope.fixPrice = data.fixPrice;
                    }
                });
                break;
            case '004006':
            case '005013':
                break;
            default:
                $state.go("main");
                break;
        }
        //确认变现
        $scope.confirmCash = function () {
            switch($scope.orderstatus.substring(0,3)){
                case '004':
                    $scope.orderstatus = '004006';//回收业务（待审核）
                    break;
                case '005':
                    $scope.orderstatus = '005013';//换款业务（待审核）
                    break;
            }
            OrderService.update({
                orderno: $stateParams.orderno,
                shoporderstatuscode: $scope.orderstatus
            }).success(function (data) {
                if (data && data.n == 1) {
                    setTimeout(function () {
                        $state.go('orderlist');
                    }, 1000);
                }
            });
        };
        //放弃变现
        $scope.dropCash = function () {
            var tip;
            switch($scope.orderstatus.substring(0,3)){
                case '004':
                    $scope.orderstatus = '004009';//回收业务（待返回）
                    tip="您已放弃变现，请耐心等待物品原样返回";
                    break;
                case '005':
                    $scope.orderstatus = '005014';//换款业务（待返回）
                    tip="您已放弃换款与变现，请耐心等待物品原样返回";
                    break;
            }
            OrderService.update({
                orderno: $stateParams.orderno,
                shoporderstatuscode: $scope.orderstatus
            }).success(function (data) {
                if (data && data.n == 1) {
                    CommonService.toolTip(tip, '');
                    setTimeout(function () {
                        $state.go('orderlist');
                    }, 1000);
                }
            });
        }
    }])
    //确认换款
    .controller('CfmExchangeCtrl', ['$scope','$state','$stateParams','OrderService',function ($scope,$state,$stateParams,OrderService) {
        $scope.orderstatus=$stateParams.orderstatus;
        switch ($scope.orderstatus){
            case '005005':
                //根据订单号查询实际定价金额
                OrderService.selectActualPrice({orderNo:$stateParams.orderno}).success(function (data){
                    if(data && data.fixPrice){
                        $scope.fixPrice = data.fixPrice;
                    }
                });
                break;
            default:
                $state.go("main");
                break;
        }
        //确认换款
        $scope.confirmBarter = function () {
            localStorage.setItem("actualprice",$scope.fixPrice);
            localStorage.setItem("exchangeorderno",$stateParams.orderno);
            OrderService.update({orderno:$stateParams.orderno,shoporderstatuscode:'005006'}).success(function (data) {
                if(data && data.n==1){
                    if(localStorage.getItem("toExchangeGoodId")==""||localStorage.getItem("toExchangeGoodId")==null) {
                        $state.go('barterlist');
                    }else {
                        //具体商品详情
                        $state.go('barterdetail', {goodId: localStorage.getItem("toExchangeGoodId")});
                    }
                }
            });
        };
        //放弃换款
        $scope.dropBarter = function () {
            OrderService.update({orderno:$stateParams.orderno,shoporderstatuscode:'005012'}).success(function (data) {
                if(data && data.n==1){
                    $state.go('cfmrecycle', {orderno: $stateParams.orderno,orderstatus:'005012'});
                }
            });
        }
    }])
    //换款列表
    .controller('BarterListCtrl', ['$scope','GoodService',function ($scope,GoodService) {
        $scope.evaluatePrice=localStorage.getItem("evaluationPrice");
        $scope.barterlistinfo = [];
        $scope.page = 0;//当前页数
        $scope.total = 1;//总页数
        $scope.moreFlag = false; //是否显示加载更多
        $scope.noDataFlag = false;  //没有数据显示
        $scope.getBarterList = function () {
            if ((arguments != [] && arguments[0] == 0)) {
                $scope.page = 0;
                $scope.barterlistinfo = [];
            }
            $scope.page++;
            $scope.moreFlag = false;
            $scope.noDataFlag = false;
            //分页显示
            GoodService.getBarterList({
                amount: 0,
                pagenow: $scope.page,
                type: "all"
            }).success(function (data) {
                angular.forEach(data.pagingList, function (item) {
                    $scope.barterlistinfo.push(item);
                });
                if (data.myrows == 0) $scope.noDataFlag = true;
                $scope.total = data.myrows;
                if ($scope.total > $scope.barterlistinfo.length) {
                    $scope.moreFlag = true;
                }
            })
        };
        $scope.getBarterList();
    }])
    //换款详情
    .controller('BarterDetailCtrl', ['$rootScope','$scope','$state','$stateParams','GoodService','CommonService','WeiXinService','OrderService','WalletService','EvaluateService',function ($rootScope,$scope,$state,$stateParams,GoodService,CommonService,WeiXinService,OrderService,WalletService,EvaluateService) {
        $rootScope.commonService = CommonService;
        $scope.evaluatePrice = localStorage.getItem("evaluationPrice");
        function getBanners(arr) {
            var html = "";
            if (arr) {
                for (var i = 0, len = arr.length; i < len; i++) {
                    html += "<li class='swiper-slide'><img src='" + arr[i].imgurl + "' style='height:360px;'></li>";
                }
            }
            $(".banner .swiper-wrapper").html(html);
            var swiper = new Swiper('.banner', {
                pagination: '.spot',
                paginationClickable: true,
                autoplay: false
            });
        }

        //初始化参数
        $scope.balance = 0;//账户余额

        WalletService.getBalance({userId: localStorage.getItem("jinlele_userId")}).success(function (data) {
            $scope.balance = data.balance;
        });

        $scope.carData = {cartotalnum: 0, cartotalprice: 0};
        EvaluateService.getShopcharTotal({serviceId: localStorage.getItem("barterServiceId")}).success(function (data) {
            $scope.totalnum = data.totalnum;
            if(data.echeck){
                $scope.carData.cartotalnum = data.echeck.cartotalnum;
                $scope.carData.cartotalprice = data.echeck.cartotalprice;
            }
            $scope.totalprice =  $scope.carData.cartotalprice - $scope.evaluatePrice;//预选合计总金额
        });


        $scope.bannerurl = "";
        $scope.stocknum = 0;//库存数
        $scope.menuWidth = {"width": "33.333%"};
        $scope.gooddetail = {
            userId: localStorage.getItem("jinlele_userId"),
            goodId: $stateParams.goodId,
            goodchildId: "",
            num: 1
        };
        $scope.totalprice = 0;//合计
        GoodService.getGoodDetail({
            goodId: $stateParams.goodId,
            userId: $scope.gooddetail.userId
        }).success(function (data) {
            console.log("getGoodDetail==" + JSON.stringify(data));
            $scope.goodDetail = data.good;
            $scope.goodChilds = data.goodchilds;
            $scope.favourites = data.favourites;
            $scope.bannerurl = data.imgurls;
            $scope.bannerurl.splice(0, 0, {"imgurl": data.good.bannerurl});
            getBanners($scope.bannerurl);
            $scope.price = data.good.minprice;
            $scope.exprice = data.good.minexprice;
            $scope.stocknum = data.good.stocknum;
            if ($scope.goodChilds && $scope.goodChilds.length > 0) {
                angular.forEach($scope.goodChilds, function (item) {
                    item.flag = false;
                });
            }
            $scope.haspriceflag = ($scope.evaluatePrice-$scope.exprice*$scope.gooddetail.num)>0? true : false;//剩余金额
            $scope.hasprice = Math.abs($scope.exprice*$scope.gooddetail.num - $scope.evaluatePrice) ;//补的金额
            console.log("$scope.goodChilds==" + JSON.stringify($scope.goodChilds));
            console.log("$scope.evaluatePrice==" +$scope.evaluatePrice);
            console.log("$scope.cartotalprice==" +$scope.carData.cartotalprice);
            console.log("$scope.totalprice==" +$scope.totalprice);
            console.log("$scope.hasprice==" +$scope.hasprice);
            console.log("$scope.exprice==" +$scope.exprice);
        });
        GoodService.getGoodCommentCount({goodId: $stateParams.goodId}).success(function (data) {
            $scope.goodcommentcount = data.total;
        });
        GoodService.getGoodComments({goodId: $stateParams.goodId, pagenow: 1}).success(function (data) {
            $scope.goodcomments = data.comments;
        });
        $scope.changeNum = function () {
            if (!/^\+?[1-9][0-9]*$/.test($scope.gooddetail.num)) {
                $scope.gooddetail.num = 1;
            }
            if ($scope.gooddetail.num > $scope.stocknum) {
                $scope.gooddetail.num = $scope.stocknum;
            }
        };
        //选择数量时:动态改变价格剩补价，和合计金额
        $scope.changePrice =  function(){
            $scope.totalprice = $scope.gooddetail.num * $scope.exprice  + $scope.carData.cartotalprice -  $scope.evaluatePrice;
            $scope.hasprice = Math.abs($scope.totalprice);
            if($scope.totalprice>0) {
                $scope.haspriceflag = false;
            }else if($scope.totalprice<0){
                $scope.haspriceflag = true;
            }else {
                $scope.haspriceflag = '';
            }
        }
        $scope.addNum = function () {
            if ($scope.gooddetail.num < $scope.stocknum) {
                $scope.gooddetail.num++;
                $scope.changePrice();
            }
        };
        $scope.minusNum = function () {
            if ($scope.gooddetail.num > 1) {
                $scope.gooddetail.num--;
                $scope.changePrice();
            }
        };
        //购物车结算
        $scope.settleAccounts = function () {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $scope.goodInfo = [];
            var obj = {};
            obj.serviceId = localStorage.getItem("barterServiceId");
            obj.id = $scope.gooddetail.goodId;
            obj.childId = $scope.gooddetail.goodchildId;
            obj.buynum = $scope.gooddetail.num;
            obj.checked = 1;
            $scope.goodInfo.push(obj);
            OrderService.addBarterCart($scope.goodInfo).success(function (data) {
                if (data && data.errmsg == "ok") {
                    $state.go("bartercart");//跳转购物车
                }
            });
        };
        //返回上一页
        $scope.back = function () {
            window.history.go(-1);
        };
        //添加购物车
        $scope.addToCar = function () {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $scope.goodInfo = [];
            var obj = {};
            obj.serviceId = localStorage.getItem("barterServiceId");
            obj.id = $scope.gooddetail.goodId;
            obj.childId = $scope.gooddetail.goodchildId;
            obj.buynum = $scope.gooddetail.num;
            obj.checked = 0;
            $scope.goodInfo.push(obj);
            OrderService.addBarterCart($scope.goodInfo).success(function (data) {
                if (data && data.errmsg == "ok") {
                    CommonService.toolTip("添加成功", "tool-tip-message-success");
                }
                if (data && data.errmsg == "error") {
                    CommonService.toolTip("已添加", "tool-tip-message-success");
                }
                $scope.totalnum = data.totalnum;
            });
        };

        //选多几款
        $scope.selectMore = function () {
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $scope.goodInfo = [];
            var obj = {};
            obj.serviceId = localStorage.getItem("barterServiceId");
            obj.id = $scope.gooddetail.goodId;
            obj.childId = $scope.gooddetail.goodchildId;
            obj.buynum = $scope.gooddetail.num;
            obj.checked = 1;
            $scope.goodInfo.push(obj);
            OrderService.addBarterCart($scope.goodInfo).success(function (data) {
                $scope.totalnum = data.totalnum;
                if (data) {
                    $state.go("showResult");//跳转筛选页
                }
            });
        };
        //换此款
        $scope.selectThis = function () {
            //添加购物车
            if (!$scope.gooddetail.goodchildId) {
                CommonService.toolTip("请选择您要的商品信息", "tool-tip-message");
                return;
            }
            $scope.goodInfo = [];
            var obj = {};
            obj.serviceId = localStorage.getItem("barterServiceId");
            obj.id = $scope.gooddetail.goodId;
            obj.childId = $scope.gooddetail.goodchildId;
            obj.buynum = $scope.gooddetail.num;
            obj.checked = 1;
            $scope.goodInfo.push(obj);
            OrderService.addBarterCart($scope.goodInfo).success(function (data) {
                if (data && data.errmsg == "ok") {
                    $state.go("procphoto", {name: "exchange"});//跳转拍照页
                }
            });
        }
    }])
    //估价结果推荐
    .controller('ShowResultCtrl',['$scope','GoodService','EvaluateService','WalletService',function($scope,GoodService,EvaluateService,WalletService){

        $scope.evaluationPrice=localStorage.getItem("evaluationPrice");
        console.log('$scope.evaluationPrice=='+$scope.evaluationPrice);
        $scope.cartotalnum = 0;
        $scope.cartotalprice = 0;
        $scope.totalprice = 0;
        EvaluateService.getShopcharTotal({serviceId: localStorage.getItem("barterServiceId")}).success(function (data) {
            $scope.evaluationPrice=localStorage.getItem("evaluationPrice");

            console.log(data);
            console.log(1);
            $scope.totalnum = data.totalnum;
            console.log('$scope.totalnum='+ $scope.totalnum);
            if(data.echeck){
                console.log("=="+1);
                $scope.cartotalnum = data.echeck.cartotalnum;
                $scope.cartotalprice = data.echeck.cartotalprice;
                console.log('$scope.cartotalprice=='+ $scope.cartotalprice);
                console.log('$scope.evaluatePrice=='+ $scope.evaluationPrice);
                $scope.totalprice =  $scope.cartotalprice - $scope.evaluationPrice*1;//预选合计总金额
                console.log('$scope.totalprice=='+ ($scope.cartotalprice - $scope.evaluationPrice));
            }else{
               $scope.totalprice =  - $scope.evaluationPrice;//预选合计总金额
                console.log(  $scope.totalprice );
            }
        });
        $scope.menuWidth = {"width": "33.333%"};
        //初始化参数
        $scope.balance = 0;//账户余额

        WalletService.getBalance({userId: localStorage.getItem("jinlele_userId")}).success(function (data) {
            $scope.balance = data.balance;
        });


        GoodService.getBarterList({
            amount: $scope.evaluationPrice,
            pagenow: 1,
            type: "free"
        }).success(function (data) {
            $scope.freeList=data.pagingList;
        });
        GoodService.getBarterList({
            amount: $scope.evaluationPrice,
            pagenow: 1,
            type: "new"
        }).success(function (data) {
            $scope.newList=data.pagingList;
        });
    }])
    //更多款（免费、补差价）
    .controller('MoreCtrl',['$scope','$stateParams','GoodService',function($scope,$stateParams,GoodService) {
        $scope.evaluationPrice = localStorage.getItem("evaluationPrice");
        $scope.type = $stateParams.category;//款式种类
        $scope.barterlistinfo = [];
        $scope.page = 0;//当前页数
        $scope.total = 1;//总页数
        $scope.moreFlag = false; //是否显示加载更多
        $scope.noDataFlag = false;  //没有数据显示
        $scope.getBarterList = function () {
            if ((arguments != [] && arguments[0] == 0)) {
                $scope.page = 0;
                $scope.barterlistinfo = [];
            }
            $scope.page++;
            $scope.moreFlag = false;
            $scope.noDataFlag = false;
            //分页显示
            GoodService.getBarterList({
                amount: $scope.evaluationPrice,
                pagenow: $scope.page,
                type: $scope.type
            }).success(function (data) {
                angular.forEach(data.pagingList, function (item) {
                    $scope.barterlistinfo.push(item);
                });
                if (data.myrows == 0) $scope.noDataFlag = true;
                $scope.total = data.myrows;
                if ($scope.total > $scope.barterlistinfo.length) {
                    $scope.moreFlag = true;
                }
            })
        };
        $scope.getBarterList();
    }]);