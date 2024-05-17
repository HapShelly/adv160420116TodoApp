import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.model.TodoDatabase


val DB_NAME = "newtododb"
//fun buildDb(context: Context): TodoDatabase {
//    val db = TodoDatabase.buildDatabase(context)
//    return db
//}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null")
        database.execSQL("ALTER TABLE todo ADD COLUMN is_done INTEGER DEFAULT 0 not null")
    }
}

fun buildDb(context: Context):TodoDatabase {
    val db = Room.databaseBuilder(context,
        TodoDatabase::class.java, DB_NAME)
        .addMigrations(MIGRATION_1_2)
        .build()

    return db
}

