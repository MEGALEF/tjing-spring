/**
 * 
 */
function Home($scope, $http){
	$http.get('/item/owned').success(function(data){
		$scope.items = data;
	});
	$http.get('/pool/mine').success(function(data){
		$scope.pools = data;
	});
}