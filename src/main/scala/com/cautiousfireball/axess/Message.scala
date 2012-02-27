package com.cautiousfireball.axess

sealed trait Message
case class ScanSite(site: String) extends Message
case class ScanResult(site: String, result: String) extends Message