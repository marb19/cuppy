import de.hybris.platform.servicelayer.search.FlexibleSearchQuery
import de.hybris.y2ysync.services.SyncExecutionService

def customerJob = findJob 'c4cCustomerSyncJob'
syncExecutionService.startSync(customerJob, SyncExecutionService.ExecutionMode.SYNC);

def findJob(code) {
	def fQuery = new FlexibleSearchQuery('SELECT {PK} FROM {Y2YSyncJob} WHERE {code}=?code')
	fQuery.addQueryParameter('code', code)
	flexibleSearchService.searchUnique(fQuery)
}