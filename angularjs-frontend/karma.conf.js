/**
 * Created by ANDREW on 9/1/2015.
 */

module.exports = function(karma){
  karma.set({
    basePath : './',
    files : [
      //'node_modules/angular/angular.min.js',
      //'node_modules/angular-mocks/angular-mocks.js',
      //'node_modules/angular-mocks/ngMock.js',
      'app/scripts/**/*.js',
      'test/unit/**/*.js'
    ],
    preprocessors: {
      'app/scripts/**/*.js': ['browserify'],
      'test/unit/**/*.js': ['browserify']
    },
    //babelPreprocessor: {
    //  options: {
    //    sourceMap: 'inline'
    //  },
    //  filename: function (file) {
    //    return file.originalPath.replace(/\.js$/, '.es5.js');
    //  },
    //  sourceFileName: function (file) {
    //    return file.originalPath;
    //  }
    //},

    watchify: {
      poll: true,
      usePolling: true
    },

    browserify: {
      debug: true,
      watch: true,
      configure: function(bundle) {
        bundle.once('prebundle', function() {
          bundle.transform('babelify').plugin('proxyquireify/plugin');
        });
      }
    },


    usePolling: true,
    autoWatch : true,

    colors: true,

    frameworks: ['browserify', 'jasmine'],

    browsers : ['Chrome'],

    plugins : [
      'karma-chrome-launcher',
      'karma-firefox-launcher',
      'karma-jasmine',
      'karma-junit-reporter',
      'karma-browserify',
      'karma-babel-preprocessor'
      //'karma-chai'
    ],

    junitReporter : {
      outputFile: 'test_out/unit.xml',
      suite: 'unit'
    }

  });
};