package com.ixxus.hastalavista.crawler.api

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.servlet.ModelAndView

/**
  * Navigation controller.
  *
  * Created by alexneatu on 22/05/2017.
  */
@Controller
class WebController {

    @GetMapping(Array("/"))
    def showForm(): String = {
        "index"
    }

    @GetMapping(Array("/search"))
    def searchForm(): String = {
        "search"
    }

    @GetMapping(Array("/analytics"))
    def analyticsForm(): String = {
        "analytics"
    }
}
