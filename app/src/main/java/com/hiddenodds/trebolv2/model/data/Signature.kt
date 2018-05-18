package com.hiddenodds.trebolv2.model.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Signature: RealmObject() {
    @PrimaryKey
    var id: String = ""
    var name: String = ""
}