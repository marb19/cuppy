angular.module('sanitizeHtmlInputModule', [])
    .directive('sanitizeHtmlInput', function() {
        return {
            restrict: "A",
            replace: false,
            transclude: false,
            priority: 1000,
            link: function($scope, element) {
                element.change(
                    function() {
                        var target = element.val();
                        target = target.replace(new RegExp('{', 'g'), '%7B');
                        target = target.replace(new RegExp('}', 'g'), '%7D');
                        element.val(target);
                    });
            }
        };
    });
