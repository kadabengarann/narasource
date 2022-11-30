package id.co.mka.narasource.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.narasource.core.data.UserRepository
import id.co.mka.narasource.core.data.repository.ActivityRepository
import id.co.mka.narasource.core.data.repository.ArticleRepository
import id.co.mka.narasource.core.data.repository.NotificationRepository
import id.co.mka.narasource.core.domain.repository.IActivityRepository
import id.co.mka.narasource.core.domain.repository.IArticleRepository
import id.co.mka.narasource.core.domain.repository.INotificationRepository
import id.co.mka.narasource.core.domain.repository.IUserRepository

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository

    @Binds
    abstract fun provideArticleRepository(articleRepository: ArticleRepository): IArticleRepository

    @Binds
    abstract fun provideActivityRepository(activityRepository: ActivityRepository): IActivityRepository

    @Binds
    abstract fun provideNotificationRepository(notificationRepository: NotificationRepository): INotificationRepository
}
