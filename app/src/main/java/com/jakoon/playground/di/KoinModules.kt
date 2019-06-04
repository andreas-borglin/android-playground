package com.jakoon.playground.di

import com.jakoon.playground.BuildConfig
import com.jakoon.playground.data.cache.Cache
import com.jakoon.playground.data.cache.InMemoryCache
import com.jakoon.playground.data.network.TypicodeJsonService
import com.jakoon.playground.presentation.list.ListPostsViewModel
import com.jakoon.playground.repository.Repository
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {
    single<TypicodeJsonService> {
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        retrofit.create(TypicodeJsonService::class.java)
    }

    single<Cache> {
        InMemoryCache()
    }

    single {
        Repository(get(), get())
    }

    viewModel { ListPostsViewModel(get()) }
}