package com.hiddenodds.trebol

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.os.LocaleList
import com.hiddenodds.trebol.dagger.AppComponent
import com.hiddenodds.trebol.dagger.AppModule
import com.hiddenodds.trebol.dagger.DaggerAppComponent
import com.hiddenodds.trebol.model.interfaces.IPersistent
import com.hiddenodds.trebol.model.persistent.network.ServiceRemote
import com.hiddenodds.trebol.tools.ConnectionNetwork
import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.*
import javax.inject.Inject

class App: Application() {
    @Inject
    lateinit var iPersistent: IPersistent
    @Inject
    lateinit var serviceRemote: ServiceRemote
    @Inject
    lateinit var connectionNetwork: ConnectionNetwork
//    @Inject
//    lateinit var localeUtils: LocaleUtils

    companion object{
        lateinit var appComponent: AppComponent
    }

    private val component: AppComponent by lazy {
        DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
        iPersistent.create()
//        localeUtils.setLocale(Locale("es"))
//        localeUtils.updateConfiguration(this,
//                baseContext.resources.configuration)

    }

    override fun onTerminate() {
        super.onTerminate()

        Realm.getDefaultInstance().close()
        val config: RealmConfiguration = Realm.getDefaultConfiguration()!!
        println(config.path + " " + Realm.getGlobalInstanceCount(config).toString())

        /*Realm.getGlobalInstanceCount(config)
        for(r:Realm in Realm.getGlobalInstanceCount(config).){

        }
        if(Realm.compactRealm(config)){
            println("Compresion realizada")
        }else{

            println(config.path + " " + Realm.getGlobalInstanceCount(config).toString())
        }*/
    }

    override fun attachBaseContext(base: Context?) {
        val locale = Locale("es")
        val context = updateLocale(base!!, locale)
        super.attachBaseContext(context)
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        localeUtils.updateConfiguration(this, newConfig)
//    }

    fun getAppComponent(): AppComponent{
        appComponent = component
        return appComponent
    }

    private fun updateLocale(context: Context, localeToSwitchTo: Locale?): Context {
        val resources: Resources = context.resources
        val configuration = resources.configuration
        val localeList = LocaleList(localeToSwitchTo)
        LocaleList.setDefault(localeList)
        configuration.setLocales(localeList)
        return context.createConfigurationContext(configuration)

    }
}