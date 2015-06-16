/**
 * 
 */
 (function(angular) {
  var controllersModule = angular.module('tjingApp.controllers');

  controllersModule.controller("FacebookImportController", ["$scope", "$http", function($scope, $http){
    var importUrl = 'pool/import/facebook/';

    $scope.facebookGroups = [];
    $scope.importedGroupMembership = null;

    getGroups();

    function getGroups(){
      $http.get(importUrl).success(function(data){
        $scope.facebookGroups = data;
      });
    };

    $scope.importGroup = function(group){
      $http.post(importUrl, group).success(function(data){
        $scope.importedGroupMembership = data;
      });
    };

  }]);


   //InteractionController
  angular.module('tjingApp.controllers').controller("MyInteractionsController", ["$scope", "Interaction", function($scope, Interaction){
    $scope.allinteractions = Interaction.query();
    $scope.incomingrequests = Interaction.query({param:'incoming'});
    $scope.outgoingrequests = Interaction.query({param:'outgoing'});

    $scope.acceptRequest = function(interaction){
      Interaction.accept(interaction, function(response){
        $scope.incomingrequests = Interaction.query({param:"incoming"});
      });
    }

    $scope.denyRequest = function(interaction){
      Interaction.deny(interaction, function(){
        $scope.incomingrequests = Interaction.query({param:"incoming"});
      }); 
    }

    //Borrower side
    $scope.confirmHandover = function (interaction){
      Interaction.handoverconfirm(interaction, function(){
        $scope.outgoingrequests = Interaction.query({param:"outgoing"});
      });
    }

    //Owner side
    $scope.confirmReturn = function(interaction){
      Interaction.returnconfirm(interaction, function(){
        $scope.incomingrequests = Interaction.query({param:"incoming"});
      });
    }
  }]);

  angular.module('tjingApp.controllers').controller('FeedController', ["$scope", "Feed", function($scope, Feed){
    $scope.feedItems = Feed.query();

    $scope.delete = function(item){
      Feed.delete({id:item.id}, function (){
        $scope.feedItems = Feed.query();
      });
    }
  }]);

  angular.module("tjingApp.controllers").controller("ItemRequestController", 
    ["$scope", "ItemRequest",
    function($scope, ItemRequest){
      $scope.myRequests = [];
      refreshMyRequests();

      $scope.postRequest = function (request){
        ItemRequest.save({text: request.text}, function(response){
          $scope.myRequests.push(response);
          refreshMyRequests();
        });
      };

      function refreshMyRequests(){
        ItemRequest.query({}, function(response){
          $scope.myRequests = response;
        });
      };
    }]);
  
  angular.module("tjingApp.controllers").controller("MyItemsController", ["$scope", "$location", "Item", "Pool", "Interaction", "Share",
    function($scope, $location, Item, Pool, Interaction, Share) {
    // Scope variable initialization
    $scope.owneditems = [];
    $scope.borroweditems = Item.query({param:'borrowed'});
    $scope.items=Item.query();
    $scope.mypools = Pool.query({param:"mine"});

    refreshMyItems();

    function refreshMyItems(){
      $scope.owneditems = Item.query({param:'owned'});
    }
    
    $scope.addItem = function(title) {
      new Item({
        title: title
      }).$save(function (data){
        $scope.owneditems.push(data); //$save returns created item. Success callback adds the item to the scope array
        $location.url('/items');
      });
      $scope.newItem = "";
    };

    $scope.setFbAvailable = function(item){
      Item.setFbAvailable({id: item.id, fbAvailable: item.fbAvailable});
    };
    
    $scope.deleteItem = function(item) {
      Item.delete({id: item.id}, function(){
        $scope.owneditems = Item.query({param:'owned'});
      });      
    };
    
    $scope.requestItem = function(item){
      Interaction.save({itemId:item.id})
    };

    $scope.shareItem = function(item, pool){
      Share.save(
      {
        itemId: item.id, 
        poolId : pool.id
      }, function(){
        $scope.owneditems = Item.query({param:'owned'});
      });
    };

    $scope.unshareItem = function(share){
      Share.delete({id: share.id}, function(){
        $scope.owneditems = Item.query({param:'owned'});
      });
    };
  }]);

  angular.module("tjingApp.controllers").controller("MyPoolsController", ["$scope", "Pool", "Item", "Membership", function($scope, Pool, Item, Membership) {
    $scope.showPools = false;

    $scope.allPools = [];
    $scope.myPools = [];
    $scope.myMemberships = [];

    refreshMyPools();
    refreshAllPools();

    function refreshMyPools(){
      Membership.query({}, function (response){
        $scope.myMemberships = response;
        $scope.myPools = [];
        for (membership in $scope.myMemberships){
          $scope.myPools.push(membership.pool);
        }
      });
    };

    function refreshAllPools(){
      Pool.query({}, function(response){
        $scope.allPools = response;
      });
    };

    $scope.searchPools = function(searchStr){
      return Pool.query({q: searchStr});
    };

    $scope.joinPool = function(pool){
      Membership.save({poolId:pool.id},function(){
        refreshMyPools();
      });
    };

    $scope.leavePool = function(membership){
      Membership.delete({membershipId:membership.id}, function(){ //TODO äuuuuuäuäuuähh
        refreshMyPools();
      })
    };

    $scope.createPool = function(pool){
      Pool.save({
        title: pool.title,
        //description: "", //TODO: add description
        privacy: "OPEN" //TODO: enable other privacy modes
      }, function() { //TODO:Does save return anything? In that case use that data instead of sending a new request
        refreshAllPools();
        refreshMyPools();
      });
    };
  }]);

  angular.module("tjingApp.controllers").controller("NavbarController", ["$scope", "$location", "$http", "User", function($scope, $location, $http, User){
    $scope.searchResult = [];

    $scope.currentUser = User.current(function(data){
      if (data.facebookId!=null){ //If the User object contains a facebookId, use it to get the profile picture from facebook
        $scope.profilePicUrl = "http://graph.facebook.com/" +data.facebookId + "/picture?type=small"
      }
    });
    $scope.profilePicUrl = "/messages/error.png";

    $scope.search = function(){
      $location.url("/searchresult/"+$scope.searchStr);
    };

    $scope.signout = function(){
      location.href="/signout";
    }
  }]);

  angular.module("tjingApp.controllers").controller("SearchResultController", 
    ["$scope", "$routeParams", "$http", 
    function($scope, $routeParams, $http){
    $scope.searchResults = [];

    $http.get("/item/search/"+$routeParams.searchStr).success(function(data){
      $scope.searchResults = data;
    });
  }]);

  angular.module("tjingApp.controllers").controller("ItemController", 
    ["$scope", "$routeParams", "Item", "Pool", "Membership", 
    function($scope, $routeParams, Item, Pool, Membership){
      $scope.currentItem = {}; 
      $scope.myMemberships = [];

      Membership.query({}, function(data){
        $scope.myMemberships = data;
      });

      Item.get({id: $routeParams.itemId}, function(data){
        $scope.currentItem = data;
      });
  }]);


}(angular));