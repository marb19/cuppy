package com.hybris.backoffice.cockpitng.dataaccess.facades.object;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelService;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.DefaultItemModificationHistoryService;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class DefaultItemModificationHistoryServiceTest
{

	@InjectMocks
	private DefaultItemModificationHistoryService modificationHistoryService;

	@Mock
	private TypeFacade typeFacade;
	@Mock
	private ModelService modelService;

	@Mock
	private DataAttribute dataAttribute;
	@Mock
	private ItemModelContextImpl itemModelContext;
	@Mock
	private ModelValueHistory modelValueHistory;

	@Test
	public void testCreatingModificationInfoWhenPrivateAttributesAreAccessible()
	{
		// given
		final ProductModel product = new ProductModel(itemModelContext);
		final String code = "code";
		final String catalog = "catalog";
		final String identifier = "identifier";
		when(typeFacade.getAttribute(product, code)).thenReturn(dataAttribute);
		when(typeFacade.getAttribute(product, catalog)).thenReturn(null);
		when(typeFacade.getAttribute(product, identifier)).thenReturn(dataAttribute);
		when(itemModelContext.getValueHistory()).thenReturn(modelValueHistory);
		when(modelValueHistory.isDirty()).thenReturn(true);
		when(modelValueHistory.getDirtyAttributes()).thenReturn(Stream.of(code, catalog, identifier).collect(Collectors.toSet()));

		// when
		modificationHistoryService.createModificationInfo(product);

		// then
		verify(modelService).getAttributeValue(product, code);
		verify(modelService, never()).getAttributeValue(product, catalog);
		verify(modelService).getAttributeValue(product, identifier);
	}
}
