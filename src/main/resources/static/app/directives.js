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
}(angular));