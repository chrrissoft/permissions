package com.chrrissoft.permissions.common.usecases.di

import com.chrrissoft.permissions.common.usecases.classes.ResolveSpecialPermissionsEnabledUseCaseImpl
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionsEnabledUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveSpecialPermissionsEnabledUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveSpecialPermissionsEnabledUseCaseImpl) : ResolveSpecialPermissionsEnabledUseCase
}
