package com.chrrissoft.permissions.common.usecases.interfaces

import com.chrrissoft.permissions.Permission

interface ResolvePermissionEnabledUseCase {
    operator fun invoke(data: Permission): Permission
}
