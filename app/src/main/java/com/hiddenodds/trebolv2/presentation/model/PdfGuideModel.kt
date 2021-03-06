package com.hiddenodds.trebolv2.presentation.model

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
    var mapImage: LinkedHashMap<String, ProxyBitmap> = LinkedHashMap()
}