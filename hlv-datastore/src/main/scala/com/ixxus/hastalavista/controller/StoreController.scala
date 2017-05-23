package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import com.ixxus.hastalavista.datastore.rest.ResponseApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation._

/**
  * Controller giving API access to the Store(s)
  *
  * Created by alexneatu on 22/05/2017.
  */
@RestController
class StoreController() {

    val storeService = ConfigObject.storeService

    /**
      * Gets as input the xml, extract url and content, creates pages and stores them in PageStore
      *
      * @param body xml body
      * @return     success/failure message
      */
    @RequestMapping(value = Array("/pages"), method = Array(RequestMethod.POST), consumes = Array("text/plain"))
    def addPages(@RequestBody body: String): ResponseEntity[ResponseApi] = {
        storeService.addPages(body)
        ResponseEntity.ok(new ResponseApi("200", "Success"))
    }


    @RequestMapping(value = Array("/pages"), method = Array(RequestMethod.GET))
    def getPages() = {
        storeService.getPages()
    }

    @RequestMapping(value = Array("/page"), method = Array(RequestMethod.POST), consumes = Array("text/plain"))
    def addPage(@RequestBody body: String) = {
        storeService.addPage(body)
    }
}
