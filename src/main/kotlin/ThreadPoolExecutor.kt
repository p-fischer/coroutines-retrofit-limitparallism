import java.util.concurrent.SynchronousQueue
import java.util.concurrent.ThreadFactory
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

fun threadPoolExecutor(): ThreadPoolExecutor {
    val index = AtomicInteger()
    return ThreadPoolExecutor(
        ThreadPoolConfig.corePoolSize,
        ThreadPoolConfig.maxPoolSize,
        60,
        TimeUnit.SECONDS,
        SynchronousQueue(),
        ThreadFactory { runnable ->
            Thread(runnable, "Shared Thread Pool ${index.incrementAndGet()}")
        }
    )
}
