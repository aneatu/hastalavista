package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import com.ixxus.hastalavista.datastore.rest.AnalyticsApi
import com.ixxus.hastalavista.store.{HastaStore, Page}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._


/**
  * Controller providing search API access to the Store(s)
  *
  *
  */
@RestController
class AnalyticsController() {

    val analyticService = ConfigObject.analyticsService

    @GetMapping(Array("/analyticsData"))
    def analyticsData():  ResponseEntity[AnalyticsApi] = {
        val response = new AnalyticsApi("200", "Success! Analytics details.")

        response.setWords(HastaStore.words.toSeq.sortWith(_._2 > _._2).mkString("|").replaceAll("[()]", ""))
        response.setTerms(HastaStore.terms.toSeq.sortWith(_._2 > _._2).mkString("|").replaceAll("[()]", ""))
        response.setUrls(HastaStore.urls.toSeq.sortWith(_._2 > _._2).mkString("|").replaceAll("[()]", ""))

        ResponseEntity.ok(response)
    }

    @GetMapping(Array("/pageAnalytics"))
    def pageAnalytics(@RequestParam page: String):  String = {
        val pageToOpen: Page = HastaStore.pages.find(_.puid == page).head
        HastaStore.addUrls(pageToOpen.url)
        pageToOpen.url
    }
}
