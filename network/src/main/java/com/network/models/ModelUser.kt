package com.network.models

data class ModelUser(
    var id: String = "",
    var name: String = "",
    var first_name: String = "",
    var email: String = "",
    var last_name: Any = Any(),
    var phone: String = "",
    var token: String = "",
    var suspend_reason: Any = Any(),
    var token_expires_at: String = "",
    var updated_at: String = "",
    var password_changed_at: Any = Any(),
    var parent_level: Any = Any(),
    var level: Int = 0,
    var is_two_factor_authentication: Int = 0,
    var roles: List<Role> = listOf(),
    var profile: ProfileObj = ProfileObj(),
    var alp_user: Any = Any(),
    var badge: String = ""
) {
    data class Role(
        var id: Int = 0,
        var name: String = "",
        var permissions: List<Any> = listOf(),
        var is_creator: Any = Any(),
        var created_at: String = ""
    )

    data class ProfileObj(
        var id: String = ""
    )
}

