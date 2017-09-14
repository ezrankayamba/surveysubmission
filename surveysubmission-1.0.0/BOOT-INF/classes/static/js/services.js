angular.module('mfsgwApp.services', []).factory('Entity', function($rootScope, $resource, MfsGwUtility) {
	return $resource(MfsGwUtility.baseUrl()+'/:p1/:p2/:id', {
		id : '@id',
		p1: '@p1',
		p2: '@p2'
	}, MfsGwUtility.stdHeaders());
	
}).factory('MultipartEntity', function($rootScope, $resource, MfsGwUtility, $http) {
	var token = $("meta[name='_csrf']").attr("content");
	var header = $("meta[name='_csrf_header']").attr("content");
	var headerkv = {};
	headerkv[header] = token;
	headerkv['Content-Type']=undefined;
	return {
		updateMultiPart: function(url, data, callback){
			var request = $http({
				method: "PUT",
				url: url,
				data: data,
				transformRequest: angular.identity,
				headers: headerkv
			});
			
			request.then(function(response){
				callback(response);
			});
		},
		saveMultiPart: function(url, data, callback){
			var request = $http({
				method: "POST",
				url: url,
				data: data,
				transformRequest: angular.identity,
				headers: headerkv
			});
			
			request.then(function(response){
				callback(response);
			});
		}
	}
	
}).service('popupService', [function($scope, $confirm) {
	this.showPopup = function(action, message) {
		return $window.confirm(message);
	}
}]).service('pathService', [function($scope) {
	
	this.getPathParts = function(myPath) {
		var parts = ['', 'data', 'roles' ];
		try {
			var path = myPath;
			var temp = path.split('/');
			if(temp.length>=3){parts = temp;}
		} catch (e) {
			console.log(e);
		}
		return parts;
	};
	this.getSideBarMenu = function(e){
		var menu='';
		switch (e) {
		case 'roles':
			menu='roleList';
			break;
		case 'users':
			menu='userList';
			break;
		case 'projects':
			menu='projectList';
			break;
		case 'formrepos':
			menu='formReposList';
			break;

		default:menu='roleList';
			break;
		}
		
		return menu;
	};
	this.getSideBarMenuExt = function(parts){
		var menu='';
		if(parts[3] === 'new'){
			menu="entityAdd({p1:'"+parts[1]+"',p2:'"+parts[2]+"'})";
		}else if(parts[4] === 'edit'){
			menu="entityEdit({p1:'"+parts[1]+"',p2:'"+parts[2]+"',id:"+parts[3]+"})";
		}else if(parts[4] === 'view'){
			menu="entityView({p1:'"+parts[1]+"',p2:'"+parts[2]+"',id:"+parts[3]+"})";
		}else{
			console.log('Unknown context');
			console.log(parts);
		}
		return menu;
	};
}]).service('MfsGwUtility', [function($scope, $resource, $location) {
	
	this.baseUrl = function (){
		return '';
	};
	this.stdHeaders = function (){
		var token = $("meta[name='_csrf']").attr("content");
		var header = $("meta[name='_csrf_header']").attr("content");
		var headerkv = {};
		headerkv[header] = token;
		
		var globalHeaders =  {
				update : {
					method : 'PUT',
					headers : headerkv
				},
				delete : {
					method : 'DELETE',
					headers : headerkv
				},
				save : {
					method : 'POST',
					headers : headerkv
				},
				saveMultiPart : {
					method: "POST",
		            transformRequest: angular.identity,
		            headers: { 'Content-Type': undefined }
				}
			};
		
		return globalHeaders;
	};
}]);

