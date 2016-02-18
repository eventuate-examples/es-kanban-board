/**
 * Created by ANDREW on 8/31/2015.
 */
export default ['$scope', function KanbanTaskController($scope) {

  $scope.isMoving = false;

  $scope.startDeleteTask = () => {
    //console.log('Delete task');
    //debugger;
    $scope.$parent.deletion.task = $scope.task;
  };

  $scope.showTaskHistory = () => {
    $scope.$parent.showTaskHistory($scope.task);
  };

  /**
   *
   */
  $scope.onMove = () => {

    if ($scope.isMoving) {
      return;
    }

    $scope.$apply(() => {
      $scope.isMoving = true;
    });

    $scope.$emit('task_moving', {
      type: 'task_moving',
      task: $scope.task
    });
  };


  /**
   *
   * @param state
   * @param column
   * @param cb
   */
  $scope.onStill = (column) => {

    $scope.$apply(() => {
      $scope.isMoving = false;
    });

    $scope.$emit('task_moved', {
      type: 'task_moved',
      task: $scope.task,
      column
    });

  }


}];
