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
      $scope.loading = true;
      $http.get(importUrl).success(function(data){
        $scope.loading = false;
        $scope.facebookGroups = data;
      });
    };

    $scope.importGroup = function(group){
      $http.post(importUrl, group).success(function(data){
        $scope.importedGroupMembership = data;
      });
    };

  }]);

  angular.module('tjingApp.controllers').controller("MyInteractionsController", 
    ["$scope", "Interaction", "User", function($scope, Interaction, User){
      $scope.incomingrequests = Interaction.query({param:'incoming'});
      $scope.outgoingrequests = Interaction.query({param:'outgoing'});
      $scope.currentUser = User.current();

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

angular.module("tjingApp.controllers").controller("MyProfileController", ["$scope", "User", 
  function($scope, User){
    $scope.currentUser = {};

    refresh();

    $scope.save = function(){
      User.update($scope.currentUser, function(response){
        $scope.currentUser = response;
      });
    };

    function refresh(){
      User.current({}, function(response){
        $scope.currentUser = response;
      });
    };
  }]);

angular.module("tjingApp.controllers").controller("MyItemsController", ["$scope", "$location", "Item", "Pool", "Interaction", "Share",
  function($scope, $location, Item, Pool, Interaction, Share) {
    // Scope variable initialization
    $scope.myItems = [];

    refreshMyItems();

    function refreshMyItems(){
      $scope.myItems = Item.query({param:'owned'});
    }
    
    $scope.addItem = function(item) {
      Item.save(item, function(response){
        $scope.myItems.push(response);
        $scope.newItem = "";
        $location.path('/item/' + response.id);
      });
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
      Interaction.save({itemId: item.id}, function(response){
        $location.path('/interaction/'+response.id);
      })
    };
  }]);

angular.module("tjingApp.controllers").controller("MyPoolsController", 
  ["$scope", "$location", "Pool", "Item", "Membership", function($scope, $location, Pool, Item, Membership) {
    $scope.showPools = false;
    $scope.myPools = [];
    $scope.myMemberships = [];

    refreshMyPools();

    $scope.searchStr = "";
    $scope.searchResults = null;

    function refreshMyPools(){
      Membership.query({}, function (response){
        $scope.myMemberships = response;
        $scope.myPools = [];
        for (membership in $scope.myMemberships){
          $scope.myPools.push(membership.pool);
        }
      });
    };

    $scope.searchPools = function(){
      Pool.query({q: $scope.searchStr}, function(response){
        $scope.searchResults = response;
      });
    };

    $scope.joinPool = function(pool){
      var newMembership = {pool: {id : pool.id}};
      Membership.save(newMembership, function(){
        refreshMyPools();
      });
    };

    $scope.leavePool = function(membership){
      Membership.delete({membershipId:membership.id}, function(){ //TODO äuuuuuäuäuuähh
        refreshMyPools();
      })
    };

    $scope.createPool = function(pool){
      Pool.save(pool, function(response) { //TODO:Does save return anything? In that case use that data instead of sending a new request
        $location.url("/pool/"+response.id);
      });
    };
  }]);

angular.module("tjingApp.controllers").controller("NavbarController", 
  ["$scope", "$location", "$http", "User", "Feed", "Messaging", "Notifications",
  function($scope, $location, $http, User, Feed, Messaging, Notifications){

    $scope.searchResult = [];
    $scope.feedItems = Feed.query();
    $scope.notifService = Notifications;

    $scope.$watch('notifService.newNotification', function(newVal, oldVal, scope){
      if(newVal){
        $scope.feedItems.unshift(newVal);
      }
    });

    $scope.currentUser = User.current(function(data){
       //connect();
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
  ["$scope", "$routeParams", "$location", "Item", "Membership", "Share", "User", "Interaction",
  function($scope, $routeParams, $location, Item, Membership, Share, User, Interaction){
    $scope.currentItem = {}; 
    $scope.myMemberships = [];
    $scope.currentUser = User.current();
    $scope.uploadResponse;
    $scope.shareMap = {};

    $scope.isOwner = {};

    refreshItemsAndMemberships();

    $scope.imgUploadUrl = function(){
      return "/item/"+ $scope.currentItem.id +"/image";
    }

    $scope.updatePublic = function(){
      Item.update({id: $scope.currentItem.id, sharedPublic: $scope.currentItem.sharedPublic}, function(response){
        $scope.currentItem = response;
      });
    }

    $scope.uploadComplete = function(response){
      $scope.uploadResponse = response;
    }

    function buildShareMap(item){
      $scope.shareMap = {};
      for (i = 0; i< $scope.myMemberships.length; i++){
        var pool = $scope.myMemberships[i].pool;
        $scope.shareMap[pool.id] = pool;
      }
      for (i = 0; i< item.shares.length; i++){
        var share = item.shares[i];
        $scope.shareMap[share.pool.id].itemIsShared = true;
      }
    }

    function refreshItemsAndMemberships(){
      Membership.query({}, function(data){
        $scope.myMemberships = data;

        Item.get({id: $routeParams.itemId}, function(data2){
          $scope.currentItem = data2;

          $scope.isOwner = $scope.currentItem.owner.id == $scope.currentUser.id;
          if ($scope.isOwner){
            buildShareMap($scope.currentItem);
          }
        });
      });
    }  

     $scope.requestItem = function(){
      Interaction.save({itemId:$scope.currentItem.id}, function(response){
        $location.path("/interaction/"+response.id);
      })
    };

    $scope.shareItem = function(item, pool){
      Share.save(
      {
        itemId: item.id, 
        poolId : pool.id
      }, function(){
        refreshItemsAndMemberships();
      });
    };

    $scope.unshareItem = function(item, pool){
      for(i=0; i<$scope.currentItem.shares.length; i++){
        if ($scope.currentItem.shares[i].pool.id==pool.id){ //Iterate through item shares to find the right one
          Share.delete({id: $scope.currentItem.shares[i].id}, function(){
            refreshItemsAndMemberships();
          })
        }
      }
    };
  }]);

angular.module("tjingApp.controllers").controller("InteractionController",
  ["$scope", "$routeParams", "Interaction", "User", "Messaging", 
  function($scope, $routeParams, Interaction, User, Messaging){

    $scope.messagingService = Messaging;
    $scope.currentInteraction = {};  
    $scope.isOwner = null;
    $scope.currentUser = null;
    $scope.text = "";

    refresh();

    $scope.$watch('messagingService.newMessage', function(newVal, oldVal, scope){
      if(newVal){
        if (newVal.interactionId == $scope.currentInteraction.id){
          $scope.currentInteraction.conversation.push(newVal);
        }
      }
    });

    function refresh(){
      $scope.currentInteraction = Interaction.get({id: $routeParams.interactionId}, function(){
        $scope.currentUser = User.current({}, function(response){
          $scope.currentUser = response;
          $scope.isOwner = ($scope.currentUser.id == $scope.currentInteraction.item.owner.id);
        })  
      });
    }

    $scope.acceptRequest = function(){
      Interaction.accept($scope.currentInteraction, function(response){
        $scope.currentInteraction = response;
      });
    }

    $scope.denyRequest = function(){
      Interaction.deny($scope.currentInteraction, function(response){
        $scope.currentInteraction = response;
      });
    }

    //Borrower side
    $scope.confirmHandover = function (){
      Interaction.handoverconfirm($scope.currentInteraction, function(response){
        $scope.currentInteraction = response;
      });
    }

    //Owner side
    $scope.confirmReturn = function(){
      Interaction.returnconfirm($scope.currentInteraction, function(response){
        $scope.currentInteraction = response;
      });
    }

    $scope.send = function() {
      if ($scope.text && $scope.text.length>0){
        Messaging.send($scope.currentInteraction.id, $scope.text);  
      }
      $scope.text = "";
    }
  }]);

angular.module("tjingApp.controllers").controller("HomeController",
  ["$scope", function($scope){

  }]);

angular.module("tjingApp.controllers").controller("PoolController", 
  ["$scope", "$routeParams", "Pool", "Membership", "Share", "Item",
  function($scope, $routeParams, Pool, Membership, Share, Item){
    $scope.currentPool = {};
    $scope.memberships = [];
    $scope.shares = [];
    $scope.myShares = [];
    $scope.myMembership = null;
    $scope.myItems = [];
    $scope.shareMap = [];
    
    refresh();

    $scope.makeAdmin = function(membership){
      Membership.update({id: membership.id, role:"ADMIN"}, function(response){
        membership = response;
      });
    }

    $scope.joinPool = function(){
      var newMembership = {pool:{id: $scope.currentPool.id}};
      Membership.save(newMembership, function(response){
        $scope.myMembership = response;
      });
    };

    $scope.leavePool = function(){
      Membership.delete({membershipId: $scope.myMembership.id}, function(){ //TODO äuuuuuäuäuuähh
        $scope.myMembership = null;
      });
    };

    $scope.kickMember = function(membership){
      Membership.delete({membershipId: membership.id}, function(){ //TODO äuuuuuäuäuuähh
        membership = null;
      });
    }

    $scope.addShare = function(item){
      Share.save(
      {
        itemId: item.id, 
        poolId : $scope.currentPool.id
      }, function(){
        refresh(); //TODO optimize
      });
    };

    $scope.removeShare = function(share){
      Share.delete(share, function(){
        refresh(); //TODO optiumize
      });
    };

    function buildShareMap(){
      $scope.shareMap = [];
      for (i=0; i<$scope.myItems.length; i++){
        var item = $scope.myItems[i];
        var share = findShare(item)
        $scope.shareMap.push({item: item, share: share, isShared: share ? true : false});

        function findShare(item){
          for(i=0; i<$scope.myShares.length; i++){
            var share = $scope.myShares[i];
            if(share.item.id == item.id) return share;
          }
          return null;
        }
      }
    }

    function refresh(){ //TODO could be made nicer w an endpoint that retrieves membership or a specific Pool
      Pool.get({id: $routeParams.poolId}, function(pool){
        $scope.currentPool = pool;

        $scope.myItems = Item.query({param: "owned"}, function(){
          $scope.myShares = Share.query({pool: pool.id}, function(){
            buildShareMap();
          });
        });

        Membership.query({}, function(myMemberships){

          for(i =0; i< myMemberships.length; i++){
            var m = myMemberships[i];

            if (m.pool.id == $scope.currentPool.id){
              $scope.myMembership = m;

              //Get pool memberships and shares
              $scope.memberships = Pool.memberships({id: $scope.currentPool.id}, function(){});
              $scope.shares = Pool.shares({id: $scope.currentPool.id}, function(){});
            }
          }
        });
      });
    };
  }]);

angular.module("tjingApp.controllers").controller("UserController",
  ["$scope", "$routeParams", "User", "Item",
  function($scope, $routeParams, User, Item){
    $scope.currentUser = {};
    $scope.items = [];

    refresh();

    function refresh(){
      User.get({id: $routeParams.userId}, function(data){
        $scope.currentUser = data;
        Item.query({owner: $scope.currentUser.id}, function(response){
          $scope.items = response;
        })
      });
    }

  }]);
}(angular));