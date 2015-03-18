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
	$http.get('/interaction/incoming').success(function(data){
		$scope.incoming = data;
	});
	
	$http.get('/item/').success(function(data){
		$scope.poolitems = data;
	});
}