package com.chrrissoft.permissions.common.usecases.classes

import com.chrrissoft.permissions.common.app.SpecialPermission
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionEnabledUseCase
import com.chrrissoft.permissions.common.usecases.interfaces.ResolveSpecialPermissionsEnabledUseCase
import javax.inject.Inject

class ResolveSpecialPermissionsEnabledUseCaseImpl @Inject constructor(
    private val ResolveSpecialPermissionEnabledUseCase: ResolveSpecialPermissionEnabledUseCase
) : ResolveSpecialPermissionsEnabledUseCase {
    override fun invoke(data: List<SpecialPermission>): List<SpecialPermission> {
        return data.map { ResolveSpecialPermissionEnabledUseCase(it) }
    }
}
