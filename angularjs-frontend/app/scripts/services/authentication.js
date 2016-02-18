/**
 * Created by andrew on 05/10/15.
 */
import app from '../components/App';

app.factory('AuthenticationService',
  ['$rootScope', '$q', '$document', '$http', 'SessionStorageService',
  ($rootScope, $q, $document, $http, sess) => {

    //const authTokenKey = 'x-access-token';
    const userInfoKey = 'userInfo';

    let userInfo = null;

    let authPromise = null;

    let lastEmail = null;

    //const defaultAuthPayload = {
    //  email: defaultEmail
    //};

    const apiRoot = '/api',
      apiPathAuth = {
        method: 'POST',
        url: `${apiRoot}/authenticate`
      };

    const auth = (email, again = false) => {

      let againUsed = false;
      if (again === true) {
        authPromise = null;
      }

      if (authPromise) {
        return authPromise;
      }

      let { accessToken, userName } = userInfo || {};

      if (accessToken) {

        authPromise = $q((rs) => {
          rs(accessToken);
        });

      } else {

        let currentEmail = email ? { email } : ( userName ? { email: userName } : lastEmail);

        authPromise = $http({
          ...apiPathAuth,
          data: currentEmail
        })
          .then(k => k, err => {
            if (again === true && (!againUsed)) {
              againUsed = true;
              return $http({
                ...apiPathAuth,
                data: currentEmail
              });
            }
            throw err;
          })
          .then((res) => {

            if (!res || !res.data) {
              lastEmail = null;
              authPromise = null;
              userInfo = null;
              return null;
            }

            let { token } = res.data;

            lastEmail = currentEmail;
            authPromise = Promise.resolve(token);
            return token;


          }, () => {
            lastEmail = null;
            authPromise = null;
            userInfo = null;
          });
      }


      return authPromise;
    };

    init();


    return {
      auth,
      login,
      logout,
      isLoggedIn: () => {
        return !!userInfo;
      },
      getUserInfo: () => {
        return userInfo;
      }
    };

    function login(email) {

      return $q((rs, rj) => {
        auth(email).then((accessToken) => {
          userInfo = {
            accessToken,
            userName: (email || '').toLowerCase()
          };

          sess(userInfoKey, userInfo);
          rs(userInfo);
        }, rj);
      });

    }

    function logout() {
      return $q((rs) => {
        sess(userInfoKey, null);
        userInfo = null;
        authPromise = null;
        lastEmail = null;
        rs(userInfo);
      });

    }


    function init() {
      userInfo = sess(userInfoKey);
    }

}]);