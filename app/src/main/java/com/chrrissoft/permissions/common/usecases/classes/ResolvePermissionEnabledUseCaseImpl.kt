package com.chrrissoft.permissions.common.usecases.classes

import com.chrrissoft.permissions.PermissionApp
import com.chrrissoft.permissions.Util.hasPermission
import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionEnabledUseCase
import javax.inject.Inject

class ResolvePermissionEnabledUseCaseImpl @Inject constructor(
    private val app: PermissionApp,
) : ResolvePermissionEnabledUseCase {
    override fun invoke(data: Permission): Permission {
        return data.copy(enabled = app.hasPermission(data.permission))
    }
}
