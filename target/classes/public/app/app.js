 var app=angular.module("chatApp", [ "ngRoute","LocalStorageModule","ui.bootstrap","chatApp.controllers","chatApp.services" ]);
 //'$scope','$location', 'GeolocationService', 'UserService','localStorageService','$window','$filter',
  app.config( function($routeProvider) {
	    $routeProvider.
	      when('/', {
	        templateUrl: 'templates/main.htm',
	        controller: 'MainController'
	    }).when('/messages/inbox', {
	        templateUrl: 'templates/chat.htm',
	        controller: 'InboxController'
	    })
	    .when('/messages/outbox', {
	        templateUrl: 'templates/chat.htm',
	        controller: 'OutboxController'
	    }).
	      otherwise({
	        redirectTo: '/'
	      });
	});
