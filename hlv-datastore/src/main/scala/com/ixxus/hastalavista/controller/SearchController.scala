package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import com.ixxus.hastalavista.datastore.rest.{ResponseApi, SearchResult}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._

import scala.collection.JavaConverters._

/**
  * Controller providing search API access to the Store(s)
  *
  * Created by alexneatu on 22/05/2017.
  */
@RestController
class SearchController() {

    val searchService = ConfigObject.searchService

    /**
      * Find page by url.
      *
      * This is used just for development and testing purpose.
      *
      * @param url  page's url
      * @return     page
      */
    @RequestMapping(value = Array("/find/page"), method = Array(RequestMethod.GET))
    def findPages(@RequestParam url: String) = {
        searchService.findPage(url)
    }

    /**
      * Searches for terms and returns a list of pages
      *
      * @param term term to search fot
      * @return     list of pages and details
      */
    @GetMapping(Array("/find"))
    def findByTerm(@RequestParam term: String):  ResponseEntity[ResponseApi] = {
        val results = searchService.findByTerm(term)

        val response = new ResponseApi("200", "Success! Here is the list of results.")
        response.setData(results.map(r => new SearchResult(r._1, r._2, r._3, r._4)).asJava)
        ResponseEntity.ok(response)
    }
}
