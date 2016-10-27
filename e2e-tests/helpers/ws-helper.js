/**
 * Created by andrew on 10/25/16.
 */

// https://github.com/andresdominguez/protractor-meetup/blob/master/test/e2e/api-helper.js
// http://stackoverflow.com/questions/20619225/accessing-angular-inside-protractor-test

// BoardCreatedEvent
export const BoardCreatedEventHelper = () => {
  return browser.executeAsyncScript(function(callback) {
    const ServerListenerService = window.$injector.get('ServerListenerService');
    const $log = window.$injector.get('$log');
    $log.log('getting ServerListenerService:' + (new Date() - 0));
    ServerListenerService.on('BoardCreatedEvent', function(data) {
      $log.log('received event ServerListenerService: ' + (new Date() - 0));
      callback(data);
    });
  });
};

// TaskCreatedEvent
export const TaskCreatedEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskCreatedEvent', callback);
});

// TaskDeletedEvent
export const TaskDeletedEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskDeletedEvent', callback);
});

// TaskStartedEvent
export const TaskStartedEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskStartedEvent', callback);
});

// TaskScheduledEvent
export const TaskScheduledEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskScheduledEvent', callback);
});

// TaskCompletedEvent
export const TaskCompletedEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskCompletedEvent', callback);
});

// TaskBacklogEvent
export const TaskBacklogEventHelper = () => browser.executeAsyncScript(function(callback) {
  const ServerListenerService = window.$injector.get('ServerListenerService');
  ServerListenerService.on('TaskBacklogEvent', callback);
});
