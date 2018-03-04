package com.hiddenodds.trebolv2.model.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class TypeNotification: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var description: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        description = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(description)
        return parcel
    }

}