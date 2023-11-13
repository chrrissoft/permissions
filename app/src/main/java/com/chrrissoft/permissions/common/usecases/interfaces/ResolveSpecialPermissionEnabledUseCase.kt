package com.chrrissoft.permissions.common.usecases.interfaces

import com.chrrissoft.permissions.common.app.SpecialPermission

interface ResolveSpecialPermissionEnabledUseCase {
    operator fun invoke(data: SpecialPermission): SpecialPermission
}
