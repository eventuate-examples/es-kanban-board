/**
 * Created by ANDREW on 8/31/2015.
 */
export default ['$scope', 'StatusDataService', function KanbanColumnController($scope, statusDataSvc) {

  $scope.isMoving = false;
  $scope.isMovingHost = false;
  $scope.isColumnAlert = false;
  //
  //let tagIdx = 1;
  //$scope.$watch('$scope.tasks', (val) => {
  //  if (val['_tagged']) {
  //    console.log('Already tagged:', val['_tagged'])
  //  } else {
  //    val['_tagged'] = tagIdx++;
  //  }
  //  console.log(val && val.length);
  //  debugger;
  //  $scope.tsks = val;
  //});

  $scope.alertColumn = () => {

    $scope.$apply(() => {
      $scope.isColumnAlert = true;
    });
  };

  $scope.relaxColumn = () => {
    $scope.$apply(() => {
      $scope.isColumnAlert = false;
    });
  };

  $scope.tryAcceptTask = (data) => {
    if ($scope.isColumnAlert) {
      $scope.$apply(() => {
        let updateVerb = null;

        switch ($scope.ctrl.status) {
          case 'started':
            updateVerb = 'start';
            break;
          case 'scheduled':
            updateVerb = 'schedule';
            break;
          case 'completed':
            updateVerb = 'complete';
            break;
          case 'backlog':
            updateVerb = 'backlog';
            break;
        }

        //debugger;
        if ($scope.ctrl.status !== data.task.status) {
          // #1:
          //data.task.oldStatus = data.task.status;
          // #2:
          data.task.status = $scope.ctrl.status;

          data.task.updateVerb = updateVerb;
          data.task.updatedDate = new Date() - 0;

          //debugger;
          $scope.$root.$broadcast('task_moved_updated', data.task);
          $scope.$root.$emit('task_moved_updated', data.task);

        }

      });
    }
  };



  $scope.$on('task_moving', (evt, data) => {
    //debugger;
    console.log('KanbanColumnController: task_moving');

    $scope.$apply(() => {
      $scope.isMoving = true;
      $scope.isMovingHost = true;
      $scope.taskStatus = data.task.status;
      $scope.movedTask = data.task.id;
    });
  });

  $scope.$on('task_moved', (evt, arg) => {
    //debugger;
    console.log('KanbanColumnController: task_moved');
    $scope.$apply(() => {
      $scope.isMoving = false;
      $scope.isMovingHost = false;
      delete $scope.taskStatus;
      delete $scope.movedTask;
    });

  });


}];