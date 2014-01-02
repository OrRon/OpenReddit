/**
*  Open Reddit
*
* Educational Code by Or Ron for a college work.
* This module is a basic reddit like web application
* its based on Angular + Bootstarp
*/
var baseurl = "http://webedu13.mtacloud.co.il:8080/OpenReddit/"
var app = angular.module('OpenReddit', ['ngRoute']);

app.config( function ( $routeProvider ) {


//////////////////////////////////////////////////////////////////////
//
// Routes
//
//////////////////////////////////////////////////////////////////////
	$routeProvider
	.when('/', 
	{
		templateUrl: 'welcome.html', 
		controller: 'BlankController'
	})
	.when('/logout', 
	{
		templateUrl: 'welcome.html', 
		controller: 'LogoutController'
	})
	.when('/login', 
	{
		templateUrl: 'login.html', 
		controller: 'LoginController'
	})
	.when('/register', 
	{
		templateUrl: 'register.html', 
		controller: 'RegisterController'
	})
	.when('/top', 
	{
		templateUrl: 'stories.html', 
		controller: 'TopStoriesController'
	})
	.when('/new', 
	{
		templateUrl: 'stories.html', 
		controller: 'NewStoriesController'
	})
	.when('/new_story', 
	{
		templateUrl: 'new_story.html', 
		controller: 'NewStoryController'
	})
	.when('/stories/:id', 
	{
		templateUrl: 'story.html', 
		controller: 'StoryDetailController'
	})
	.when('/subreddits', 
	{
		templateUrl: 'subreddits.html', 
		controller: 'SubredditsController'
	})
	.when('/subreddits/:id', 
	{
		templateUrl: 'stories.html', 
		controller: 'SubredditsStoriesController'
	})
    .when('/search/:keyword',
    {
        templateUrl: 'stories.html',
        controller: 'SearchStoriesController'
    })
	.otherwise({
		redirectTo:'/'
	})


})

//////////////////////////////////////////////////////////////////////
//
// Services
//
//////////////////////////////////////////////////////////////////////

app.factory('db',function(){

    /*
    This service will manage the DB accesses
     */

    var db = openDatabase('mydb', '1.0', 'history', 2 * 1024 * 1024);
    db.transaction(function (tx) {
        tx.executeSql('CREATE TABLE IF NOT EXISTS stories (id unique, text)');

    });

    return {
        addStory: function (story) {

            db.transaction(function (tx) {
                tx.executeSql('INSERT INTO stories (id, text) VALUES ('+ story._storyID+ ', "'+story._title+'")');


            },function(error)
            {
                console.log('Oops.  Error was '+error.message+' (Code '+error.code+')');
            });
        },
        getStories: function (callback) {

            db.transaction(function (tx) {
                tx.executeSql('SELECT * FROM stories', [], function (tx, results) {
                    var len = results.rows.length, i;
                    var stories = [];
                    for (i = 0; i < len; i++) {
                        stories.push(results.rows.item(i));
                        console.log(results.rows.item(i).text);
                    }
                    callback(stories);
                });


            },function(error)
            {
                console.log('Oops.  Error was '+error.message+' (Code '+error.code+')');
            });
        },




    }
})
app.factory('Users', function($http) {
   return {
        
        login: function(username,password)
        {
        	return $http.get(baseurl + 'api/users/login?user=' + username + '&password=' + password);
        },
        logout: function()
        {
        	return $http.get(baseurl + 'api/users/logout');
        },
        registerUser: function(user){
 
		   	return $http({
			    method: 'POST',
			    url: baseurl + 'api/users',
			    data: "password="+user.password+"&email="+user.email+"&username=" + user.username,
			    headers: {
			        'Content-Type': 'application/x-www-form-urlencoded'
			    }})
        }
   }
});
app.factory('Subreddits', function($http) {
   return {
        getSubreddits: function() {
             //return the promise directly.
             return $http.get(baseurl + 'api/subreddits');
        },
        getStories: function(id)
        {
        	return $http.get(baseurl + 'api/subreddits/' + id + '/stories');
        }
   }
});

app.factory('Stories', function($http) {
   return {
        getTopStories: function() {
             //return the promise directly.
             return $http.get(baseurl + 'api/stories');
        },
        getNewStories: function() {
             //return the promise directly.
             return $http.get(baseurl + 'api/stories/new');
        },
        getStoryWithId: function(id) {
             //return the promise directly.
             return $http.get(baseurl + 'api/stories/' + id);
        },
        upVoteToStoryWithId: function(id){
        	
        	return $http.put(baseurl + 'api/stories/' + id + '/up')
        },
        downVoteToStoryWithId: function(id){
        	
        	return $http.put(baseurl + 'api/stories/' + id + '/down')
        },
        addStory: function(story){
 
		   	return $http({
			    method: 'POST',
			    url: baseurl + 'api/stories',
			    data: "title="+story._title+"&text="+story._text+"&subredditID=" + story._subreddit,
			    headers: {
			        'Content-Type': 'application/x-www-form-urlencoded'
			    }})
        },
       searchStory: function(keyword) {
           //return the promise directly.
           return $http.get(baseurl + 'api/stories/search/' + keyword);
       }


   }
});


//////////////////////////////////////////////////////////////////////
//
// Controllers
//
//////////////////////////////////////////////////////////////////////

app.controller('TopStoriesController',function($scope,Stories,db){
	$scope.active = {}
	$scope.active.top = "active"
	Stories.getTopStories()
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.stories = data['_results'];
		}
	});
    db.getStories(function(data){
        $scope.history = data;
        console.log(data)
    })
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
app.controller('NewStoriesController',function($scope,Stories,db){
	$scope.active = {}
	$scope.active.new = "active"
	Stories.getNewStories()
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.stories = data['_results'];
		}
	});
    db.getStories(function(data){
        $scope.history = data;
        console.log(data)
    })
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
app.controller('StoryDetailController',function($scope,$routeParams,Stories,db){

	
	Stories.getStoryWithId($routeParams.id)
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.story = data['_results'];
            db.addStory($scope.story)
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

app.controller('NewStoryController',function($scope,$location,Stories,Subreddits){

	Subreddits.getSubreddits()
        .success( function (data){
            if (data['_error'] == 0) {
                $scope.subreddits = data['_results'];
            }
        });
	
	$scope.addNewStory = function(story) {

		Stories.addStory(story)
		.success(function(data){
			$location.path('/new')

		})

	}
	
	 
});

app.controller('SearchStoriesController',function($scope,$routeParams,Stories,db){



    db.getStories(function(data){
        $scope.history = data;
        console.log(data)
    })

    Stories.searchStory($routeParams.keyword)
        .success(function(data){

            console.log(data)
            if (data['_error'] == 0) {
                var newArray =[];
                for ( var i=0; i< data['_results'].length; i++ ) {
                    newArray.push(jQuery.parseJSON( data['_results'][i]));

                }
                $scope.stories = newArray;

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
app.controller('SubredditsStoriesController',function($scope,$routeParams,Subreddits,db){


    db.getStories(function(data){
        $scope.history = data;
        console.log(data)
    })
	

		Subreddits.getStories($routeParams.id)
		.success(function(data){
			if (data['_error'] == 0) {
				$scope.stories = data['_results'];
			}
		});

	
	
	 
});
app.controller('SubredditsController',function($scope,$location,Subreddits){

	
	
	

		Subreddits.getSubreddits()
		.success(function(data){
			if (data['_error'] == 0) {
				$scope.subreddits = data['_results'];
			}
		});

	
	
	 
});

app.controller('BlankController',function($scope,Stories){

	Stories.getTopStories()
	.success(function (data) {
		if (data['_error'] == 0) {
			$scope.articles = data['_results'];
		}
	});
	
});
app.controller('RegisterController',function($scope,Users,$location){

	$scope.register = function(user) {
		Users.registerUser(user)
		.success(function (data) {
		if (data['_error'] == 0) {
			Users.login(user.username,user.password)
			.success(function (data) {
				if (data['_error'] == 0) {
					$location.path('/top')
				}
			});
			
		}
		else {
			$scope.alert = "Somthing wrong"
		}
	});
	}
	
});

app.controller('LoginController',function($scope,Users,$location){

	$scope.doLogin = function(user) {
		Users.login(user.username,user.password)
		.success(function (data) {
		if (data['_error'] == 0) {
			$location.path('/top')
		}
		else {
			$scope.alert = "Wrong username or password"
		}
	});
	}
	
});
app.controller('LogoutController',function($scope,Users,$location){

	
		Users.logout()
		.success(function (data) {
			if (data['_error'] == 0) {
				$location.path('/')
			}
			
		});
	
});

