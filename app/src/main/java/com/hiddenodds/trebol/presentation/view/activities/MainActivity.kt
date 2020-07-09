package com.hiddenodds.trebol.presentation.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hiddenodds.trebol.R
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.presentation.view.fragments.MenuFragment
import com.hiddenodds.trebol.presentation.view.fragments.SignInFragment
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.Constants
import com.hiddenodds.trebol.tools.PreferenceHelper
import com.hiddenodds.trebol.tools.PreferenceHelper.set
import com.hiddenodds.trebol.tools.Variables
import android.os.StrictMode
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.AppBarLayout

class MainActivity : AppCompatActivity() {

    @BindView(R.id.app_bar)
    @JvmField var appBarLayout: AppBarLayout? = null

    private val ACTION_HOME = 16908332

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportActionBar!!.setBackgroundDrawable(getDrawable(R.drawable.head_back))
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        ChangeFormat.setVariablesConnect(this)
        val signInFragment = SignInFragment()
        addFragment(signInFragment)
    }

    fun displayHome(flag: Boolean){
        supportActionBar!!.setDisplayHomeAsUpEnabled(flag)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == ACTION_HOME){
            Variables.endApp = false
            supportFragmentManager.popBackStack(null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE)

            val fragmentMenu = MenuFragment()
            addFragment(fragmentMenu)
        }

        return super.onOptionsItemSelected(item)

    }

    fun handleBackPressInThisActivity(){
        (App.appComponent.context() as App).serviceRemote.closeConnection()
        val prefs = PreferenceHelper.customPrefs(this,
                Constants.PREFERENCE_TREBOL)
        prefs[Constants.TECHNICAL_KEY] = ""
        prefs[Constants.TECHNICAL_PASSWORD] = ""
        CachingLruRepository.instance.getLru().evictAll()
        finish()
        //android.os.Process.killProcess(android.os.Process.myPid())
    }

    /*@SuppressLint("PrivateResource")
    private fun addFragment(newFragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in,
                        R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.flContent, newFragment, newFragment.javaClass.simpleName)
                .commit()
    }*/

    private fun addFragment(newFragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.flContent, newFragment)
        fragmentTransaction.commit()
    }
    override fun onDestroy() {
        super.onDestroy()
        (App.appComponent.context() as App).onTerminate()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
}

