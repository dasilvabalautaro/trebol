package com.hiddenodds.trebol.model.persistent.file

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import com.hiddenodds.trebol.App
import java.io.File

@SuppressLint("StaticFieldLeak")
object ManageFile {
    private const val directoryWork = "FolderWork"
    private var context: Context = App.appComponent.context()

    fun deleteDirectoryOfWork(){
        val dir = getAlbumStorageDir()
        val files = dir.listFiles()
        if (files != null) {
            for (f in files) {
                if (f.delete()) {
                    println("Delete OK")
                }
            }
        }
    }

    fun isFileExist(file: String): Boolean{
        val fileFind = File(getAlbumStorageDir(), file)
        if (fileFind.exists()){
            return true
        }
        return false
    }

    fun getFile(file: String): Uri?{
        val fileFind = File(getAlbumStorageDir(), file)
        if (fileFind.exists()){
            return Uri.fromFile(fileFind)
        }
        return null
    }

    fun getFileSingle(file: String): File?{
        val fileFind = File(getAlbumStorageDir(), file)
        if (fileFind.exists()){
            return fileFind
        }
        return null
    }

    fun deleteFileOfWork(code: String){
        val prefix = "end"
        val dir = getAlbumStorageDir()
        val files = dir.listFiles()
        files?.asSequence()?.filter {
            (it.name == "$code.jpg" || it.name == "$code.pdf"
                    || it.name == "$code$prefix.pdf")
                    && it.delete() }?.forEach { _ -> println("Delete OK") }
    }

    fun getAlbumStorageDir(): File {
        val dir = File(context.filesDir, directoryWork)
        return if (!dir.exists()){
            dir.mkdirs()
            dir
        }
        else{
            dir
        }
    }

    fun getAlbumStorageDirUri(): Uri{
        return Uri.fromFile(getAlbumStorageDir())
    }
}