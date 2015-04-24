/**
 * 
 */
(function(angular) {
var tjingServices = angular.module("tjingApp.services", ["ngResource"]);

  tjingServices.factory("Membership", ['$resource', function($resource){
    var membershipUrl = "/membership/:membershipId";
    return $resource(membershipUrl);
  }]);

  tjingServices.factory("Share", ['$resource', function($resource){
    var shareUrl = "/share/:shareId";
    return $resource(shareUrl);
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
      },
      setFbAvailable: {
        method: "POST",
        url: itemlURL + "/fbavailable"
      }
      
    });
  }]);

  tjingServices.factory('Feed', ['$resource', function($resource){
    return $resource('/feed/:id', {});
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
      deny: {
        method:"DELETE",
        url: interactionURL +'/deny'
      },
      handoverconfirm:{
        method:"PATCH",
        url: interactionURL +'/handoverconfirm'
      },
      returnconfirm:{
        method:"PATCH",
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
        url:userUrl+"/me"
      }
    }
    )
  }]);
}(angular));