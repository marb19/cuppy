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

HOST = 'localhost'
PORT = 9001
SECURE_PORT = 9002
//SECURE_PORT = 9998
BASE_SITE = 'wsTest'
INTEGRATION_BASE_SITE = 'wsIntegrationTest'

DEFAULT_HTTP_URI = "http://${HOST}:${PORT}"
DEFAULT_HTTPS_URI = "https://${HOST}:${SECURE_PORT}"

OAUTH2_TOKEN_URI = DEFAULT_HTTPS_URI
OAUTH2_TOKEN_ENDPOINT_PATH = "/${AUTHWEBROOT}/oauth/token"
OAUTH2_TOKEN_ENDPOINT_URI = OAUTH2_TOKEN_URI + OAUTH2_TOKEN_ENDPOINT_PATH

HTTPS_AUTHWEBROOT = DEFAULT_HTTPS_URI+"/${AUTHWEBROOT}"

OAUTH2_CALLBACK_URI = "http://${HOST}:9001/${AUTHWEBROOT}/oauth2_callback"

CLIENT_ID='mobile_android'
CLIENT_SECRET='secret'
TRUSTED_CLIENT_ID='trusted_client'
TRUSTED_CLIENT_SECRET='secret'

FAIL_ON_NAMING_CONVENTION_ERROR=false

CUSTOMER_USERNAME='democustomer'
CUSTOMER_PASSWORD='1234'
CUSTOMER_MANAGER_USERNAME='customermanager'
CUSTOMER_MANAGER_PASSWORD='1234'
