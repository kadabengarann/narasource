package id.co.mka.narasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.narasource.core.domain.usecase.ActivityInteractor
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase
import id.co.mka.narasource.core.domain.usecase.UserInteractor
import id.co.mka.narasource.core.domain.usecase.UserUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

    @Binds
    @Singleton
    abstract fun provideActivityUseCase(activityInteractor: ActivityInteractor): ActivityUseCase
}
