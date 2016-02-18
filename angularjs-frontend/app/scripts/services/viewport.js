/**
 * Created by ANDREW on 9/10/2015.
 */
import app from '../components/App';

app.factory('ViewportService',
  ['$window', '$timeout', '$rootScope',
  ($window, $timeout, $rootScope) => {

    let diffVert = 0;
    let diffHor = 0;

    let viewport = {
      width: 0,
      height: 0,
      widthAdjusted: 0,
      heightAdjusted: 0
    }; // Default size for server-side rendering

    function handleWindowResize() {
      if ((viewport.width !== $window.innerWidth) ||
        (viewport.height !== $window.innerHeight)) {
        viewport = {
          width: $window.innerWidth,
          height: $window.innerHeight,
          widthAdjusted: Math.max(0, ($window.innerWidth - diffHor)),
          heightAdjusted: Math.max(0, ($window.innerHeight - diffVert))
        };
        console.log(viewport);
        $timeout(() => {
          $rootScope.viewport = viewport;
        });
      }
    }

    return {
      on: () => {
        $window.addEventListener('resize', handleWindowResize);
        $window.addEventListener('orientationchange', handleWindowResize);

        $rootScope.$watch(() => {
          return $window.innerHeight;
        }, handleWindowResize);

        $rootScope.$watch(() => {
          return $window.innerWidth;
        }, handleWindowResize);

        handleWindowResize();
      },
      off: () => {
        $window.removeEventListener('resize', handleWindowResize);
        $window.removeEventListener('orientationchange', handleWindowResize);
      },
      subtractH: (value) => {
        diffVert = value;
        handleWindowResize();
        //$rootScope.viewportHeight = Math.max(0, ($window.innerHeight - diffVert));
      },
      subtractW: (value) => {
        diffHor = value;
        handleWindowResize();
        //$rootScope.viewportWidth = Math.max(0, ($window.innerWidth - diffHor));
      }
    }


  }]);