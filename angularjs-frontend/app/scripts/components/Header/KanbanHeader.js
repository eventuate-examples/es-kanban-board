/**
 * Created by andrew on 14/10/15.
 */
/**
 * Created by andrew on 05/10/15.
 */


import app from '../App';

app.directive('kbnHeader',
  ['$route', 'AuthenticationService',
    ($route, authSvc) => {

      return {
        templateUrl: './views/KanbanHeader.tpl.html',
        controller: ($scope) => {

          $scope.isLoggedIn = authSvc.isLoggedIn();

          let { userName = '' } = authSvc.getUserInfo() || {};
          $scope.userName = userName;

          $scope.logout =
            () => authSvc.logout()
              .then($route.reload);
        }
      };
    }]);

export default app;
