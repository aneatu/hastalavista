package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import com.ixxus.hastalavista.store.Page
import org.springframework.web.bind.annotation._

import scala.xml.XML

/**
  * Controller providing search API access to the Store(s)
  *
  *
  */
@RestController
class SearchController() {

    val searchService = ConfigObject.searchService

    @RequestMapping(value = Array("/find/page"), method = Array(RequestMethod.GET))
    def findPages(@RequestParam url: String) = {
        searchService.findPage(url)
    }

    @RequestMapping(value = Array("/find"), method = Array(RequestMethod.GET))
    def findByTerm(@RequestParam term: String) = {
        searchService.findByTerm(term)
    }
}
