package com.mkitsimple.counterboredom.di.modules

import com.mkitsimple.counterboredom.data.network.Api
import com.mkitsimple.counterboredom.data.repositories.NotificationRepository
import com.mkitsimple.counterboredom.di.scopes.AppScope
import com.mkitsimple.counterboredom.util.BASE_URL
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class AppModule {

    @AppScope
    @Provides
    fun provideRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @AppScope
    @Provides
    fun provideRepository(api: Api) = NotificationRepository(api)
}