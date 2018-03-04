package com.hiddenodds.trebolv2.model.persistent.network

import com.hiddenodds.trebolv2.tools.Constants

object StatementSQL {

    fun getTechnical(): String{
        return "SELECT * FROM " + Constants.TBL_TEAM_TECHNICAL
    }

    fun getItems(): String{
        return "SELECT CODIGO, DESCRIPCIO FROM " + Constants.TBL_ITEMS +
                " WHERE LOCALIZACI = 'F'"
    }

    fun getTypeNotification(): String{
        return "SELECT TRB_TIPO, DESCRIPCION FROM " + Constants.TBL_TYPE_NOTIFICATION
    }


}