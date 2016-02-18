/**
 * Created by andrew on 05/10/15.
 */
import app from '../App';

app.directive('kbnLogin', ['$log', '$document', '$location', '$timeout', 'AuthenticationService',
  ($log, $document, $location, $timeout, authSvc) => {
    return {
      templateUrl: './views/KanbanLogin.tpl.html',
      controller: ($scope) => {
        $scope.login = () => {
          if ($scope.email) {
            authSvc.login($scope.email).then(
              () => $location.path('/'),
              (err) => {
                return err;
              }
            );
          }
          console.log($scope.email);
        };
      },
      link: (scope, tElem) => {

        $timeout(focusFirstInput);

        function focusFirstInput(){
          tElem.find('input[type="text"]:not([readonly]):not([disabled]),input[type="email"]:not([readonly]):not([disabled])').focus();
        }

      }
    };
  }
]);

export default app;