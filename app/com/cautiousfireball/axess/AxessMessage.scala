package com.cautiousfireball.axess

case class ScanRequest(siteName: String)
case class ScanResult(siteName: String, scanResult: String)
case class ScanPage(url: String)
case class PageNote(url: String, cat: String, msg: String)
case class PageScanResult(url: String, notes: List[PageNote])
case class NewUrls(urls: Set[String])
