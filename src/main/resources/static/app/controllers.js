/**
 * 
 */
 (function(angular) {
  var controllersModule = angular.module('tjingApp.controllers');

  controllersModule.controller("FacebookImportController", ["$scope", "$http", function($scope, $http){
    $scope.importedPools = [];

    $scope.importFacebook = function(){
      $http.get('pool/import/facebook').success(function(data){
        $scope.importedPools = data;
      });
    };
  }]);


   //InteractionController
  angular.module('tjingApp.controllers').controller("InteractionController", ["$scope", "Interaction", function($scope, Interaction){
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
  
  angular.module("tjingApp.controllers").controller("ItemController", ["$scope", "Item", "Pool", "Interaction",
    function($scope, Item, Pool, Interaction) {
    // Scope variable initialization
    $scope.owneditems = Item.query({param:'owned'});
    $scope.borroweditems = Item.query({param:'borrowed'});
    $scope.items=Item.query();
    $scope.mypools = Pool.query({param:"mine"});
    
    $scope.addItem = function(title) {
      new Item({
        title: title
      }).$save(function (data){
        $scope.owneditems.push(data); //$save returns created item. Success callback adds the item to the scope array
      });
      $scope.newItem = "";
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
      Item.share(
      {
        item: item, 
        pool: pool
      }, function(){
        $scope.owneditems = Item.query({param:'owned'});
      });
    };

    $scope.unshareItem = function(share){ //TODO: It would be better to post a DELETE to the share resource
      Item.unshare({id: share.id}, function(){
        $scope.owneditems = Item.query({param:'owned'});
      });
    }
  }]);

  angular.module("tjingApp.controllers").controller("PoolController", ["$scope", "Pool", "Item", "Membership", function($scope, Pool, Item, Membership) {
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

  angular.module("tjingApp.controllers").controller("UserController", ["$scope", "User", function($scope, User){
    $scope.currentUser = User.current(function(data){
      if (data.facebookId!=null){ //If the User object contains a facebookId, use it to get the profile picture from facebook
        $scope.profilePicUrl = "http://graph.facebook.com/" +data.facebookId + "/picture?type=small"
      }
    });
    $scope.profilePicUrl = "/messages/error.png"
  }]);


}(angular));