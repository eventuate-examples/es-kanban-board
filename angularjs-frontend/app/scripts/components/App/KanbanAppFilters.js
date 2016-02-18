/**
 * Created by ANDREW on 9/4/2015.
 */
import app from './KanbanModule';

app.filter('taskStatus', () =>
  (tasks, status) => (tasks || []).filter((task) => task.status === status));

app.filter('count', () =>
  (items = []) => items.length);

app.filter('toJSON1', () => (obj) => {
  function replacer(key, value) {
    if (/^\$/.test(key)) {
      return undefined;
    }
    return value;
  }
  return JSON.stringify(obj, replacer);
});

export default app;