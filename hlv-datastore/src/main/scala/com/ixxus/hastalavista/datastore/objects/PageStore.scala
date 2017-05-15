package com.ixxus.hastalavista.datastore.objects

import scala.util.matching.Regex

/**
  * Created by alexneatu on 10/05/2017.
  */
class PageStore private (val url: String, val content: String){

    val analyticsUrl = "/analytics/clicks?url=" + analyticsDecorator(url)
    val words: Array[String] = content.split("\\s+")
    var relatedPages: Set[String] = Set()

    private def this(url: String, content: String, relatedPages: Set[String]) {
        this(url, content)
        this.relatedPages = relatedPages
    }

    def analyticsDecorator(str: String, left: String = "<", right: String = ">"): String = left + str + right

}

object PageStore {

    def apply(url: String, content: String) = {
        val relatedPages: Set[String] = extractRelatedPages(url, content)
        new PageStore(url, content, relatedPages)
    }

    def extractRelatedPages(pageUrl: String, content: String): Set[String] = {
        import java.net.URL
        val pageUrlObj = new URL(pageUrl)
        val domain: String = s"${pageUrlObj.getProtocol}://${pageUrlObj.getHost}"

        // might be another solution to use xml parsing
        val hrefRegex = new Regex("<a href=\"(.*?)\"");
        hrefRegex.findAllIn(content)
            .map(url => url.replaceAll("<a href=", "").replaceAll("\"", ""))
            .map(url => if (url.startsWith("/"))  domain + url else url)
            .toSet
    }

    def main(args: Array[String]): Unit = {
        //val str: String = "</table>\n<div id=\"mp-other\" style=\"padding-top:4px; padding-bottom:2px;\">\n<h2><span class=\"mw-headline\" id=\"Other_areas_of_Wikipedia\">Other areas of Wikipedia</span></h2>\n<ul>\n<li><b><a href=\"http://www.google.com/test.mp3\" title=\"Wikipedia:Community portal\">Community portal</a></b> – Bulletin board, projects, resources and activities covering a wide range of Wikipedia areas.</li>\n<li><b><a href=\"/wiki/Wikipedia:Help_desk\" title=\"Wikipedia:Help desk\">Help desk</a></b> – Ask questions about using Wikipedia.</li>\n<li><b><a href=\"/wiki/Wikipedia:Local_Embassy\" title=\"Wikipedia:Local Embassy\">Local embassy</a></b> – For Wikipedia-related communication in languages other than English.</li>\n<li><b><a href=\"/wiki/Wikipedia:Reference_desk\" title=\"Wikipedia:Reference desk\">Reference desk</a></b> – Serving as virtual librarians, Wikipedia volunteers tackle your questions on a wide range of subjects.</li>"
        //val set: Set[String] = Set("http://www.bbc.co.uk/", "www.google.co.uk")
        //extractRelatedPages("http://www.google.co.uk/abc/cde",str) foreach println
    }
}
