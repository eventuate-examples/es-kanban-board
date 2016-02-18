/**
 * Created by andrew on 05/10/15.
 */
import app from '../components/App';

app.factory('SessionStorageService',
  ['$window',
  ($window) => {

    return (...args) => {
      switch (args.length) {
        case 1:
        {
          // GET
          let [ key ] = args;
          let result = null;
          try {
            result = JSON.parse($window.sessionStorage[key]);
          } catch (ex) {}
          return result;
        }

        case 2:
        {
          // SET
          let [ key, value ] = args;
          if (value === null) {
            // DELETE
            $window.sessionStorage[key] = null;
          } else {
            $window.sessionStorage[key] = JSON.stringify(value);
          }
          break;
        }

        default:
          throw new Error('Invalid use of session storage API');
      }
    };


  }]);