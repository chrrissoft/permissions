package com.chrrissoft.permissions.common.usecases.classes

import com.chrrissoft.permissions.Permission
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionEnabledUseCase
import com.chrrissoft.permissions.common.usecases.interfaces.ResolvePermissionsEnabledUseCase
import javax.inject.Inject

class ResolvePermissionsEnabledUseCaseImpl @Inject constructor(
    private val ResolvePermissionEnabledUseCase: ResolvePermissionEnabledUseCase,
) : ResolvePermissionsEnabledUseCase {
    override fun invoke(data: List<Permission>): List<Permission> {
        return data.map { ResolvePermissionEnabledUseCase(it) }
    }
}
