package by.trokay.more.sushi.di

import by.trokay.more.sushi.data.OrderSenderHelper
import by.trokay.more.sushi.data.ProductRepositoryImpl
import by.trokay.more.sushi.data.datasource.ProductsDataSourceImpl
import by.trokay.more.sushi.domain.OrderSender
import by.trokay.more.sushi.domain.datasource.ProductsDataSource
import by.trokay.more.sushi.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class MainModule {

    @Binds
    @Singleton
    abstract fun provideDataSource(ds: ProductsDataSourceImpl): ProductsDataSource

    @Binds
    @Singleton
    abstract fun provideRepo(repo: ProductRepositoryImpl): ProductsRepository

    @Binds
    abstract fun provideOrderSender(sender: OrderSenderHelper): OrderSender
}