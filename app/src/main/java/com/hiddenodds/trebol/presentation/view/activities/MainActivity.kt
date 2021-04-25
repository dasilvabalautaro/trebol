package com.hiddenodds.trebol.presentation.view.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.appbar.AppBarLayout
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

class MainActivity : AppCompatActivity() { //ActivityCompat.OnRequestPermissionsResultCallback

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.app_bar)
    @JvmField var appBarLayout: AppBarLayout? = null

    private val actionHome = 16908332
    //private val permissionRequestCode = 2296

    /*private val startForResult = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            Toast.makeText(this, "The permissions have be accepted!", Toast.LENGTH_SHORT).show()
            
        }
    }*/
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        /*if (!checkPermission()){
            requestPermission()
        }
        else {
            Toast.makeText(this, "Accept permission for storage access!", Toast.LENGTH_SHORT).show()
        }*/

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        supportActionBar!!.setBackgroundDrawable(
                ContextCompat.getDrawable(
                        this, R.drawable.head_back))
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        ChangeFormat.setVariablesConnect(this)
        val signInFragment = SignInFragment()
        addFragment(signInFragment)
    }

    fun displayHome(flag: Boolean){
        supportActionBar!!.setDisplayHomeAsUpEnabled(flag)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == actionHome){
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

   /* private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val readExternal = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
            val writeExternal = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
            readExternal == PackageManager.PERMISSION_GRANTED && writeExternal == PackageManager.PERMISSION_GRANTED
        }
    }*/

    /*private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = Uri.parse(String.format("package:%s", applicationContext.packageName))
                startForResult.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                startForResult.launch(intent)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(this, arrayOf(WRITE_EXTERNAL_STORAGE), permissionRequestCode)
        }

    }*/
    
   /* override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionRequestCode -> if (grantResults.isNotEmpty()) {
                
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && 
                        grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Accept permission for storage access!", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "Allow permission for storage access!", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }*/
}

