package com.matrix.callblocker.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matrix.callblocker.data.BlockedContactDao
import com.matrix.callblocker.domain.model.Contact

@Database(
    entities = [Contact::class],
    version = 1
)
abstract class ContactDatabase: RoomDatabase() {

    abstract val blockedContactDao: BlockedContactDao

    companion object {
        const val DATABASE_NAME = "Contacts_db"
    }
}