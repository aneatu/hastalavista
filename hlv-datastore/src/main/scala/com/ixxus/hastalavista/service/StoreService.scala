package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.store.Page

import scala.xml.XML

/**
  * Created by Michael.Seddon on 12-May-17.
  */
class StoreService extends AbstractService {

    def addPage(xml: String): String = {
        val pbod = XML.loadString(xml)
        val url = (pbod \ "url").head.text
        val cont = (pbod \ "contents").head.text
        pageStore += Page(url, cont)
        pageStore.pages.size + " " + url + " " + cont
    }

    def addPages(xml: String): Unit = {
        val pbod = XML.loadString(xml)
        val urls = (pbod \ "page" \ "url").map(u => u.text)
        val conts = (pbod \ "page" \ "contents").map(c => c.text)

        urls.zip(conts).foreach(t => pageStore += Page(t._1, t._2))
    }

    def getPages(): String = {
        (for (p <- pageStore.pages) yield p.puid + " " + p.url).mkString("\n")
    }

}
