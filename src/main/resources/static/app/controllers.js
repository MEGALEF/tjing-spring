/**
 * 
 */
(function(angular) {
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
  
  angular.module("tjingApp.controllers").controller("ItemController", ["$scope", "Item", "Pool",
    function($scope, Item, Pool) {
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
      Item.request(item);
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
    $scope.allpools = Pool.query();
    $scope.myPools = [];
    $scope.myMemberships = Membership.query({}, function(){
      for (membership in $scope.myMemberships){
      $scope.myPools.push(membership.pool);
    }
    });
    


    $scope.searchPools = function(searchStr){
      return Pool.query({q: searchStr});
    };

    $scope.joinPool = function(pool){
      Membership.save({poolId:pool.id});
    };

    $scope.leavePool = function(pool){
      Membership.delete({membershipId:'0'}, function(){ //TODO äuuuuuäuäuuähh
        //$scope.mypools = ; //Refresh pools
      })
    };

    $scope.createPool = function(pool){
      Pool.save({
        title: pool.title,
        //description: "", //TODO: add description
        privacy: "OPEN" //TODO: enable other privacy modes
      }, function() { //TODO:Does save return anything? In that case use that data instead of sending a new request
        $scope.mypools = Pool.query({param:'mine'});
      $scope.allpools = Pool.query();
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