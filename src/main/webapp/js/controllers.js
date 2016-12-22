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
        $scope.selected = [];
        $scope.updateSelection = function($event, id){
            var checkbox = $event.target;
            var action = (checkbox.checked?'add':'remove');
            if(action == 'add' && $scope.selected.indexOf(id) == -1){
                $scope.selected.push(id);
                $($event.target).siblings("label").addClass("on");
            }
            if(action == 'remove' && $scope.selected.indexOf(id)!=-1){
                var idx = $scope.selected.indexOf(id);
                $scope.selected.splice(idx,1);
                $($event.target).siblings("label").removeClass("on");
            }
            if ($scope.cartlist.myrows==$scope.selected.length){
                $('#all').siblings("label").addClass("on");
            }else{
                $('#all').siblings("label").removeClass("on");
            }
        }
        $scope.isSelected = function(id){
            return $scope.selected.indexOf(id)>=0;
        }
        $scope.choseall=function(){
            var label =$('#all').siblings("label");
            if(label.hasClass("on")){
                label.siblings("input").removeAttr("checked");
                label.removeClass("on");
                angular.forEach($document.find('label'),function(node){
                    node.className ="check_label";
                });
                angular.forEach($document.find('input'),function(node){
                    if(node.type=="checkbox"){
                        node.checked=false;
                    }
                });
            }else{
                label.siblings("input").prop("checked","checked");
                label.addClass("on");
                angular.forEach($document.find('label'),function(node){
                    node.className+=" on";
                });
                angular.forEach($document.find('input'),function(node){
                    if(node.type=="checkbox"){
                        node.checked=true;
                    }
                });
            }
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

        $scope.gooddetail={
            userId:localStorage.getItem("jinlele_id"),
            goodId:$stateParams.id,
            num:1
        };
        GoodService.getGoodDetail({goodId:$stateParams.id , userId:$scope.gooddetail.userId}).success(function (data) {
            $scope.goodDetail=data.good;
            $scope.totalnum=data.totalnum;
            $scope.initNum = data.totalnum;
            console.log(data);
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
    })
    //发表评论
    .controller('AddCommentCtrl', function ($scope) {

    })
    //确认订单
    .controller('ConfirmOrderCtrl', function ($scope) {
        $(".check_label").checkbox();
    })
    //翻新
    .controller('RefurbishCtrl', function ($scope) {

    })
    //翻新支付
    .controller('RefpayCtrl', function ($scope) {

    })
    //翻新平台收货
    .controller('RefReceiptCtrl', function ($scope) {

    })
    //维修
    .controller('RepairCtrl', function ($scope) {

    })
    //检测
    .controller('DetectCtrl', function ($scope) {

    })
    //回收
    .controller('RecycleCtrl', function ($scope) {

    })
    //换款
    .controller('ExchangCtrl', function ($scope) {

    })

