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
describe("GenericEditor form view", function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentWithOnlyUrlLink/genericEditorTest.html');
    });

    it("given only urlLink attribute is present, when the component is rendered then external urlLink is a textbox (shortstring)", function() {

        expect(element(by.css("[id='urlLink-shortstring']")).isPresent()).toBe(true);

    });



});
