import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.withContext

internal class NetworkApi(
    private val retrofitWebserviceApi: RetrofitWebserviceApi,
    private val dispatcher: CoroutineDispatcher = newFixedThreadPoolContext(
        CoroutineDispatcherConfig.ioDispatcherLimit,
        "Background Dispatcher"
    ),
    // A separate IO dispatcher pool so the many calls to getEntries don't block other calls
    private val noParallelismDispatcher: CoroutineDispatcher = newSingleThreadContext(
        "Single Thread Dispatcher"
    ),
) {
    /**
     * Represents an endpoint, which needs to be called with a lot of different parameters at the same time (about 1000 times).
     * It's important these calls don't block the whole thread pool.
     */
    suspend fun getEntries(description: String) = withContext(noParallelismDispatcher) {
        println("Network API getEntries() start ${Thread.currentThread()}")
        retrofitWebserviceApi.getEntries(description)
        println("Network API getEntries() end ${Thread.currentThread()}")
    }

    /**
     * This call should not be blocked by [getEntries] calls, but be executed shortly after it is called.
     */
    suspend fun getCategories() = withContext(dispatcher) {
        println("Network API getCategories() start ${Thread.currentThread()}")
        retrofitWebserviceApi.getCategories()
        println("Network API getCategories() end ${Thread.currentThread()}")
    }
}
