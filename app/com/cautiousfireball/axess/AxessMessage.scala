package com.cautiousfireball.axess

sealed trait AxessMessage
case class ScanRequest(siteName: String)
case class ScanResult(siteName: String, scanResult: String)
