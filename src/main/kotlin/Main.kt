import kotlinx.coroutines.*

object ThreadPoolConfig {
    const val corePoolSize = 2
    const val maxPoolSize = Int.MAX_VALUE
}

object OkHttpDispatcherConfig {
    const val maxParallelRequests = 7
}

object ConnectionPoolConfig {
    const val maxIdleConnections = 3
}

object CoroutineDispatcherConfig {
    const val ioDispatcherLimit = 64
}

fun main() {

    val entryApi = EntryApi(okHttpClient(threadPoolExecutor()))
    val categoryApi = CategoryApi(okHttpClient(threadPoolExecutor()))
    val networkApi = NetworkApi(categoryApi, entryApi)

    runBlocking {
        coroutineScope {
            launch {
                (1..10).map { index -> async { networkApi.getEntries(index.toString()) } }.awaitAll()
            }
            launch {
                networkApi.getCategories()
            }
        }
    }
}
