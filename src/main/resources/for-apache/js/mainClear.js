var $ = jQuery.noConflict();
jQuery(function($) {
    $("#star").each(function() {
        postars($('.cover')[0]).init();
    });
});