(function(angular) {
  angular.module("tjingApp.controllers", []);
  
  var tjingApp = angular.module("tjingApp", ["tjingApp.controllers", "tjingApp.services", "ngRoute"]);


   tjingApp.config(function($routeProvider) {
        $routeProvider

            .when('/items', {
                templateUrl : 'app/partials/items.html',
                controller  : 'ItemController'
            })

            .when('/pools', {
            	templateUrl : 'app/partials/pools.html',
            	controller : 'PoolController'
            })

            .when('/interactions', {
            	templateUrl : 'app/partials/interactions.html',
            	controller : 'InteractionController'
            })

            .when('/groupimport', {
                templateUrl : 'app/partials/groupimport.html',
                controller : "FacebookImportController"
            })

            .when('/newitem', {
                templateUrl : 'app/partials/newitem.html',
                controller : "ItemController"
            })

            .when('/searchresult/:searchStr', {
                templateUrl : 'app/partials/searchresult.html',
                controller : "SearchResultController"
            })

    });
}(angular));