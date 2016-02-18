/**
 * Created by ANDREW on 9/4/2015.
 */
import app from './KanbanModule';

app.config([
  '$locationProvider',
  '$routeProvider',
  ($locationProvider, $routeProvider) => {
    //$locationProvider.hashPrefix('!');

    let commonRouteObj = {
      templateUrl: '../../../views/home-page.html',
      resolve: {
        auth: ["$q", "AuthenticationService", function($q, authenticationSvc) {
          var userInfo = authenticationSvc.getUserInfo();

          if (userInfo) {
            return $q.when(userInfo);
          } else {
            return $q.reject({ authenticated: false });
          }
        }]
      }
      ,
      controller: ['$scope','$routeParams','$rootScope',
        function($scope, $routeParams, $rootScope){

          if ($routeParams.boardId) {
            $rootScope.boardId = $routeParams.boardId;
          } else {
            $rootScope.boardId = null;
          }
        }

      ]
    };

    // routes
    $routeProvider
      .when('/login', {
        templateUrl: '../../../views/login-page.html'
      })
      .when('/board/:boardId', commonRouteObj)
      .when('/', commonRouteObj)
      .otherwise({
        redirectTo: '/'
      });
  }
]);

export default app;