/**
 * Created by ANDREW on 8/27/2015.
 */
'use strict';
// //http://stackoverflow.com/questions/24827964/browserify-with-twitter-bootstrap
import 'babel-polyfill';
import './util/jquery-bootstrap';
import 'bootstrap';
import './services/index';
import app from './components/index';

export default app;
