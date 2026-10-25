package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
import com.example.util.SecurityUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserEntity::class,
        PostEntity::class,
        PostLikeEntity::class,
        CommentEntity::class,
        FollowEntity::class,
        ChatMessageEntity::class,
        ReportEntity::class,
        BetouAiMessageEntity::class,
        ProjectEntity::class,
        CharacterEntity::class,
        WardrobeItemEntity::class,
        CoinTransactionEntity::class,
        NotificationEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao
    abstract fun postLikeDao(): PostLikeDao
    abstract fun commentDao(): CommentDao
    abstract fun followDao(): FollowDao
    abstract fun chatDao(): ChatDao
    abstract fun reportDao(): ReportDao
    abstract fun betouAiDao(): BetouAiDao
    abstract fun projectDao(): ProjectDao
    abstract fun characterDao(): CharacterDao
    abstract fun wardrobeDao(): WardrobeDao
    abstract fun coinDao(): CoinDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ava_studio.db"
                )
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val database = getInstance(context)
                                seedInitialData(database)
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(database: AppDatabase) {
            // Seed Default Users with salted cryptographic hashes
            val founderAyham = database.userDao().getUserById("user_ayham_creator") ?: UserEntity(
                id = "user_ayham_creator",
                displayName = "Ayham",
                username = "ayham_creative",
                email = "ayham@avastudio.io",
                passwordHash = SecurityUtils.hashPassword("AyhamFounder2026!"),
                bio = "المؤسس والمصمم الرئيسي لتطبيق AVA Studio. أصنع تصاميم وهوية بصرية فخمة.",
                avatarUri = null,
                bannerUri = null,
                coinsBalance = 1500,
                favoriteStyle = "Dark Premium",
                preferredLanguage = "ar",
                isVerified = true,
                isBanned = false,
                isFounder = true,
                followersCount = 1840,
                followingCount = 42,
                isEmailVerified = true
            ).also { database.userDao().insertUser(it) }

            val creatorSara = database.userDao().getUserById("user_sara_art") ?: UserEntity(
                id = "user_sara_art",
                displayName = "Sara Visuals",
                username = "sara_art",
                email = "sara@avastudio.io",
                passwordHash = SecurityUtils.hashPassword("CreativeSara2026!"),
                bio = "مصممة بوسترات ومحررة صور سينمائية. مهتمة بإعلانات الموضة والأزياء.",
                avatarUri = null,
                bannerUri = null,
                coinsBalance = 620,
                favoriteStyle = "Luxury",
                preferredLanguage = "ar",
                isVerified = true,
                isBanned = false,
                isFounder = false,
                followersCount = 530,
                followingCount = 110,
                isEmailVerified = true
            ).also { database.userDao().insertUser(it) }

            val creatorOmar = database.userDao().getUserById("user_omar_kicks") ?: UserEntity(
                id = "user_omar_kicks",
                displayName = "Omar Street",
                username = "omar_street",
                email = "omar@avastudio.io",
                passwordHash = SecurityUtils.hashPassword("Streetwear2026!"),
                bio = "Streetwear aesthetic & sneaker visual concepts. Love AVA creative studio.",
                avatarUri = null,
                bannerUri = null,
                coinsBalance = 410,
                favoriteStyle = "Streetwear",
                preferredLanguage = "ar",
                isVerified = false,
                isBanned = false,
                isFounder = false,
                followersCount = 280,
                followingCount = 95,
                isEmailVerified = true
            ).also { database.userDao().insertUser(it) }

            // Seed Initial Posts for Feed
            val samplePosts = listOf(
                PostEntity(
                    id = "post_01",
                    userId = founderAyham.id,
                    authorName = founderAyham.displayName,
                    authorUsername = founderAyham.username,
                    isAuthorVerified = true,
                    isAuthorFounder = true,
                    content = "أطلقنا رسمياً التحديث الجديد لتطبيق AVA مع ميزات التواصل الاجتماعي ومحرر الصور المتقدم ومساعد بيتو AI! يسعدني سماع آرائكم وتصاميمكم 🖤✨",
                    imageType = "ad",
                    mediaTag = "AVA NOIR • DROP 01",
                    likesCount = 84,
                    commentsCount = 12,
                    isLikedByMe = true,
                    isBookmarked = true,
                    createdAt = System.currentTimeMillis() - 3600000L * 2
                ),
                PostEntity(
                    id = "post_02",
                    userId = creatorSara.id,
                    authorName = creatorSara.displayName,
                    authorUsername = creatorSara.username,
                    isAuthorVerified = true,
                    isAuthorFounder = false,
                    content = "تجربة جلسة تصوير بورتريه سينمائية وتعديل الإضاءة بدون المساس بالملامح الطبيعية في ستوديو الصور الشخصية. النتيجة رائعة وطبيعية 100% 📸",
                    imageType = "photo",
                    mediaTag = "PORTRAIT • NATURAL ENHANCEMENT",
                    likesCount = 47,
                    commentsCount = 6,
                    isLikedByMe = false,
                    isBookmarked = false,
                    createdAt = System.currentTimeMillis() - 3600000L * 5
                ),
                PostEntity(
                    id = "post_03",
                    userId = creatorOmar.id,
                    authorName = creatorOmar.displayName,
                    authorUsername = creatorOmar.username,
                    isAuthorVerified = false,
                    isAuthorFounder = false,
                    content = "صممت شخصيتي الافتراضية الجديدة في كولكشن الستريت وير وقميص كرة السلة. من برأيكم أفضل في التنسيق؟ 🔥👟",
                    imageType = "character",
                    mediaTag = "AVATAR • HIGH TOP KICKS",
                    likesCount = 31,
                    commentsCount = 4,
                    isLikedByMe = false,
                    isBookmarked = false,
                    createdAt = System.currentTimeMillis() - 3600000L * 9
                )
            )
            samplePosts.forEach { database.postDao().insertPost(it) }

            // Seed Comments
            val sampleComments = listOf(
                CommentEntity(
                    id = "comm_01",
                    postId = "post_01",
                    userId = creatorSara.id,
                    authorName = creatorSara.displayName,
                    authorUsername = creatorSara.username,
                    isVerified = true,
                    text = "مبروك الإطلاق يا أيهم! التصميم الأسود والبنفسجي فخم جداً 💜",
                    createdAt = System.currentTimeMillis() - 3600000L
                ),
                CommentEntity(
                    id = "comm_02",
                    postId = "post_01",
                    userId = creatorOmar.id,
                    authorName = creatorOmar.displayName,
                    authorUsername = creatorOmar.username,
                    isVerified = false,
                    text = "محرر الصور وسرعة التصدير فرقت بشكل خيالي. برافو!",
                    createdAt = System.currentTimeMillis() - 1800000L
                )
            )
            sampleComments.forEach { database.commentDao().insertComment(it) }

            // Seed Follows
            database.followDao().insertFollow(FollowEntity("user_sara_art_user_ayham_creator", creatorSara.id, founderAyham.id))
            database.followDao().insertFollow(FollowEntity("user_omar_kicks_user_ayham_creator", creatorOmar.id, founderAyham.id))

            // Seed Chat Messages
            database.chatDao().insertMessage(
                ChatMessageEntity(
                    id = "msg_01",
                    senderId = creatorSara.id,
                    receiverId = founderAyham.id,
                    senderName = creatorSara.displayName,
                    senderUsername = creatorSara.username,
                    text = "مرحباً أيهم، هل يمكنك إلقاء نظرة على تصميم البوستر الجديد؟",
                    timestamp = System.currentTimeMillis() - 7200000L,
                    isRead = true
                )
            )
            database.chatDao().insertMessage(
                ChatMessageEntity(
                    id = "msg_02",
                    senderId = founderAyham.id,
                    receiverId = creatorSara.id,
                    senderName = founderAyham.displayName,
                    senderUsername = founderAyham.username,
                    text = "أهلاً سارة، بكل سرور! التباين والخط اللاتيني رائعان جداً.",
                    timestamp = System.currentTimeMillis() - 3600000L,
                    isRead = true
                )
            )

            // Seed Betou AI Initial messages
            database.betouAiDao().insertMessage(
                BetouAiMessageEntity(
                    id = "ai_msg_01",
                    userId = founderAyham.id,
                    isUser = false,
                    text = "مرحباً بك! أنا بيتو AI، مساعدك الإبداعي والذكي في AVA. يمكنني مساعدتك في توليد نصوص إعلانية جذابة، اقتراح فلاتر وتنسيقات ألوان، أو تحسين جودة الصور والتصاميم. ماذا تريد أن نبتكر اليوم؟",
                    promptSuggestion = "اقترح عنواناً فخماً لإعلان أزياء شتوية",
                    timestamp = System.currentTimeMillis()
                )
            )

            // Seed Sample Projects
            val sampleProjects = listOf(
                ProjectEntity(
                    id = "proj_01",
                    userId = founderAyham.id,
                    title = "AURA Midnight Streetwear Ad",
                    projectType = "advertisement",
                    aspectRatio = "4:5",
                    layersJson = "{\"layers\":[{\"text\":\"AURA STUDIOS\",\"color\":\"#FFFFFF\"},{\"text\":\"DROP 01 - MIDNIGHT\",\"color\":\"#A78BFA\"}]}"
                ),
                ProjectEntity(
                    id = "proj_02",
                    userId = founderAyham.id,
                    title = "Cinematic Studio Portrait",
                    projectType = "personal_photo",
                    aspectRatio = "1:1",
                    layersJson = "{\"adjustments\":{\"brightness\":5,\"contrast\":12,\"sharpness\":18,\"vignette\":10}}"
                )
            )
            sampleProjects.forEach { database.projectDao().insertProject(it) }

            // Seed Character
            val sampleCharacter = CharacterEntity(
                id = "char_01",
                userId = founderAyham.id,
                name = "Aero",
                gender = "male",
                skinToneHex = "#D08B5B",
                faceShape = "sharp",
                hairStyle = "textured_crop",
                hairColorHex = "#1E1B2E",
                eyeStyle = "focused",
                eyeColorHex = "#2E1C4E",
                topWear = "streetwear_hoodie",
                topColorHex = "#7C3AED",
                bottomWear = "cargo_pants",
                bottomColorHex = "#181524",
                shoes = "high_top_kicks",
                accessory = "gold_chain",
                pose = "confident",
                backdrop = "studio_dark",
                isCurrentAvatar = true
            )
            database.characterDao().insertCharacter(sampleCharacter)

            // Seed Notifications
            database.notificationDao().insertNotification(
                NotificationEntity(
                    id = "notif_01",
                    userId = founderAyham.id,
                    titleEn = "New Like on your post",
                    titleAr = "إعجاب جديد على منشورك",
                    messageEn = "Sara Art liked your post: 'AVA NOIR • DROP 01'",
                    messageAr = "أبدت سارة إعجابها بمنشورك: 'AVA NOIR • DROP 01'",
                    type = "like"
                )
            )
            database.notificationDao().insertNotification(
                NotificationEntity(
                    id = "notif_02",
                    userId = founderAyham.id,
                    titleEn = "Daily Reward Available",
                    titleAr = "المكافأة اليومية متاحة",
                    messageEn = "Claim your daily +50 AVA Coins in the studio wallet.",
                    messageAr = "استلم 50 عملة AVA يومية من محفظتك.",
                    type = "reward"
                )
            )
        }
    }
}
