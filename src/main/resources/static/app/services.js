/**
 * 
 */
 (function(angular) {
  var tjingServices = angular.module("tjingApp.services", ["ngResource"]);

  tjingServices.factory("Membership", ['$resource', function($resource){
    var membershipUrl = "/membership/:membershipId";
    return $resource(membershipUrl, {
      membershipId : "@id"
    }, {
      update : {
        method: "PATCH"
      }
    });
  }]);

  tjingServices.factory("Share", ['$resource', function($resource){
    var shareUrl = "/share/:shareId";
    return $resource(shareUrl, {
      shareId: "@id"
    });
  }]);

  tjingServices.factory("ItemRequest", ['$resource', function($resource) {
    var itemRequestUrl = "/itemrequest/:id";
    return $resource(itemRequestUrl, {

    });
  }]);

  //Item factory
  tjingServices.factory("Item", ['$resource', function($resource) {
    var itemlURL = "/item/:id";
    return $resource(itemlURL, {
      id: '@id'
    }, {
      update: {
        method: "PATCH"
      },
      request:{
        method:"POST",
        url: itemlURL + '/request'
      },
      share:{
        method: "POST",
        url: itemlURL +'/sharetopool/:poolId',
        params : {
          id: '@item.id',
          poolId: '@pool.id'
        }
      },
      unshare: {
        method :"DELETE",
        url : '/share/:id', //Deletes from the share resource. Like a proper REST call should. Look into making everything like this maybe.
        params : {
          id: '@id'
        }
      },
      setFbAvailable: {
        method: "POST",
        url: itemlURL + "/fbavailable"
      }
      
    });
  }]);

  tjingServices.factory('Feed', ['$resource', function($resource){
    return $resource('/notifications/:id', {
      id:"@id"
    },{
      update:{
        method:"PATCH"
      }
    });
  }]);

  tjingServices.factory("Share", ["$resource", function($resource){
    var shareUrl = "/share/:id"
    return $resource(shareUrl, {});
  }]);

  //Pool factory
  tjingServices.factory("Pool", ['$resource', function($resource){
    return $resource('/pool/:id', {
      id : '@id'
    }, {
      leave: {
        method: "DELETE",
        url: "pool/:id/leave",
        params: {
          id: "@id"
        },
        isArray: true //This endpoint returns an array of the remaining member pools. Possibly use this method for all the other actions
      },
      memberships : {
        method : "GET",
        url: "/pool/:id/memberships",
        params: {
          id: "@id"
        },
        isArray: true
      },
      shares : {
        method: "GET",
        url : "/pool/:id/shares",
        params: {
          id: "@id"
        },
        isArray: true
      },
      search: {
        method : "GET",
        url: "/pool/search/:searchStr",
        isArray : true
      }      
    })
  }]);

  //Interaction service
  tjingServices.factory("Interaction", ['$resource', function($resource){
    var interactionURL = '/interaction/:id';
    return $resource(interactionURL, {
      id:'@id'
    }, {
      accept: {
        method:"PATCH",
        url: interactionURL +'/accept'
      },
      handoverconfirm:{
        method:"PATCH",
        url: interactionURL +'/handoverconfirm'
      },
      returnconfirm:{
        method:"PATCH",
        url: interactionURL +'/returnconfirm'
      },
      unread: {
        method:"GET",
        url:"/messages/unread"
      }
    })
  }]);

  tjingServices.factory("User", ['$resource', function($resource){
    var userUrl = '/user/:id';
    return $resource(userUrl, {
      id: '@id'
    }, {
      current: {
        method: "GET",
        url:userUrl+"/me"
      },
      update: {
        method: "PATCH"
      }
      
    })
  }]);

  tjingServices.factory("InteractionMessage", ["$resource", function($resource){
    var msgUrl = "/interactionmessage/:id";

    return $resource(msgUrl, {
      id: "@id"
    }, {
      update: {
        method : "PATCH"
      }  
    });
  }]);

  tjingServices.factory("ItemCategory", ["$resource", function($resource){
    return $resource("/itemcategory");
  }]);

  tjingServices.factory("Notifications",["$rootScope", "InteractionMessage","Feed", function($rootScope, InteractionMessage, Feed){
    var Service = function(){
      var self = this;

      self.newNotification = null;
      self.notifications = [];
      self.notifTick=0;

      self.tick=0;
      self.unread=[];
      self.newMessage =null;

      InteractionMessage.query({}, function(response){
        self.unread = response;
        self.tick++;
      }); 

      Feed.query({}, function(response){
        self.notifications = response;
        self.notifTick++;
      })

      self.markAsRead = function(interactionId){
        for(i=0; i<self.unread.length; i++){
          if (self.unread[i].interaction.id == interactionId){
            self.unread.splice(i, 1);
            i--;
          }
        }
      }

      connect();
      function connect() {
        var socket = new SockJS('/feed');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
          stompClient.subscribe('/user/queue/notifications/', function(data){  
            var notification = JSON.parse(data.body);
            self.notifications.unshift(notification);
            self.newNotification = notification;
            self.notifTick++;
            $rootScope.$apply();
          });

          stompClient.subscribe('/user/queue/messaging/', function(data){
            var message = JSON.parse(data.body);
            self.newMessage = message;
            self.unread.push(message);
            self.tick++;
            $rootScope.$apply();
          });
        });
      }

      function disconnect() {
        if (stompClient != null) {
          stompClient.disconnect();
        }
        setConnected(false);
      //console.log("Disconnected");
    }
  }
  return new Service;
}]);

}(angular));