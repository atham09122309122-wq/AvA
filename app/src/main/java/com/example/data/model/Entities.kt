package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val bio: String = "Digital Creator & Visual Artist at AVA Studio",
    val avatarUri: String? = null,
    val bannerUri: String? = null,
    val coinsBalance: Int = 500,
    val favoriteStyle: String = "Dark Premium",
    val preferredLanguage: String = "ar", // Default to Arabic as requested, toggles seamlessly
    val isVerified: Boolean = false,
    val isBanned: Boolean = false,
    val isFounder: Boolean = false,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUri: String? = null,
    val isAuthorVerified: Boolean = false,
    val isAuthorFounder: Boolean = false,
    val content: String,
    val imageType: String? = null, // "ad", "photo", "character", "design", null
    val mediaTag: String? = null,
    val likesCount: Int = 0,
    val commentsCount: Int = 0,
    val isLikedByMe: Boolean = false,
    val isBookmarked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "post_likes")
data class PostLikeEntity(
    @PrimaryKey val id: String, // "${postId}_${userId}"
    val postId: String,
    val userId: String
)

@Entity(tableName = "comments")
data class CommentEntity(
    @PrimaryKey val id: String,
    val postId: String,
    val userId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUri: String? = null,
    val isVerified: Boolean = false,
    val text: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "follows")
data class FollowEntity(
    @PrimaryKey val id: String, // "${followerId}_${followingId}"
    val followerId: String,
    val followingId: String,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey val id: String,
    val senderId: String,
    val receiverId: String,
    val senderName: String,
    val senderUsername: String,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val reporterId: String,
    val targetType: String, // "post", "user"
    val targetId: String,
    val reason: String,
    val details: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "pending" // "pending", "resolved", "dismissed"
)

@Entity(tableName = "betou_ai_messages")
data class BetouAiMessageEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val isUser: Boolean,
    val text: String,
    val promptSuggestion: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val projectType: String, // advertisement, personal_photo, image_editor, story
    val aspectRatio: String = "1:1",
    val thumbnailUri: String? = null,
    val layersJson: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val name: String,
    val gender: String = "male",
    val skinToneHex: String = "#E0AC69",
    val faceShape: String = "sharp",
    val hairStyle: String = "textured_crop",
    val hairColorHex: String = "#1A1A1A",
    val eyeStyle: String = "focused",
    val eyeColorHex: String = "#4A3728",
    val topWear: String = "streetwear_hoodie",
    val topColorHex: String = "#8B5CF6",
    val bottomWear: String = "cargo_pants",
    val bottomColorHex: String = "#1E1B2E",
    val shoes: String = "high_top_kicks",
    val accessory: String = "gold_chain",
    val pose: String = "confident",
    val backdrop: String = "studio_dark",
    val isCurrentAvatar: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "wardrobe_items")
data class WardrobeItemEntity(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameAr: String,
    val category: String, // tops, bottoms, shoes, accessories, outfits
    val styleTag: String, // streetwear, basketball, y2k, premium, minimal
    val priceCoins: Int,
    val isUnlocked: Boolean = false,
    val iconName: String = "check"
)

@Entity(tableName = "coin_transactions")
data class CoinTransactionEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val titleEn: String,
    val titleAr: String,
    val amount: Int,
    val timestamp: Long = System.currentTimeMillis(),
    val transactionType: String = "reward"
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val titleEn: String,
    val titleAr: String,
    val messageEn: String,
    val messageAr: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: String = "info"
)
