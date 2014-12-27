
//use grunt.cmd in windows

module.exports = function (grunt) {
    grunt.initConfig({
        injector: {
            options: { ignorePath: 'src/main/resources/public/', addRootSlash: false },
            local_dependencies: {
                files: {
                    'src/main/resources/public/index.html': ['bower.json', 'src/main/resources/public/app/**/*.js', 'src/main/resources/public/assets/**/*.css']
                }
            }
        }
    });
    grunt.loadNpmTasks('grunt-injector');
    grunt.registerTask('default', 'injector');
};

