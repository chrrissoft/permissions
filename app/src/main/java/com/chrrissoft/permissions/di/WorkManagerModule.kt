package com.chrrissoft.permissions.di

import android.content.Context
import androidx.work.*
import androidx.work.WorkManager.getInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import com.chrrissoft.permissions.common.app.BackgroundLocationPermissionWorker as Worker

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {
    @Provides
    fun provide(@ApplicationContext ctx: Context): WorkManager {
        return getInstance(ctx)
    }

    @Provides
    @AccessBackgroundLocationRequest
    fun provideAccessBackgroundLocationRequest(): OneTimeWorkRequest {
        return OneTimeWorkRequestBuilder<Worker>()
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AccessBackgroundLocationRequest
}
