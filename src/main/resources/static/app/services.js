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
          poolId: '@poolId'
        }
      }
    });
  }]);

  //Pool factory
  tjingServices.factory("Pool", ['$resource', function($resource){
    return $resource('/pool/:id', {
      id : '@id'
    }, {
      join: {
        method : "POST",
        url: '/pool/:id/join'
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
}(angular));