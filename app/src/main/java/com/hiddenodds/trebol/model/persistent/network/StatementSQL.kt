package com.hiddenodds.trebol.model.persistent.network

object StatementSQL {

    const val TBL_CUSTOMER = "DATEN01"
    const val TBL_MACHINE = "DATOFIMACHINE01"
    const val TBL_TECHNICAL = "DATOFITECH01"
    const val TBL_NOTIFICATION = "DATOFIWARNING01"
    const val TBL_CONTACTS = "DATCTTO01"
    val TBL_AGENT = "DATVD01"
    val TBL_CONTRACT = "DATOFIMACHCONTRACT01"
    const val TBL_ITEMS = "DATIN01"
    const val TBL_ADD_PIECES = "DATOFIPIECES01"
    const val TBL_DEL_PIECES = "DATAS01"
    const val TBL_TYPE_NOTIFICATION = "CONVERSIONAVISOS01"
    const val TBL_TEAM_TECHNICAL = "CONVERSIONTRD"
    
    fun getCustomer(code: String): String{
        return "SELECT W.CODIGOAVISO, W.TECNICO, C.PERSONA, C.TELEFONO1, C.EMAIL FROM " +
                TBL_NOTIFICATION + " W JOIN " + TBL_CONTACTS +
                " C ON W.TRB_CONTACT = C.CODIGO WHERE W.TECNICO = '" + code +
                "' AND W.FECHAFIN = '1900-01-01'"
    }

    fun getTechnical(): String{
        return "SELECT * FROM $TBL_TEAM_TECHNICAL"
    }

    fun getItems(): String{
        return "SELECT CODIGO, DESCRIPCIO FROM " + TBL_ITEMS +
                " WHERE LOCALIZACI = 'F'"
    }

    fun getTypeNotification(): String{
        return "SELECT TRB_TIPO, DESCRIPCION FROM $TBL_TYPE_NOTIFICATION"
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
                "M.ESTADO2 FROM " + TBL_NOTIFICATION +
                " W JOIN " + TBL_MACHINE + " M ON " +
                "W.CODIGOMAQUINA = M.CODIGO JOIN " + TBL_CUSTOMER +
                " C ON M.CLIENTE = C.NIF WHERE W.TECNICO = '" + code +
                "' AND W.FECHAFIN = '1900-01-01' ORDER BY W.FECHAREALIZACIO ASC"
    }

    fun updateNotification(): String{
        return "UPDATE " + TBL_NOTIFICATION +
                " SET FECHAFIN = ?, FECHAREALIZACIO = ?, " +
                "REALIZADO = ?, OBSERVACIONES = ?, DURACION = ?, " +
                "DESPLAZAMIENTO = ?, TRB_PARTE2 = ? " +
                "WHERE LTRIM(RTRIM(CODIGOAVISO)) = ?"
    }

    fun updateMachine(): String{
        return "UPDATE " + TBL_MACHINE +
                " SET OTROSDATOS = ?, TRB_HORAS = ?, " +
                "NUMEROCIRCUITOS = ?, NUMEROCOMPRESOR = ? " +
                "WHERE LTRIM(RTRIM(CODIGO)) = ?"
    }

    fun getMaxRowPieces(): String{
        return "SELECT MAX(NUMERO) AS BIG FROM $TBL_ADD_PIECES"
    }

    fun addPieces(): String{
        return "INSERT INTO $TBL_ADD_PIECES (CODIGO, UNIDADES, " +
                "MAQUINA, OBSERVACIONES, AVISO, TIPOOPERACION, " +
                "PRECIOV, CENTRO, GENOPE, SERIEAVISO, TECNICO, NUMERO) " +
                "VALUES (?, ?, ?, ?, ?, 2, 0.0, '', 0, 'AV', ?, ?)"
    }

    fun getNameStore(): String{
        return "SELECT ALMACEN FROM $TBL_TECHNICAL WHERE CODIGO = ?"
    }

    fun getQuantityInStore(): String{
        return "SELECT EXISTENCIA FROM $TBL_DEL_PIECES WHERE " +
                "LTRIM(RTRIM(CODIGO)) = ? AND LTRIM(RTRIM(ALMACEN)) = ?"
    }

    fun updateQuantityInStore(): String{
        return "UPDATE $TBL_DEL_PIECES SET EXISTENCIA = ? " +
                "WHERE LTRIM(RTRIM(CODIGO)) = ? AND LTRIM(RTRIM(ALMACEN)) = ?"
    }
}