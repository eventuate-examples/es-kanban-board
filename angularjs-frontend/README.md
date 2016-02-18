# es-kanban-board

#Application requirements

# Install dependencies

Run:

    npm install
    
# Configuration

Setup environment variables:
    
    KANBAN_BOARD_PORT
    KANBAN_BOARD_SECRET
    KANBAN_BOARD_MONGODB_URI
    EVENT_STORE_USER_ID
    EVENT_STORE_PASSWORD
    EVENT_STORE_URL
    EVENT_STORE_STOMP_SERVER_HOST
    EVENT_STORE_STOMP_SERVER_PORT
    LOG_LEVEL

# Authentication on API Server
1. POST request to /api/authenticate contains the user email. The response will be a JSON contains authentication token:


    {"success":true,"message":"Authentication is successful","token":"eyJhbGciOiJIUzI1NiJ9.dmFuZHJ1QHVrci5uZXQ.PZIihTrhsPVS6xCQxehP2K3vUBJw1xg_tKO_ZPq1_3E"}

2. Use the authentication token in every next request to API as "x-access-token" header.


# Running Application in development mode

Install Gulp:

    npm install -g gulp
    
Install Babel:

    npm install -g babel
 
Run the command:

    gulp dev
    
Visit http://localhost:5000
 

http://mindthecode.com/lets-build-an-angularjs-app-with-browserify-and-gulp/

```

app
  index.html
  images            // Visual assets
  scripts           // Your javascript
     controllers
     directives
     services
     main.js        // Single main entry point
  styles            // SCSS or LESS or CSS files
  views             // Templates
dist                // The target and 'www' folder
Gulpfile.js         // Gulp instructions file
package.json        // Package file with installation references

```


`npm install gulp browserify gulp-browserify gulp-clean gulp-concat gulp-jshint gulp-util gulp-embedlr gulp-livereload tiny-lr connect-livereload express --save-dev`
`npm install gulp-util gulp-embedlr gulp-livereload tiny-lr connect-livereload express --save-dev`


http://mherman.org/blog/2014/08/14/kickstarting-angular-with-gulp/#.Vd87XvmqpHw

Why We Should Stop Using Bower � And How to Do It
http://gofore.com/ohjelmistokehitys/stop-using-bower/

Debowerify with grunt-browserify :
How to run debowerify with grunt-browserify?
https://github.com/eugeneware/debowerify/issues/38

http://stackoverflow.com/questions/24190351/using-gulp-browserify-for-my-react-js-modules-im-getting-require-is-not-define

jQuery - Bootstrap
GOOD: http://stackoverflow.com/questions/22966615/browserify-jquery-is-not-defined
BAD: http://stackoverflow.com/questions/24827964/browserify-with-twitter-bootstrap
ISSUE: https://github.com/thlorenz/browserify-shim/issues/152