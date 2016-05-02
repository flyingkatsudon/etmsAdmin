require.config({
    shim: {
        'bootstrap': {deps: ['jquery', 'css!bower_components/bootstrap/dist/css/bootstrap.min']},
        'metisMenu': {deps: ['jquery', 'css!bower_components/metisMenu/dist/metisMenu.min', 'css!bower_components/font-awesome/css/font-awesome.min']},
        'sb-admin-2': {deps: ['metisMenu', 'css!bower_components/startbootstrap-sb-admin-2/dist/css/sb-admin-2']},
        'backbone': {deps: ['jquery', 'underscore']},
        'jqgrid': {deps: ['jquery', 'css!bower_components/jqGrid/css/ui.jqgrid-bootstrap', 'bower_components/jqGrid/js/minified/i18n/grid.locale-kr']},
        'morris': {deps: ['css!bower_components/morrisjs/morris']},
        'bootstrap-select': {deps: ['bootstrap', 'css!bower_components/bootstrap-select/dist/css/bootstrap-select.min']}
    },
    paths: {
        'jquery': 'bower_components/jquery/dist/jquery.min',
        'bootstrap': 'bower_components/bootstrap/dist/js/bootstrap.min',
        'metisMenu': 'bower_components/metisMenu/dist/metisMenu.min',
        'sb-admin-2': 'bower_components/startbootstrap-sb-admin-2/dist/js/sb-admin-2',
        'backbone': 'bower_components/backbone/backbone-min',
        'underscore': 'bower_components/underscore/underscore-min',
        'jqgrid': 'bower_components/jqGrid/js/jquery.jqGrid.min',
        'uuid': 'bower_components/lil-uuid/uuid.min',
        'text': 'bower_components/text/text',
        'morris': 'bower_components/morrisJs/morris.min',
        'jquery-bootstrap': 'js/dist/jquery.bootstrap.min',
        'jquery-file-download': 'bower_components/jquery-file-download/src/Scripts/jquery.fileDownload',
        'bootstrap-dialog': 'bower_components/bootstrap3-dialog/dist/js/bootstrap-dialog.min'
    },
    map: {
        '*': {'css': 'bower_components/require-css/css.min'}
    }
});