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
      * @param body to extract the URLs to crawler
      */
    @RequestMapping(value = Array("/crawler"), method = Array(RequestMethod.POST))
    def startCrawling(@RequestBody body: String) = {
        val xml = XML.loadString(body)
        val urls = (xml \ "url").map(_.text)
        val result = crawler.crawlerToXML(urls.head, linksToScan)


        // folding with functions
        val xmlRes = result.foldLeft(StringBuilder.newBuilder.append("<pages>"))((acc: StringBuilder, t: (String, String)) =>
                                                acc.append("<page><url>" + t._1 + "</url><contents>" + t._2 + "</contents></page>")).append("</pages>").mkString

        // partial function
        val xmlRes1 = result.foldLeft(StringBuilder.newBuilder.append("<pages>")){
            case (builder, (url, content)) => builder.append("<page><url>" + url + "</url><contents>" + content + "</contents></page>")
        }.append("</pages>").mkString

        // this is using maps
        val xmlResMap = "<pages>" + result.map(t => "<page><url>" + t._1 + "</url><contents>" + t._2 + "</contents></page>").mkString + "</pages>"

        val restTemplate: RestTemplate = new RestTemplate()
        restTemplate.postForObject("http://localhost:8090/pages", xmlRes.replaceAll("&", ""), classOf[String])
    }
}
