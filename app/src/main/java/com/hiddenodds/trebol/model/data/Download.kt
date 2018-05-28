package com.hiddenodds.trebol.model.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Download: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var notification: String = ""
    var customer: String = ""
    var state: Byte = 0

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        notification = parcel.readString()
        customer = parcel.readString()
        state = parcel.readByte()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(notification)
        parcel.writeString(customer)
        parcel.writeByte(state)

        return parcel
    }

}