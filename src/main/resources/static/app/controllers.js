/**
 * 
 */
(function(angular) {
  var AvailableItemsController = function($scope, Item) {
    Item.query(function(response) {
      $scope.items = response ? response : [];
    });
    
    $scope.addItem = function(title) {
      new Item({
        title: title,
        title: title
      }).$save(function(item) {
        $scope.items.push(item);
      });
      $scope.newItem = "";
    };
    
    $scope.updateItem = function(item) {
      item.$update();
    };
    
    $scope.deleteItem = function(item) {
      item.$remove(function() {
        $scope.items.splice($scope.items.indexOf(item), 1);
      });
    };
  };
  
  AvailableItemsController.$inject = ['$scope', 'Item'];
  angular.module("tjingApp.controllers").controller("AppController", AppController);
}(angular));