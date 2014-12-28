app = angular.module("chatApp.services", []);


app.factory("GeolocationService", function($q, $window, $rootScope) {
	return function() {
		var deferred = $q.defer();

		if (!$window.navigator) {
			$rootScope.$apply(function() {
				deferred.reject(new Error("Geolocation is not supported"));
			});
		} else {
			$window.navigator.geolocation.getCurrentPosition(
					function(position) {
						$rootScope.$apply(function() {
							deferred.resolve(position);
						});
					}, function(error) {
						$rootScope.$apply(function() {
							deferred.reject(error);
						});
					});
		}

		return deferred.promise;
	}
});

app.service("UserService", function($q, $http) {
	
	var outboxSelected;
	var inboxSelected;
	
	this.setOutboxSelected=function(email){
		outboxSelected = email;
	}
	
	this.getOutboxSelected=function(){
		outboxSelected;
	}

	this.sendLocation = function(position, email) {
		var deferred = $q.defer();
		user = {
			coOrd : position.coords,
			email : email
		};
		$http.post('/users/nearby', user).success(function(data) {
			deferred.resolve(data.content);
		}).error(function(msg, code) {
			deferred.reject(msg);
			console.error(msg, code);
		});
		return deferred.promise;
	};

	this.postQuestion = function(email, question) {
		var deferred = $q.defer();
		user = {
			tagline : question,
			email : email
		}
		$http.post('/users/question/add', user).success(function(data) {
			deferred.resolve(data);
		}).error(function(msg) {
			deferred.reject(msg);
			console.error(msg);
		});
		return deferred.promise;
	};

	
});

app.factory("AuthService", function($q,$http) {
	var isAuthenticated;
	var isAccepted;

	return {
		setAuth : function(result) {
			isAuthenticated = result
		},
		getAuth : function() {
			return isAuthenticated;
		},
		authenticate : function(email,secret) {
			var deferred = $q.defer();
			$http.post('/users/authenticate', {
				email : email,
				code : secret,
			}).success(function(data) {
				if(data)
					isAuthenticated = true;
				deferred.resolve(data);
			}).error(function(msg, code) {
				deferred.reject(msg);
				console.error(msg, code);
			});
			return deferred.promise;
		}

	};
});


app.factory('alertService', ['$timeout', '$rootScope',
                                     function($timeout, $rootScope) {
                                         alertService = {};
                                         $rootScope.alerts = [];

                                         alertService.add = function(type, title, msg, timeout) {
                                             $rootScope.alerts.push({
                                                 type: type,
                                                 title: title,
                                                 msg: msg,
                                                 close: function() {
                                                     return alertService.closeAlert(this);
                                                 }
                                             });

                                             if(typeof timeout == 'undefined') {
                                                 timeout = 8000;
                                             }

                                             if (timeout) {
                                                 $timeout(function(){
                                                     alertService.closeAlert(this);
                                                 }, timeout);
                                             }
                                         }

                                         alertService.closeAlert = function(alert) {
                                             return this.closeAlertIdx($rootScope.alerts.indexOf(alert));
                                         }

                                         alertService.closeAlertIdx = function(index) {
                                             return $rootScope.alerts.splice(index, 1);
                                         }

                                         return alertService;
                                     }
                                 ]);