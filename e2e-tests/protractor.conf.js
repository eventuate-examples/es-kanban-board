/**
 * Created by andrew on 29/12/15.
 */
'use strict';

require('babel-core/register')({
  "presets": ["es2015", "stage-0"],
  "plugins": ["transform-runtime", "transform-regenerator"],
  "sourceMaps": true
});

console.log('e2e/protractor.conf.js is loaded');

exports.config = {
  specs: ['*spec.js'],
  showStack: true,
  capabilities: {
    browserName: 'chrome'
  },
  framework: 'jasmine',
  onPrepare: function() {
    browser.ignoreSynchronization = true;

    var SpecReporter = require('jasmine-spec-reporter');
    // add jasmine spec reporter
    jasmine.getEnv().addReporter(new SpecReporter({displayStacktrace: 'all'}));

  }
};