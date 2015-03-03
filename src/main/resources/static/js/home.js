/**
 * 
 */
function Home($scope, $http){
	$http.get('/user/items').success(function(data){
		$scope.items = data;
	});
	$http.get('/user/pools').success(function(data){
		$scope.pools = data;
	});
}