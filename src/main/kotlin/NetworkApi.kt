import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.HttpException

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
    suspend fun getEntries(description: String) =
        retrofitWebserviceApi.getEntries(description)
            .executeWithDispatcher(noParallelismDispatcher, "getEntries($description)")

    /**
     * This call should not be blocked by [getEntries] calls, but be executed shortly after it is called.
     */
    suspend fun getCategories() =
        retrofitWebserviceApi.getCategories().executeWithDispatcher(dispatcher, "getCategories()")

    private suspend fun <T> Call<T>.executeWithDispatcher(dispatcher: CoroutineDispatcher, operationName: String): T =
        withContext(dispatcher)
        {
            println("${currentTime()} ${currentThreadInfo()} -- Network API $operationName start")
            val response = execute()
            println("${currentTime()} ${currentThreadInfo()} -- Network API $operationName end")
            if (response.isSuccessful) {
                checkNotNull(response.body())
            } else {
                throw HttpException(response)
            }
        }
}
