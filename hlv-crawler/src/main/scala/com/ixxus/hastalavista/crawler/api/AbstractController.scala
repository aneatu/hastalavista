package com.ixxus.hastalavista.crawler.api

import com.ixxus.hastalavista.crawler.CrawlerApplication.ConfigObject

/**
  * Created by aneatu on 15/05/2017.
  */
trait AbstractController {
    val crawler = ConfigObject.crawler.scanService
}
