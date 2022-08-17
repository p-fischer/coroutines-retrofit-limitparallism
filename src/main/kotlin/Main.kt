import kotlinx.coroutines.*

object CoroutineDispatcherConfig {
    const val ioDispatcherLimit = 64
}

fun main() {

    val okHttpClient = okHttpClient()
    val retrofitApi = RetrofitWebserviceApi(okHttpClient)
    val networkApi = NetworkApi(retrofitApi)

    runBlocking {
        coroutineScope {
            launch {
                (1..10).map { index -> async { networkApi.getEntries(index.toString()) } }.awaitAll()
                print("Finished")
            }
            launch {
                networkApi.getCategories()
            }
        }
    }
}
