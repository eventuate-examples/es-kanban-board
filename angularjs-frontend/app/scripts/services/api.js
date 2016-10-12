/**
 * Created by ANDREW on 9/10/2015.
 */
import app from '../components/App';

app.factory('ApiService', ['$http', '$q', 'AuthenticationService', ($http, $q, authSvc) => {

  const authTokenKey = 'x-access-token';

  //const defaultEmail = ''; //'testUser+1@gmail.com';

  const { auth } = authSvc;

  const getOpts = {
    method: 'GET',
    cache: false,
    params: { 'cbust': new Date() - 0 }
  };

  const apiRoot = '/api',
    apiPaths = {
      'boardsGet': {
        ...getOpts,
        url: `${apiRoot}/boards/`
      },
      'createBoard': {
        method: 'POST',
        url: `${apiRoot}/boards/`
      },
      'createTask': {
        method: 'POST',
        url: `${apiRoot}/tasks/`
      },
      //'board': `${apiRoot}/boards/:id`,
      /*
       * title: req.body.title,
       creator: req.body.creator,
       createdDate: timestamp,
       updatedDate: timestamp,
       status: req.body.status,
       data:  req.body.data
       */

      'tasksGet': {
        ...getOpts,
        url: `${apiRoot}/tasks/` // GET

      },
     'taskSave': {
        url: `${apiRoot}/tasks/`,
        method: 'PUT'
      },
      'taskDelete': {
        url: `${apiRoot}/tasks/`,
        method: 'DELETE'
      },
      'taskHistory': {
        ...getOpts,
        url: (t) => `${apiRoot}/tasks/${t.id}/history`
      }
    },


    getBoards = () =>
      auth()
        .then(makeRequestFor(apiPaths.boardsGet))
        .then((res) => (res && res.data))
        .then((data) => {
          return data.boards || [];
        }),

    getOwnBoards = () => {
      let { userName } = authSvc.getUserInfo();
      if (!userName) {
        return $q.resolve([]);
      }
      return getBoards()
        .then(brds => brds.filter(b => b.createdBy === userName));
    },

    getBoard = (boardId) =>
      auth()
        .then(makeRequestFor(apiPaths.boardsGet, null, `${boardId}`))
        .then((res) => (res && res.data))
        .then((d) => {

          return d.data;
        }),

    createBoard = (boardName, boardData = null) => {
      if (!authSvc.isLoggedIn()) {
        return $q.reject(null);
      }
      let { userName } = authSvc.getUserInfo();
      return auth()
        .then(makeRequestFor(apiPaths.createBoard, {
          title: boardName,
          boardDetails: {
            ...boardData
          },
          creation: {
            who: userName,
            when: new Date() - 0
          },
          update: {
            who: userName,
            when: new Date() - 0
          }
        }))
        .then((res) => (res && res.data));

      //.then((res) => res && res.data);
    },

    taskHistory = (task) => {
      return auth(null, true)
        .then(makeRequestFor(apiPaths.taskHistory, { id: task.id }))
        .then((res) => (res && res.data))
        .then((d) => {
          //debugger;
          //
          //if (!d.success) {
          //  throw new Error(d.message || 'Server error');
          //}
          return d.data;
        });
    },

    createTask = (taskTitle, taskData = null) => {
      if (!authSvc.isLoggedIn()) {
        return $q.reject(null);
      }
      let { userName } = authSvc.getUserInfo();
      return auth()
        .then(makeRequestFor(apiPaths.createTask, {
          "creation":{"who":userName,"when":new Date() - 0},
          "update":{"who":userName,"when":new Date() - 0},
          "taskDetails":{
            title: taskTitle,
            ...taskData
          }
        }))
        .then((res) => res && res.data);

    },

    getTasks = (boardId) =>
      auth()
        .then(makeRequestFor(apiPaths.tasksGet, { boardId } ))
        .then((res) => (res && res.data))
        .then((data) => {
          if (!data) {
            return [];
          }

          let { tasks =  [], backlog = [] }  = data;

          return [...backlog, ...tasks];
        })
        .then((data) => {
          //debugger;
          console.info('getTasks:', data);
          return data;
        }),

    saveTask = (task) => {
      let { updateVerb = null, ...taskRest} = task;
      let updatePath = (updateVerb !== null) ? `${taskRest.id}/${updateVerb}` : `${taskRest.id}`;

      return auth()
        .then(makeRequestFor(apiPaths.taskSave, taskRest, updatePath));
    },
    deleteTask = (task) =>
      auth()
        .then(makeRequestFor(apiPaths.taskDelete, task, `${task.id}`))
        .then((data) => {
            console.log('deleteTask:', data);
            return data;
          });



  return {
    auth,
    getBoards,
    getOwnBoards,
    getBoard,
    createBoard,
    createTask,
    getTasks,
    saveTask,
    taskHistory,
    deleteTask
  };

  function makeRequestFor(options, data = null, appendPath = null) {
    let isGet = options.method === 'GET';
    let params = isGet ? {
      'cbust': new Date() - 0,
      ...data
    } : {};

    let { url, ...restOptions} = options;
    if (typeof  url == 'function') {
      url = url(data || {});
    }

    if (appendPath) {
      //let { id = '' } = data;
      url += appendPath;
    }

    return ((token) => $http({
      url,
      ...restOptions,
      headers: {
        [authTokenKey]: token,
        'Cache-Control': 'no-cache',
        'Pragma': 'no-cache'
      },
      params,
      data
    })).then(
      data => data,
      data => {
        console.error(data);
        return data
      });
  }

  //function wrap(fn, data) {
  //  console.log(data);
  //  return (...args) => {
  //    console.log(...args);
  //    return fn(...args);
  //  };
  //}


}]);