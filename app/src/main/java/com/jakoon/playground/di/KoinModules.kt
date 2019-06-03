package com.jakoon.playground.di

import com.jakoon.playground.BuildConfig
import com.jakoon.playground.api.TypicodeJsonService
import com.jakoon.playground.vm.ListPostsViewModel
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

    viewModel { ListPostsViewModel(get()) }
}