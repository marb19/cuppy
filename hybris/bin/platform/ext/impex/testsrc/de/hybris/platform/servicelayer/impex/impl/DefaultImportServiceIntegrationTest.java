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
package de.hybris.platform.servicelayer.impex.impl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.fail;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.impex.constants.ImpExConstants;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.exceptions.ModelInitializationException;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.interceptor.Interceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.InterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import de.hybris.platform.servicelayer.interceptor.impl.DefaultInterceptorRegistry;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorMapping;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.testframework.TestUtils;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.Config;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class DefaultImportServiceIntegrationTest extends ServicelayerTest
{

	private static final Logger LOG = Logger.getLogger(DefaultImportServiceIntegrationTest.class);

	@Resource
	private ImportService importService;
	@Resource
	private ModelService modelService;
	@Resource
	private MediaService mediaService;
	@Resource 
	private FlexibleSearchService flexibleSearchService;
	@Resource
	private UserService userService;
   
	private final PropertyConfigSwitcher distributedImpexFlag = new PropertyConfigSwitcher(ImportService.DISTRIBUTED_IMPEX_GLOBAL_FLAG);

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
	}

	@After
	public void setLegacyMode()
	{
        distributedImpexFlag.switchBackToDefault();
	}

	@Test
	public void testFileBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";
		final File testFile = File.createTempFile("test", "test");
		final PrintWriter writer = new PrintWriter(testFile);
		writer.write(data);
		writer.close();

		final ImpExResource res = new FileBasedImpExResource(testFile, "windows-1252");
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
		assertEquals("windows-1252", media.getEncoding().getCode().toLowerCase());
	}

	@Test
	public void testStreamBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";

		final ImpExResource res = new StreamBasedImpExResource(new ByteArrayInputStream(data.getBytes()),
				CSVConstants.HYBRIS_ENCODING);
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
	}

	@Test
	public void testMediaBasedImportResource() throws IOException
	{
		final String data = "INSERT Language;isocode;active\n;test;true";
		final ImpExMediaModel newMedia = modelService.create(ImpExMediaModel._TYPECODE);
		try
		{
			modelService.initDefaults(newMedia);
		}
		catch (final ModelInitializationException e)
		{
			throw new SystemException(e);
		}
		modelService.save(newMedia);
		mediaService.setDataForMedia(newMedia, data.getBytes());

		final ImpExResource res = new MediaBasedImpExResource(newMedia);
		final ImpExMediaModel media = res.getMedia();
		assertNotNull(media);
		assertArrayEquals(data.getBytes(), mediaService.getDataFromMedia(media));
	}

	@Test
	public void testImportByResource()
	{
		final ImpExResource mediaRes = new StreamBasedImpExResource(
				new ByteArrayInputStream("INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportResult result = importService.importData(mediaRes);
		assertNotNull(result);
		assertNotNull("Language 'test' was not imported", getOrCreateLanguage("test"));
	}

	@Test
	public void testImportByConfig()
	{
		final ImpExResource mediaRes = new StreamBasedImpExResource(
				new ByteArrayInputStream("INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);

		final ImportConfig config = new ImportConfig();
		config.setScript(mediaRes);

		final ImportResult result = importService.importData(config);
		assertNotNull(result);
		assertTrue(result.isSuccessful());
		assertFalse(result.isError());
		assertFalse(result.hasUnresolvedLines());
		assertTrue(result.isFinished());
		assertNotNull("Language 'test' was not imported", getOrCreateLanguage("test"));
	}

	@Test
	public void testImportByConfigWithError()
	{
		internalImportByConfigWithError(false);
	}

	@Test
	public void testImportByConfigWithErrorFailSafe()
	{
		internalImportByConfigWithError(true);
	}

	private void internalImportByConfigWithError(final boolean failOnError)
	{
		try
		{
			TestUtils.disableFileAnalyzer();
			final ImpExResource mediaRes = new StreamBasedImpExResource(
					new ByteArrayInputStream("UPDATE Language;isocode[unique=true];active\n;test;true".getBytes()),
					CSVConstants.HYBRIS_ENCODING);

			final ImportConfig config = new ImportConfig();
			config.setScript(mediaRes);
			config.setFailOnError(failOnError);

			final ImportResult result = importService.importData(config);
			assertNotNull(result);
			assertFalse(result.isSuccessful());
			assertTrue(result.isError());
			assertTrue(result.hasUnresolvedLines());
			assertNotNull(result.getUnresolvedLines());
			assertTrue(result.isFinished());
		}
		finally
		{
			TestUtils.enableFileAnalyzer();
		}
	}

	@Test
	public void testCreateExportImportJobs()
	{
		//innerTestType("ZoneDeliveryMode");
		try
		{
			final ItemModel modelItem = modelService.create("ImpExImportCronJob");

			modelService.save(modelItem);
			//System.out.println(modelService.getSource(modelItem));
			Assert.assertEquals(ImpExManager.getInstance().createDefaultImpExImportCronJob().getComposedType(),
					((Item) modelService.getSource(modelItem)).getComposedType());

		}
		finally
		{
			//nothing here
		}

		try
		{
			final ItemModel modelItem = modelService.create("ImpExExportCronJob");

			modelService.save(modelItem);
			//System.out.println(modelService.getSource(modelItem));
			Assert.assertEquals(ImpExManager.getInstance().createDefaultExportCronJob().getComposedType(),
					((Item) modelService.getSource(modelItem)).getComposedType());

		}
		finally
		{
			//nothing here
		}
	}


	@Test
	public void shouldImportScriptWithLegacyModeOnWhenGlobalSwitchIsOffUsingImportConfig()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "false");
		final ImpExResource mediaRes = new StreamBasedImpExResource(
				new ByteArrayInputStream("INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setLegacyMode(Boolean.TRUE);
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isFalse();
		assertThat(importResult.getCronJob().getLegacyMode()).isTrue();
	}

	@Test
	public void shouldImportScriptWithLegacyModeOffWhenGlobalSwitchIsOnUsingImportConfig()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		final ImpExResource mediaRes = new StreamBasedImpExResource(
				new ByteArrayInputStream("INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setLegacyMode(Boolean.FALSE);
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isTrue();
		assertThat(importResult.getCronJob().getLegacyMode()).isFalse();
	}

	@Test
	public void shouldImportScriptWithGlobalModeWhenImportConfigHasLegacyModeNull()
	{
		// given
		Config.setParameter(ImpExConstants.Params.LEGACY_MODE_KEY, "true");
		final ImpExResource mediaRes = new StreamBasedImpExResource(
				new ByteArrayInputStream("INSERT Language;isocode;active\n;test;true".getBytes()), CSVConstants.HYBRIS_ENCODING);
		final ImportConfig config = new ImportConfig();
		config.setSynchronous(true);
		config.setFailOnError(true);
		config.setScript(mediaRes);
		config.setRemoveOnSuccess(false);

		// when
		final ImportResult importResult = importService.importData(config);

		// then
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
		assertThat(Boolean.parseBoolean(Config.getParameter(ImpExConstants.Params.LEGACY_MODE_KEY))).isTrue();
		assertThat(importResult.getCronJob().getLegacyMode()).isNull();
	}

	@Test
	public void shouldSeeFinishedStateForAsyncImport() throws InterruptedException
	{
		// given
		final ImportConfig config = new ImportConfig();
		config.setScript(//
				"INSERT Title;code[unique=true]\n"//
						+ ";foo;\n" //
						+ "\"#% java.lang.Thread.sleep(2000);\";\n" //
						+ ";bar;\n" //
		);
		config.setSynchronous(false);
		config.setFailOnError(true);
		config.setRemoveOnSuccess(false);
		config.setEnableCodeExecution(Boolean.TRUE);

		// when
		final long now = System.currentTimeMillis();
		final ImportResult importResult = importService.importData(config);
		final long maxWait = System.currentTimeMillis() + (30 * 1000);
		do
		{
			Thread.sleep(500);
		}
		while (!importResult.isFinished() && System.currentTimeMillis() < maxWait);

		// then
		assertThat(System.currentTimeMillis() - now).isGreaterThanOrEqualTo(2000);
		assertThat(importResult.isFinished()).isTrue();
		assertThat(importResult.isError()).isFalse();
	}

	@Test
	public void shouldDumpLinesWithInvalidOrNoHeader()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/invalid-headers-test-1.impex",
				"UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();

		// we expect 5 lines to be correct
		// 1st line is without header
		// 4th line has wrong column in header
		assertThat(result.getCronJob().getValueCount()).isEqualTo(5);
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(config.getMaxThreads()).isGreaterThan(1);

		final String importResults = new String(mediaService.getDataFromMedia(result.getUnresolvedLines()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Should dump lines with invalid or no header results:");
			LOG.debug(importResults);
		}
	}

	@Test
	public void shouldNotImportLinesWithPreviousHeaderIfInvalidCurrentOne()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource("/impex/testfiles/invalid-headers-test-2.impex",
				"UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();
		assertThat(result.getCronJob().getValueCount()).isEqualTo(1);
		assertThat(result.hasUnresolvedLines()).isTrue();
		assertThat(config.getMaxThreads()).isGreaterThan(1);

		final String importResults = new String(mediaService.getDataFromMedia(result.getUnresolvedLines()));
		if (LOG.isDebugEnabled())
		{
			LOG.debug("Should not import lines with previous header if invalid current one results:");
			LOG.debug(importResults);
		}
	}

	@Test
	public void shouldRunImportUsingDistributedImpEx() throws Exception
	{
		// given
		final String inputScript = "INSERT_UPDATE Title;code[unique=true]\n;foo\n;bar\n;baz";
		final ImportConfig config = new ImportConfig();
		config.setScript(inputScript);
		config.setDistributedImpexEnabled(true);

		// when
		final ImportResult importResult = importService.importData(config);
        waitForDistributedImpEx(importResult, 10);

		// then
		assertThat(importResult).isNotNull();
		assertThat(importResult.isFinished()).isTrue();
        assertThat(importResult.isRunning()).isFalse();

        final TitleModel fooTitle = findTitleForCode("foo");
        final TitleModel barTitle = findTitleForCode("bar");
        final TitleModel bazTitle = findTitleForCode("baz");
        assertThat(fooTitle).isNotNull();
        assertThat(barTitle).isNotNull();
        assertThat(bazTitle).isNotNull();
	}

    @Test
    public void importConfigShouldOverrideGlobalFlagForDistributedImpexSetToFalse() throws Exception
    {
    	// given
        distributedImpexFlag.switchToValue("false");
        final ImportConfig importConfig = new ImportConfig();
        importConfig.setDistributedImpexEnabled(true);

    	// when
        final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

        // then
        assertThat(distributedImpexEnabled).isTrue();
    }

    @Test
    public void importConfigShouldOverrideGlobalFlagForDistributedImpexSetToTrue() throws Exception
    {
    	// given
        distributedImpexFlag.switchToValue("true");
        final ImportConfig importConfig = new ImportConfig();
        importConfig.setDistributedImpexEnabled(false);

    	// when
        final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

        // then
        assertThat(distributedImpexEnabled).isFalse();
    }

    @Test
    public void defaultSettingForDistributedImpexShouldBeDisabled() throws Exception
    {
    	// given
        final ImportConfig importConfig = new ImportConfig();

    	// when
        final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

        // then
        assertThat(distributedImpexEnabled).isFalse();
    }

    @Test
    public void shouldEnableGloballyDistributedImpex() throws Exception
    {
    	// given
        distributedImpexFlag.switchToValue("true");
        final ImportConfig importConfig = new ImportConfig();

    	// when
        final boolean distributedImpexEnabled = ((DefaultImportService) importService).isDistributedImpexEnabled(importConfig);

        // then
        assertThat(distributedImpexEnabled).isTrue();
    }

	private void waitForDistributedImpEx(final ImportResult importResult, final long seconds) throws InterruptedException
	{
		final long end = System.nanoTime() + TimeUnit.SECONDS.toNanos(seconds);
		double sleepMs = 10;

		while (System.nanoTime() < end)
		{
			Thread.sleep((long) sleepMs);
            sleepMs = Math.min(1000.0, sleepMs * 1.1);

            if (importResult.isFinished())
            {
                return;
            }
		}

        fail("Distributed impex process didn't finished in " + seconds + " seconds");
	}

    private TitleModel findTitleForCode(final String code)
    {
        final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Title} WHERE {code}=?code");
        fQuery.addQueryParameter("code", code);

        final SearchResult<TitleModel> searchResult = flexibleSearchService.search(fQuery);

        if (searchResult.getCount() == 0)
        {
            return null;
        }

        if (searchResult.getCount() > 1)
        {
            fail("Found more than one TitleModel with code: " + code);
        }

        return searchResult.getResult().get(0);
    }

	@Test
	public void invalidCellDecoratorShouldFailMultithreadedImport()
	{
		// given
		final ImportConfig config = new ImportConfig();
		final ClasspathImpExResource impExResource = new ClasspathImpExResource(
				"/impex/testfiles/invalid-cell-decorator-test.impex", "UTF-8");
		config.setScript(impExResource);
		config.setSynchronous(true);
		config.setMaxThreads(16);
		config.setFailOnError(true);

		// when
		final ImportResult result = importService.importData(config);

		// then
		assertThat(result.isError()).isTrue();
		assertThat(result.getCronJob().getValueCount()).isEqualTo(4);
		assertThat(result.hasUnresolvedLines()).isTrue();
	}

	
	@Test
	public void testDumpAfterErrorInCreationSingleThreaded() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(1, false);
	}

	@Test
	public void testDumpAfterErrorInCreationMultiThreaded() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(10, false);
	}

	@Test
	public void testDumpAfterErrorInCreationBatchMode() throws UnsupportedEncodingException
	{
		testDumpAfterErrorInCreationInternal(1, true);
	}

	
	void testDumpAfterErrorInCreationInternal( int threads , boolean batch ) throws UnsupportedEncodingException
	{
		getOrCreateLanguage("de");
		getOrCreateLanguage("en");
		
		final ImportConfig config = new ImportConfig();
		config.setScript(
			"INSERT_UPDATE Title; code[unique=true]; name[lang=de]; name[lang=en]\n"+
			";TTT1;TTT1-de;TTT1-en;\n"+
			";TTT2;TTT2-de;TTT2-en;\n"+
			";TTT3;TTT3-de;TTT3-en;\n"
		);
		
		final Set<String> processed = new ConcurrentHashSet<>();

		final Interceptor throwErrorOnFirstCreation = new ValidateInterceptor<TitleModel>()
		{
			@Override
			public void onValidate(TitleModel model, InterceptorContext ctx) throws InterceptorException
			{
				if( ctx.isNew(model) && !processed.contains(model.getCode()))
				{
					processed.add(model.getCode());
					switch(model.getCode())
					{
						case "TTT1":
							throw new InterceptorException("Test exception - regular one for TTT1");
						case "TTT2":
							throw new IllegalStateException("Test exception - illegal state exception one for TTT2");
						case "TTT3":
							throw new RuntimeException("Test exception - runtime one for TTT3");
					}
				}
			}
		};
		final InterceptorMapping iMapping = new InterceptorMapping(TitleModel._TYPECODE, throwErrorOnFirstCreation, Collections.emptyList()); 
		
		config.setDumpingEnabled(true);
		config.setFailOnError(false);
		config.setSynchronous(true);
		config.setLegacyMode(Boolean.FALSE);
		config.setMaxThreads(threads);
		config.setDistributedImpexEnabled(batch);
		
		DefaultInterceptorRegistry intReg = (DefaultInterceptorRegistry) Registry.getApplicationContext().getBean("interceptorRegistry", InterceptorRegistry.class);
		
		intReg.registerInterceptor(iMapping);
		try
		{
			ImportResult res = importService.importData(config);
			
			assertTrue("interceptor didn't hit all expected titles", processed.containsAll(Arrays.asList("TTT1","TTT2","TTT3")));
			
			assertFalse("import still running",res.isRunning());
			assertTrue("import not finished",res.isFinished());
			assertFalse("import got error",res.isError());
			assertTrue("import wasn't successful",res.isSuccessful());
			
			assertTitleImported("TTT1", "TTT1-de", "TTT1-en");
			assertTitleImported("TTT2", "TTT2-de", "TTT2-en");
			assertTitleImported("TTT3", "TTT3-de", "TTT3-en");
		}
		finally
		{
			intReg.unregisterInterceptor(iMapping);
		}
	}
	
	void assertTitleImported(String code, String nameDE, String nameEN )
	{
		final TitleModel title = userService.getTitleForCode(code);
		assertNotNull(title);
		assertFalse("imported title was new",modelService.isNew(title));
		assertTrue("imported title was not up to date",modelService.isUpToDate(title));
		assertEquals("imported title has wrong code",code,title.getCode());
		assertEquals("imported title has wrong name (de)",nameDE,title.getName(Locale.GERMAN));
		assertEquals("imported title has wrong name (en)",nameEN,title.getName(Locale.ENGLISH));
	}
	
}
