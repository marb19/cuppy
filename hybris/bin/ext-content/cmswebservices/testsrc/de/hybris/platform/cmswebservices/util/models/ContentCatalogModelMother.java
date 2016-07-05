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
package de.hybris.platform.cmswebservices.util.models;

import de.hybris.platform.cms2.model.contents.ContentCatalogModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSContentCatalogDao;
import de.hybris.platform.cmswebservices.data.LocalizedValueData;
import de.hybris.platform.cmswebservices.localization.data.LocalizedValueMap;
import de.hybris.platform.cmswebservices.util.builder.ContentCatalogModelBuilder;
import org.springframework.beans.factory.annotation.Required;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;


public class ContentCatalogModelMother extends AbstractModelMother<ContentCatalogModel>
{

	public enum CatalogTemplate {
		ID_APPLE("Apple's Content Catalog"),
		ID_ONLINE("online content catalog"),
		ID_STAGED("staged content catalog");

		private final LocalizedValueMap humanName;

		CatalogTemplate(final String humanName) { this.humanName = buildName(Locale.UK, humanName); }

		CatalogTemplate(final LocalizedValueMap humanName) {
			this.humanName = humanName;
		}

		public LocalizedValueMap getHumanName() {
			return humanName;
		}

		public String getFirstInstanceOfHumanName() {
			Map<String, String> map = getHumanName().getValue();
			String key = map.keySet().stream().findFirst().get();
			return map.get(key);
		}

		public static LocalizedValueMap buildName(Locale loc, String name) {
			Map<String, String> map = new HashMap<>();
			map.put(loc.getLanguage(), name);

			LocalizedValueMap localizedValueMap = new LocalizedValueMap();
			localizedValueMap.setValue(map);
			return new LocalizedValueMap() {
				@Override
				public Map<String, String> getValue() {
					return map;
				}

				@Override
				public void setValue(Map<String, String> value) {
					throw new RuntimeException("setValue is unsupported for mock CatalogTemplate");
				}
			};
		}
	}

	private CMSContentCatalogDao cmsContentCatalogDao;

	protected ContentCatalogModel defaultCatalog()
	{
		return ContentCatalogModelBuilder.aModel().withDefault(Boolean.TRUE).build();
	}

	public ContentCatalogModel createAppleContentCatalogModel()
	{
		return createContentCatalogModelWithIdAndName(ID_APPLE.name(), ID_APPLE.getFirstInstanceOfHumanName());
	}

	public ContentCatalogModel createContentCatalogModelFromTemplate(final CatalogTemplate template)
	{
		return createContentCatalogModelWithIdAndName(template.name(), template.getFirstInstanceOfHumanName());
	}

	public ContentCatalogModel createContentCatalogModelWithIdAndName(final String id, final String name)
	{
		return getOrSaveAndReturn(
				() -> getCmsContentCatalogDao().findContentCatalogById(id),
				() -> ContentCatalogModelBuilder.fromModel(defaultCatalog())
						.withName(name, Locale.ENGLISH)
						.withId(id)
						.build());
	}

	protected CMSContentCatalogDao getCmsContentCatalogDao()
	{
		return cmsContentCatalogDao;
	}

	@Required
	public void setCmsContentCatalogDao(final CMSContentCatalogDao cmsContentCatalogDao)
	{
		this.cmsContentCatalogDao = cmsContentCatalogDao;
	}
}
