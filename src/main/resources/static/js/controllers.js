angular.module('mfsgwApp.controllers',
		[ 'angular-confirm', 'ui.bootstrap.tpls' ]).controller(
		'EntityListController',
		function($scope, $confirm, $state, popupService, pathService, $window,
				Entity, $http, $location) {
			var parts = pathService.getPathParts($location.path());
			$scope.refresh = function() {
				$scope.entities = Entity.query({
					p1 : parts[1],
					p2 : parts[2]
				});
			};

			$scope.entityDelete = function(entity) {
				var data = {
					headerText : 'Action: Delete',
					bodyText : 'Are you sure you want to delete?',
					ok : 'Yes',
					cancel : 'No'
				};
				$confirm(data, {
					templateUrl : 'views/confirm.html'
				}).then(function() {
					console.log("Confirmed!");
					entity.$delete({
						p1 : parts[1],
						p2 : parts[2]
					}, function() {
						$scope.refresh();
					});
				}, function() {
					console.log("Not Confirmed!");
				});
			}
			$scope.refresh();
		}).controller('EntityViewController',
		function($scope, $stateParams, Entity, pathService, $location) {
			var parts = pathService.getPathParts($location.path());
			$scope.entity = Entity.get({
				id : $stateParams.id,
				p1 : parts[1],
				p2 : parts[2]
			});

		}).controller(
		'EntityCreateController',
		function($scope, $state, $stateParams, Entity, $http, pathService,
				$location) {

			$scope.entity = new Entity();
			var parts = pathService.getPathParts($location.path());
			$scope.addEntity = function() {
				$scope.entity.$save({
					p1 : parts[1],
					p2 : parts[2]
				}, function() {
					$state.go(pathService.getSideBarMenu(parts[2]));
				});
			}

			/* Select options */
			var folder = parts[2] ? parts[2] : 'roles';
			if (folder === 'users') {
				$scope.roleOptions = Entity.query({
					p1 : 'data',
					p2 : 'roles'
				});
				$scope.entity.role = $scope.roleOptions[0];
			} else if (folder === 'formrepos') {
				$scope.projectOptions = Entity.query({
					p1 : 'data',
					p2 : 'projects'
				});
				$scope.entity.project = $scope.projectOptions[0];
			}

		}).controller('EntityEditController',
		function($scope, $state, $stateParams, Entity, pathService, $location) {
			var parts = pathService.getPathParts($location.path());
			$scope.updateEntity = function() {
				$scope.entity.$update({
					p1 : parts[1],
					p2 : parts[2]
				}, function() {
					$state.go(pathService.getSideBarMenu(parts[2]));
				});
			};

			$scope.loadEntity = function() {
				$scope.entity = Entity.get({
					id : $stateParams.id,
					p1 : parts[1],
					p2 : parts[2]
				});

			};

			var folder = parts[2] ? parts[2] : 'roles';
			if (folder === 'users') {
				$scope.roleOptions = Entity.query({
					p1 : 'data',
					p2 : 'roles'
				});
			} else if (folder === 'formrepos') {
				$scope.projectOptions = Entity.query({
					p1 : 'data',
					p2 : 'projects'
				});
			}
			$scope.loadEntity();
		}).controller('HomeController',
		function($scope, $stateParams, $http, MfsGwUtility) {

		});