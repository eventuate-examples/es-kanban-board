/**
 * Created by ANDREW on 9/4/2015.
 */
import app from '../App';

app.directive('kbnBoard',
  ['$document', '$timeout', '$rootScope',
  ($document, $timeout, $rootScope) => {

  return {
    templateUrl: './views/KanbanBoard.tpl.html',
    controller: 'KanbanBoardController',
    //controllerAs: 'ctl',
    link: (scope, elT, attrs) => {

      scope.isHidden = true;

      let callCount = 0;

      scope.getTableHeight = () => {
        let pos = elT.find('[data-viewport-ref]').offset();

        let vpHeight = $rootScope.viewportHeight;

        console.log(callCount++);

        return Math.max(0, vpHeight - pos.top);
      };

      scope.$watch(attrs.kbnBoard, (newVal, oldVal) => {
        scope.board = newVal;
        scope.isHidden = !newVal;
      });

      scope.$watch('submitted', (newVal) => {
        if (newVal === true) {
          $('.modal').modal('hide');
          $timeout(() => {
            scope.submitted = false;
          });
        }
      });

      scope.$watch('areColumnsOnAlert', (newVal, oldVal) => {

        if (newVal === oldVal) {
          return;
        }
        if (newVal) {
          angular.element($document)
            .off('mouseenter', '.b_column')
            .off('mouseleave', '.b_column')
            .on('mouseenter', '.b_column', (evt) => {
              let sc = locateScope(evt);
              if (sc) {
                sc.alertColumn();
              }
            })
            .on('mouseleave', '.b_column', (evt) => {
              let sc = locateScope(evt);
              if (sc) {
                sc.relaxColumn();
              }
            });

        } else {
          angular.element($document)
            .off('mouseenter', '.b_column')
            .off('mouseleave', '.b_column');
        }

      });

      /**
       * Located the column scope based on the selector
       * @param evt
       * @returns {*}
       */
      function locateScope(evt) {
        let el = angular.element(evt.currentTarget).closest('[kbn-column]');
        if (!el.length) { return; }

        return el.scope();
      }

    }
  };
}]);

export default app;