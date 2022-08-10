import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

internal fun okHttpClient(
    threadPoolExecutor: ThreadPoolExecutor?,
) =
    OkHttpClient.Builder().apply {
        addInterceptor(LoggingInterceptor())

        connectionPool(ConnectionPool(ConnectionPoolConfig.maxIdleConnections, 5, TimeUnit.MINUTES))

        if (threadPoolExecutor != null) {
            dispatcher(Dispatcher(threadPoolExecutor))
        }
    }
        .build()
