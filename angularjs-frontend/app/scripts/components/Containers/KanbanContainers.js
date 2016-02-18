/**
 * Created by ANDREW on 9/4/2015.
 */
import app from '../App';

app.directive('cntFixed',
  ['$document', '$log',
    ($document, $log) => {
      return {
        //controllerAs: 'ctrl',
        replace: true,
        scope: {
          dirWidth: '=cntWidth',
          dirHeight: '=cntHeight'
        },
        templateUrl: './views/ContainerFixed.tpl.html',
        transclude: true,
        link: (scope, tElem, attrs, controller, transclude) => {

          console.log(`cntFixed - link - dirWidth: ${scope.dirWidth}`);
          debugger;

          let content = tElem.find('.b_cnt-body');
          transclude((el, trScope) => {
            content.empty().append(el);
            trScope.dirWidth = scope.dirWidth;
            trScope.dirHeight = scope.dirHeight;
            debugger;
          });

          //let trElems = transclude();
          //tElem.find('>.b_body').empty().append(trElems);

          tElem.on('$destroy', () => {
            //$interval.cancel(timeoutId);
          });

          scope.$on('$destroy', () => {

          });
        }
      };
    }]);

app.directive('cntVertical',
  ['$document', '$log',
    ($document, $log) => {
      return {
        replace: true,
        templateUrl: './views/ContainerVertical.tpl.html',
        transclude: true,
        //controllerAs: 'ctl',
        //controller: function() {},
        link: (scope, tElem, attrs, controller, transclude) => {

          console.log(`cntVertical - link - dirWidth: ${scope.dirWidth}`);

          let head = tElem.find('.b_cnt-head'),
            body = tElem.find('.b_cnt-body'),
            foot = tElem.find('.b_cnt-head');

          transclude((el, trScope) => {

            let trHead = el.filter('[data-container-head]'),
              trFoot = el.filter('[data-cintainer-foot]'),
              trBody = el.not(trHead).not(trFoot);

            head.empty().append(trHead);
            foot.empty().append(trFoot);

            let excludeHeights = [
              head.outerHeight(true),
              foot.outerHeight(true)
            ];

            body.empty().append(trBody);

            trScope.dirWidth = scope.dirWidth;
            trScope.dirHeight = Math.max(0, scope.dirHeight - sum(excludeHeights));
            debugger;
          });




          tElem.on('$destroy', () => {
            //$interval.cancel(timeoutId);
          });

          scope.$on('$destroy', () => {

          });
        }
      };
}]);

app.directive('cntVerticalTop',
  ['$document', '$log',
    ($document, $log) => {
      return {
        replace: true,

        templateUrl: './views/ContainerVerticalTop.tpl.html',
        transclude: true,
        link: (scope, elT, attrs) => {

        }
      };
}]);

app.directive('cntVerticalTopBottom',
  ['$document', '$log',
    ($document, $log) => {
      return {
        replace: true,
        templateUrl: './views/ContainerVerticalTopBottom.tpl.html',
        transclude: true,
        link: (scope, elT, attrs) => {}
      };
    }]);


app.directive('cntHorizontal',
  ['$document', '$log', '$timeout',
    ($document, $log, $timeout) => {
      return {
        replace: true,
        templateUrl: './views/ContainerHorizontal.tpl.html',
        transclude: true,
        link: (scope, elT, attrs, controller, transclude) => {
          //debugger;
          let body = elT.find('.b_cnt-body');

          transclude((els, trScope) => {
            body.empty().css({ width: '10000px' }).append(els);
          });

          let attrSel = attrs['cntHorizontal'];
          let childrenSel = attrSel ? `> ${attrSel}, > * > ${attrSel}` : '> *';

          scope.$watch(() => attrSel ? body.find(childrenSel).length : body[0].children.length,
            () => {
              body.css({ width: '100000px' });
              $timeout(() => {
                let widths = body.find(childrenSel)
                  .toArray()
                  .map(elem => $(elem).outerWidth(true));

                let totalWidth = sum(...widths, Math.max(0, widths.length - 1) * 30);

                //debugger;
                body.css({ width: totalWidth });
              }, 2);
          });


        }
      };
    }]);

function sum(...args) {
  return args.reduce( ((memo, x) => memo + x), 0);
}

export default app;