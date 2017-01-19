/**
 * Created by ANDREW on 9/10/2015.
 */
import app from '../components/App';
import SockJS from 'sockjs-client';
import StompJS from 'stompjs';

app.factory('ServerListenerService', ['$rootScope', '$log', '$window', ($rootScope, $log, $window) => {

  let subscribers = [];
  let socket = null;
  let stomp = null;
  let subscription = null;

  return {
    on (eventName, callback) {
      const refId = Math.random().toString().substr(2);
      subscribers = [{ refId, eventName, callback }, ...subscribers];
      getListener();
      $log.log('App adds listener to ' + eventName + ' event');
      return () => {
        subscribers = subscribers.filter(s => s.refId !== refId);
      }
    }
  };

  function getListener() {
    if (!socket) {
      socket = new SockJS('/events/');
      stomp = new StompJS.over(socket);

      stomp.heartbeat.outgoing = 5000;
      stomp.heartbeat.incoming = 0;

      $log.log('App connecting to stomp server');
      stomp.connect({}, () => {
        $log.log('App connected to stomp server');
        subscription = stomp.subscribe('/events', (msg) => {
          $log.log('App received a message from stomp server');

          const { body = null, ack, nack, command } = msg;

          if (command != 'MESSAGE') {
            return;
          }

          if (!body) {
            if (nack) {
              nack();
            }
            return;
          }

          let bodyObj = null;
          try {
            bodyObj = JSON.parse(body);
          } catch(ex) {
            if (nack) {
              nack();
            }
            return;
          }

          const {
            entityId,
            eventData = null,
            eventId,
            eventType,
            ...rest } = bodyObj;

          if (!eventData) {
            if (nack) {
              nack();
            }
            return;
          }

          let eventDataObj = null;
          try {
            eventDataObj = JSON.parse(eventData);
          } catch (ex) {
            if (nack) {
              nack();
            }
            return;
          }

          const [ evtType ] = eventType.split('.').reverse();

          const {
            eventData: eventDataX,
            ...restBody
          } = bodyObj;

          $log.log('Sockets, incoming message:', restBody);
          $log.info(eventDataObj);

          const result = {
            type: evtType,
            data: { ...eventDataObj, id: entityId },
            msg: { ...bodyObj, eventData: { ...eventDataObj } } // copy into empty object
          };

          if (!$window._lastSocketMessage || !$window._lastSocketMessage.length) {
            $window._lastSocketMessage = [];
          }
          // $window.alert(1);
          $window._lastSocketMessage.push(result);

          const handlers = subscribers
            .filter((s) => (s.eventName === evtType) || (s.eventName === '*'));

          handlers.forEach(h => {
            $rootScope.$apply(() => {
              h.callback(result);
            });
          });

          if (ack) {
            ack();
          }
        });

      }, (err) => {
        //debugger;
        console.info(err);
      });

    }
    return socket;
  }

}]);