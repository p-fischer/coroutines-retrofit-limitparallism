import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class NetworkApi(
    private val retrofitWebserviceApi: RetrofitWebserviceApi,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
        .limitedParallelism(CoroutineDispatcherConfig.ioDispatcherLimit),
    // A separate IO dispatcher pool so the many calls to getEntries don't block other calls
    private val noParallelismDispatcher: CoroutineDispatcher = dispatcher.limitedParallelism(1),
) {
    /**
     * Represents an endpoint, which needs to be called with a lot of different parameters at the same time (about 1000 times).
     * It's important these calls don't block the whole thread pool.
     */
    suspend fun getEntries(description: String) = withContext(noParallelismDispatcher) {
        println("${currentTime()} ${currentThreadInfo()} -- Network API getEntries($description) start")
        retrofitWebserviceApi.getEntries(description).execute()
        println("${currentTime()} ${currentThreadInfo()} -- Network API getEntries($description) end")
    }

    /**
     * This call should not be blocked by [getEntries] calls, but be executed shortly after it is called.
     */
    suspend fun getCategories() = withContext(dispatcher) {
        println("${currentTime()} ${currentThreadInfo()} -- Network API getCategories() start")
        retrofitWebserviceApi.getCategories().execute()
        println("${currentTime()} ${currentThreadInfo()} -- Network API getCategories() end")
    }
}
