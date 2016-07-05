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
package de.hybris.smartedit.controllers;


public enum Page {

    LOGIN_PAGE("loginPage"),
    SMART_EDIT_ROOT_PAGE("index");

    private final String viewName;

    Page(final String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }
}
