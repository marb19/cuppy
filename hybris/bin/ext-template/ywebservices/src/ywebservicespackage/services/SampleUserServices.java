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
package ywebservicespackage.services;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


public interface SampleUserServices
{
	UserModel getUserById(String id);

	Collection<UserModel> getUsers();
}
