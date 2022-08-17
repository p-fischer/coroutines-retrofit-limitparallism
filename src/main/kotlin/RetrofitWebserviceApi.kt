import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

internal interface RetrofitWebserviceApi {

    @GET("entries")
    fun getEntries(@Query("description") description: String): Call<EntriesResponse>

    @GET("categories")
    fun getCategories(
    ): Call<CategoriesResponse>
}

@Serializable
class EntriesResponse(val count: Int, val entries: List<Entry>?)

@Serializable
class Entry(val API: String)

@Serializable
class CategoriesResponse(val count: Int, val categories: List<String>)

@OptIn(ExperimentalSerializationApi::class)
internal fun RetrofitWebserviceApi(
    okHttpClient: OkHttpClient,
): RetrofitWebserviceApi {

    val json = Json { ignoreUnknownKeys = true }
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val retrofit = Retrofit.Builder()
        .addConverterFactory(jsonConverterFactory)
        .baseUrl("https://api.publicapis.org/")
        .client(okHttpClient)
        .build()

    return retrofit.create(RetrofitWebserviceApi::class.java)
}
