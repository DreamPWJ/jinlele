// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers', 'starter.services', 'starter.config', 'starter.directive'])


    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider.state('index', {  //APP首页面
            url: '/index',
            templateUrl: 'index.html',
            controller: 'IndexCtrl'
        }).state('category', {  //分类
            url: '/category',
            templateUrl: 'html/category.html',
            controller: 'CategoryCtrl'
        })

      /*  $urlRouterProvider.otherwise('index')*/
    })