/**
 * Created by ANDREW on 9/4/2015.
 */
import app from '../App';

app.directive('kbnColumn',
  ['$document', '$log', 'StatusDataService',
  ($document, $log, statusDataSvc) => {
  return {
    templateUrl: './views/KanbanColumn.tpl.html',
    controller: 'KanbanColumnController',
    controllerAs: 'ctrl',
    //replace: true,
    scope: true,
    bindToController: {
      status: '@'
      //tasks: '='
    },
    link: (scope, elT, attrs) => {

      scope.$watch('ctrl.status', (val) => {

        if (!val) {
          return;
        }

        scope.statusVerbose = statusDataSvc.verboseStatus(val);
      });

      //scope.ctrl.filteredTasks = [];

      //scope.$watch('ctrl.tsks', (val) => {
      //
      //  const { ctrl } = scope;
      //  const { status: currentStatus } = ctrl;
      //  const tasks = (val || []).filter(({ status }) => (status == currentStatus));
      //  debugger;
      //  ctrl.filteredTasks = tasks;
      //});

      scope.$on('task_moved_updated', (evt, task) => {

        scope.ctrl.tasks = [...scope.ctrl.tasks];

      });


      scope.$on('go_off_alert', (evt, data) => {

        //debugger;
        console.log(`kbnColumn: ${scope.ctrl.status} go_off_alert`);
        console.info(data);

        scope.tryAcceptTask(data);
        scope.relaxColumn();

      });
    }
  };
}]);

export default app;