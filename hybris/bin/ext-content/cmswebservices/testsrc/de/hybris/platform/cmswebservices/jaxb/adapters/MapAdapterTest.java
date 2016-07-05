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
package de.hybris.platform.cmswebservices.jaxb.adapters;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cmswebservices.data.AbstractCMSComponentData;
import de.hybris.platform.cmswebservices.data.BannerComponentData;
import de.hybris.platform.cmswebservices.data.CMSParagraphComponentData;
import de.hybris.platform.cmswebservices.data.LocalizedValueData;
import de.hybris.platform.cmswebservices.data.SimpleBannerComponentData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueString;
import de.hybris.platform.webservicescommons.jaxb.MoxyJaxbContextFactoryImpl;
import de.hybris.platform.webservicescommons.jaxb.adapters.DateAdapter;
import de.hybris.platform.webservicescommons.jaxb.adapters.XSSStringAdapter;
import de.hybris.platform.webservicescommons.jaxb.metadata.impl.DefaultMetadataNameProvider;
import de.hybris.platform.webservicescommons.jaxb.metadata.impl.DefaultMetadataSourceFactory;
import de.hybris.platform.webservicescommons.mapping.SubclassRegistry;
import de.hybris.platform.webservicescommons.mapping.config.FieldMapper;
import de.hybris.platform.webservicescommons.mapping.impl.DefaultFieldSetBuilder;
import de.hybris.platform.webservicescommons.mapping.impl.DefaultFieldSetLevelHelper;
import de.hybris.platform.webservicescommons.mapping.impl.DefaultSubclassRegistry;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.xml.transform.StringSource;

@UnitTest
public class MapAdapterTest
{


	@Test
	public void testMarshalParagraph() throws JAXBException {

		CMSParagraphComponentData componentData = new CMSParagraphComponentData();

		Map<String, String> localizedContent = new HashMap<String, String>();
		localizedContent.put("en", "english content");
		localizedContent.put("fr", "french content");
		LocalizedValueMap localizedValueMap = new LocalizedValueMap();
		localizedValueMap.setValue(localizedContent);

		componentData.setContent(localizedValueMap);

		String json = marshal(componentData);

		final AbstractCMSComponentData abstractComponentDataUnmarshaled = unmarshal(json,
				AbstractCMSComponentData.class);

		Assert.assertTrue(abstractComponentDataUnmarshaled instanceof CMSParagraphComponentData);
		CMSParagraphComponentData componentDataUnmarshaled = (CMSParagraphComponentData)abstractComponentDataUnmarshaled;
		Assert.assertNotNull(componentDataUnmarshaled.getContent());
		Assert.assertTrue(componentDataUnmarshaled.getContent() instanceof LocalizedValueMap);

		LocalizedValueMap localizedValueMapUnmarshaled = (LocalizedValueMap) componentDataUnmarshaled.getContent();
		Assert.assertEquals(localizedValueMap.getValue().size(), localizedValueMapUnmarshaled.getValue().size());
		Assert.assertNotNull(localizedValueMapUnmarshaled.getValue().entrySet().stream().findFirst().get().getValue());
		Assert.assertEquals(localizedValueMap.getValue().get("en"), localizedValueMapUnmarshaled.getValue().get("en"));
	}

	@Test
	public void testMarshalContainerData() throws JAXBException {

		ContainerData container = new ContainerData();
		container.setName("TEST");
		Map<String, String> metadata = new HashMap<String, String>();
		metadata.put("category", "test 1");
		metadata.put("creation", "test 2");
		container.setContent(metadata);

		String json = marshal(container);

		final ContainerData containerDataUnmarshalled = unmarshal(json,
				ContainerData.class);

		Assert.assertEquals(container.getName(), containerDataUnmarshalled.getName());
		Assert.assertNotNull(container.getContent());
		Assert.assertEquals(container.getContent().size(), containerDataUnmarshalled.getContent().size());
		Assert.assertNotNull(containerDataUnmarshalled.getContent().entrySet().stream().findFirst().get().getValue());
	}


	private static MoxyJaxbContextFactoryImpl createMoxyJaxbContextFactoryImpl()
	{
		final SubclassRegistry subClassRegistry = new DefaultSubclassRegistry();
		subClassRegistry.registerSubclass(AbstractCMSComponentData.class, CMSParagraphComponentData.class);
		subClassRegistry.registerSubclass(AbstractCMSComponentData.class, SimpleBannerComponentData.class);
		subClassRegistry.registerSubclass(AbstractCMSComponentData.class, BannerComponentData.class);
		subClassRegistry.registerSubclass(AbstractCMSComponentData.class, LocalizedValueData.class);
		subClassRegistry.registerSubclass(LocalizedValueData.class, LocalizedValueMap.class);
		subClassRegistry.registerSubclass(LocalizedValueData.class, LocalizedValueString.class);

		final DefaultFieldSetBuilder fieldSetBuilder = new DefaultFieldSetBuilder();
		fieldSetBuilder.setSubclassRegistry(subClassRegistry);
		fieldSetBuilder.setDefaultMaxFieldSetSize(50000);
		fieldSetBuilder.setFieldSetLevelHelper(new DefaultFieldSetLevelHelper());
		fieldSetBuilder.setDefaultRecurrencyLevel(4);

		final DefaultMetadataSourceFactory metadataSourceFactory = new DefaultMetadataSourceFactory();
		final DefaultMetadataNameProvider nameProvider = new DefaultMetadataNameProvider();
		metadataSourceFactory.setNameProvider(nameProvider);

		// build MoxyJaxbContextFactory
		MoxyJaxbContextFactoryImpl moxyJaxbContextFactory = new MoxyJaxbContextFactoryImpl();
		moxyJaxbContextFactory.setSubclassRegistry(subClassRegistry);
		moxyJaxbContextFactory.setTypeAdapters(Arrays.asList(KeyValueMapAdapter.class, XSSStringAdapter.class, DateAdapter.class));
		moxyJaxbContextFactory.setMetadataSourceFactory(metadataSourceFactory);
		moxyJaxbContextFactory.setWrapCollections(false);
		moxyJaxbContextFactory.setAnalysisDepth(5);
		return moxyJaxbContextFactory;
	}

	private static FieldMapper fieldMapper(final Class sourceclass, final Class destClass)
	{
		final FieldMapper fieldMapper = new FieldMapper();
		fieldMapper.setSourceClass(sourceclass);
		fieldMapper.setDestClass(destClass);
		return fieldMapper;
	}


	public static <T> String marshal(T cls) throws JAXBException {

		JAXBContext context = createMoxyJaxbContextFactoryImpl().createJaxbContext(cls.getClass(), LocalizedValueMap.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		marshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		marshaller.marshal(cls, byteArrayOutputStream);
		return byteArrayOutputStream.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(String source, Class<T> cls)
			throws JAXBException {
		JAXBContext context = createMoxyJaxbContextFactoryImpl().createJaxbContext(cls);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		unmarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);

		StreamSource streamSource = new StringSource(source);
		return unmarshaller.unmarshal(streamSource, cls).getValue();
	}
}


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class ContainerData {

	private String name;

	@XmlPath(".")
	@XmlJavaTypeAdapter(KeyValueMapAdapter.class)
	private Map<String, String> content = new HashMap<String, String>();

	public String getName()
	{
		return name;
	}

	public void setName(final String name)
	{
		this.name = name;
	}

	public void setContent(final Map<String, String> content)
	{
		this.content = content;
	}

	public Map<String, String> getContent()
	{
		return content;
	}
}