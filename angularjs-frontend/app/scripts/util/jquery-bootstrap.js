/**
 * Created by ANDREW on 8/28/2015.
 */
import $ from 'jquery';
window.$ = window.jQuery = $;


$('body').on('shown.bs.modal', '.modal', function focusFirstInput(){
  $(this).find('input[type="text"]:not([readonly]):not([disabled])').focus();
});


