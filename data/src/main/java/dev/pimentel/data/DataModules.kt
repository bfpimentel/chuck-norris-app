package dev.pimentel.data

import androidx.room.Room
import com.squareup.moshi.Moshi
import dev.pimentel.data.repositories.CategoriesRepositoryImpl
import dev.pimentel.data.repositories.FactsRepositoryImpl
import dev.pimentel.data.repositories.SearchTermsRepositoryImpl
import dev.pimentel.data.sources.remote.CategoriesRemoteDataSource
import dev.pimentel.data.sources.remote.FactsRemoteDataSource
import dev.pimentel.domain.repositories.CategoriesRepository
import dev.pimentel.domain.repositories.FactsRepository
import dev.pimentel.domain.repositories.SearchTermsRepository
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val REQUEST_TIMEOUT = 60L

private val moshiModule = module {
    single { Moshi.Builder().build() }
}

private val networkModule = module {
    single {
        val apiUrl = androidContext().getString(R.string.api_url)

        OkHttpClient.Builder()
            .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            .build()
            .let { client ->
                Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create(get()))
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
    single { get<Retrofit>().create(FactsRemoteDataSource::class.java) }
}

private val localDataSourceModule = module {
    single { get<ChuckNorrisDatabase>().categoriesLocalDataSource() }
    single { get<ChuckNorrisDatabase>().searchTermsLocalDataSource() }
}

private val repositoryModule = module {
    single<CategoriesRepository> { CategoriesRepositoryImpl(get(), get()) }
    single<SearchTermsRepository> { SearchTermsRepositoryImpl(get()) }
    single<FactsRepository> { FactsRepositoryImpl(get()) }
}

val dataModules = listOf(
    moshiModule,
    networkModule,
    databaseModule,
    remoteDataSourceModule,
    localDataSourceModule,
    repositoryModule
)
