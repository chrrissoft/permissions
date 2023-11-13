package com.chrrissoft.permissions.common.usecases.di

import com.chrrissoft.permissions.common.usecases.classes.ResolveSpecialPermissionEnabledUseCaseImpl
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionEnabledUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolveSpecialPermissionEnabledUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolveSpecialPermissionEnabledUseCaseImpl) : ResolveSpecialPermissionEnabledUseCase
}
