package com.matrix.callblocker.data

import androidx.room.*
import com.matrix.callblocker.domain.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface BlockedContactDao {

    @Query("SELECT * FROM Contact")
    fun getContacts(): Flow<List<Contact>>

    @Query("SELECT * FROM Contact WHERE id = :id")
    suspend fun getContactById(id: Int): Contact?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM Contact WHERE phoneNumber = :phoneNumber")
    suspend fun searchContact(phoneNumber: String): List<Contact>
}