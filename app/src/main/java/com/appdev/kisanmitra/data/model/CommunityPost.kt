package com.appdev.kisanmitra.data.model
data class CommunityPost(
    var postId: String = "",
    var userId: String = "",
    var username: String = "",
    var location: String = "",
    var description: String = "",
    var imageBase64: String = "",
    var likes: Int = 0,
    var timestamp: Long = 0,
    var commentCount: Int = 0
)