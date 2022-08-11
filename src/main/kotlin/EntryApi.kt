import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

internal interface EntryApi {

    @GET("entries")
    suspend fun getEntries(@Query("description") description: String): EntriesResponse
}

@Serializable
class EntriesResponse(val count: Int, val entries: List<Entry>?)

@Serializable
class Entry(val API: String)

@OptIn(ExperimentalSerializationApi::class)
internal fun EntryApi(
    okHttpClient: OkHttpClient,
): EntryApi {

    val json = Json { ignoreUnknownKeys = true }
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val retrofit = Retrofit.Builder()
        .addConverterFactory(jsonConverterFactory)
        .baseUrl("https://api.publicapis.org/")
        .client(okHttpClient)
        .build()

    return retrofit.create(EntryApi::class.java)
}
