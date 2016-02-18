/**
 * Created by ANDREW on 9/10/2015.
 */
import app from '../components/App';

app.factory('StatusDataService', () => {

  const statusToVerbose = {
    'backlog': {
      'verbose': 'Backlog',
      '-stuck': 'bottom'
    },
    'scheduled': {
      'verbose': 'To Do'
    },
    'started': {
      'verbose': 'Doing'
    },
    'completed': {
      'verbose': 'Done'
    }
  };

  return {
    verboseStatus: (input) => (statusToVerbose[input] && statusToVerbose[input].verbose) || input,
    statuses: statusToVerbose
  };

});