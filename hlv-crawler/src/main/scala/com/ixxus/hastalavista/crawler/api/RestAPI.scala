package com.ixxus.hastalavista.crawler.api

import javax.validation.Valid

import com.ixxus.hastalavista.crawler.services.ScanService
import com.ixxus.hastalavista.datastore.rest.{Crawler, ResponseApi}
import org.springframework.http.ResponseEntity
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation._
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.ModelAndView

import scala.collection.JavaConverters._
import scala.util.parsing.json.JSON


/**
  * Created by alexneatu on 14/05/2017.
  */

@RestController
class RestAPI {

    val  xml11pattern: String = "[^\\x09\\x0A\\x0D\\x20-\\uD7FF\\uE000-\\uFFFD\\u10000-u10FFFF]"


    /**
      *
      * API responsible to start the crawler with a specific URL.
      *
      * This is the POST body sent to the store application after the pages are downloaded.
      * <code>
      *     <pages>
      *         <page>
      *             <url>blaba</url>
      *             <contents>content</contents>
      *        </page>
      *        <page>
      *             <url>blaba</url>
      *             <contents>content</contents>
      *         </page>
      *     </pages>
      * </code>
      *
      */
    @PostMapping(Array("/crawler"))
    def startCrawling(@RequestBody @Valid crawler: Crawler, errors: Errors): ResponseEntity[ResponseApi] = {

        if (errors.hasErrors) {
            ResponseEntity.ok(new ResponseApi("400", errors.getAllErrors().asScala.map(x => x.getDefaultMessage).mkString(", ")))
        } else {
            val result = ScanService.crawlerToXML(crawler.getWebsite, crawler.getNoLinks)

            // folding with functions
            val xmlRes = result.foldLeft(StringBuilder.newBuilder.append("<pages>"))((acc: StringBuilder, t: (String, String)) =>
                acc.append("<page><url>" + t._1 + "</url><contents>" + t._2.replaceAll("[^\\dA-Za-z ]", " ") + "</contents></page>")).append("</pages>").mkString

            val restTemplate: RestTemplate = new RestTemplate()
            val resp = restTemplate.postForObject("http://localhost:8090/pages", xmlRes, classOf[String])
            val status = JSON.parseFull(resp) match {
                case Some(m: Map[String, Any]) => m("status") match {
                    case s: String => s
                }
            }
            if (status == "200") {
                var response = new ResponseApi("200", "Success")
                response.setData(crawler)
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.badRequest().body(new ResponseApi("400", "Store problems..."))
            }
        }
    }

    @GetMapping(Array("/find"))
    def findByTerm(@RequestParam term: String):  ResponseEntity[String] = {
        if (term.isEmpty) {
            ResponseEntity.ok("{\"message\":\"Search term is empty!\"}")
        } else {
            val restTemplate: RestTemplate = new RestTemplate()
            val resp = restTemplate.getForObject("http://localhost:8090/find?term={term}", classOf[String], term.trim   )

            ResponseEntity.ok(resp)
        }
    }

    @GetMapping(Array("/analyticsData"))
    def analytics():  ResponseEntity[String] = {
        val restTemplate: RestTemplate = new RestTemplate()
        val resp = restTemplate.getForObject("http://localhost:8090/analyticsData", classOf[String], "")

        ResponseEntity.ok(resp)
    }

    @GetMapping(Array("/pageAnalytics"))
    def pageAnalytics(@RequestParam page: String):  ModelAndView = {
        val restTemplate: RestTemplate = new RestTemplate()
        val url = restTemplate.getForObject("http://localhost:8090/pageAnalytics?page={page}", classOf[String], page.trim)
        val fixUrl = (xurl: String) => {if (xurl.startsWith("www")) "http://" + xurl else xurl }
        new ModelAndView("redirect:" + fixUrl(url))
    }
}
