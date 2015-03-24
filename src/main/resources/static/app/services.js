/**
 * 
 */
(function(angular) {
  var ItemFactory = function($resource) {
    return $resource('/item/:id', {
      id: '@id'
    }, {
      update: {
        method: "PUT"
      },
      remove: {
        method: "DELETE"
      }
    });
  };
  
  ItemFactory.$inject = ['$resource'];
  angular.module("tjingApp.services").factory("Item", ItemFactory);
}(angular));