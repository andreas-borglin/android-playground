package com.jakoon.babylon.di

import com.jakoon.babylon.BuildConfig
import com.jakoon.babylon.api.TypicodeJsonService
import com.jakoon.babylon.vm.ListPostsViewModel
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