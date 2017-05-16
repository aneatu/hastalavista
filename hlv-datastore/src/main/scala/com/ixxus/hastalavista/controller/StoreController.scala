package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject
import org.springframework.web.bind.annotation._

/**
  * Controller giving API access to the Store(s)
  *
  * Created by Michael.Seddon on 10-May-17.
  */
@RestController
class StoreController() extends AbstractController {

    val storeService = ConfigObject.storeService

    @RequestMapping(value = Array("/page"),
        method = Array(RequestMethod.POST),
        consumes = Array("text/plain"))
    def addPage(@RequestBody body: String) = {
        storeService.addPage(body)
    }

    @RequestMapping(value = Array("/pages"),
        method = Array(RequestMethod.POST),
        consumes = Array("text/plain"))
    def addPages(@RequestBody body: String) = {
        storeService.addPages(body)
    }

    @RequestMapping(value = Array("/pages"),
        method = Array(RequestMethod.GET))
    def getPages() = {
        storeService.getPages()
    }
}
