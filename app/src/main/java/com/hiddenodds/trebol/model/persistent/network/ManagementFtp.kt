package com.hiddenodds.trebol.model.persistent.network

import org.apache.commons.net.ftp.FTP
import org.apache.commons.net.ftp.FTPClient
import org.apache.commons.net.ftp.FTPReply
import java.io.BufferedInputStream
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class ManagementFtp @Inject constructor() {
    private var userName = "aplicacion"
    private var password = "21App19feb*"
    private val host = "ftp.trebolgroup.es"
    private val port = 21
    private val ftpClient = FTPClient()
    private val directoryWork = "/." //Path complete

    fun connect(): Boolean{
        try {
            ftpClient.connect(this.host, this.port)
            val reply = ftpClient.replyCode
            if (FTPReply.isPositiveCompletion(reply)){
                val loginRet = ftpClient.login(this.userName, this.password)
                if (loginRet){
                    ftpClient.enterLocalPassiveMode()
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE)
                    return true
                }
                else{
                    println("Login incorrect FTP")
                }
            }else{
                println("Fail connect")
            }

        }catch (ex:Exception){
            println("Connect FTP : ${ex.message}")
        }
        return false
    }

    fun close(){
        try {
            ftpClient.logout()
        }catch (io: IOException){
            println("Error FTP: ${io.message}")
        }finally {
            if (ftpClient.isConnected){
                try {
                    ftpClient.disconnect()
                }catch (ie: IOException){
                    println("Error FTP: ${ie.message}")
                }

            }
        }
    }

    fun upload(pathFile: String, nameFile: String): Boolean{
        try {
            val fileStream = BufferedInputStream(FileInputStream(pathFile))
            if (ftpClient.storeFile(nameFile, fileStream)){
                fileStream.close()
                return true
            }else{
                println("Error FTP Store")
            }
        }catch (io: IOException)
        {
            println("Error FTP Upload: ${io.message}")
        }
        return false
    }

    fun changeDirectory(): Boolean{
        try {
            ftpClient.changeWorkingDirectory(this.directoryWork)
            val directoryNow = ftpClient.printWorkingDirectory()
            if (directoryNow.compareTo(this.directoryWork) == 0){
                return true
            }else{
                println("Directory different FTP")
            }
        }catch (io: IOException){
            println("Error FTP directory: ${io.message}")
        }

        return false
    }
}