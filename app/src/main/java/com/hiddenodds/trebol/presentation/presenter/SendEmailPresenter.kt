package com.hiddenodds.trebol.presentation.presenter

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import com.hiddenodds.trebol.presentation.model.EmailModel
import java.lang.Exception
import javax.inject.Inject

class SendEmailPresenter @Inject constructor(): BasePresenter(){

    var emailModel: EmailModel? = null

    fun executeSendEmail(){
        if (emailModel!!.whoFor.isNotEmpty()){
            val emailTO = arrayOf(emailModel!!.whoFor)
            val emailCC = emailModel!!.whoCopy.split(";").toTypedArray()
            var pathFile: Uri? = null
            if (emailModel!!.clip.isNotEmpty()){
                pathFile = ManageFile.getFile(emailModel!!.clip)
            }

            try {
                launchEmailIntent(emailTO, emailCC, pathFile)
                showMessage(context.getString(R.string.send_email))
            }catch (ae: ActivityNotFoundException){
                if (!ae.message.isNullOrEmpty()){
                    println(ae.message)
                }

            }catch (ex: Exception){
                println(ex.localizedMessage)
            }

        }else{
            showError(context.getString(R.string.address_empty))
        }
    }

    @Throws(ActivityNotFoundException::class)
    private fun launchEmailIntent(emailTo: Array<String>,
                                  emailCc: Array<String>,
                                  pathFile: Uri?){
        val emailIntent = Intent(Intent.ACTION_SEND)
        //emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, emailTo)
        emailIntent.putExtra(Intent.EXTRA_CC, emailCc)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailModel!!.subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailModel!!.message)
        if (pathFile != null){
            emailIntent.type = "application/pdf"
            emailIntent.putExtra(Intent.EXTRA_STREAM, pathFile)
        }else{
            emailIntent.type = "text/plain"
        }
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        ContextCompat.startActivity(context, emailIntent, null)
    }

}