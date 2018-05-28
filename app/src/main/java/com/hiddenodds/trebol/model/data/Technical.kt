package com.hiddenodds.trebol.model.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Technical: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var trd: RealmList<String> = RealmList()
    var notifications: RealmList<Notification> = RealmList()

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        password = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)

        return parcel
    }

}