import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.time.Clock
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class LoggingInterceptor : Interceptor {
    private val logger = HttpLoggingInterceptor.Logger { println(it) }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val connection = chain.connection()

        val requestStartMessage =
            ("${currentTime()} ${currentThreadInfo()} --> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")
        logger.log(requestStartMessage)
        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logger.log("${currentTime()} ${currentThreadInfo()} <-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logger.log(
            "${currentTime()} ${currentThreadInfo()} <-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${", $bodySize body"})"
        )

        return response
    }
}
