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

    fun getNotifications(code: String): String{
        return "SELECT W.CODIGOMAQUINA, W.CODIGOAVISO, " +
                "W.FECHAINICIO, W.TRB_TIPO, W.HORAINICIO1, " +
                "W.HORAINICIO2, W.TRB_SINTOMA, W.REALIZADO, " +
                "W.OBSERVACIONES, W.FECHAFIN, W.FECHAREALIZACIO, " +
                "W.TECNICO, C.NIF, C.CODIGOA3, C.INTERNET, C.TELEFONO1, " +
                "C.CONTACTO, C.COMERCIAL, M.TRB_RAZONSOCIAL, M.PROVINCIA, " +
                "M.LOCALIDAD, M.DIRECCION, M.TRB_PEAJEPRE, M.TRB_SATD, " +
                "M.TRB_SATDK, M.CODIGOPOSTAL, M.FECHAINSTALACIO, " +
                "M.ZONA, M.CODIGOPRODUCTO, M.NSERIE, M.TRB_TINTA, " +
                "M.ESTADO2 FROM " + Constants.TBL_NOTIFICATION +
                " W JOIN " + Constants.TBL_MACHINE + " M ON " +
                "W.CODIGOMAQUINA = M.CODIGO JOIN " + Constants.TBL_CUSTOMER +
                " C ON M.CLIENTE = C.NIF WHERE W.TECNICO = '" + code +
                "' AND W.FECHAFIN = '1900-01-01' ORDER BY W.FECHAREALIZACIO ASC"
    }
}