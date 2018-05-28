package com.hiddenodds.trebol.model.persistent.database

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.interfaces.IPersistent
import io.realm.Realm
import io.realm.RealmConfiguration

class InstanceRealm(private val context: Context): IPersistent {
    private val schemaVersion: Long = 1

    override fun create() {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
                .compactOnLaunch()
                .name(context.resources.getString(R.string.database))
                .schemaVersion(schemaVersion)
                .deleteRealmIfMigrationNeeded()
                .build()

        Realm.setDefaultConfiguration(configuration)

    }

}