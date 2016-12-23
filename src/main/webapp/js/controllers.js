angular.module('starter.controllers', [])
    .config(function ($httpProvider) { //统一配置设置
        //服务注册到$httpProvider.interceptors中  用于接口授权
        // $httpProvider.interceptors.push('AuthInterceptor');
        /* $httpProvider.defaults.headers.common['Authorization'] = localStorage.getItem('token');*/
    })


    //APP首页面
    .controller('MainCtrl', function ($scope, $rootScope, MainService) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });

        //加载此页面的时候
        //自动读取网页授权接口获取用户的opendId,从而得到用户的信息，得到前台用户的id，这里暂时强制设定用户的id
        localStorage.setItem("jinlele_id" , 1); //1应该是从数据库中查到的


        //获取首页信息
        MainService.getIndexInfo().success(function (data) {
            $scope.indexinfo = data;
        }).then(function () {
            //首页新品推荐分页显示
            MainService.getNewProducts({pagenow: 1}).success(function (data) {
                $scope.newProductsinfo = data;

            })
        })

    })

    //分类tab
    .controller('CategoryCtrl', function ($scope, $stateParams,$window, CategoryService,ResizeService) {
        ResizeService.autoHeight();
        $window.onresize = ResizeService.autoHeight;
        $scope.init = {
            pagenow:1,
            goodArr:[],//产品列表
            //根据分类id查询所有的商品 分页展示
            findGoods:function (catogoryId) {
                CategoryService.getGoodsByCidPaging($scope.init.pagenow , catogoryId).success(function (data) {
                    $scope.goods = data.pagingList;
                });
            },
            //根据一级分类id 查询所有的二级分类
            getSecondCatogories:function (pid) {
                CategoryService.getSecondCatogories(pid).success(function (data) {
                    $scope.secondCatogories = data;
                    if($scope.secondCatogories!=null && $scope.secondCatogories.length>0){
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
    .controller('ShoppingCartCtrl', function ($scope,$document,CartService) {
        $scope.init={
            userid:1,
            pagenow:1
        };
        CartService.getcartinfo($scope.init).success(function(data){
            $scope.cartlist=data;
        });
        $scope.m = [];
        $scope.checked = [];
        //全选
        $scope.selectAll = function ($event){
            var choseall = $event.target;
            if($scope.select_all) {
                $scope.select_one = true;
                $scope.checked = [];
                angular.forEach($scope.cartlist.pagingList, function (i, index) {
                    $scope.checked.push(i.id);
                    $scope.m[i.id] = true;
                })
                $('#'+choseall.id).siblings("label").addClass("on");
                angular.forEach($scope.checked, function (i, index) {
                    $('#'+i).siblings("label").addClass("on");
                })
            }else {
                $scope.select_one = false;
                $scope.checked = [];
                $scope.m = [];
                $('.check_label').removeClass("on");
            }
            console.log($scope.checked);
        };
        //单选
        $scope.selectOne = function ($event,select) {
            var choseone=$event.target;
            angular.forEach($scope.m , function (i, id) {
                var index = $scope.checked.indexOf(id);
                if(i && index === -1) {
                    $scope.checked.push(id);
                    $('#'+choseone.id).siblings("label").addClass("on");
                } else if (!i && index !== -1){
                    $scope.checked.splice(index, 1);
                    $('#'+choseone.id).siblings("label").removeClass("on");
                };
            });
            if ($scope.cartlist.pagingList.length === $scope.checked.length) {
                $scope.select_all = true;
                $('#all').siblings("label").addClass("on");
            } else {
                $scope.select_all = false;
                $('#all').siblings("label").removeClass("on");
            }
            console.log($scope.checked);
        }
        $scope.del=function(){
            console.log($scope.checked);
        }
    })
    //会员
    .controller('MemberCtrl', function ($scope) {

    })
    //商城订单
    .controller('OrderListCtrl', function ($scope) {
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
    })
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
    .controller('CashdetailCtrl',function($scope){

    })
    //我的收藏
    .controller('FavorCtrl', function ($scope) {

    })
    //商品列表
    .controller('GoodListCtrl', function ($scope, GoodService, $stateParams) {

        //获取产品列表
        GoodService.getGoodList({pagenow:1,categoryname: $stateParams.name, querytype: 0}).success(function (data) {
            $scope.goodList=data;
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
    .controller('GoodDetailCtrl', function ($scope,$stateParams,$rootScope,GoodService,AddtoCartService,CommonService) {
        var swiper = new Swiper('.banner', {
            pagination: '.spot',
            paginationClickable: true
        });
        //初始化参数
        $scope.init ={
            bannerUrl:"",//商品主图
            stockNum:0//库存数
        };
        $scope.gooddetail={
            userId:localStorage.getItem("jinlele_id"),
            goodId:$stateParams.id,
            num:1
        };
        GoodService.getGoodDetail({goodId:$stateParams.id , userId:$scope.gooddetail.userId}).success(function (data) {
            console.log(JSON.stringify(data));
            $scope.goodDetail=data.good;
            $scope.goodChilds = data.goodchilds;
            $scope.totalnum=data.totalnum;
            $scope.initNum = data.totalnum;
            $scope.init.bannerUrl = $scope.goodChilds[0].imgurl;
            $scope.init.stockNum = $scope.goodChilds[0].stocknumber;

        });

        $scope.addtocart = function(){
            $scope.changeNum();
            AddtoCartService.addtocart($scope.gooddetail).success(
                function(data){
                    console.log('data===' + data);
                    $rootScope.commonService=CommonService;
                    CommonService.toolTip("添加成功","tool-tip-message-success");
                }
            )
        }
        $scope.changeNum = function () {
            $scope.totalnum =    $scope.initNum + parseInt($scope.gooddetail.num || 0);
        }
        $scope.addNum = function () {
            $scope.gooddetail.num++;
            $scope.totalnum =  $scope.totalnum + 1;
        }
        $scope.minusNum = function () {
            if($scope.gooddetail.num>1){
                $scope.gooddetail.num --;
                $scope.totalnum = $scope.totalnum -1;
            }
        }
        //切换主图和库存
        $scope.changeGoodChild = function (index) {
            $scope.init.bannerUrl = $scope.goodChilds[index].imgurl;
            $scope.init.stockNum = $scope.goodChilds[index].stocknumber;
        }
    })
    //发表评论
    .controller('AddCommentCtrl', function ($scope) {

    })
    //确认订单
    .controller('ConfirmOrderCtrl', function ($scope) {
        $(".check_label").checkbox();
    })
    //流程-拍照
    .controller('ProcPhotoCtrl', function ($scope,$stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        $scope.localflag=false;
        if($stateParams.name=="repair"){
            $scope.localflag=true;
        }

    })
    //流程-提交订单
    .controller('ProcCommitOrderCtrl', function ($scope,$stateParams,$window) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        $scope.showaddr=$stateParams.name=='recycle'?false:true;
        $scope.goback=function(){
            $window.history.back();
        }

    })
    //流程-平台收货
    .controller('ProcReceiveCtrl', function ($scope,$stateParams,$window) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        $scope.tracking=$stateParams.name=='recycle'?true:false;

    })
    //流程-检测
    .controller('ProcTestCtrl', function ($scope,$stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
    })
    //流程-邮寄
    .controller('ProcPostCtrl', function ($scope,$stateParams,$location) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        if($stateParams.name=="recycle"){
            $location.path("/");
        }

    })
    //流程-验货
    .controller('ProcCheckCtrl', function ($scope,$stateParams,$location) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        if($stateParams.name=="recycle"){
            $location.path("/");
        }

    })
    //流程-评价
    .controller('ProcAddCmtCtrl', function ($scope,$stateParams) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
    })
    //估价结果(回收、换款)
    .controller('ProcPricingCtrl', function ($scope,$stateParams,$location) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        $scope.decide=false;
        switch ($stateParams.name){
            case "recycle":
                $scope.decide=true;
                break;
            case "exchange":
                break;
            default :
                $location.path("/");
                break;
        }
    })
    //翻新
    .controller('ProcRefurbishCtrl', function ($scope,$stateParams,$location) {
        console.log($stateParams.name);
        $scope.pagetheme=$stateParams.name;
        if($stateParams.name!="refurbish"){
            $location.path("/");
        }

    })
    //维修-定价
    .controller('ProcFixpriceCtrl', function ($scope,$stateParams) {
        $scope.pagetheme=$stateParams.name;
    })
    //维修
    .controller('ProcRepairCtrl', function ($scope,$stateParams) {
        $scope.pagetheme=$stateParams.name;
        if($stateParams.name!="repair"){
            $location.path("/");
        }
    })
    //回收-估价
    .controller('RecycleCtrl', function ($scope) {

    })
    //换款
    .controller('ExchangCtrl', function ($scope) {

    })

