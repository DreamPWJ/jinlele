
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'starter.config', 'starter.directive'])

    .config(function ($stateProvider, $urlRouterProvider) {
        $stateProvider
            .state('main', {  //APP首页面
                url: '/main',
                templateUrl: 'html/main.html',
                controller: 'IndexCtrl'
            })
            .state('category', {  //分类
                url: '/category/:id',
                templateUrl: 'html/category.html',
                controller: 'CategoryCtrl'
            })

        $urlRouterProvider.otherwise('main')
    })