package co.geisyanne.meuapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "group_table")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true) val groupId: Long = 0,
    @ColumnInfo(name = "name") val name: String,

    ) : Parcelable {
    @IgnoredOnParcel
    @Ignore var selected: Boolean = false
}

