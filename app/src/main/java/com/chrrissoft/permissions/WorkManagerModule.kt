package com.chrrissoft.permissions

import android.content.Context
import androidx.work.*
import androidx.work.OutOfQuotaPolicy.DROP_WORK_REQUEST
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import com.chrrissoft.permissions.location.BackgroundLocationPermissionWorker as Worker

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {
    @Provides
    fun provide(@ApplicationContext ctx: Context): WorkManager {
        println("-------------- ${ctx is PermissionApp} ----------------")
        return WorkManager.getInstance(ctx)
    }

    @Provides
    @AccessBackgroundLocationRequest
    fun provideAccessBackgroundLocationRequest(@ApplicationContext ctx: Context): OneTimeWorkRequest {
        println("-------------- ${ctx is PermissionApp} ----------------")
        return OneTimeWorkRequestBuilder<Worker>()
            .build()
    }

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class AccessBackgroundLocationRequest
}
