require.config({
    shim: {
        layout: {deps: ['jquery']}
    },
    paths: {
        jquery: 'components/jquery/dist/jquery.min',
        layout: 'components/jquery-ui-layout/source/stable/jquery.layout_and_plugins.min'
    },
    map: {
        '*': {'css': 'webjars/require-css/0.1.8/css.min'}
    }
});