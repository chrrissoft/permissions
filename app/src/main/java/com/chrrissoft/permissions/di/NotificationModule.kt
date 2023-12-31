package com.chrrissoft.permissions.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build.VERSION_CODES.O
import androidx.annotation.RequiresApi
import com.chrrissoft.permissions.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    @Provides
    fun provideNotificationManager(@ApplicationContext ctx: Context): NotificationManager {
        return ctx.getSystemService(NotificationManager::class.java)
    }

    @Provides
    @RequiresApi(O)
    fun provideGeneralChannel(): NotificationChannel {
        return NotificationChannel(
            Constants.GENERAL_CHANNEL_ID,
            Constants.GENERAL_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
    }
}
