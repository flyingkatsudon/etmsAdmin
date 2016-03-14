require.config({
    shim : {
        layout : {deps:['jquery']}
    },
    paths : {
        jquery : 'webjars/jquery/2.2.1/jquery'
        layout : 'webjars/jqueryui-layout/1.4.0/jquery.layout'
    },
    map : {
        '*' : {'css' : 'webjars/require-css/0.1.8/css.min'}
    }
});