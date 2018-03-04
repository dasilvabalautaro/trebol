package com.hiddenodds.trebolv2.presentation.view.activities

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.view.fragments.SignInFragment
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.set

class MainActivity : AppCompatActivity() {
    @BindView(R.id.app_bar)
    @JvmField var appBarLayout: AppBarLayout? = null

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

    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }else{
            (App.appComponent.context() as App).serviceRemote.closeConnection()
            val prefs = PreferenceHelper.customPrefs(this,
                    Constants.PREFERENCE_TREBOL)
            prefs[Constants.TECHNICAL_KEY] = ""
            finish()
            android.os.Process.killProcess(android.os.Process.myPid())
        }


    }

    @SuppressLint("PrivateResource")
    fun addFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.design_bottom_sheet_slide_in,
                        R.anim.design_bottom_sheet_slide_out)
                .replace(R.id.flContent, fragment, fragment.javaClass.simpleName)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
    }

}

