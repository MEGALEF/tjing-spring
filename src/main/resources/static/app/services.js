/**
 * 
 */
(function(angular) {
var tjingServices = angular.module("tjingApp.services", ["ngResource"]);

  //Item factory
  tjingServices.factory("Item", ['$resource', function($resource) {
    var itemlURL = "/item/:id";
    return $resource(itemlURL, {
      id: '@id'
    }, {
      update: {
        method: "PUT"
      },
      remove: {
        method: "DELETE"
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
      }
    });
  }]);

  tjingServices.factory('Feed', ['$resource', function($resource){
    return $resource('/feed/', {});
  }]);

  //Pool factory
  tjingServices.factory("Pool", ['$resource', function($resource){
    return $resource('/pool/:id', {
      id : '@id'
    }, {
      join: {
        method : "POST",
        url: '/pool/:id/join',
        params: {
          id: "@id"
        }
      },
      leave: {
        method: "DELETE",
        url: "pool/:id/leave",
        params: {
          id: "@id"
        },
        isArray: true //This endpoint returns an array of the remaining member pools. Possibly use this method for all the other actions

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
        method:"PUT",
        url: interactionURL +'/accept'
      },
      deny: {
        method:"PUT",
        url: interactionURL +'/deny'
      },
      handoverconfirm:{
        method:"PUT",
        url: interactionURL +'/handoverconfirm'
      },
      returnconfirm:{
        method:"PUT",
        url: interactionURL +'/returnconfirm'
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
        url:"/user/"
      },
      profilepic: { //TODO: This doesn't seem to be working
        method: "GET",
        url: "http://graph.facebook.com/:userId/picture?type=small",
        params: {
          userId: "@facebookId"
        }
      }
    }
    )
  }]);
}(angular));