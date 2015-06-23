(function(angular) {
  angular.module("tjingApp.controllers", []);
  
  var tjingApp = angular.module("tjingApp", ["tjingApp.controllers", "tjingApp.services", "ngRoute"]);
  var partialsUrl = 'app/partials/';

   tjingApp.config(function($routeProvider) {
        $routeProvider

            .when('/home', {
                templateUrl : partialsUrl +"home.html",
                controller : "HomeController"
            })

            .when('/myprofile', {
                templateUrl : partialsUrl + "myprofile.html",
                controller : "MyProfileController"
            })

            .when('/items', {
                templateUrl : 'app/partials/myitems.html',
                controller  : 'MyItemsController'
            })

            .when('/pools', {
            	templateUrl : 'app/partials/mypools.html',
            	controller : 'MyPoolsController'
            })

            .when('/interactions', {
            	templateUrl : 'app/partials/myinteractions.html',
            	controller : 'MyInteractionsController'
            })

            .when('/groupimport', {
                templateUrl : 'app/partials/groupimport.html',
                controller : "FacebookImportController"
            })

            .when('/newitem', {
                templateUrl : 'app/partials/newitem.html',
                controller : "MyItemsController"
            })

            .when('/searchresult/:searchStr', {
                templateUrl : 'app/partials/searchresult.html',
                controller : "SearchResultController"
            })

            .when('/item/:itemId', {
                templateUrl : 'app/partials/item.html',
                controller : "ItemController"
            })

            .when('/pool/:poolId', {
                templateUrl : 'app/partials/pool.html',
                controller: "PoolController"
            })

            .when('/user/:userId', {
                templateUrl : partialsUrl + "user.html",
                controller : "UserController"
            })

            .when('/interaction/:interactionId', {
                templateUrl : partialsUrl + "interaction.html",
                controller : "InteractionController"
            })

            .otherwise({
                redirectTo: '/'
            })

    });
}(angular));