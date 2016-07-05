/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.converters.impl;

import de.hybris.platform.converters.ModifiableConfigurablePopulator;
import de.hybris.platform.converters.config.ConfigurablePopulatorModification;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;


/**
 * Bean post processor for applying modifications to ModifiableConfigurablePopulaotr beans. Applies modification
 * declared for given populator or any parent in spring declaration.
 */
public class ModifableConfigurablePopulatorPostProcessor implements BeanFactoryAware, BeanPostProcessor
{
	private static final Logger LOG = Logger.getLogger(ModifableConfigurablePopulatorPostProcessor.class);

	protected ConfigurableListableBeanFactory beanFactory;

	protected Set<String> getParentNames(final String beanName)
	{
		final Set<String> result = new HashSet<String>();

		String name = beanName;
		do
		{
			result.add(name);
			final BeanDefinition definition = beanFactory.getBeanDefinition(name);
			name = definition == null ? null : definition.getParentName();
		}
		while (name != null);
		return result;
	}

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException
	{
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException
	{
		if (bean instanceof ConfigurablePopulatorModification)
		{
			LOG.debug("Processing: " + beanName);
			applyModification((ConfigurablePopulatorModification) bean);
		}
		return bean;
	}

	private void applyModification(final ConfigurablePopulatorModification modification)
	{
		final Map<String, ModifiableConfigurablePopulator> targets = beanFactory
				.getBeansOfType(ModifiableConfigurablePopulator.class);
		final Map<ModifiableConfigurablePopulator, String> targetsNames = targets.entrySet().stream()
				.collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
		final Map<String, Set<String>> parentNames = targets.keySet().stream()
				.collect(Collectors.toMap(e -> e, e -> getParentNames(e)));

		final String targetName = targetsNames.get(modification.getTarget());

		parentNames.entrySet().stream() //
				.filter(e -> e.getValue().contains(targetName)) //only targets where parents contain target
				.map(e -> e.getKey()) // name of target
				.map(k -> targets.get(k)) // target itself
				.filter(t -> t != null) // without nulls just in case
				.forEach(t -> t.applyModification(modification)); //apply modification to them
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}
}
