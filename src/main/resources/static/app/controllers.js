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
      Interaction.accept(interaction);
    }

    $scope.denyRequest = function(interaction){
      Interaction.deny(interaction);
      
    }

    $scope.confirmHandover = function (interaction){
      Interaction.handoverconfirm(interaction);

    }

    $scope.confirmReturn = function(interaction){
      Interaction.returnconfirm(interaction);

    }
  }]);
  
  angular.module("tjingApp.controllers").controller("ItemController", ["$scope", "Item", "Pool",
    function($scope, Item, Pool) {
    $scope.owneditems = Item.query({param:'owned'});
    $scope.borroweditems = Item.query({param:'borrowed'});
    $scope.items=Item.query();
    $scope.mypools = Pool.query({param:"mine"});
    
    $scope.addItem = function(title) {
      new Item({
        title: title
      }).$save();
      $scope.newItem = "";
      $scope.owneditems = Item.query({param:'owned'});
    };
  
    
    $scope.deleteItem = function(item) {
      Item.delete({id: item.id});
      $scope.owneditems = Item.query({param:'owned'});
    };
    
    $scope.requestItem = function(item){
      Item.request(item);
    };

    $scope.shareItem = function(itemId, poolId){
      Item.share({
        id: itemId, 
        poolId: poolId});
    };
  }]);

  angular.module("tjingApp.controllers").controller("PoolController", ["$scope", "Pool", function($scope, Pool) {
    $scope.mypools = Pool.query({param:'mine'});
    $scope.allpools = Pool.query();

    $scope.searchPools = function(searchStr){
      return Pool.query({q: searchStr});
    };

    $scope.joinPool = function(pool){
      Pool.join(pool);
      $scope.mypools = Pool.query({param:'mine'});
    };

    $scope.createPool = function(pool){
      Pool.save({
        title: pool.title,
        //description: "", //TODO: add description
        privacy: "OPEN" //TODO: enable other privacy modes
      });
      $scope.mypools = Pool.query({param:'mine'});
      $scope.allpools = Pool.query();
    };
  }]);

  angular.module("tjingApp.controllers").controller("UserController", ["$scope", "User", function($scope, User){
    $scope.currentUser = User.current();
  }]);


}(angular));