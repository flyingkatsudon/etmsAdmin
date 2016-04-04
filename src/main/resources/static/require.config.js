require.config({
    shim: {
        'layout': {deps: ['jquery']},
        'layout.tab': {deps: ['layout']},
        'jqueryui': {deps: ['jquery', 'css!components/jquery-ui/themes/smoothness/jquery-ui.min']},
        'jquery.file.download': {deps: ['jquery']},
        'jqgrid': {deps: ['jquery', 'components/jqGrid/js/minified/i18n/grid.locale-kr', 'css!components/jqGrid/css/ui.jqgrid']},
        'jqgridlocale': {deps: ['jquery']},
        'backbone': {deps: ['jquery', 'underscore']},
        'underscore': {exports: '_'}
    },
    paths: {
        'chartjs':'components/Chart.js/Chart.min',
        'jquery': 'components/jquery/dist/jquery.min',
        'jqgrid': 'components/jqGrid/js/jquery.jqGrid.min',
        'jqueryui': 'components/jquery-ui/jquery-ui.min',
        'jquery.file.download': 'components/jquery-file-download/src/Scripts/jquery.fileDownload',
        'layout': 'components/jquery-ui-layout/source/stable/jquery.layout_and_plugins.min',
        'layout.tab': 'components/jquery-ui-layout/source/stable/callbacks/jquery.layout.resizeTabLayout.min',
        'backbone': 'components/backbone/backbone-min',
        'underscore': 'components/underscore/underscore-min',
        'text': 'components/text/text',
        'uuid': 'components/lil-uuid/uuid.min',
        'moment' : 'components/moment/min/moment-with-locales.min',
        'multiline' : 'components/multiline/browser'
    },
    map: {
        '*': {'css': 'components/require-css/css.min'}
    }
});