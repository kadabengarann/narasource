package id.co.mka.naraq.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.naraq.core.domain.usecase.UserInteractor
import id.co.mka.naraq.core.domain.usecase.UserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase
}
