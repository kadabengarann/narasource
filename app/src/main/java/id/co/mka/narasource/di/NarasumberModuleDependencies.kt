package id.co.mka.narasource.di

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.co.mka.narasource.core.domain.usecase.ActivityUseCase

@EntryPoint
@InstallIn(SingletonComponent::class)
interface NarasumberModuleDependencies {
    fun activityUseCase(): ActivityUseCase
}
