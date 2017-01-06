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
                        openid: localStorage.getItem("openId")
                    }
                    //调用微信支付服务器端接口
                    WeiXinService.getweixinPayData($scope.param).success(function (data) {
                        WeiXinService.wxchooseWXPay(data) //调起微支付接口
                            .then(function (msg) {
                                switch (msg) {
                                    case "get_brand_wcpay_request:ok":
                                        //调用支付后，跳转订单详情
                                        $state.go("orderdetail", {orderno: r.orderno});
                                        break;
                                    default :
                                        //未支付，跳转订单列表
                                        $state.go("orderlist");
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
    .controller('PayResultCtrl',  ['$scope', '$stateParams', function ($scope, $stateParams) {
        $scope.orderno=$stateParams.orderno;
    }])
    //订单详情
    .controller('OrderDetailCtrl', ['$scope', '$stateParams', 'OrderService', 'CommonService', function ($scope, $stateParams, OrderService,CommonService) {
        OrderService.getOrderDetailInfo({orderno: $stateParams.orderno}).success(function (data) {
            $scope.orderinfo = data.order;//订单总信息
            $scope.address = data.address;//订单总信息
            $scope.orderdetail = data.orderdetail;//订单详情
        })
        $scope.cancleorder=function(orderno){
            //修改后，重新请求数据
            OrderService.cancleOrder({orderno: orderno}).success(function (data) {
                if (parseInt(data.resultnumber) > 0) {
                    CommonService.toolTip("订单取消成功！", "tool-tip-message-success");
                    $state.go("category", {id: 1});
                }
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
            var commentinfo = {};
            var flag=true;;
            commentinfo.orderno = $stateParams.orderno;
            commentinfo.userId = localStorage.getItem("jinlele_userId");
            commentinfo.descriplevel = $scope.currentId;
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

    //商城订单
    .controller('OrderListCtrl', ['$scope', 'WeiXinService', 'OrderListService', 'OrderService','CommonService', '$ionicScrollDelegate', function ($scope, WeiXinService, OrderListService, OrderService,CommonService,$ionicScrollDelegate) {

        $(function () {
            $('.default').dropkick();
            theme:'black'
        });
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
            OrderListService.getorderLists( {userid: localStorage.getItem("jinlele_userId"),pagenow: $scope.page ,type:$scope.type}).success(function (data) {
                console.log("DATA=="+ JSON.stringify(data));
                angular.forEach(data.pagingList, function (item) {
                    $scope.orderlistsinfo.push(item);
                })
                console.log(" $scope.orderlistsinfo=="+ JSON.stringify(data));
                 if(data.myrows == 0) $scope.noDataFlag = true;
                console.log( "length=="+ $scope.orderlistsinfo.length);
                console.log( "data.myPageCount=="+ data.myrows);
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
                    OrderListService.getorderLists($scope.init).success(function (data) {
                        $scope.list = data;
                    });
                }
            });
        }


        //微信支付调用
        $scope.weixinPay = function (ordeno, totalprice) {
            $scope.param = {
                totalprice: 0.01, //totalprice
                orderNo: ordeno,
                descrip: '你的订单已付款成功！',
                openid: localStorage.getItem("openId")
            }
            //调用微信支付服务器端接口
            WeiXinService.getweixinPayData($scope.param).success(function (data) {
                WeiXinService.wxchooseWXPay(data) //调起微支付接口
                    .then(function (msg) {
                        if (msg == "get_brand_wcpay_request:ok") {
                            CommonService.toolTip("支付成功","tool-tip-message-success");
                            // //修改订单状态  006代表的是商城订单
                            OrderService.updateOrder({orderno: orderno, type: '006'}).success(function (data) {
                                //成功后，跳转到下一个页面
                                if (data && data.n == 1) {
                                    $state.go('orderlist');
                                }
                            });
                        } else {
                            console.log("支付未成功");
                        }
                    });
            })
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


        //取消收藏

    })

    //流程-拍照
    .controller('ProcPhotoCtrl', function ($scope, $stateParams, WeiXinService, $rootScope, CommonService, ProcPhotoService, $state) {
        $rootScope.commonService = CommonService;
        WeiXinService.mediaIds = []; //置空媒体id数组
        console.log($stateParams.name);
        $scope.pagetheme = $stateParams.name;
        $scope.localflag = false;
        $scope.localIds = [];// 上传图片的微信路径 数组
        $scope.type = "";//服务类型 001翻新
        $scope.service = {  //服务实体
            price: 1980, //价格
            descrip: ""    //描述
        };
        if ($stateParams.name == "repair") {
            $scope.localflag = true;
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
            //暂时模拟
            // $state.go("proccommitorder", {name: pagetheme ,serviceId:2,aturalprice:$scope.service.price});

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
            if (pagetheme == "refurbish") {
                $scope.type = '001';
            }
            if (pagetheme == "repair") {
                $scope.type = '002';
            }
            if (pagetheme == "detect") {
                $scope.type = '003';
            }
            if (pagetheme == "recycle") {
                $scope.type = '004';
            }

            //①前台去上传图片的到微信并返回媒体Id 放入集合中
            //通过config接口注入权限验证配置

            //②后台处理:拿到mediaId去后台上传图片传到服务器本地路径 //然后将本地图片上传到七牛并返回七牛图片url,在后台保存数据到翻新服务表 ，照片表 ，翻新服务_照片中间表
            $scope.params = {
                userId: localStorage.getItem("jinlele_userId"),
                mediaIds: WeiXinService.mediaIds,
                aturalprice: $scope.service.price,
                descrip: $scope.service.descrip,
                storeId: 1,//暂时设定门店id为1 ，以后会根据地理位置动态获取
                type: $scope.type //上传类型 翻新001维修002检测003回收004服务信息表买方005卖方收货006
            };
            console.log(JSON.stringify($scope.params));
            ProcPhotoService.saveService($scope.params).success(function (data) {
                console.log(data.serviceId);
                if (data) {
                    var serviceId = data.serviceId;
                    //③后台处理成功后，跳转到下单页面
                    sessionStorage.setItem("jinlele_procphoto_pathname", pagetheme);
                    sessionStorage.setItem("jinlele_procphoto_serviceId", data.serviceId);
                    sessionStorage.setItem("jinlele_procphoto_aturalprice", $scope.service.price);
                    $state.go("proccommitorder");
                }
            })

        }

    })





    //流程-翻新服务 提交订单并付款
    .controller('ProcCommitOrderCtrl', function ($scope, $state, AddressService, OrderService, $stateParams, $window, ProcCommitOrderService, WeiXinService, CategoryService) {
        $scope.pagetheme = sessionStorage.getItem("jinlele_procphoto_pathname");
        $scope.serviceId = sessionStorage.getItem("jinlele_procphoto_serviceId");
        $scope.aturalprice = sessionStorage.getItem("jinlele_procphoto_aturalprice");

        console.log("$stateParams==" + JSON.stringify($stateParams));
        $scope.showaddr = $scope.pagetheme == 'recycle' ? false : true;
        $scope.address = {};
        $scope.show = false; //用户控制地址显示

        $scope.order = {
            storeId: "",
            sendway: "001",
            getway: "001",
            totalprice: ""
        };
        $scope.productArr = [];
        $scope.product = {
            firstCatogoryId: [],//一级分类id
            secondCatogoryId: [], //二级分类id
            num: [],
            memo: []
        };
        $scope.$watch("product.num",function () {
            //遍历
            $scope.totalnum = 0;
            if ($scope.product.num.length > 0) {
                for (var i = 0, len = $scope.product.num.length; i < len; i++) {
                    $scope.totalnum += $scope.product.num[i] * 1;
                }
            }
            $scope.totalprice = $scope.totalnum * $scope.aturalprice;
            console.log(" $scope.totalprice ==" + $scope.totalprice);
        },true) ;

        $scope.sendwayFlag = false;//寄件方式切换
        $scope.getwayFlag = false; //取件方式切换
        $scope.sendwayValue = ['001', '002'];//寄件取件方式值
        $scope.getwayValue = ['001', '002'];//寄件取件方式值
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
            CategoryService.getSecondCatogByPid($scope.product.firstCatogoryId[index]).success(function (data) {
                $scope["secondcatagories" +index] = data;
                console.log(JSON.stringify(data))
            });
        }
        //添加产品
        var i = 0;
        $scope.addProduct = function () {
            $scope.productArr.push(i++);
        }
        //计算总数量和总价格
        // $scope.numblur = function () {
        //     //遍历
        //     $scope.totalnum = 0;
        //     if ($scope.product.num.length > 0) {
        //         for (var i = 0, len = $scope.product.num.length; i < len; i++) {
        //             $scope.totalnum += $scope.product.num[i] * 1;
        //         }
        //     }
        //     var price = $scope.aturalprice;
        //
        //     $scope.totalprice = $scope.totalnum * price;
        //     console.log(" $scope.totalprice ==" + $scope.totalprice);
        // }
        //生成订单并付款
        $scope.procreceive = function () {
            //获取地址id
            AddressService.getReceiptAddressId({
                userid: localStorage.getItem("jinlele_userId"),
                userName: $scope.address.userName,
                postalCode: $scope.address.postalCode,
                provinceName: $scope.address.provinceName,
                cityName: $scope.address.cityName,
                countryName: $scope.address.countryName,
                detailInfo: $scope.address.detailInfo,
                nationalCode: $scope.address.nationalCode,
                telNumber: $scope.address.telNumber
            }).success(function (data) {
                $scope.address.id = data.receiptAddressId;
            });
            $scope.params = {
                userId: localStorage.getItem("jinlele_userId"),
                serviceId: $scope.serviceId,
                type: '001',    //001代表翻新
                storeId: 1,     //暂时默认是1
                totalnum: $scope.totalnum, //产品数量
                sendWay: $scope.order.sendway,     //送货方式
                getWay: $scope.order.getway,      //取货方式
                totalprice: $scope.totalprice,   //总价格
                buyeraddresId: $scope.address.id,//收货地址id外键
                products: JSON.stringify($scope.product)
            };
            console.log(JSON.stringify($scope.params));
            //保存订单 并去支付订单
            ProcCommitOrderService.saveServiceOrder($scope.params).success(function (data) {
                if (data) {
                    //调用支付接口
                    var orderno = data.orderNo;
                    var orderTime = data.orderTime;
                    $scope.param = {
                        totalprice: 0.01, //data.totalprice
                        orderNo: data.orderNo,
                        descrip: '你的翻新订单已付款成功，请尽快邮寄宝贝！',
                        openid: localStorage.getItem("openId")
                    }
                    //调用支付接口
                    console.log(JSON.stringify($scope.param));
                    //微信支付调用
                    WeiXinService.getweixinPayData($scope.param).success(function (data) {
                        WeiXinService.wxchooseWXPay(data)
                            .then(function (msg) {
                                if (msg == "get_brand_wcpay_request:ok") {
                                    console.log("支付成功");
                                    // //修改订单状态
                                    OrderService.updateOrder({orderno: orderno, type: '001'}).success(function (data) {
                                        //成功后，跳转到下一个页面
                                        if (data && data.n == 1) {
                                            $state.go('procreceive', {
                                                name: $scope.pagetheme,
                                                orderNo: orderno,
                                                orderTime: orderTime
                                            });
                                        }
                                    });
                                } else {
                                    console.log("支付未成功");
                                    // OrderService.updateOrder2({orderno:orderno}).success(function (data) {
                                    //     alert("支付未成功");
                                    // });
                                }
                            });

                    });


                }

            });
        }


    })
    //流程-平台收货
    .controller('ProcReceiveCtrl', function ($scope, $stateParams, $window) {
        console.log($stateParams.name);
        console.log($stateParams.orderNo);
        //alert($stateParams.orderTime);
        $scope.orderNo = $stateParams.orderNo;
        $scope.orderTime = $stateParams.orderTime;
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

        $scope.wxchooseImage = function () {
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

