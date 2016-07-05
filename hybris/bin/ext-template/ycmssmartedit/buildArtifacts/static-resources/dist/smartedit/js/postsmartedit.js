angular.module("smartedit",["sakExecutorDecorator","restServiceFactoryModule","ui.bootstrap","ngResource","PerspectiveModule","alertsBoxModule","ui.select","httpAuthInterceptorModule","httpErrorInterceptorModule","httpSecurityHeadersInterceptorModule","experienceInterceptorModule","languageInterceptorModule","gatewayFactoryModule","renderServiceModule","iframeClickDetectionServiceModule","sanitizeHtmlInputModule"]).config(["$logProvider",function(a){a.debugEnabled(!1)}]).directive("html",function(){return{restrict:"E",replace:!1,transclude:!1,priority:1e3,link:function(a,b){b.addClass("smartedit-html-container")}}}).directive("body",["sakExecutor","gatewayFactory",function(a,b){return{restrict:"E",replace:!1,transclude:!1,priority:1e3,link:function(c,d){var e=0,f=!1;c.$watch(a.hasPending,function(a){a===!1&&e++,1!==e||f||(f=!0,b.createGateway("smartEditBootstrap").publish("smartEditReady"),d.attr("data-smartedit-ready","true"))})}}}]).controller("SmartEditController",["$scope",function(a){}]).run(["$rootScope","$log","$compile","$http","decoratorFilterService","restServiceFactory","hitch","domain","gatewayFactory","renderService",function(a,b,c,d,e,f,g,h,i,j){i.initListener(),f.setDomain(h),e.registerPerspectiveChangedListener(function(d){b.debug("Perspective changed to: "+d.name+", with decorators: "+d.decorators);var f=e.getPrecompiledComponents();for(var g in f){var h=f[g],i="[data-smartedit-component-id='"+h.id+"'][data-smartedit-component-type='"+h.type+"']";$(i).empty(),$(i).append(h.html),c($(i))(a)}})}]),angular.element(document).ready(function(){$("textarea, input[type!=password]").each(function(){$(this).attr("data-sanitize-html-input","")});var a=window.smartedit.applications;angular.module("ApplicationManager").factory("PersistedConfiguration",function(){return{getAppModuleNames:function(){return a}}});for(var b in a){var c=a[b];try{var d=angular.module(c);d&&angular.module("ApplicationManager").requires.push(c)}catch(e){throw console.error("Failed to load app. No module exists called ["+c+"]"),e}}angular.module("smartedit").constant("domain",window.smartedit.domain).constant("smarteditroot",window.smartedit.smarteditroot),angular.bootstrap(document,["smartedit"])});