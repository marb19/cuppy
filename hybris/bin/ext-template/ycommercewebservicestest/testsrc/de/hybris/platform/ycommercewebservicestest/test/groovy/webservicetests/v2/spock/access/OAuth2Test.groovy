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
 */

package de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.access

import de.hybris.bootstrap.annotations.ManualTest
import de.hybris.platform.ycommercewebservicestest.test.groovy.webservicetests.v2.spock.AbstractSpockFlowTest

import spock.lang.Unroll

@ManualTest
@Unroll
class OAuth2Test extends AbstractSpockFlowTest {

	def "Test brute force attack"(){
		given: "Registered user"
		authorizeClient(restClient)
		def customer = registerCustomer(restClient);

		when:"try to get token 5 times with wrong password"
		def errorData1 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, 'wrongPassword', false);
		def errorData2 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, 'wrongPassword', false);
		def errorData3 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, 'wrongPassword', false);
		def errorData4 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, 'wrongPassword', false);
		def errorData5 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, 'wrongPassword', false);
		def errorData6 = getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), customer.id, customer.password, false);

		then:"user account will be disabled"
		errorData1.errors[0].type == 'InvalidGrantError'
		errorData1.errors[0].message == 'Bad credentials'
		errorData2.errors[0].type == 'InvalidGrantError'
		errorData2.errors[0].message == 'Bad credentials'
		errorData3.errors[0].type == 'InvalidGrantError'
		errorData3.errors[0].message == 'Bad credentials'
		errorData4.errors[0].type == 'InvalidGrantError'
		errorData4.errors[0].message == 'Bad credentials'
		errorData5.errors[0].type == 'InvalidGrantError'
		errorData5.errors[0].message == 'Bad credentials'
		errorData6.errors[0].type == 'InvalidGrantError'
		errorData6.errors[0].message == 'User is disabled'
	}
}
