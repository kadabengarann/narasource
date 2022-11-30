package id.co.mka.narasource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.narasource.core.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideUserUseCase(userInteractor: UserInteractor): UserUseCase

    @Binds
    @Singleton
    abstract fun provideArticleUseCase(articleInteractor: ArticleInteractor): ArticleUseCase

    @Binds
    @Singleton
    abstract fun provideActivityUseCase(activityInteractor: ActivityInteractor): ActivityUseCase

    @Binds
    @Singleton
    abstract fun provideNotificationUseCase(notificationInteractor: NotificationInteractor): NotificationUseCase
}
