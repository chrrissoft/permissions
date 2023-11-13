package com.chrrissoft.permissions.common.usecases.interfaces

import com.chrrissoft.permissions.common.app.SpecialPermission

interface ResolveSpecialPermissionsEnabledUseCase {
    operator fun invoke(data: List<SpecialPermission>): List<SpecialPermission>
}
