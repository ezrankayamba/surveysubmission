angular.module('mfsgwApp.controllers',
		[ 'angular-confirm', 'ui.bootstrap.tpls' ]).controller(
		'EntityListController',
		function($scope, $confirm, $state, popupService, pathService, $window,
				Entity, $http, $location) {
			var parts = pathService.getPathParts($location.path());
			$scope.entities = Entity.query({
				p1 : parts[1],
				p2 : parts[2]
			});

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
						$window.location.href = '';
					});
				}, function() {
					console.log("Not Confirmed!");
				});
			}

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
			} else if (folder === 'services') {
				$scope.issuerOptions = Entity.query({
					p1 : 'data',
					p2 : 'issuers'
				});
				$scope.entity.issuer = $scope.issuerOptions[0];
				$scope.partnerOptions = Entity.query({
					p1 : 'data',
					p2 : 'partners'
				});
				$scope.entity.partner = $scope.partnerOptions[0];
			}

		}).controller(
		'EntityEditController',
		function($scope, $state, $stateParams, Entity, $http, pathService,
				$location) {
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
			} else if (folder === 'services') {
				$scope.issuerOptions = Entity.query({
					p1 : 'data',
					p2 : 'issuers'
				});
				$scope.partnerOptions = Entity.query({
					p1 : 'data',
					p2 : 'partners'
				});
			}
			$scope.loadEntity();
		}).controller(
		'HomeController',
		function($scope, $stateParams, $http, MfsGwUtility) {
			// the diagram we are going to display
			var bpmnXML;

			// BpmnJS is the BPMN viewer instance
			var viewer = new BpmnJS({
				container : '#canvas'
			});

			// import a BPMN 2.0 diagram
			viewer.importXML(bpmnXML, function(err) {
				if (err) {
					// import failed :-(
				} else {
					// we did well!

					
				}
			});

			var xhr = new XMLHttpRequest();

			xhr.onreadystatechange = function() {
				if (xhr.readyState === 4) {
					viewer.importXML(xhr.response, function(err) {
						if (err) {
							console.log(err);
						} else {
							var canvas = viewer.get('canvas');
							canvas.zoom('1.0');
							var width = canvas.width;
							var height = canvas.height;
							console.log('W: '+width);
							console.log(height);
							//$("#homeTable").load(" #homeTable");
						}
					});
				}
			};

			xhr.open('GET', MfsGwUtility.baseUrl()
					+ '/bpmn/pizza-collaboration.bpmn', true);
			xhr.send(null);
		});