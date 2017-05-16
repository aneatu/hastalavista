package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import com.ixxus.hastalavista.store.Page
import org.springframework.web.bind.annotation._

import scala.xml.XML

/**
  * Controller providing search API access to the Store(s)
  *
  * Created by Michael.Seddon on 10-May-17.
  */
@RestController
class SearchController() extends AbstractController {

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
