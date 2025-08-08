package com.example.new_feature_android.models

data class UserInfo(
    val id: String,
    val name: String,
    val email: String,
    val profilePictureUrl: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "email" to email,
            "profilePictureUrl" to profilePictureUrl
        )
    }

    companion object {
        fun getDummyUserList(): List<UserInfo> = listOf(
            UserInfo("1", "John Doe", "john.doe@example.com", "https://example.com/profile/john.jpg"),
            UserInfo("2", "Jane Smith", "jane.smith@example.com", "https://example.com/profile/jane.jpg"),
            UserInfo("3", "Kim", "chulsoo.kim@example.com", "https://example.com/profile/chulsoo.jpg"),
            UserInfo("4", "Lee", "younghee.lee@example.com", null),
            UserInfo("5", "Park", "jimin.park@example.com", "https://example.com/profile/jimin.jpg")
        )
    }
}
