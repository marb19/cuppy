/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
// Karma configuration
// Generated on Sat Jul 05 2014 07:57:17 GMT-0400 (EDT)

module.exports = function(config) {
    config.set({

        // base path that will be used to resolve all patterns (eg. files, exclude)
        basePath: '../',


        // frameworks to use
        // available frameworks: https://npmjs.org/browse/keyword/karma-adapter
        frameworks: ['jasmine'],

        decorators: [
            'karma-phantomjs-launcher',
            'karma-jasmine',
            'karma-coverage',
            'karma-ng-html2js-preprocessor'
        ],

        preprocessors: {
            //'web/**/*.html': 'ng-html2js',
            'web/smartedit/**/*.js': ['coverage']
        },

        coverageReporter: {
            // specify a common output directory
            dir: 'jsTarget/test/smartedit/coverage/',
            reporters: [{
                type: 'html',
                subdir: 'report-html'
            }, {
                type: 'cobertura',
                subdir: '.',
                file: 'cobertura.xml'
            }, ]
        },

        junitReporter: {
            outputDir: 'jsTarget/test/smartedit/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
                //suite: '' // suite will become the package name attribute in xml testsuite element
        },

        // list of files / patterns to load in the browser
        files: [
            'web/webroot/static-resources/thirdparties/jquery/dist/jquery.min.js', //load jquery so that angular will leverage it and not serve with jqLite that has poor API
            'web/webroot/static-resources/thirdparties/tinymce-dist/tinymce.js',
            'web/webroot/static-resources/thirdparties/moment/moment.js',
            'web/webroot/static-resources/thirdparties/angular/angular.js',
            'web/webroot/static-resources/thirdparties/angular-route/angular-route.js',
            'web/webroot/static-resources/thirdparties/angular-resource/angular-resource.js',
            'web/webroot/static-resources/thirdparties/angular-cookies/angular-cookies.js',
            'web/webroot/static-resources/thirdparties/angular-animate/angular-animate.js',
            'web/webroot/static-resources/thirdparties/angular-mocks/angular-mocks.js',
            'web/webroot/static-resources/thirdparties/angular-bootstrap/ui-bootstrap-tpls.min.js', //needed since contains $modal
            'web/webroot/static-resources/thirdparties/angular-ui-tinymce/src/tinymce.js',
            //, 
            'smarteditJSTests/utils/**/*.js',
            'web/**/*.html',
            'jsTarget/templates.js',
            'web/common/**/*.js',
            'web/smartedit/core/**/*.js',
            'web/smartedit/services/**/*.js',
            'web/smartedit/modules/systemModule/services/toolbar/toolbar.js',
            'smarteditJSTests/unit/**/*Test.js'
        ],

        // list of files to exclude
        exclude: [
            'web/smartedit/core/smartedit.js',
            'web/smartedit/partialBackendMocks.js',
            'web/smartedit/core/smarteditbootstrap.js'
        ],


        // test results reporter to use
        // possible values: 'dots', 'progress'
        // available reporters: https://npmjs.org/browse/keyword/karma-reporter
        // coverage reporter generates the coverage
        reporters: ['progress', 'junit'], // 'coverage' interferes with gatewayProxy and proxies empty methods when it should not

        // web server port
        port: 9876,


        // enable / disable colors in the output (reporters and logs)
        colors: true,

        // level of logging
        // possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,

        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        browsers: ['PhantomJS'], //Chrome

        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};
