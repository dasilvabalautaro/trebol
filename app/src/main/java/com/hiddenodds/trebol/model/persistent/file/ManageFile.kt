package com.hiddenodds.trebol.model.persistent.file

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.hiddenodds.trebol.App
import java.io.File

object ManageFile {
    private val DIRECTORY_WORK = "SignatureTrebol"
    private var context: Context = App.appComponent.context()

    fun deleteDirectoryOfWork(){
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_WORK)

//        val file = File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), DIRECTORY_WORK)
        if (file.isDirectory) {
            val files = file.listFiles()
            if (files != null) {
                for (f in files) {
                    if (f.delete()) {
                        println("Delete OK")
                    }
                }
            }
        }
    }

    fun isFileExist(file: String): Boolean{
        val fileFind = File(getAlbumStorageDir(), file)
        if (fileFind.isFile){
            return true
        }
        return false
    }

    fun getFile(file: String): Uri?{
        val fileFind = File(getAlbumStorageDir(), file)
        if (fileFind.isFile){
            return Uri.fromFile(fileFind)
        }
        return null
    }

    fun deleteFileOfWork(code: String){
        val PRE_FIX = "end"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_WORK)

        if (file.isDirectory) {
            val files = file.listFiles()
            files?.asSequence()?.filter {
                (it.name == "$code.jpg" || it.name == "$code.pdf"
                        || it.name == "$code$PRE_FIX.pdf")
                        && it.delete() }?.forEach { _ -> println("Delete OK") }
        }
    }

    fun getAlbumStorageDir(): File {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_WORK)

        if (!file.mkdirs()) {
            println("Directory not created")
        }
        return file
    }


}