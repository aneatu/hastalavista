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
  * Rest API class responsible to manage all the requests.
  *
  * Created by alexneatu on 14/05/2017.
  */

@RestController
class RestAPI {

    /**
      *
      * API responsible to start the crawler with a specific URL.
      *
      * This is the POST body sent to the store application after the pages are downloaded.
      * Pages are sent in xml format
      *
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
      *
      * @param crawler  the crawler object that contains the website and how many url to scan
      * @param errors   erros if the fields dont meet the constraints
      * @return         ResponseEntity object
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
            val status = getValueFromJsonByName(resp, "status")

            if (status == "200") {
                var response = new ResponseApi("200", "Success")
                response.setData(crawler)
                ResponseEntity.ok(response)
            } else {
                ResponseEntity.badRequest().body(new ResponseApi("400", "Store problems..."))
            }
        }
    }

    /**
      * GET request to search for a specific term. Another call to store application is triggered to get info.
      *
      * @param term term to search for
      * @return     list of pages found plus occurrences and distances (in case of multiple terms are searched)
      */
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

    /**
      * Gets all the statistics from HastaStore and return them.
      *
      * @return list of words, terms and urls for analytics page
      */
    @GetMapping(Array("/analyticsData"))
    def analytics():  ResponseEntity[String] = {
        val restTemplate: RestTemplate = new RestTemplate()
        val resp = restTemplate.getForObject("http://localhost:8090/analyticsData", classOf[String], "")

        ResponseEntity.ok(resp)
    }

    /**
      * This method update the analytics store for clicked urls and redirects to the clicked link.
      *
      * @param page the page uuid
      * @return     a redirect to the clicked page
      */
    @GetMapping(Array("/pageAnalytics"))
    def pageAnalytics(@RequestParam page: String):  ModelAndView = {
        val restTemplate: RestTemplate = new RestTemplate()
        val url = restTemplate.getForObject("http://localhost:8090/pageAnalytics?page={page}", classOf[String], page.trim)
        val fixUrl = (xurl: String) => {if (xurl.startsWith("www")) "http://" + xurl else xurl }
        new ModelAndView("redirect:" + fixUrl(url))
    }

    /**
      * Parse the string to JSON and extract value for a specific key.
      *
      * @param jsonStr  json in string format
      * @param keyName  key name that is searched for
      * @return         key's value
      */
    private def getValueFromJsonByName(jsonStr: String, keyName: String): String = {
        JSON.parseFull(jsonStr) match {
            case Some(m: Map[String, Any]) => m(keyName) match {
                case s: String => s
            }
        }
    }
}
