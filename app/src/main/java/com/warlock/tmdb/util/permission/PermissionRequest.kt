package com.warlock.tmdb.util.permission


class PermissionRequest(
    var requestCode: Int? = null,
    var resultCallback: (PermissionResult.() -> Unit)? = null
)