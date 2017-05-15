package com.ixxus.hastalavista.crawler.api

import org.springframework.web.bind.annotation._

import scala.xml.XML

/**
  * Created by alexneatu on 14/05/2017.
  */

@RestController
class RestAPI extends AbstractController{

    val linksToScan: Int = 100

    /**
      *
      * API responsible to start the crawler with a specific URL.
      *
      * @param body to extract the URLs to crawler
      */
    @RequestMapping(value = Array("/crawler"), method = Array(RequestMethod.POST))
    def startCrawling(@RequestBody body: String) = {
        val xml = XML.loadString(body)
        val urls = (xml \ "url").map(_.text)
        val result = crawler.crawlerToXML(urls.head, linksToScan)

        // TODO: send it over to Michael's store
        result.foreach(t => println(s"(${t._1}, ${truncate(t._2.replaceAll("\n", ""))})"))
    }

    private def truncate(str: String) = if (str.length > 10) str.substring(0, 20) else str

}
