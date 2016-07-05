package de.hybris.platform.oauthauthorizationserver.test.groovy.config;


public class TestConfigFactory {
	private static final String DEFAULT_AUTHORIZATION_WEBROOT = 'authorizationserver'
	private static Map<String, ConfigObject> configsCache = new HashMap<>();

	public static synchronized ConfigObject createConfig(String propertyFileClassPath) {
		String key = propertyFileClassPath;
		if (configsCache.containsKey(key)) {
			return configsCache.get(key);
		} else {
			ConfigObject config = createConfigInternal(propertyFileClassPath);
			configsCache.put(key, config);
			return config;
		}
	}

	public static ConfigObject createConfigInternal(String propertyFileClassPath) {
		Map<String, Object> initialValues = new HashMap<>();
		initialValues.put("AUTHWEBROOT", DEFAULT_AUTHORIZATION_WEBROOT);
		return createConfigFromConfigProperties(propertyFileClassPath, initialValues);
	}

	private static ConfigObject createConfigFromConfigProperties(String propertyFile, Map<String, Object> initialValues) {
		String configScript = de.hybris.platform.oauthauthorizationserver.test.groovy.config.TestConfigFactory.getResource(propertyFile).text
		if (configScript == null) return null;

		ConfigSlurper configSlurper = new ConfigSlurper()
		configSlurper.setBinding(initialValues);

		ConfigObject configObject = configSlurper.parse(configScript)
		return configObject;
	}
}
