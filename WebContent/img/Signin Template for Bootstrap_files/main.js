/**
*  Module
*
* Description
*/
var app = angular.module('OpenReddit', ['ngRoute']);

app.config( function ( $routeProvider ) {

	$routeProvider
	.when('/', 
	{
		templateUrl: 'welcome.html', 
		controller: 'BlankController'
	})
	.when('/login', 
	{
		templateUrl: 'login.html', 
		controller: 'BlankController'
	})
	.when('/register', 
	{
		templateUrl: 'register.html', 
		controller: 'BlankController'
	})
	.when('/top', 
	{
		templateUrl: 'stories.html', 
		controller: 'StoriesController'
	})
	.when('/stories/:id', 
	{
		templateUrl: 'story.html', 
		controller: 'StoryDetailController'
	})
	.when('/new_story', 
	{
		templateUrl: 'new_story.html', 
		controller: 'NewStoryController'
	})
	.otherwise({
		redirectTo:'/'
	})


})


app.factory('Stories', function($http) {
   return {
        getTopStories: function() {
             //return the promise directly.
             return $http.get('http://webedu13.mtacloud.co.il:8080/OpenReddit/stories');
        },
        getStoryWithId: function(id) {
             //return the promise directly.
             return $http.get('http://webedu13.mtacloud.co.il:8080/OpenReddit/stories/' + id);
        },
        upVoteToStoryWithId: function(id){
        	
        	return $http.put('http://webedu13.mtacloud.co.il:8080/OpenReddit/stories/' + id + '/up')
        },
        downVoteToStoryWithId: function(id){
        	
        	return $http.put('http://webedu13.mtacloud.co.il:8080/OpenReddit/stories/' + id + '/down')
        }
   }
});

app.controller('StoriesController',function($scope,Stories){

	Stories.getTopStories()
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.stories = data['_results'];
		}
	});

	$scope.upvote = function (article) {

		article._votes += 1;
		Stories.upVoteToStoryWithId(article._storyID)
		.success( function(data) {
			
			console.log(data);
		});
	}
	$scope.downvote = function (article) {

		article._votes -= 1;
		Stories.downVoteToStoryWithId(article._storyID)
		.success( function(data) {
			
			console.log(data);
		});
	}
	
});
app.controller('StoryDetailController',function($scope,$routeParams,Stories){

	
	Stories.getStoryWithId($routeParams.id)
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.story = data['_results'];
		}
	});


	$scope.upvote = function (article) {

		article._votes += 1;
		Stories.upVoteToStoryWithId(article._storyID)
		.success( function(data) {
			
			console.log(data);
		});
	}
	$scope.downvote = function (article) {

		article._votes -= 1;
		Stories.downVoteToStoryWithId(article._storyID)
		.success( function(data) {
			
			console.log(data);
		});
	}
	 
});

app.controller('NewStoryController',function($scope,$routeParams,Stories){

	
	

	
	 
});

app.controller('BlankController',function($scope,Stories){

	Stories.getTopStories()
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.articles = data['_results'];
		}
	});
	
});

