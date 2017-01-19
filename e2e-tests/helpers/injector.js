/**
 * Created by andrew on 27/01/16.
 */
// http://blog.ng-book.com/accessing-angulars-injector-from-within-protractor/
'use strict';

export const get = function (moduleName) {
  return browser.executeAsyncScript(
    function(moduleName, callback) {
      callback(window.$injector.get(moduleName));
    }, moduleName);
};

export const invoke = function (fn, self, locals) {
  return browser.executeScript(function(fn, self, locals) {
      window.$injector.invoke(fn, self, locals);
    }, fn, self, locals);
};

export const clearReceivedSocketMessages = function() {
  return browser.executeScript('window._lastSocketMessage = [];');
};

export const getReceivedSocketMessages = function() {
  return browser.executeScript('return window._lastSocketMessage;');
};

export const setLastSocketMessage = function(val) {
  return browser.executeScript('window._lastSocketMessage = [arguments[0]];', val);
};