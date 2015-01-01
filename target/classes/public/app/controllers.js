app = angular.module('chatApp.controllers', []);

app.controller('OutboxController',function($scope,UserService,MessageService,AuthService,alertService){
	$scope.selectedOutboxUser = UserService.getOutboxSelected();
	$scope.items=[];
	$scope.max=543;
	$scope.send=function(i,toOwner,topic,mess){
		MessageService.sendMessage(AuthService.getEmail(),toOwner,topic,mess,AuthService.getPic()).then(function(msg){
			if(!msg){
				alertService.add('danger', 'Not Allowed', 'The other user must accept your response to communicate further');
			  return;
			}
				if(!AuthService.getPic())
				AuthService.setPic(msg.author);
			$scope.items[i].messages.push(msg);
		});
	}
	MessageService.getTopicsResponded(AuthService.getEmail()).then(function(data){
		for(i=0;i<data.length;i++){
			value=data[i];
			item={title:value.title,owner:value.owner,messages:value.messages};
			$scope.items.push(item);
			if($scope.selectedOutboxUser.email==value.owner)
				$scope.default=item;
			}
		if(!$scope.default){
			$scope.items[0]={title:$scope.selectedOutboxUser.tagline,owner:$scope.selectedOutboxUser.email,messages:[]}
			$scope.default = $scope.items[0];
		}
	});
	
	
	
	
});


app.controller('InboxController',function($scope, $location,AuthService,UserService,MessageService){
	if(!AuthService.getAuth()){
		$location.path("/");
		return;
	}
	

	$scope.items = [];
	$scope.max=543;
	$scope.send=function(i,toOwner,topic,mess,both){
		var index = both.indexOf(toOwner);
		if (index >= 0) {
		  both.splice( index, 1 );
		}
		MessageService.sendMessage(AuthService.getEmail(),both[0],topic,mess,AuthService.getPic()).then(function(msg){
			if(!AuthService.getPic())
				AuthService.setPic(msg.author);
			$scope.items[i].messages.push(msg);
		});
	}
	
	MessageService.getInbox(AuthService.getEmail()).then(function(data){
		angular.forEach(data,function(value,key){
			$scope.items.push(value);
		});
		$scope.default = $scope.items[0];
	});

});


app.controller('ItemController', ['$scope', function (scope) {

                scope.$parent.isopen = (scope.$parent.default === scope.item);

                scope.$watch('isopen', function (newvalue, oldvalue, scope) {
                    scope.$parent.isopen = newvalue;
                });
                
                

            }]);

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
			AuthService.setEmail($scope.self.email);
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
				if(user.content.email==$scope.self.email)
					AuthService.setPic(user.content.image);
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
			}else{
		        alertService.add('warning', 'Empty String', 'Fill some thing ');
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
			$location.path("/messages/inbox");
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

	$scope.outbox = function(user) {
		UserService.setOutboxSelected(user);
		$location.path("/messages/outbox");
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
