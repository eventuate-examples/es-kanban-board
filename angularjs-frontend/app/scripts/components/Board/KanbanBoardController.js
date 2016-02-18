/**
 * Created by ANDREW on 8/27/2015.
 */
export default ['$scope', '$log', '$timeout', 'ApiService', 'TaskDataService', 'ServerListenerService',
  function KanbanBoardController($scope, $log, $timeout, api, taskData, listenerSvc) {

    $scope.tasks = [];
    $scope.boardId = null;

    resetCreateTask();
    resetDeleteTask();

    $scope.createTask = () => {

       if ($scope.createTaskLock) {
        return;
      }

      let { taskTitle, taskData = {} } = $scope;

      if (!taskTitle) {
        return;
      }

      $scope.createTaskLock = true;

      api.createTask(taskTitle, taskData).then(() => {
        resetCreateTask();
        $scope.submitted = true;
        $scope.taskTitle = null;
        $scope.taskData = {};
      }, resetCreateTask);
    };

    function resetCreateTask() {
      $scope.createTaskLock = false;
    }

    function resetDeleteTask() {
      $scope.deleteTaskLock = false;
      $scope.deletion = {};
    }
    
    function taskUpdatedEventHandler({ type, data}) {
      //debugger;
      switch (type) {
        case 'TaskCreatedEvent': {
          //debugger;
          //const { creation = {}, update = {}, ...rest } = data;
          $scope.tasks = [...$scope.tasks, data];
          //$scope.tasks = [...$scope.tasks, {
          //  createdDate: creation.when,
          //  createdBy: creation.who,
          //  updatedBy: update.who,
          //  ...rest
          //}];
          return;
        }

        case 'TaskDeletedEvent': {
          $scope.tasks = $scope.tasks.filter(t => t.id !== data.id);
          return;
        }

        case 'TaskStartedEvent':
        case 'TaskScheduledEvent':
        case 'TaskCompletedEvent':
        case 'TaskBacklogEvent':
          updateTask($scope, data, $scope.boardId);
          break;
        default:
          return;
      }
    }

    const updateTask = ($scope, task, parentBoardId) => {

      const { id, boardId, status } = task;
      if (!id) {
        return;
      }

      const isValidRef = (boardId) || (status == 'backlog');

      const filteredTaskIdx = $scope.tasks.findIndex(t => t.id == id);

      if (isValidRef && (filteredTaskIdx >= 0)) {
        const newTask = {
          ...$scope.tasks[filteredTaskIdx],
          ...task
        };
        console.log(newTask);
      }

      const tasks = isValidRef ? ((filteredTaskIdx < 0) ? [
        ...$scope.tasks,
        task
      ] : [
        ...$scope.tasks.slice(0, filteredTaskIdx),
        {
          ...$scope.tasks[filteredTaskIdx],
          ...task
        },
        ...$scope.tasks.slice(filteredTaskIdx + 1)
      ]) : ($scope.tasks.filter(t => t.id != id));

      $scope.tasks = tasks.filter(({
          status,
          boardId
        }) => ((status == 'backlog') || (boardId == parentBoardId)));

    };

    $scope.showTaskHistory = (task) => {

      $scope.historyMarkup = [{
        info: 'Loading..'
      }];

      api.taskHistory(task)
        .then((h) => {
          if (!/array/i.test(Object.prototype.toString.call(h))) {
            throw new Error('Cannot obtain this task\'s history');
          }
          return h.map(convertEventData);
        }).then((parsedData) => {

          $scope.historyMarkup = parsedData.length ? parsedData.reduce(aggregateCollectedEvtData, {
            lastItem: null,
            markup: []
          }).markup : [{
            info: 'No data is available for this task.'
          }];

        }, (err) => {
          $scope.historyMarkup = [{
            error: err.message ? err.message : 'Cannot obtain this task\'s history'
          }];
        });
    };

    $scope.deleteTask = () => {
      let { task } = $scope.deletion;
      if (!task) {
        return;
      }

      $scope.deleteTaskLock = true;
      api.deleteTask(task)
        .then(() => {
          resetDeleteTask();
          $scope.submitted = true;
        }, resetDeleteTask);

    };
    
    $scope.$watch('board', (newVal, oldVal) => {

      if (!newVal) {
        $scope.boardId = null;
        return;
      }

      const boardId = newVal.id;

      if (!boardId) {
        $scope.boardId = null;
        return;
      }

      $scope.boardId = boardId;

      loadTasks();

    });


    $scope.$on('$destroy', listenerSvc.on('*', taskUpdatedEventHandler));


    $scope.areColumnsOnAlert = false;


    let state = 'go_off_alert';

    $scope.$on('task_moving', (evt, data) => {
      if (state === 'go_on_alert') {
        return;
      }

      state = 'go_on_alert';
      $log.log('KanbanBoardController: task_moving');

      $scope.$apply(() => {
        $scope.areColumnsOnAlert = true;
      });

      $scope.$broadcast('go_on_alert', data);

    });


    $scope.$on('task_moved', (evt, data) => {
      if (state === 'go_off_alert') {
        return;
      }

      state = 'go_off_alert';
      $log.log('KanbanBoardController: task_moved', data);

      $scope.$apply(() => {
        $scope.areColumnsOnAlert = false;
      });

      $scope.$broadcast('go_off_alert', data);

    });

    $scope.$on('task_moved_updated', (evt, task) => {

      task.boardId = $scope.boardId;

      taskData.updateTask(task).then((msg) => {
        console.log(msg);
      }, (err) => {
        console.log((err && err.message) ? err.message : err);
      });

    });


    function loadTasks() {
      if (!$scope.boardId) {
        return;
      }

      taskData.getActiveTasks($scope.boardId)
        .then((tasks) => {

          $scope.tasks = tasks;

        });
    }


    function convertEventData(evt) {

      const { id: eventId = '', eventType, eventData } = evt;

      const { creation, update } = eventData;
      let actor = (creation) ? creation.who : update.who;

      const rawTimestamp = eventId.split('-')[0];
      if (!rawTimestamp) {
        return null;
      }

      let timestamp = null;
      try {
        timestamp = Number.parseInt(rawTimestamp, 16);
      } catch(ex) {
        return null;
      }

      const verb = ((evtName) => {
        const result = {
           'TaskCreatedEvent': 'created',
           'TaskScheduledEvent': 'scheduled',
           'TaskStartedEvent': 'started',
           'TaskCompletedEvent': 'completed',
           'TaskDeletedEvent': 'deleted',
           'TaskBacklogEvent': 'back-logged'
        }[evtName];

        return result || 'did something else to..';

      })(eventType.split('.').reverse()[0]);

      //if (verb === 'created') {
      //  actor = createdBy;
      //}

      return {
        when: timestamp,
        who: actor,
        what: verb
      };
    }

    function aggregateCollectedEvtData(memo, v) {
      if (!v) { return memo; }

      if (memo.lastItem) {

      }

      memo.markup.push(v);

      memo.lastItem = v;
      return memo;
    }

}];
