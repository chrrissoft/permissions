package com.chrrissoft.permissions.di

import android.os.PowerManager
import androidx.core.content.getSystemService
import com.chrrissoft.permissions.PermissionApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object PowerManagerModule {
    @Provides
    fun provide(app: PermissionApp): PowerManager = app.getSystemService()!!
}
