package cn.edu.ecust.myandroidapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import cn.edu.ecust.myandroidapp.utils.Constants

class ChatDatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, Constants.DB_NAME, null, Constants.DB_VERSION
) {
    
    override fun onCreate(db: SQLiteDatabase) {
        // 创建用户表
        db.execSQL(CREATE_USERS_TABLE)
        
        // 创建好友表
        db.execSQL(CREATE_FRIENDS_TABLE)
        
        // 创建消息表
        db.execSQL(CREATE_MESSAGES_TABLE)
        
        // 创建通知表
        db.execSQL(CREATE_NOTIFICATIONS_TABLE)
        
        // 创建匹配历史表
        db.execSQL(CREATE_MATCH_HISTORY_TABLE)
        
        // 创建用户偏好表
        db.execSQL(CREATE_USER_PREFERENCES_TABLE)
        
        // 创建举报记录表
        db.execSQL(CREATE_REPORT_RECORDS_TABLE)
        
        // 创建屏蔽用户表
        db.execSQL(CREATE_BLOCKED_USERS_TABLE)
        
        // 创建索引
        createIndexes(db)
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // 扩展用户表
            db.execSQL(ALTER_USERS_TABLE_ADD_FIELDS)
            
            // 创建新表
            db.execSQL(CREATE_MATCH_HISTORY_TABLE)
            db.execSQL(CREATE_USER_PREFERENCES_TABLE)
            db.execSQL(CREATE_REPORT_RECORDS_TABLE)
            db.execSQL(CREATE_BLOCKED_USERS_TABLE)
            
            // 创建索引
            createIndexes(db)
        }
    }
    
    private fun createIndexes(db: SQLiteDatabase) {
        // 用户表索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_username ON ${Constants.TABLE_USERS}(${Constants.COL_USERNAME})")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_users_location ON ${Constants.TABLE_USERS}(${Constants.COL_LATITUDE}, ${Constants.COL_LONGITUDE})")
        
        // 匹配历史表索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_match_history_user1 ON ${Constants.TABLE_MATCH_HISTORY}(${Constants.COL_USER_ID_1})")
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_match_history_user2 ON ${Constants.TABLE_MATCH_HISTORY}(${Constants.COL_USER_ID_2})")
        
        // 用户偏好表索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_user_preferences_user_id ON ${Constants.TABLE_USER_PREFERENCES}(${Constants.COL_PREF_USER_ID})")
        
        // 屏蔽用户表索引
        db.execSQL("CREATE INDEX IF NOT EXISTS idx_blocked_users_user_id ON ${Constants.TABLE_BLOCKED_USERS}(${Constants.COL_BLOCK_USER_ID})")
    }
    
    companion object {
        // 用户表创建语句
        private const val CREATE_USERS_TABLE = """
            CREATE TABLE ${Constants.TABLE_USERS} (
                ${Constants.COL_USER_ID} TEXT PRIMARY KEY,
                ${Constants.COL_USERNAME} TEXT UNIQUE NOT NULL,
                ${Constants.COL_PASSWORD} TEXT NOT NULL,
                ${Constants.COL_AVATAR_RES_ID} INTEGER DEFAULT 0,
                ${Constants.COL_AVATAR_PATH} TEXT DEFAULT '',
                ${Constants.COL_IS_LOGGED_IN} INTEGER DEFAULT 0,
                ${Constants.COL_LAST_LOGIN_TIME} INTEGER DEFAULT 0,
                ${Constants.COL_AGE} INTEGER DEFAULT 18,
                ${Constants.COL_GENDER} TEXT DEFAULT '',
                ${Constants.COL_LATITUDE} REAL DEFAULT 0.0,
                ${Constants.COL_LONGITUDE} REAL DEFAULT 0.0,
                ${Constants.COL_INTERESTS} TEXT DEFAULT '',
                ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                ${Constants.COL_UPDATED_AT} INTEGER DEFAULT (strftime('%s','now'))
            )
        """
        
        // 好友表创建语句
        private const val CREATE_FRIENDS_TABLE = """
            CREATE TABLE ${Constants.TABLE_FRIENDS} (
                id TEXT PRIMARY KEY,
                username TEXT NOT NULL,
                nickname TEXT DEFAULT '',
                avatar_res_id INTEGER DEFAULT 0,
                avatar_path TEXT DEFAULT '',
                is_online INTEGER DEFAULT 0,
                last_seen INTEGER DEFAULT 0,
                signature TEXT DEFAULT ''
            )
        """
        
        // 消息表创建语句
        private const val CREATE_MESSAGES_TABLE = """
            CREATE TABLE ${Constants.TABLE_MESSAGES} (
                id TEXT PRIMARY KEY,
                sender_id TEXT NOT NULL,
                receiver_id TEXT NOT NULL,
                content TEXT NOT NULL,
                message_type TEXT DEFAULT 'text',
                timestamp INTEGER DEFAULT (strftime('%s','now')),
                is_read INTEGER DEFAULT 0
            )
        """
        
        // 通知表创建语句
        private const val CREATE_NOTIFICATIONS_TABLE = """
            CREATE TABLE ${Constants.TABLE_NOTIFICATIONS} (
                id TEXT PRIMARY KEY,
                user_id TEXT NOT NULL,
                title TEXT NOT NULL,
                content TEXT NOT NULL,
                type TEXT DEFAULT 'info',
                is_read INTEGER DEFAULT 0,
                timestamp INTEGER DEFAULT (strftime('%s','now'))
            )
        """

        // 匹配历史表创建语句
        private const val CREATE_MATCH_HISTORY_TABLE = """
            CREATE TABLE ${Constants.TABLE_MATCH_HISTORY} (
                ${Constants.COL_MATCH_ID} TEXT PRIMARY KEY,
                ${Constants.COL_USER_ID_1} TEXT NOT NULL,
                ${Constants.COL_USER_ID_2} TEXT NOT NULL,
                ${Constants.COL_MATCH_TYPE} TEXT DEFAULT 'random',
                ${Constants.COL_IS_MUTUAL} INTEGER DEFAULT 0,
                ${Constants.COL_STATUS} TEXT DEFAULT 'pending',
                ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                FOREIGN KEY (${Constants.COL_USER_ID_1}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID}),
                FOREIGN KEY (${Constants.COL_USER_ID_2}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID})
            )
        """

        // 用户偏好表创建语句
        private const val CREATE_USER_PREFERENCES_TABLE = """
            CREATE TABLE ${Constants.TABLE_USER_PREFERENCES} (
                ${Constants.COL_PREF_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Constants.COL_PREF_USER_ID} TEXT NOT NULL UNIQUE,
                ${Constants.COL_MIN_AGE} INTEGER DEFAULT 18,
                ${Constants.COL_MAX_AGE} INTEGER DEFAULT 50,
                ${Constants.COL_PREFERRED_GENDER} TEXT DEFAULT 'any',
                ${Constants.COL_MAX_DISTANCE} INTEGER DEFAULT 50,
                ${Constants.COL_INTERESTS_FILTER} TEXT DEFAULT '',
                ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                ${Constants.COL_UPDATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                FOREIGN KEY (${Constants.COL_PREF_USER_ID}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID})
            )
        """

        // 举报记录表创建语句
        private const val CREATE_REPORT_RECORDS_TABLE = """
            CREATE TABLE ${Constants.TABLE_REPORT_RECORDS} (
                ${Constants.COL_REPORT_ID} TEXT PRIMARY KEY,
                ${Constants.COL_REPORTER_ID} TEXT NOT NULL,
                ${Constants.COL_REPORTED_ID} TEXT NOT NULL,
                ${Constants.COL_REASON} TEXT NOT NULL,
                ${Constants.COL_DESCRIPTION} TEXT DEFAULT '',
                ${Constants.COL_STATUS} TEXT DEFAULT 'pending',
                ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                FOREIGN KEY (${Constants.COL_REPORTER_ID}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID}),
                FOREIGN KEY (${Constants.COL_REPORTED_ID}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID})
            )
        """

        // 屏蔽用户表创建语句
        private const val CREATE_BLOCKED_USERS_TABLE = """
            CREATE TABLE ${Constants.TABLE_BLOCKED_USERS} (
                ${Constants.COL_BLOCK_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Constants.COL_BLOCK_USER_ID} TEXT NOT NULL,
                ${Constants.COL_BLOCKED_USER_ID} TEXT NOT NULL,
                ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now')),
                FOREIGN KEY (${Constants.COL_BLOCK_USER_ID}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID}),
                FOREIGN KEY (${Constants.COL_BLOCKED_USER_ID}) REFERENCES ${Constants.TABLE_USERS}(${Constants.COL_USER_ID}),
                UNIQUE(${Constants.COL_BLOCK_USER_ID}, ${Constants.COL_BLOCKED_USER_ID})
            )
        """

        // 用户表字段扩展语句
        private const val ALTER_USERS_TABLE_ADD_FIELDS = """
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_AGE} INTEGER DEFAULT 18;
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_GENDER} TEXT DEFAULT '';
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_LATITUDE} REAL DEFAULT 0.0;
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_LONGITUDE} REAL DEFAULT 0.0;
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_INTERESTS} TEXT DEFAULT '';
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_CREATED_AT} INTEGER DEFAULT (strftime('%s','now'));
            ALTER TABLE ${Constants.TABLE_USERS} ADD COLUMN ${Constants.COL_UPDATED_AT} INTEGER DEFAULT (strftime('%s','now'));
        """
