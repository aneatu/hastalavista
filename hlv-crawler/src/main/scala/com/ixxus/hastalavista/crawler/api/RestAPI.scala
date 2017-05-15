package com.ixxus.hastalavista.crawler.api

import org.springframework.web.bind.annotation._
import org.springframework.web.client.RestTemplate

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
        //result.foldLeft("<pages>")({t: (String, String) => "<page><url>" + t._1 + "</url><contents>" + t._2 + "</contents></page>"})
        val xmlRes = "<pages>" + result.map(t => "<page><url>" + t._1 + "</url><contents>" + t._2 + "</contents></page>").mkString + "</pages>"


        val restTemplate: RestTemplate = new RestTemplate()
        restTemplate.postForObject("http://localhost:8090/pages", xmlRes, classOf[String])
    }
}
