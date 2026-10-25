package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.BetouAiMessageEntity
import com.example.data.model.CharacterEntity
import com.example.data.model.ChatMessageEntity
import com.example.data.model.CoinTransactionEntity
import com.example.data.model.CommentEntity
import com.example.data.model.FollowEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.PostEntity
import com.example.data.model.PostLikeEntity
import com.example.data.model.ProjectEntity
import com.example.data.model.ReportEntity
import com.example.data.model.UserEntity
import com.example.data.model.WardrobeItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getFirstUser(): UserEntity?

    @Query("SELECT * FROM users ORDER BY createdAt DESC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE username LIKE '%' || :query || '%' OR displayName LIKE '%' || :query || '%'")
    fun searchUsers(query: String): Flow<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("UPDATE users SET coinsBalance = coinsBalance + :delta WHERE id = :userId")
    suspend fun updateCoins(userId: String, delta: Int)

    @Query("UPDATE users SET avatarUri = :avatarUri WHERE id = :userId")
    suspend fun updateUserAvatar(userId: String, avatarUri: String)

    @Query("UPDATE users SET isVerified = :isVerified WHERE id = :userId")
    suspend fun setVerification(userId: String, isVerified: Boolean)

    @Query("UPDATE users SET isBanned = :isBanned WHERE id = :userId")
    suspend fun setBanned(userId: String, isBanned: Boolean)

    @Query("UPDATE users SET followersCount = followersCount + :delta WHERE id = :userId")
    suspend fun updateFollowers(userId: String, delta: Int)

    @Query("UPDATE users SET followingCount = followingCount + :delta WHERE id = :userId")
    suspend fun updateFollowing(userId: String, delta: Int)

    @Query("UPDATE users SET passwordHash = :newPasswordHash WHERE id = :userId")
    suspend fun updatePassword(userId: String, newPasswordHash: String)

    @Query("UPDATE users SET isEmailVerified = :verified, emailVerificationCode = NULL WHERE id = :userId")
    suspend fun setEmailVerified(userId: String, verified: Boolean)

    @Query("UPDATE users SET emailVerificationCode = :code WHERE id = :userId")
    suspend fun setVerificationCode(userId: String, code: String?)

    @Query("UPDATE users SET resetToken = :token, resetTokenExpiry = :expiry WHERE id = :userId")
    suspend fun setResetToken(userId: String, token: String?, expiry: Long)

    @Query("UPDATE users SET passwordHash = :newPasswordHash, resetToken = NULL, resetTokenExpiry = 0 WHERE id = :userId")
    suspend fun resetPassword(userId: String, newPasswordHash: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: String)
}

@Dao
interface PostDao {
    @Query("SELECT * FROM posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUser(userId: String): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE id = :id LIMIT 1")
    suspend fun getPostById(id: String): PostEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Query("DELETE FROM posts WHERE id = :id")
    suspend fun deletePost(id: String)

    @Query("UPDATE posts SET likesCount = likesCount + :delta, isLikedByMe = :isLiked WHERE id = :postId")
    suspend fun updateLikes(postId: String, delta: Int, isLiked: Boolean)

    @Query("UPDATE posts SET isBookmarked = :isBookmarked WHERE id = :postId")
    suspend fun updateBookmark(postId: String, isBookmarked: Boolean)

    @Query("UPDATE posts SET commentsCount = commentsCount + 1 WHERE id = :postId")
    suspend fun incrementComments(postId: String)
}

@Dao
interface PostLikeDao {
    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    suspend fun isLiked(postId: String, userId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: PostLikeEntity)

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun removeLike(postId: String, userId: String)
}

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId ORDER BY createdAt ASC")
    fun getCommentsForPost(postId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Query("DELETE FROM comments WHERE id = :id")
    suspend fun deleteComment(id: String)
}

@Dao
interface FollowDao {
    @Query("SELECT * FROM follows WHERE followerId = :userId")
    fun getFollowing(userId: String): Flow<List<FollowEntity>>

    @Query("SELECT * FROM follows WHERE followingId = :userId")
    fun getFollowers(userId: String): Flow<List<FollowEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM follows WHERE followerId = :followerId AND followingId = :followingId)")
    suspend fun isFollowing(followerId: String, followingId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollow(follow: FollowEntity)

    @Query("DELETE FROM follows WHERE followerId = :followerId AND followingId = :followingId")
    suspend fun deleteFollow(followerId: String, followingId: String)
}

@Dao
interface ChatDao {
    @Query("""
        SELECT * FROM chat_messages 
        WHERE (senderId = :user1 AND receiverId = :user2) 
           OR (senderId = :user2 AND receiverId = :user1) 
        ORDER BY timestamp ASC
    """)
    fun getConversation(user1: String, user2: String): Flow<List<ChatMessageEntity>>

    @Query("""
        SELECT * FROM chat_messages 
        WHERE senderId = :userId OR receiverId = :userId 
        ORDER BY timestamp DESC
    """)
    fun getAllMessagesForUser(userId: String): Flow<List<ChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessageEntity)

    @Query("UPDATE chat_messages SET isRead = 1 WHERE senderId = :senderId AND receiverId = :receiverId")
    suspend fun markAsRead(senderId: String, receiverId: String)
}

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("UPDATE reports SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)
}

@Dao
interface BetouAiDao {
    @Query("SELECT * FROM betou_ai_messages WHERE userId = :userId ORDER BY timestamp ASC")
    fun getMessages(userId: String): Flow<List<BetouAiMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: BetouAiMessageEntity)

    @Query("DELETE FROM betou_ai_messages WHERE userId = :userId")
    suspend fun clearHistory(userId: String)
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getProjectsByUser(userId: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE userId = :userId ORDER BY updatedAt DESC LIMIT 5")
    fun getRecentProjects(userId: String): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProject(id: String)

    @Query("DELETE FROM projects WHERE userId = :userId")
    suspend fun deleteProjectsByUserId(userId: String)
}

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters WHERE userId = :userId ORDER BY updatedAt DESC")
    fun getCharactersByUser(userId: String): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun getCharacterById(id: String): CharacterEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacter(character: CharacterEntity)

    @Query("DELETE FROM characters WHERE id = :id")
    suspend fun deleteCharacter(id: String)
}

@Dao
interface WardrobeDao {
    @Query("SELECT * FROM wardrobe_items")
    fun getAllItems(): Flow<List<WardrobeItemEntity>>

    @Query("SELECT * FROM wardrobe_items WHERE category = :category")
    fun getItemsByCategory(category: String): Flow<List<WardrobeItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<WardrobeItemEntity>)

    @Query("UPDATE wardrobe_items SET isUnlocked = 1 WHERE id = :id")
    suspend fun unlockItem(id: String)
}

@Dao
interface CoinDao {
    @Query("SELECT * FROM coin_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactions(userId: String): Flow<List<CoinTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CoinTransactionEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    fun getNotifications(userId: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    suspend fun markAllAsRead(userId: String)
}
