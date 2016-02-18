/**
 * Created by ANDREW on 9/4/2015.
 */
import app from './App';

import './Containers';
import './Login';
import './Header';
import './Board';
import './BoardList';
import './Column';
import './Task';
import './WorkFlow';

app.run(["$rootScope", "$location",

  ($rootScope, $location) => {
    $rootScope.$on("$routeChangeSuccess", (userInfo) => {
      console.log(userInfo);
    });

    $rootScope.$on("$routeChangeError", (event, current, previous, eventObj) => {
      if (eventObj.authenticated === false) {
        $location.path("/login");
      }
    });

}]);

app.run(function($injector, $window) {
  $window.$injector = $injector;
});


export default app;