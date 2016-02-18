/**
 * Created by ANDREW on 9/4/2015.
 */
import app from '../App';
import $ from 'jquery'

app.directive('kbnTask', ['$log', '$document', ($log, $document) => {
  return {
    templateUrl: './views/KanbanTask.tpl.html',
    restrict: 'A',
    require: '^kbnColumn',
    replace: true,
    controller: 'KanbanTaskController',
    scope: {
      task: '='
    },

    link: (scope, el, attrs, columnCtl) => {

      console.log(`kbnTask: Task.id: ${scope.task.id}`);

      let clone = null;

      //let options = scope.$eval(attrs.kbnTask);
      let state = {
        startX: 0,
        startY: 0,
        x: 0,
        y: 0
      };

      el.on('mousedown', (evt) => {
        // Prevent default dragging of selected content
        if ($(evt.target).is('.btn')) {
          return;
        }

        evt.preventDefault();
        state = {
          startX: evt.pageX - evt.offsetX - state.x,
          startY: evt.pageY - evt.offsetY - state.y,
          offsetX: 0,
          offsetY: 0,
          x: state.x,
          y: state.y
        };

        //debugger;

        if (clone) {
          clone.remove();
        }

        clone = el.clone()
          .addClass('m_absolute')
          .css({
            //zIndex: '-1',
            top: `${state.startY}px`,
            left: `${state.startX}px`
          });

        $('body').append(clone);

        $log.log(state);
        $document.on('mousemove', mousemove);
        $document.on('mouseup', mouseup);

      });

      scope.$on('task_moved', (evt, {task, column}) => {
        //if ((scope.task && scope.task.id) !== task.id) {
        //  return;
        //}

        if (clone) {
          clone.animate({
            zIndex: '',
            top: `${state.startY}px`,
            left: `${state.startX}px`
          }, killAnimatedClone);
        }

        state = {
          startX: 0,
          startY: 0,
          x: 0,
          y: 0
        };

      });

      scope.$on('task_moved_updated', (evt, task) => {
        //debugger;
        //console.log('killAnimatedClone');
        killAnimatedClone(true);
      });

      function killAnimatedClone(doNotRemove) {
        if (clone) {
          clone.finish();
        }
        if (doNotRemove) {
          return;
        }
        clone.remove();
        clone = null;
      }

      function mousemove(evt) {
        evt.preventDefault();
        state = {
          ...state,
          //startX: state.startX,
          //startY: state.startY,
          x: evt.pageX ,//- state.startX,
          y: evt.pageY //- state.startY
        };
        clone.css({
          //zIndex: '-1',
          top: state.y - state.offsetY + 'px',
          left: state.x - state.offsetX + 'px'
        });
        scope.onMove();
      }

      function mouseup() {
        $document.off('mousemove', mousemove);
        $document.off('mouseup', mouseup);

        scope.onStill(columnCtl);
      }

    }
  };
}]);