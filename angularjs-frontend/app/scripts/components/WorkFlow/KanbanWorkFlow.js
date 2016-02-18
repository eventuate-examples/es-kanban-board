/**
 * Created by andrew on 05/10/15.
 */
//import './KanbanWorkFlowDirective';

import app from '../App';

app.directive('kbnWorkFlow',
  ['$log', '$route', '$location', '$timeout', '$rootScope', 'AuthenticationService', 'SessionStorageService', 'ApiService', 'ViewportService',
  ($log, $route, $location, $timeout, $rootScope, authSvc, sess, api, vp) => {

    const boardKey = 'boardIdKey';



  return {
    templateUrl: './views/KanbanWorkFlow.tpl.html',
    controller: ($scope) => {

      init();

      $scope.isLoggedIn = authSvc.isLoggedIn();

      let { userName = '' } = authSvc.getUserInfo();
      $scope.userName = userName;

      $scope.logout = () => authSvc.logout().then(() => $route.reload());

      $scope.selectedBoard = null;
      $scope.selectedBoardId = null;

      $scope.selectBoard = (board) => {
        //debugger;
        $timeout(() => {
          if (board.id) {
            $location.path(`/board/${board.id}`);
          }
          //sess(boardKey, board);
          $scope.selectedBoard = board;
        });
      };

      $scope.deselectBoard = () => {
        sess(boardKey, null);
        $location.path('/');
        $timeout(() => {
          $scope.selectedBoard = null;
          $scope.selectedBoardId = null;
        });
      };

      function init() {
        $timeout(() => {
          //$scope.selectedBoard = sess(boardKey);
          $scope.selectedBoardId = $rootScope.boardId;
          if ($scope.selectedBoardId) {
            api.getBoard($scope.selectedBoardId)
              .then((board) => {
                $timeout(() => {
                  $scope.selectedBoard = board;
                });
              }, $scope.deselectBoard);
          }
        });
      }


    },

    link: (scope, elemT, attrs) => {

      const verticalBP1 = 630;

      //debugger;
      $rootScope.$watch('viewport.heightAdjusted', (val) => {
        if (!val) { return; }
        console.log('heightAdjusted height:', val);
        $('html').toggleClass('rwd-tall', val > verticalBP1).toggleClass('rwd-pudgy', val <= verticalBP1);
      });

      vp.on();
      vp.subtractH(72);

      scope.$on('$destroy', vp.off);

    }
  };
}]);

export default app;
