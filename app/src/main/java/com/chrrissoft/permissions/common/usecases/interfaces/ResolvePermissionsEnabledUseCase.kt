package com.chrrissoft.permissions.common.usecases.interfaces

import com.chrrissoft.permissions.Permission

interface ResolvePermissionsEnabledUseCase {
    operator fun invoke(data: List<Permission>): List<Permission>
}
