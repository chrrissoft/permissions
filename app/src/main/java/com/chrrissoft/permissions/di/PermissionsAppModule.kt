package com.chrrissoft.permissions.di

import android.content.Context
import com.chrrissoft.permissions.PermissionApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PermissionsAppModule {
    @Provides
    fun provide(@ApplicationContext ctx: Context): PermissionApp = ctx as PermissionApp
}
