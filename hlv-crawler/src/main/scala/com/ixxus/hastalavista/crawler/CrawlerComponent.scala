package com.ixxus.hastalavista.crawler

import com.ixxus.hastalavista.crawler.services.ScanServiceComponent

/**
  * Created by aneatu on 15/05/2017.
  */
trait CrawlerComponent {

    trait Crawler {
        this: Crawler with ScanServiceComponent =>
        val scanService: ScanService
    }

    val crawler: Crawler

    class CrawlerImpl extends Crawler with ScanServiceComponent {
        override val scanService: ScanService = new ScanServiceImpl
    }

}
