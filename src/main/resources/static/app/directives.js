(function(angular){
	var myApp = angular.module("tjingApp");
	var templatesDirectory = "app/templates/";

	myApp.directive("tjingUser", function(){
		return {
			scope: {
				user: '='
			},
			restrict: "E",
			link: function(scope, elem, attr){
			
			},
			templateUrl: "/app/templates/user.tpl.html"
		};
	});

	myApp.directive("tjingInteraction", function(){
		return {
			scope: {
				interaction : '='
			},
			restrict: "E",
			template: "<tjing-user user='interaction.borrower'></tjing-user> asked to borrow your {{interaction.item.title}}"
		};
	});

	myApp.directive("tjingItemrequest", function(){
		return {
			scope: {
				itemrequest : '='
			},
			restrict : "E",
			templateUrl: "/app/templates/itemrequest.tpl.html"
		}
	});

	myApp.directive("tjingItem", function(){
		return {
			scope: {
				item : '='
			},
			restrict : 'E',
			templateUrl : "app/templates/item.tpl.html"
		}
	});

	myApp.directive("tjingPool", function(){
		return {
			scope: {
				pool: "="
			},
			restrict : "E",
			templateUrl : "app/templates/pool.tpl.html"
		}
	})
}(angular));