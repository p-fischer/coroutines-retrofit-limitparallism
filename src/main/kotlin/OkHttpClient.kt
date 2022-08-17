import okhttp3.OkHttpClient

internal fun okHttpClient() =
    OkHttpClient.Builder().apply {
        addInterceptor(LoggingInterceptor())
    }
        .build()
