package com.hiddenodds.trebolv2.presentation.view.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebolv2.presentation.view.fragments.SignInFragment
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.set
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

class MainActivity : AppCompatActivity() {
    @BindView(R.id.app_bar)
    @JvmField var appBarLayout: AppBarLayout? = null

    private var isWarnedToClose = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportActionBar!!.setBackgroundDrawable(getDrawable(R.drawable.head_back))
        }else{
            supportActionBar!!.setBackgroundDrawable(resources
                    .getDrawable(R.drawable.head_back))
        }
        val signInFragment = SignInFragment()
        addFragment(signInFragment)

    }

  /*  override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            handleBackPressInThisActivity()
        }

    }*/

    private fun handleBackPressInThisActivity(){
        if (isWarnedToClose){
            (App.appComponent.context() as App).serviceRemote.closeConnection()
            val prefs = PreferenceHelper.customPrefs(this,
                    Constants.PREFERENCE_TREBOL)
            prefs[Constants.TECHNICAL_KEY] = ""
            prefs[Constants.TECHNICAL_PASSWORD] = ""
            CachingLruRepository.instance.getLru().evictAll()
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }else{
            isWarnedToClose = true
            toast(getString(R.string.lbl_close_app))
            launch{
                delay(2000)
                isWarnedToClose = false
            }
        }
    }

    @SuppressLint("PrivateResource")
    private fun addFragment(newFragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in,
                        R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.flContent, newFragment, newFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun Activity.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}

