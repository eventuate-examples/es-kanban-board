/**
 * Created by ANDREW on 9/10/2015.
 */
import app from '../components/App';

app.factory('TaskDataService', ['ApiService', (api) => {

  return {
    getActiveTasks: boardId =>
      api
        .getTasks(boardId)
        .then((tasks) => tasks
          .filter(({ metaStatus }) => metaStatus !== 'deleted')
          .filter(({ deleted = false }) => !deleted)
        ),

    updateTask: t => api.saveTask(t)
  };

}]);