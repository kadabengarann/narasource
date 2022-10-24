package id.co.mka.narasource.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.narasource.core.data.IUserRepository
import id.co.mka.narasource.core.data.UserRepository

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideUserRepository(userRepository: UserRepository): IUserRepository
}
