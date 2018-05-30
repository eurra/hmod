
var console = { 
    __noSuchMethod__: function() {
        var newArgs = java.util.Arrays.copyOfRange(
            Java.to(arguments, Java.type('java.lang.Object[]')), 
            1, arguments.length
        );

        this.handler.apply(arguments[0], newArgs).runCommand(); 
    } 
};
