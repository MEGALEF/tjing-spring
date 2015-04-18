(function(angular){
	var myApp = angular.module("tjingApp");

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
}(angular));