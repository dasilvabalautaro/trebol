package com.hiddenodds.trebol.presentation.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.presentation.view.fragments.MenuFragment
import com.hiddenodds.trebol.presentation.view.fragments.SignInFragment
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.Constants
import com.hiddenodds.trebol.tools.PreferenceHelper
import com.hiddenodds.trebol.tools.PreferenceHelper.set
import com.hiddenodds.trebol.tools.Variables
import io.realm.Realm

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar!!.setBackgroundDrawable(getDrawable(R.drawable.head_back))
        }else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                supportActionBar!!.setBackgroundDrawable(this.getDrawable(R.drawable.head_back))
            }else{
                supportActionBar!!.setBackgroundDrawable(this.resources
                        .getDrawable(R.drawable.head_back))
            }
        }
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
        Realm.getDefaultInstance().close()
        finish()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    @SuppressLint("PrivateResource")
    private fun addFragment(newFragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in,
                        R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.flContent, newFragment, newFragment.javaClass.simpleName)
                .commit()
    }

    private fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}

