import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

internal interface CategoryApi {

    @GET("categories")
    suspend fun getCategories(
    ): CategoriesResponse
}

@Serializable
class CategoriesResponse(val count: Int, val categories: List<String>)

@OptIn(ExperimentalSerializationApi::class)
internal fun CategoryApi(
    okHttpClient: OkHttpClient,
): CategoryApi {

    val json = Json { ignoreUnknownKeys = true }
    val jsonConverterFactory = json.asConverterFactory("application/json".toMediaType())

    val retrofit = Retrofit.Builder()
        .addConverterFactory(jsonConverterFactory)
        .baseUrl("https://api.publicapis.org/")
        .client(okHttpClient)
        .build()

    return retrofit.create(CategoryApi::class.java)
}
