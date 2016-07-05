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
describe('experienceService - ', function() {

    var $q, experienceService;
    var siteService, catalogService, languageService;
    var siteDescriptor, catalogVersionDescriptor, languageDescriptor;

    beforeEach(customMatchers);

    beforeEach(module('experienceServiceModule', function($provide) {
        siteService = jasmine.createSpyObj('siteService', ['getSiteById']);
        siteDescriptor = {
            someProperty: Math.random()
        };

        $provide.value('siteService', siteService);

        catalogService = jasmine.createSpyObj('catalogService', ['getCatalogsForSite']);
        catalogVersionDescriptor = {
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        };

        $provide.value('catalogService', catalogService);

        languageService = jasmine.createSpyObj('languageService', ['getLanguagesForSite']);
        languageDescriptor = {
            someProperty: Math.random()
        };

        $provide.value('languageService', languageService);

    }));

    beforeEach(inject(function(_$q_, _experienceService_) {
        $q = _$q_;
        experienceService = _experienceService_;
    }));

    it('GIVEN a siteId, catalogId and catalogVersion, buildDefaultExperience will reconstruct an experience', function() {

        //GIVEN
        siteService.getSiteById.andReturn($q.when(siteDescriptor));
        catalogService.getCatalogsForSite.andReturn($q.when([{
            catalogId: 'someValue',
            catalogVersion: 'someCatalogVersion'
        }, catalogVersionDescriptor]));
        languageService.getLanguagesForSite.andReturn($q.when([languageDescriptor, {}]));

        // WHEN
        var promise = experienceService.buildDefaultExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        expect(promise).toBeResolvedWithData({
            siteDescriptor: siteDescriptor,
            catalogDescriptor: catalogVersionDescriptor,
            languageDescriptor: languageDescriptor,
            time: null
        });

        expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
        expect(catalogService.getCatalogsForSite).toHaveBeenCalledWith('mySiteId');
        expect(languageService.getLanguagesForSite).toHaveBeenCalledWith('mySiteId');
    });

    it('GIVEN a siteId, catalogId and unknown catalogVersion, buildDefaultExperience will return a rejected promise', function() {

        //GIVEN
        siteService.getSiteById.andReturn($q.when(siteDescriptor));
        catalogService.getCatalogsForSite.andReturn($q.when([{
            catalogId: 'someValue',
            catalogVersion: 'someCatalogVersion'
        }, catalogVersionDescriptor]));
        languageService.getLanguagesForSite.andReturn($q.when([languageDescriptor, {}]));

        // WHEN
        var promise = experienceService.buildDefaultExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'unknownVersion'
        });

        // THEN
        expect(promise).toBeRejectedWithData('no catalogVersionDescriptor found for myCatalogId catalogId and unknownVersion catalogVersion');

        expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
        expect(catalogService.getCatalogsForSite).toHaveBeenCalledWith('mySiteId');
        expect(languageService.getLanguagesForSite).not.toHaveBeenCalled();
    });

    it('GIVEN a siteId, unknown catalogId and right catalogVersion, buildDefaultExperience will return a rejected promise', function() {

        //GIVEN
        siteService.getSiteById.andReturn($q.when(siteDescriptor));
        catalogService.getCatalogsForSite.andReturn($q.when([{
            catalogId: 'someValue',
            catalogVersion: 'someCatalogVersion'
        }, catalogVersionDescriptor]));
        languageService.getLanguagesForSite.andReturn($q.when([languageDescriptor, {}]));

        // WHEN
        var promise = experienceService.buildDefaultExperience({
            siteId: 'mySiteId',
            catalogId: 'unknownCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        expect(promise).toBeRejectedWithData('no catalogVersionDescriptor found for unknownCatalogId catalogId and myCatalogVersion catalogVersion');

        expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
        expect(catalogService.getCatalogsForSite).toHaveBeenCalledWith('mySiteId');
        expect(languageService.getLanguagesForSite).not.toHaveBeenCalled();
    });

    it('GIVEN a wrong siteId, buildDefaultExperience will return a rejected promise', function() {

        //GIVEN
        siteService.getSiteById.andReturn($q.reject(siteDescriptor));
        catalogService.getCatalogsForSite.andReturn($q.when([{
            catalogId: 'someValue',
            catalogVersion: 'someCatalogVersion'
        }, catalogVersionDescriptor]));
        languageService.getLanguagesForSite.andReturn($q.when([languageDescriptor, {}]));

        // WHEN
        var promise = experienceService.buildDefaultExperience({
            siteId: 'mySiteId',
            catalogId: 'myCatalogId',
            catalogVersion: 'myCatalogVersion'
        });

        // THEN
        expect(promise).toBeRejected();

        expect(siteService.getSiteById).toHaveBeenCalledWith('mySiteId');
        expect(catalogService.getCatalogsForSite).not.toHaveBeenCalled();
        expect(languageService.getLanguagesForSite).not.toHaveBeenCalled();
    });

});
