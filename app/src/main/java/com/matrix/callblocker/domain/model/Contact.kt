package com.matrix.callblocker.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.matrix.callblocker.ui.theme.*

@Entity
data class Contact(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phoneNumber") val phoneNumber: String,
    @PrimaryKey val id: Int? = null
)

class InvalidContactException(message: String): Exception(message)