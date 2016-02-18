/**
 * Created by ANDREW on 8/27/2015.
 */

'use strict';

// https://markgoodyear.com/2015/06/using-es6-with-gulp/
// https://gist.github.com/stephensauceda/ce81e95c6f6c5747d8aa

import gulp from 'gulp'; // ES6 imports!
import gulpLoadPlugins from 'gulp-load-plugins';
import babel from 'gulp-babel';
import gutil from 'gulp-util';
import jshint from 'gulp-jshint';

import { stream as wiredep } from 'wiredep';

import browserify from 'browserify';
import watchify from 'watchify';
import trf_babelify from 'babelify';
import trf_ngannotate from 'browserify-ngannotate';
import trf_debowerify from 'debowerify';

import concat from 'gulp-concat';
import clean from 'gulp-clean';
import source from 'vinyl-source-stream';
import buffer  from 'vinyl-buffer';

import sass from 'gulp-sass';
import autoprefixer from 'gulp-autoprefixer';
import sourcemaps from 'gulp-sourcemaps';

import embedlr from 'gulp-embedlr';
import refresh from 'gulp-livereload';
import lrserverFactory from 'tiny-lr';
import express from 'express';
import livereload from 'connect-livereload';

import browserSync from 'browser-sync';
import del from 'del';

const $ = gulpLoadPlugins();
const reload = browserSync.reload;
const src = Object.create(null);

const dirs = {
  src: 'app',
  clientDest: '../prebuilt-angularjs-client'
};

const
  scripts = {
    src: `${dirs.src}/scripts/main.js`,
    watch: [
      `${dirs.src}/scripts/*.js`,
      `${dirs.src}/scripts/**/*.js`
    ],
    dest: `${dirs.clientDest}/js/`
  },

  images= {
    watch: [
      `${dirs.src}/images/**/*`
    ],
    src: `${dirs.src}/images/**/*`,
    dest: `${dirs.clientDest}/images/**/*`
  },

  styles = {
    watch: [
      `${dirs.src}/styles/*.scss`,
      `${dirs.src}/scripts/components/**/*.scss`,
      `${dirs.src}/styles/**/*.scss`
    ],
    src: [
      `${dirs.src}/styles/*.scss`,
      //`${dirs.src}/scripts/components/**/*.scss`,
      './node_modules/bootstrap/dist/css/bootstrap.css',
      './node_modules/bootstrap/dist/css/bootstrap-theme.css'
    ],
    bootstrap: [
      './node_modules/bootstrap/dist/css/bootstrap.css',
      './node_modules/bootstrap/dist/css/bootstrap-theme.css'
    ],
    dest: `${dirs.clientDest}/styles/`
  },

  fonts = {
    src: [
      `${dirs.src}/fonts/**`,
      './node_modules/bootstrap/dist/fonts/**'
    ],
    dest: `${dirs.clientDest}/fonts/`
  },

  views = {
    watch: [
      `${dirs.src}/views/**/*`,
      `${dirs.src}/scripts/components/**/*.tpl.htm*`
    ],
    src: [
      `./${dirs.src}/views/**/*`,
      `./${dirs.src}/scripts/components/**/*.tpl.htm*`
    ],
    dest: `${dirs.clientDest}/views/`
  },

  paths = {
    scripts,
    styles,
    views
  };

const livereloadport = 35729,
  serverport = 5000,
  lrserver = lrserverFactory();


// Dev task
gulp.task('dev', ['watch', 'serve'], () => {


  // Start live reload
  lrserver.listen(livereloadport);
  // Run the watch task, to keep taps on changes
  gulp.run('watch');
});

// Launch API server
gulp.task('serve', cb => {
  
  //Start dbUpdater service
  require('../nodejs-server/src/dbUpdater');

  let prepareWebServer = require('../nodejs-server/src/lib/prepareWebServer.js')['default'];

  const app = express();
  // Add live reload
  app.use(livereload({ port: livereloadport }));

  console.dir(prepareWebServer);
  const webServer = prepareWebServer(app, `./${dirs.clientDest}`);

  // Start web server
  webServer.listen(serverport, () => {
    console.log('The server is running at the port ' + serverport);
  });

  

});

gulp.task('build-app', ['lint', 'browserify', 'views', 'fonts', 'styles']);

gulp.task('watch', ['lint', 'browserify', 'views', 'fonts', 'styles'], function() {
  // Watch our scripts
  gulp.watch(paths.scripts.watch, [
    'lint',
    'browserify'
  ]);

  gulp.watch(paths.views.watch, [
    'views'
  ]);

  gulp.watch(paths.styles.watch, [
    'styles'
  ]);
});


// https://github.com/taptapship/wiredep: Wire Bower dependencies to your source code.
gulp.task('bower', () => {
  gulp.src('./app/index.html')
    .pipe(wiredep())
    .pipe(gulp.dest('./dest'));
});

//https://markgoodyear.com/2015/06/using-es6-with-gulp/

// TODO: https://github.com/google/web-starter-kit/blob/master/gulpfile.babel.js
// TODO: BOWER: http://andy-carter.com/blog/a-beginners-guide-to-package-manager-bower-and-using-gulp-to-manage-components
// TODO: BOWER: http://stackoverflow.com/questions/22901726/how-can-i-integrate-bower-with-gulp-js
gulp.task('styles', () => {
  const AUTOPREFIXER_BROWSERS = [
    'ie >= 10',
    'ie_mob >= 10',
    'ff >= 30',
    'chrome >= 34',
    'safari >= 7',
    'opera >= 23',
    'ios >= 7',
    'android >= 4.4',
    'bb >= 10'
  ];

  gulp.src(paths.styles.src)
    .pipe($.changed('.tmp/styles', { extension: '.css' }))
    .pipe($.sourcemaps.init())
    .pipe($.sass({
      precision: 10
    }).on('error', $.sass.logError))
    //.pipe($.autoprefixer(AUTOPREFIXER_BROWSERS))
    .pipe($.sourcemaps.write())
    .pipe(gulp.dest('.tmp/styles'))
    // Concatenate and minify styles
    //.pipe($.if('*.css', $.minifyCss()))
    .pipe($.sourcemaps.write('.'))
    .pipe(gulp.dest(paths.styles.dest))
    .pipe($.size({ title: 'styles' }));

  gulp.src(paths.styles.bootstrap)
    .pipe(gulp.dest(paths.styles.dest));



    gulp.src(paths.styles.src)
      .pipe(sourcemaps.init())
      .pipe(sass.sync().on('error', $.sass.logError))
      //.pipe(autoprefixer("last 2 versions", "> 1%", "ie 8"))
      .pipe(sourcemaps.write('.'))
      .pipe(gulp.dest(paths.styles.dest));
});

// Optimize images
//gulp.task('images', () =>
//    gulp.src('app/images/**/*')
//      .pipe($.cache($.imagemin({
//        progressive: true,
//        interlaced: true
//      })))
//      .pipe(gulp.dest('dist/images'))
//      .pipe($.size({title: 'images'}))
//);

//// Copy all files at the root level (app)
//gulp.task('copy', () =>
//    gulp.src([
//      'app/*',
//      '!app/*.html',
//      'node_modules/apache-server-configs/dist/.htaccess'
//    ], {
//      dot: true
//    }).pipe(gulp.dest('dist'))
//      .pipe($.size({title: 'copy'}))
//);

// Copy web fonts to dist
gulp.task('fonts', () =>
    gulp.src(fonts.src)
      .pipe(gulp.dest(fonts.dest))
      .pipe($.size({title: 'fonts'}))
);




// Views task
gulp.task('views', function() {
  // Get our index.html
  gulp.src(`${dirs.src}/index.html`)
    // And put it in the dist folder
    .pipe(gulp.dest(`${dirs.clientDest}/`));

  // Any other view files from app/views
  gulp.src(paths.views.src)
    // Will be put in the dist/views folder
    .pipe($.rename({dirname: ''}))
    .pipe(gulp.dest(paths.views.dest))
    .pipe(refresh(lrserver)); // Tell the lrserver to refresh;
});

// Scan your HTML for assets & optimize them
gulp.task('html', () => {
  const assets = $.useref.assets({searchPath: '{.tmp,app}'});

  return gulp.src('app/**/*.html')
    .pipe(assets)
    // Remove any unused CSS
    // Note: If not using the Style Guide, you can delete it from
    //       the next line to only include styles your project uses.
    .pipe($.if('*.css', $.uncss({
      html: [
        'app/index.html'
      ],
      // CSS Selectors for UnCSS to ignore
      ignore: [
        /.navdrawer-container.open/,
        /.app-bar.open/
      ]
    })))

    // Concatenate and minify styles
    // In case you are still using useref build blocks
    .pipe($.if('*.css', $.minifyCss()))
    .pipe(assets.restore())
    .pipe($.useref())

    // Minify any HTML
    .pipe($.if('*.html', $.minifyHtml()))
    // Output files
    .pipe(gulp.dest('dist'))
    .pipe($.size({title: 'html'}));
});

// Clean output directory
gulp.task('clean', cb => del([ `${dirs.clientDest}/*` ], { dot: true, force: true }, cb));
// Clean output directory

// Browserify task
// http://advantcomp.com/blog/ES6Modules/
// http://problematic.io/2015/02/26/using-babel-and-browserify-with-gulp/
gulp.task('browserify', function() {
  // Single point of entry (make sure not to src ALL your files, browserify will figure it out for you)
  browserify({
    entries: paths.scripts.src,
    insertGlobals: true,
    debug: true,
    compact: false
  })
    //.add(require.resolve("babel/polyfill"))
    .transform(trf_babelify.configure({
      //"optional": ['runtime'],
      compact: false,
      "presets": ["es2015", "stage-0"]
    }))
    .transform(trf_ngannotate)
    .transform(trf_debowerify)
    .bundle()
    //gulp.src([`${dirs.src}/scripts/main.js`])
    //  .pipe()
    // Bundle to a single file
    //.pipe(concat('bundle.js'))
    // Output it to our dist folder
    .pipe(source('./app.js'))
    .pipe(gulp.dest(paths.scripts.dest));
});



// JSHint task
gulp.task('lint', function() {
  gulp.src(paths.scripts.watch)
    // TODO: uncomment .pipe(jshint())
    // You can look into pretty reporters as well, but that's another story
    .pipe(jshint.reporter('default'));
});





// https://gist.github.com/danharper/3ca2273125f500429945
function compile(watch) {
  let bundler = watchify(browserify('./app/scripts/main.js', { debug: true })
    .transform(trf_babelify)
    .transform(trf_ngannotate)
    .transform(trf_debowerify));

  function rebundle() {
    bundler.bundle()
      .on('error', function(err) { console.error(err); this.emit('end'); })
      .pipe(source('build.js'))
      .pipe(buffer())
      .pipe(sourcemaps.init({
        loadMaps: true
      }))
      .pipe(sourcemaps.write('./'))
      .pipe(gulp.dest('./build'));
  }

  if (watch) {
    bundler.on('update', () => {
      console.log('-> bundling...');
      rebundle();
    });
  }

  rebundle();
}

function watch() {
  return compile(true);
}

gulp.task('build', () => compile());
gulp.task('watch2', () => watch());







