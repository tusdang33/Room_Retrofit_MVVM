package com.kaizm.learnmvvmandbinding.data.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.kaizm.learnmvvmandbinding.common.Const.API_URL
import com.kaizm.learnmvvmandbinding.common.Const.AUTHORIZATION_KEY
import com.kaizm.learnmvvmandbinding.common.Const.PREFIX_HEADER
import com.kaizm.learnmvvmandbinding.common.Const.PREFS_KEY
import com.kaizm.learnmvvmandbinding.common.SaveToken
import com.kaizm.learnmvvmandbinding.data.repository.retrofit.ProductRepository
import com.kaizm.learnmvvmandbinding.data.repository.retrofit.ProductRepositoryImpl
import com.kaizm.learnmvvmandbinding.data.repository.room.AppDatabase
import com.kaizm.learnmvvmandbinding.data.repository.room.CartRepository
import com.kaizm.learnmvvmandbinding.data.repository.room.CartRepositoryImpl
import com.kaizm.learnmvvmandbinding.data.service.DatabaseService
import com.kaizm.learnmvvmandbinding.data.service.ProductService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDAO(appDatabase: AppDatabase): DatabaseService = appDatabase.getDao()

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "Database").build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(client).build()
    }

    @Singleton
    @Provides
    fun provideProductService(retrofit: Retrofit): ProductService =
        retrofit.create(ProductService::class.java)

    @Singleton
    @Provides
    fun provideProductRepository(productService: ProductService): ProductRepository =
        ProductRepositoryImpl(productService = productService)

    @Singleton
    @Provides
    fun provideCartRepository(databaseService: DatabaseService): CartRepository =
        CartRepositoryImpl(databaseService)

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)


    @Singleton
    @Provides
    fun provideOkHttp(saveToken: SaveToken): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val token = saveToken.getToken()?.let { token ->
                "$PREFIX_HEADER $token"
            }
            val request = chain.request().let {
                if (token == null) {
                    it
                } else {
                    it.newBuilder().addHeader(AUTHORIZATION_KEY, token).build()
                }
            }
            chain.proceed(request)
        }.dispatcher(Dispatcher().apply { maxRequests = 1 }).build()
    }
}