package com.hiddenodds.trebolv2.model.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey


open class Customer: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var name: String = ""
    var phone: String = ""
    var email: String = ""
    var tech: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        tech = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(tech)
        return parcel
    }

}