angular.module('mfsgwApp', [ 'ui.router', 'ngResource', 'mfsgwApp.controllers',
		'mfsgwApp.services' ]);

angular.module('mfsgwApp').config(function($stateProvider, $httpProvider) {
	var frag = "/:p1/:p2";
	$stateProvider.state('entityView', {
		url : frag + '/:id/view',
		params : {
			p1 : {
				value : 'data'
			},
			p2 : {
				value : 'roles'
			}
		},
		templateUrl : function(stateParams) {
			console.log(stateParams);
			return 'views/' + stateParams.p2 + '/view.html'
		},
		controller : 'EntityViewController'
	}).state('entityAdd', {
		url : frag + '/new',
		params : {
			p1 : {
				value : 'data'
			},
			p2 : {
				value : 'roles'
			}
		},
		templateUrl : function(stateParams) {
			console.log(stateParams);
			return 'views/' + stateParams.p2 + '/add.html'
		},
		controller : 'EntityCreateController'
	}).state('entityEdit', {
		url : frag + '/:id/edit',
		params : {
			p1 : {
				value : 'data'
			},
			p2 : {
				value : 'roles'
			}
		},
		templateUrl : function(stateParams) {
			return 'views/' + stateParams.p2 + '/edit.html'
		},
		controller : 'EntityEditController'
	}).state('roleList', {// side bar menus
		url : '/data/roles',
		templateUrl : 'views/roles/list.html',
		controller : 'EntityListController'
	}).state('userList', {
		url : '/data/users',
		templateUrl : 'views/users/list.html',
		controller : 'EntityListController'
	}).state('projectList', {
		url : '/data/projects',
		templateUrl : 'views/projects/list.html',
		controller : 'EntityListController'
	}).state('formReposList', {
		url : '/data/formrepos',
		templateUrl : 'views/formrepos/list.html',
		controller : 'EntityListController'
	}).state('home', {
		url : '/home',
		templateUrl : 'views/home.html',
		controller : 'HomeController'
	});
}).run(function($state, $trace, $rootScope, pathService, $location) {
	//$trace.enable("TRANSITION", "VIEWCONFIG");
	$location.path("home");
});
