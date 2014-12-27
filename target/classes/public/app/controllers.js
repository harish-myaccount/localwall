app = angular.module('chatApp.controllers', []);

app.controller('MainController', function($scope, $location,
		GeolocationService, UserService, localStorageService, $window, $filter,alertService,
		AuthService, $modal) {
	$scope.position = null;
	$scope.message = "";
	$scope.users = null;
	$scope.authenticated = AuthService.getAuth();
	GeolocationService().then(function(position) {
		$scope.position = position;
		if (localStorageService.get('email')) {
			$scope.self.email = localStorageService.get('email');
			$scope.start();
		}
	}, function(reason) {
		console.log(reason);
		$scope.message = "Allow browser to share location to start using app"
	});

	$scope.self = {
		email : ''
	};
	$scope.reset = function() {
		localStorageService.set('email', '');
		$window.location.reload();
	}
	$scope.start = function() {
		localStorageService.set('email', $scope.self.email)
		var promise = UserService.sendLocation($scope.position,
				$scope.self.email);
		promise.then(function(response) {
			$scope.users = [];
			angular.forEach(response, function(user, i) {
				$scope.users.push(user)
			});
		}, function(error) {
			$scope.message = error
		});
	};

	$scope.postQuestion = function() {
		if ($scope.authenticated) {
			if ($scope.self.tagline && $scope.self.tagline.trim()) {
				UserService.postQuestion($scope.self.email,
						$scope.self.tagline).then(
						function(user) {
							$scope.users = $filter('filter')($scope.users,
									!$scope.self.email);
							$scope.users.push({
								content : user,
								self : true
							});
						});
			}
		} else {
			$modal.open($scope.popattr).result.then(function(secret) {
				AuthService.authenticate($scope.self.email,secret).then(function(allowed){
					if(allowed){
						$scope.authenticated = allowed;
						$scope.postQuestion();
					}else{
				        alertService.add('danger', 'Permission Denied', 'Wrong secret code');
					}
				})
			});
		}
	}
$scope.popattr={
		templateUrl : '/popup.htm',
		    controller : 'ModalInstanceCtrl',
		    resolve :{email: function(){
		    	return $scope.self.email;
		    }}
		};

	$scope.inbox = function() {
		if ($scope.authenticated)
			$location.path("/chat");
		else {
			$modal.open($scope.popattr).result.then(function(secret) {
				AuthService.authenticate($scope.self.email,secret).then(function(allowed){
					if(allowed){
						$scope.authenticated = allowed;
						$scope.inbox();
					}else{
				        alertService.add('danger', 'Permission Denied', 'Wrong secret code');
					}
				})
			});
		}

	}

	$scope.outbox = function() {

	}
});
app.controller('ModalInstanceCtrl', function($scope, $modalInstance,email) {
	$scope.email=email;
	$scope.secret='';
	$scope.ok = function() {
		if($scope.secret.trim())
		$modalInstance.close($scope.secret);
		else
			$modalInstance.dismiss('empty string')
	};

	$scope.cancel = function() {
		$modalInstance.dismiss('cancel');
	};
});
