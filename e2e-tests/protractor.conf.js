/**
 * Created by andrew on 29/12/15.
 */
'use strict';

require('babel-core/register')({
  "presets": ["es2015", "stage-0"],
  "plugins": ["transform-runtime", "transform-regenerator"],
  "sourceMaps": true
});

var HtmlScreenshotReporter = require('protractor-jasmine2-screenshot-reporter');

var screenshotReporter = new HtmlScreenshotReporter({
  dest: 'target/screenshots',
  filename: 'report_' + (new Date() - 0) + '.html',
  cleanDestination: true,
  showSummary: true,
  showConfiguration: true,
  // reportTitle: false,
  ignoreSkippedSpecs: true,
  showQuickLinks: true
});


console.log('e2e-tests/protractor.conf.js is loaded');

exports.config = {
  specs: ['*spec.js'],
  showStack: true,
  capabilities: {
    browserName: 'chrome',
    "loggingPrefs": {
      "driver": "ALL",
      "server": "ALL",
      "browser": "ALL"}
  },
  framework: 'jasmine2',
  beforeLaunch: function() {
    return new Promise(function(resolve){
      screenshotReporter.beforeLaunch(resolve);
    });
  },
// Close the report after all tests finish
  afterLaunch: function(exitCode) {
    return new Promise(function(resolve){
      screenshotReporter.afterLaunch(resolve.bind(this, exitCode));
    });
  },
  onPrepare: function() {
    browser.ignoreSynchronization = true;

    var SpecReporter = require('jasmine-spec-reporter');
    // add jasmine spec reporter
    jasmine.getEnv().addReporter(new SpecReporter({displayStacktrace: 'all'}));
    jasmine.getEnv().addReporter(screenshotReporter);

  }
};