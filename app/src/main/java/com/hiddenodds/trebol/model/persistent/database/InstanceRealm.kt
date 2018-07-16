package com.hiddenodds.trebol.model.persistent.database

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.interfaces.IPersistent
import io.realm.CompactOnLaunchCallback
import io.realm.Realm
import io.realm.RealmConfiguration

class InstanceRealm(private val context: Context): IPersistent {
    override fun close() {
        Realm.getDefaultInstance().close()

    }

    private val schemaVersion: Long = 1

    override fun create() {
        Realm.init(context)
        val configuration = RealmConfiguration.Builder()
                .compactOnLaunch { totalBytes, usedBytes ->
                    val fiftyMB = (50 * 1024 * 1024).toLong()
                    return@compactOnLaunch totalBytes > fiftyMB &&
                            (usedBytes.toDouble()) / (totalBytes.toDouble()) < 0.5
                }
                .name(context.resources.getString(R.string.database))
                .schemaVersion(schemaVersion)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(configuration)

    }

}

/*
compactOnLaunch { totalBytes, usedBytes ->
    val fiftyMB = (50 * 1024 * 1024).toLong()
    return@compactOnLaunch totalBytes > fiftyMB &&
            (usedBytes.toDouble()) / (totalBytes.toDouble()) < 0.5
}*/
