package com.chrrissoft.permissions.common.usecases.di

import com.chrrissoft.permissions.common.usecases.classes.ResolvePermissionEnabledUseCaseImpl
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionEnabledUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ResolvePermissionEnabledUseCaseModule {
    @Binds
    abstract fun binds(impl: ResolvePermissionEnabledUseCaseImpl) : ResolvePermissionEnabledUseCase
}
