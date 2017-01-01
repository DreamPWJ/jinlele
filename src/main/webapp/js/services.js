angular.module('starter.services', [])
//service在使用this指针，而factory直接返回一个对象
    .service('CommonService', function ($rootScope, $timeout) {
        return {

            toolTip: function (msg, type) { //全局tooltip提示
                this.message = msg;
                this.type = type;
                //提示框显示最多3秒消失
                var _self = this;
                $timeout(function () {
                    _self.message = null;
                    _self.type = null;
                }, 3000);
            }


        }
    })
    .service('MainService', function ($q, $http, JinLeLe) { //首页服务定义
        return {
            getIndexInfo: function (params) { //获取首页信息
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/index/getIndexInfo"
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getNewProducts: function (params) { //首页新品推荐分页显示
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/index/getNewProducts/" + params.pagenow
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }

        }
    })

    .service('CategoryService', function ($q, $http, JinLeLe) {
        return {
            getcatogories: function (params) { //获取首页信息
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getFirstCatogotory'
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getSecondCatogories: function (pid) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getSecondCatogaryByPid/' + pid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getGoodsByCidPaging: function (pagenow, catogoryId) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getGoodsByCidPaging/' + pagenow + '/' + catogoryId
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //根据一级分类查询 二级分类不分页
            getSecondCatogByPid: function (pid) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getSecondCatogByPid/' + pid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
        }
    })
    .service('GoodService', function ($q, $http, JinLeLe) { //商品相关的服务
        return {
            getGoodList: function (params) { //获取产品列表
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: encodeURI(JinLeLe.api + '/good/getGoodList/' + params.pagenow + '/' + params.categoryname + '/' + params.querytype)
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            getGoodDetail: function (params) { //获取产品详情
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: "GET",
                    url: JinLeLe.api + '/good/getGoodDetail/' + params.goodId + "/" + params.userId
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('AccountService', function ($q, $http, JinLeLe, $state, $interval, $timeout, $ionicLoading) {
        return {
            login: function (datas) { //登录
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'POST',
                    url: JinLeLe.api + "/user/login",
                    data: datas
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            addFilenames: function ($scope, params, imageUrl) {//上传附件

            }

        }
    })
    //地址管理
    .service('AddressService', function ($q, $http, JinLeLe) {
        return {
            //最新地址信息
            getlatestinfo:function(params){
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/service/getLatestInfo/"+params.userid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //获取数据库地址id，有则查，无则创建
            getReceiptAddressId: function (datas) { //商品
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/service/createReceiptAddressId",
                    params:datas
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    //翻新等服务提交订单的页面Service
    .service('ProcCommitOrderService', function ($q, $http, JinLeLe) {
        return {
            //添加购物车
            findAllStores: function () { //商品
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/store/findAllStores",
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //新增服务订单
            saveServiceOrder:function (datas) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/serviceOrder/saveServiceOrder",
                    params:datas
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })

    .service('AddtoCartService', function ($q, $http, JinLeLe) {
        return {
            //添加购物车
            addtocart: function (datas) { //商品
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/addtocart",
                    params: datas
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('CartService', function ($q, $http, JinLeLe) {
        return {
            //遍历购物车数据
            getcartinfo: function (params) { //商品
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/getCartList/" + params.pagenow + '/' + params.userid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //删除购物车数据
            deleteCart:function(params){//商品
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/deleteShoppingCart/" + params.userid + '/' + params.gcIdStr
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //生成订单
            saveOrder: function (shopOrder) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/saveOrder",
                    params: shopOrder
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('OrderListService', function ($q, $http, JinLeLe) {
        return {
            getorderLists: function (params) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/getOrderListDetail/" + params.pagenow + '/' + params.userid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    //会员页面service
    .service('MemberService' , function ($q, $http, JinLeLe) {
        return {
            //得到用户信息
            getUserInfo: function (openid) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/user/getUserInfo/" + openid
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('OrderService',function($q, $http, JinLeLe){
        return {
            getOrderDetailInfo:function(params){
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/getOrderDetailInfo/" + params.orderno
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            cancleOrder:function(params){
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/cancleOrder/" + params.orderno
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //修改订单状态 为成功
            updateOrder:function (data) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/updateOrder",
                    params:data
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //修改订单状态 为未付款
            updateOrder2:function (data) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/order/updateOrder2",
                    params:data
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('ProcPhotoService',function($q, $http, JinLeLe){
        return {
            saveService:function(params){
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise;
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/service/saveService",
                    params:params
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .service('ResizeService', function ($window) {
        return {
            autoHeight: function () {
                if ($window.innerHeight) {//FF
                    nowHeight = $window.innerHeight;
                } else {
                    nowHeight = document.documentElement.clientHeight;
                }
                var jianHeight = 50;
                if (nowHeight > jianHeight) {
                    document.getElementById('fenlei').style.height = nowHeight - jianHeight + 'px';
                } else {
                    document.getElementById('fenlei').style.height = jianHeight + 'px';
                }
                var jianHeight = 50;
                if (nowHeight > jianHeight) {
                    document.getElementById('leibie').style.height = nowHeight - jianHeight + 'px';
                } else {
                    document.getElementById('leibie').style.height = jianHeight + 'px';
                }
            }
        }
    })

    .service('WeiXinService', function ($q, $http, JinLeLe ,$sce) { //微信 JS SDK 接口服务定义
        return {
            mediaIds:[],//上传下载媒体id数组
            localIds:[], //选择图片后生成的图片数组
            //获取微信签名
            getWCSignature: function (params) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'POST',
                    url: JinLeLe.api + "/weixin/jsconnect",
                    params: params
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //获取微信支付统一下单接口参数
            getweixinPayData: function (params) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                   //url: JinLeLe.api + "/weixin/weixinPay/" + parseInt(new Date().getTime() / 1000) + "/0.01/商品微信支付测试/okhnkvvnLaxUQxAeH6v8SUcu9jZQ"
                    url: JinLeLe.api + "/weixin/weixinPay/" + params.orderNo + "/"+params.totalprice+"/"+params.descrip+"/"+params.openid,
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            //获取下载微信媒体文件
            getWCMedia: function (params) {
                var deferred = $q.defer();// 声明延后执行，表示要去监控后面的执行
                var promise = deferred.promise
                promise = $http({
                    method: 'GET',
                    url: JinLeLe.api + "/wc/media",
                    params: params
                }).success(function (data) {
                    deferred.resolve(data);// 声明执行成功，即http请求数据成功，可以返回数据了
                }).error(function (err) {
                    deferred.reject(err);// 声明执行失败，即服务器返回错误
                });
                return promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            },
            isWeiXin: function isWeiXin() { //判断是否是微信内置浏览器
                var ua = window.navigator.userAgent.toLowerCase();
                if (ua.match(/MicroMessenger/i) == 'micromessenger') {
                    return true;
                } else {
                    return false;
                }
            },
            /*    所有需要使用JS-SDK的页面必须先注入配置信息，否则将无法调用（同一个url仅需调用一次，对于变化url的SPA的web app可在每次url变化时进行调用,
             目前Android微信客户端不支持pushState的H5新特性，所以使用pushState来实现web app的页面会导致签名失败，此问题会在Android6.2中修复*/
            weichatConfig: function (timestamp, nonceStr, signature) { //微信JS SDK 通过config接口注入权限验证配置
                wx.config({
                    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                    appId: 'wx7a6a63e9ee94e24d', // 必填，公众号的唯一标识
                    timestamp: timestamp, // 必填，生成签名的时间戳
                    nonceStr: nonceStr, // 必填，生成签名的随机串
                    signature: signature,// 必填，签名，见附录1
                    jsApiList: ['checkJsApi', 'chooseImage', 'uploadImage','openAddress', 'getLocation', 'scanQRCode', 'onMenuShareAppMessage', 'onMenuShareTimeline', 'onMenuShareQQ', 'onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                });
            },
            wxcheckJsApi: function () { //判断当前客户端版本是否支持指定微信 JS SDK接口
                wx.checkJsApi({
                    jsApiList: ['chooseImage'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
                    success: function (res) {
                        // 以键值对的形式返回，可用的api值true，不可用为false
                        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
                    }
                });
            },
            wxchooseImage: function (callback) { //拍照或从手机相册中选图接口
                WeiXinService = this;
                wx.chooseImage({
                    count: 6, // 默认9
                    sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                    sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                    success: function (results) {
                        var localIds = results.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                        var newlocalIds = [];
                        for (var i = 0, len = localIds.length; i < len; i++) {
                            localIds[i] =  localIds[i].toString();
                             $sce.trustAsResourceUrl(localIds[i]);
                             newlocalIds.push(localIds[i]);
                             WeiXinService.wxuploadImage(localIds[i]);
                        }
                        callback.call(this , newlocalIds);
                    }
                });
            },
            wxuploadImage: function (localId) {//微信上传图片接口
                WeiXinService = this;
                wx.uploadImage({
                    localId: localId, // 需要上传的图片的本地ID，由chooseImage接口获得
                    isShowProgressTips: 1, // 默认为1，显示进度提示
                    success: function (res) {
                        var serverId = res.serverId; // 返回图片的服务器端ID
                        WeiXinService.mediaIds.push(serverId);
                    }
                });
            },
            wxgetLocation: function () { //获取地理位置接口
                wx.getLocation({
                    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                    success: function (res) {
                        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                        var speed = res.speed; // 速度，以米/每秒计
                        var accuracy = res.accuracy; // 位置精度
                        localStorage.setItem("latitude", latitude);
                        localStorage.setItem("longitude", longitude);
                    }
                });
            },
            wxscanQRCode: function ($scope, type) {//调起微信扫一扫接口
                wx.scanQRCode({
                    needResult: type, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
                    scanType: ["qrCode", "barCode"], // 可以指定扫二维码还是一维码，默认二者都有
                    success: function (res) {
                        if (type == 1) {// 当needResult 为 1 时，扫码返回的结果
                            var result = res.resultStr;
                        }
                    }
                });
            },

            wxonMenuShareAppMessage: function (title, desc, link, imgUrl) { //获取“分享给朋友”按钮点击状态及自定义分享内容接口
                wx.onMenuShareAppMessage({
                    title: title, // 分享标题
                    desc: desc, // 分享描述
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    type: '', // 分享类型,music、video或link，不填默认为link
                    dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            wxonMenuShareTimeline: function (title, link, imgUrl) {//获取“分享到朋友圈”按钮点击状态及自定义分享内容接口
                wx.onMenuShareTimeline({
                    title: title, // 分享标题
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            wxonMenuShareQQ: function (title, desc, link, imgUrl) {//获取“分享到QQ”按钮点击状态及自定义分享内容接口
                wx.onMenuShareQQ({
                    title: title, // 分享标题
                    desc: desc, // 分享描述
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            wxonMenuShareQZone: function (title, desc, link, imgUrl) {//获取“分享到QQ空间”按钮点击状态及自定义分享内容接口
                wx.onMenuShareQZone({
                    title: title, // 分享标题
                    desc: desc, // 分享描述
                    link: link, // 分享链接
                    imgUrl: imgUrl, // 分享图标
                    success: function () {
                        // 用户确认分享后执行的回调函数
                    },
                    cancel: function () {
                        // 用户取消分享后执行的回调函数
                    }
                });
            },
            wxopenAddress : function ($scope) {//编辑并获取收货地址
                WeiXinService = this;
                wx.openAddress({
                    success: function (res) {
                        // 用户成功拉出地址
                        $scope.address = res;
                        $scope.show=true;
                        $scope.$apply();
                    },
                    cancel: function () {
                        // 用户取消拉出地址
                    }
                });
            },
            wxchooseWXPay: function (data) {//微信支付请求接口
                var defered=$q.defer();
                function onBridgeReady() {
                    WeixinJSBridge.invoke(
                        'getBrandWCPayRequest', {
                            "appId": "wx7a6a63e9ee94e24d",     //公众号名称，由商户传入
                            "timeStamp": data.timeStamp,         //时间戳，自1970年以来的秒数
                            "nonceStr": data.nonceStr, //随机串
                            "package": data.package,
                            "signType": "MD5",         //微信签名方式:
                            "paySign": data.paySign //微信签名
                        },
                        function (res) {
                            // alert(JSON.stringify(res));
                            console.log(res.err_msg);  // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返
                            defered.resolve(res.err_msg); // 声明执行成功，即http请求数据成功，可以返回数据了
                        }
                    );
                }

                if (typeof WeixinJSBridge == "undefined") {
                    if (document.addEventListener) {
                        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
                    } else if (document.attachEvent) {
                        document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
                        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
                    }
                } else {
                    onBridgeReady();
                }

                /*           wx.chooseWXPay({
                 timestamp: data.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                 nonceStr: data.nonceStr, // 支付签名随机串，不长于 32 位
                 package: data.package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
                 signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
                 paySign: data.paySign, // 支付签名
                 success: function (res) {
                 alert("微信支付成功=" + JSON.stringify(res));
                 // 支付成功后的回调函数
                 },


                 });
                 wx.error(function (res) {
                 alert(res.errMsg);
                 });*/

                return defered.promise; // 返回承诺，这里并不是最终数据，而是访问最终数据的API
            }
        }
    })
    .factory('AuthInterceptor', function () {//设置请求头信息的地方是$httpProvider.interceptors。也就是为请求或响应注册一个拦截器。使用这种方式首先需要定义一个服务

        return {
            request: function (config) {
                config.headers = config.headers || {};
                var token = localStorage.getItem('token');
                if (token) {
                    config.headers.authorization = token;
                }
                return config;
            },
            response: function (response) {

            },
            responseError: function (response) {
                // ...
            }
        };
    })
