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
angular.module('decoratorServiceModule', ['functionsModule'])
    .factory('DecoratorServiceClass', function(uniqueArray, regExpFactory) {

        var DecoratorServiceClass = function() {
            this.componentDecoratorsMap = {};
        };

        DecoratorServiceClass.prototype.addMappings = function(map) {

            for (var regexpKey in map) {
                var decoratorsArray = map[regexpKey];
                this.componentDecoratorsMap[regexpKey] = uniqueArray((this.componentDecoratorsMap[regexpKey] || []), decoratorsArray);
            }

        };

        DecoratorServiceClass.prototype.getDecoratorsForComponent = function(componentType) {
            var decoratorArray = [];
            if (this.componentDecoratorsMap) {
                for (var regexpKey in this.componentDecoratorsMap) {
                    if (regExpFactory(regexpKey).test(componentType)) {
                        decoratorArray = uniqueArray(decoratorArray, this.componentDecoratorsMap[regexpKey]);
                    }
                }
            }
            return decoratorArray;
        };

        return DecoratorServiceClass;
    })
    .factory('decoratorService', function(DecoratorServiceClass) {
        return new DecoratorServiceClass();
    });
