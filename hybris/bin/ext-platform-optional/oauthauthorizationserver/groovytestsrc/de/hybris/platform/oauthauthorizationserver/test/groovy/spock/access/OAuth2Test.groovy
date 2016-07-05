/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2014 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 */

package de.hybris.platform.oauthauthorizationserver.test.groovy.spock.access

import de.hybris.platform.oauthauthorizationserver.test.groovy.spock.AbstractSpockTest

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

import de.hybris.bootstrap.annotations.ManualTest

import spock.lang.Unroll
import groovyx.net.http.HttpResponseDecorator

@ManualTest
@Unroll
class OAuth2Test extends AbstractSpockTest {

	static final def CLIENTS = [
			[
					clientId: getClientId(),
					clientSecret: getClientSecret(),
					redirectUri: getClientRedirectUri()
			],
			[
					clientId: getTrustedClientId(),
					clientSecret: getTrustedClientSecret()
			]
	]

	static final def USERS = [
			[
					username: getCustomerUserName(),
					password: getCustomerPassword()
			],
			[
					username: getCustomerManagerUserName(),
					password: getCustomerManagerPassword()
			]
	]

	def "Get token using client credentials : #params"() {
		expect:
		getOAuth2TokenUsingClientCredentials(restClient, params.clientId, params.clientSecret)

		where:
		params << CLIENTS
	}


	def "Get token using password : #params"() {
		expect:
		getOAuth2TokenUsingPassword(restClient, getClientId(), getClientSecret(), getCustomerUserName(), getCustomerPassword())

		where:
		params << [CLIENTS, USERS].combinations().collect { it[0] + it[1] }
	}

	def "Refresh token : #params"() {
		given:
		def token = getOAuth2TokenUsingPassword(restClient, params.clientId, params.clientSecret, params.username, params.password)

		expect:
		refreshOAuth2Token(restClient, token.refresh_token, params.clientId, params.clientSecret, params.redirectUri)

		where:
		params << [CLIENTS, USERS].combinations().collect { it[0] + it[1] }
	}

	def "Get token with wrong client id : #params"() {
		when:
		HttpResponseDecorator response = restClient.post(
				uri: getOAuth2TokenUri(),
				path: getOAuth2TokenPath(),
				body: [
						'grant_type': 'client_credentials',
						'client_id': 'WRONG',
						'client_secret': params.clientSecret
				],
				contentType: JSON,
				requestContentType: URLENC)

		then:
		with(response) {
			status == SC_UNAUTHORIZED
			isNotEmpty(data.errors)
			data.errors[0].type == 'UnauthorizedError'
		}

		where:
		params << CLIENTS
	}

	def "Get token with wrong client secret : #params"() {
		when:
		HttpResponseDecorator response = restClient.post(
				uri: getOAuth2TokenUri(),
				path: getOAuth2TokenPath(),
				body: [
						'grant_type': 'client_credentials',
						'client_id': params.clientId,
						'client_secret': 'WRONG'
				],
				contentType: JSON,
				requestContentType: URLENC)

		then:
		with(response) {
			status == SC_UNAUTHORIZED
			isNotEmpty(data.errors)
			data.errors[0].type == 'BadClientCredentialsError'
		}

		where:
		params << CLIENTS
	}

	def "Get token with wrong grant type"() {
		when:
		HttpResponseDecorator response = restClient.post(
				uri: getOAuth2TokenUri(),
				path: getOAuth2TokenPath(),
				body: [
						'grant_type': 'WRONG',
						'client_id': getTrustedClientId(),
						'client_secret': getTrustedClientSecret()
				],
				contentType: JSON,
				requestContentType: URLENC)

		then:
		with(response) {
			status == SC_BAD_REQUEST
			isNotEmpty(data.errors)
			data.errors[0].type == 'UnsupportedGrantTypeError'
		}
	}
}
