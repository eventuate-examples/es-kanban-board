/**
 * Created by andrew on 06/10/15.
 */
import app from '../App';

app.directive('kbnBoardList', ['$log', '$timeout', 'ApiService', 'ServerListenerService',
  ($log, $timeout, api, listenerSvc) => {
  return {
    templateUrl: './views/KanbanBoardList.tpl.html',
    controller: ($scope) => {

      const addBoardBoard = {
        id: -1,
        title: '',
        tag: new Date() - 0
      };

      $scope.boards = [];
      loadBoards();

      $scope.isMeta = (board) => (board && (board.id === -1) && (board.tag === addBoardBoard.tag));
      $scope.isBoard = (board) => (board && (board.id !== -1) && (!board.tag));


      $scope.createBoardLock = false;


      $scope.createBoard = () => {
        let { boardName, boardData } = $scope;

        if (!boardName) {
          return;
        }

        if ($scope.createBoardLock) {
          return;
        }

        $scope.createBoardLock = true;

        api.createBoard(boardName, boardData).then((data) => {
          processBoardCreation({ data });
          createBoardUnlock();
          $scope.submitted = true;
          $scope.boardName = '';
          $scope.boardData = {};
        }, createBoardUnlock);
      };

      function createBoardUnlock() {
        $scope.createBoardLock = false;
      }

      const processBoardCreation = ({ data }) => {
        const locationIdx = $scope.boards.findIndex(b => b.id === data.id);
        if (locationIdx < 0) {
          $scope.boards = [...$scope.boards, data];
        } else {
          $scope.boards = [...$scope.boards];
          $scope.boards[locationIdx] = data;
        }
      };

      $scope.$on('$destroy', listenerSvc.on('BoardCreatedEvent', ({ data }) => {
        //const { creation = {}, update = {}, ...rest } = data;
        return processBoardCreation({ data });
        //return processBoardCreation({
        //  data: {
        //    createdDate: creation.when,
        //    createdBy: creation.who,
        //    updatedDate: update.when,
        //    updatedBy: update.who,
        //    ...rest
        //  }
        //});
      }));

      function loadBoards() {
        api.getBoards()
          .then((data) => {

          $timeout(() => {
            $scope.boards = [addBoardBoard, ...data];
          });

        });
      }


    },
    link: (scope, elT, attrs) => {
      scope.$watch('submitted', (newVal) => {
        if (newVal === true) {
          elT.find('.modal').modal('hide');
          $timeout(() => {
            scope.submitted = false;
          });
        }
      });
    }
  };
}]);

export default app;
