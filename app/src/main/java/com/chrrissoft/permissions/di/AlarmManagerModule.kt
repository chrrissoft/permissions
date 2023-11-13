package com.chrrissoft.permissions.di

import android.app.AlarmManager
import androidx.core.content.getSystemService
import com.chrrissoft.permissions.PermissionApp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AlarmManagerModule {
    @Provides
    fun provide(app: PermissionApp): AlarmManager = app.getSystemService()!!
}
