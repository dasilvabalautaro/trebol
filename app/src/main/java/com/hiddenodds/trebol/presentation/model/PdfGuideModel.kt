package com.hiddenodds.trebol.presentation.model

import java.io.Serializable
import java.util.*

class PdfGuideModel: Serializable {
    var client = ""
    var part = ""
    var series = ""
    var datePdf = ""
    var nameVerification = ""
    var nameTest = ""
    var nameMaintenance = ""
    var nameKnow = ""
    var observations = ""
    var signatureTechnical = ""
    var signatureClient = ""
    var id = ""
    var clientSignature = ""
    var nameTechnical = ""
    var mapImage: LinkedHashMap<String, ProxyBitmap> = LinkedHashMap()
}