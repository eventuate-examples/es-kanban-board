
import 'babel-polyfill';

import gulp from 'gulp'; // ES6 imports!
import gulpLoadPlugins from 'gulp-load-plugins';
import babel from 'gulp-babel';
import del from 'del';

import express from 'express';

import prepareWebServer from './src/lib/prepareWebServer.js';
import { protractor } from 'gulp-protractor';

import args from 'yargs';

const e2eDir = 'test/e2e';

const $ = gulpLoadPlugins();
const src = Object.create(null);

const dirs = {
  src: 'src',
  dst: 'dist',
  client: '../prebuilt-angularjs-client'
};

const serverPort = 5000;
const javaServerPort = 8080;

const app = express();
const webServer = prepareWebServer(app, `./${dirs.client}`);
const isCI = args.type === 'ci';

// Launch API server
gulp.task('serve', () => {

  // Start web server
  webServer.listen(serverPort, () => {
    console.log('The server is running at the port ' + serverPort);
  });

  //Start dbUpdater service
  require('./src/dbUpdater');
});

gulp.task('test-e2e:serve', cb => {
  webServer.listen(serverPort, cb);
});

gulp.task('test-e2e', [ 'test-e2e:serve' ], cb => {

  const url = 'http://' + 'localhost' + ':' + webServer.address().port;
  console.log(url);

  gulp.src(e2eDir + '/*.spec.js')
    .pipe(protractor({
    configFile: e2eDir + '/protractor.conf.js',
    args: ['--baseUrl', url]
  })).on('error', err => {

    webServer.close();

    throw err;

    cb();

  }).on('end', () => {

    webServer.close();
    cb();

  });
});

gulp.task('test-e2e-java', [  ], cb => {


  gulp.src(e2eDir + '/*.spec.js')
    .pipe(protractor({
      configFile: e2eDir + '/protractor.conf.js',
      args: ['--baseUrl', 'http://localhost:' + javaServerPort]
    }).on('error', err => {

    throw err;

    //cb();

  }).on('end', () => {

    cb();

  }));
});


gulp.task('build', ['clean'], function () {
  return gulp.src([ `${dirs.src}/**`, `!${dirs.src}/bootstrap.js` ])
    .pipe(babel())
    .pipe(gulp.dest(dirs.dst));
});

// Clean output directory
gulp.task('clean', cb => del([ 'dist/*' ], { dot: true }, cb));
