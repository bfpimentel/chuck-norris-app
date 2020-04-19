package dev.pimentel.data

import androidx.room.Room
import dev.pimentel.data.repositories.CategoriesRepository
import dev.pimentel.data.repositories.CategoriesRepositoryImpl
import dev.pimentel.data.repositories.SearchTermsRepository
import dev.pimentel.data.repositories.SearchTermsRepositoryImpl
import dev.pimentel.data.sources.CategoriesRemoteDataSource
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60L

private val networkModule = module {
    single {
        val apiUrl = androidContext().getString(R.string.news_api_url)

        OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
            .let { client ->
                Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build()
            }
    }
}

private val databaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            ChuckNorrisDatabase::class.java,
            ChuckNorrisDatabase::class.simpleName!!
        ).build()
    }
}

private val remoteDataSourceModule = module {
    single { get<Retrofit>().create(CategoriesRemoteDataSource::class.java) }
}

private val localDataSourceModule = module {
    single { get<ChuckNorrisDatabase>().categoriesLocalDataSource() }
    single { get<ChuckNorrisDatabase>().searchQueriesLocalDataSource() }
}

private val repositoryModule = module {
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get()) }
    single<SearchTermsRepository> { SearchTermsRepositoryImpl(get()) }
}

val dataModules = listOf(
    networkModule,
    databaseModule,
    remoteDataSourceModule,
    localDataSourceModule,
    repositoryModule
)
