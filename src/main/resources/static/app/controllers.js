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

  angular.module('tjingApp.controllers').controller("MyInteractionsController", 
    ["$scope", "Interaction", "User", function($scope, Interaction, User){
    $scope.allinteractions = Interaction.query();
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

  angular.module("tjingApp.controllers").controller("MyPoolsController", 
    ["$scope", "Pool", "Item", "Membership", function($scope, Pool, Item, Membership) {
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

  angular.module("tjingApp.controllers").controller("MyProfileController", function(){});
  //TODO ^^

  angular.module("tjingApp.controllers").controller("NavbarController", 
    ["$scope", "$location", "$http", "User", "Feed", 
    function($scope, $location, $http, User, Feed){
    
    var stompClient = null;
    $scope.searchResult = [];
    $scope.feedItems = Feed.query();
    $scope.currentUser = User.current(function(data){
       connect();
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

    function connect() {
        var socket = new SockJS('/feed');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/queue/notifications/', function(notification){
              $scope.$apply(function(){
                $scope.feedItems.unshift(JSON.parse(notification.body));
                //TODO: set some kind of flag for unread notifications
              });
            });
        });
    }

    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        setConnected(false);
        console.log("Disconnected");
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
    ["$scope", "$routeParams", "Item", "Membership", "Share", "User", "Interaction",
    function($scope, $routeParams, Item, Membership, Share, User, Interaction){
      $scope.currentItem = {}; 
      $scope.myMemberships = [];
      $scope.currentUser = User.current();

      $scope.isOwner = {};

      refreshItemsAndMemberships();

      function buildShareMap(item){
        item.shareMap = {};
        for (i = 0; i< $scope.myMemberships.length; i++){
          var pool = $scope.myMemberships[i].pool;
          item.shareMap[pool.id] = pool;
        }
        for (i = 0; i< item.shares.length; i++){
          var share = item.shares[i];
          item.shareMap[share.pool.id].itemIsShared = true;
        }
      }

      function refreshItemsAndMemberships(){
        Membership.query({}, function(data){
          $scope.myMemberships = data;

          Item.get({id: $routeParams.itemId}, function(data2){
            $scope.currentItem = data2;

            buildShareMap($scope.currentItem);
            $scope.isOwner = $scope.currentItem.owner.id == $scope.currentUser.id;
          });
        });
      }  

      $scope.shareItem = function(item, pool){
        Share.save(
        {
          itemId: item.id, 
          poolId : pool.id
        }, function(){
          refreshItemsAndMemberships();
        });
      };

      $scope.requestItem = function(){
        Interaction.save({itemId:$scope.currentItem.id}, function(data){
          var b = data;
        })
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
    ["$scope", "$routeParams", "Interaction", "User", 
    function($scope, $routeParams, Interaction, User){

    $scope.currentInteraction = {};  
    $scope.isOwner = null;
    $scope.currentUser = null;
    
    var stompClient = null;

    refresh();

    function refresh(){
      $scope.currentInteraction = Interaction.get({id: $routeParams.interactionId}, function(){
        connect();

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
      
    function connect() {
        var socket = new SockJS('/messaging');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/messaging/'+$scope.currentInteraction.id, function(message){
              $scope.$apply(function(){
                $scope.currentInteraction.conversation.push(JSON.parse(message.body));
              });
            });
        });
    }

    $scope.send = function(msg) {
        stompClient.send("/app/messaging/"+$scope.currentInteraction.id, {}, JSON.stringify({ 'text': msg }));
    }

    function disconnect() {
        if (stompClient != null) {
            stompClient.disconnect();
        }
        setConnected(false);
        //console.log("Disconnected");
    }
  }]);

  angular.module("tjingApp.controllers").controller("PoolController", 
    ["$scope", "$routeParams", "Pool", "Membership",
    function($scope, $routeParams, Pool, Membership){
    $scope.currentPool = {};
    $scope.myMemberships = [];
    $scope.membership = null;

    refresh();

    $scope.joinPool = function(){
      Membership.save({poolId: $scope.currentPool.id},function(response){
        $scope.membership = response;
      });
    };

    $scope.leavePool = function(){
      Membership.delete({membershipId: $scope.membership.id}, function(){ //TODO äuuuuuäuäuuähh
        $scope.membership = null;
      })
    };

    function refresh(){ //TODO could be made nicer w an endpoint that retrieves membership or a specific Pool
      Pool.get({id: $routeParams.poolId}, function(data){
        $scope.currentPool = data;

        Membership.query({}, function(data2){
          $scope.myMemberships = data2;

          for(i =0; i<$scope.myMemberships.length; i++){
            var membership = $scope.myMemberships[i];

            if (membership.pool.id == $scope.currentPool.id){
              $scope.membership = membership;
            }
          }
        });
      });
    };
  }]);

    angular.module("tjingApp.controllers").controller("UserController",
      ["$scope", "$routeParams", "User",
      function($scope, $routeParams, User){
        $scope.currentUser = {};

        User.get({id: $routeParams.userId}, function(data){
          $scope.currentUser = data;
        });
      }]);
}(angular));