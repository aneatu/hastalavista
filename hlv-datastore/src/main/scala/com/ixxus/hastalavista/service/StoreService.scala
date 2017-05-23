package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.store.{HastaStore, Page}

import scala.xml.XML

/**
  * Store Service.
  *
  * Created by alexneatu on 22/05/2017.
  */
class StoreService {

    def addPage(xml: String): String = {
        val pbod = XML.loadString(xml)
        val url = (pbod \ "url").head.text
        val cont = (pbod \ "contents").head.text
        HastaStore.pages += Page(url, cont)
        HastaStore.pages.size + " " + url + " " + cont
    }

    def addPages(xml: String) {
        val pbod = XML.loadString(xml)
        val urls = (pbod \ "page" \ "url").map(u => u.text)
        val conts = (pbod \ "page" \ "contents").map(c => c.text)

        urls.zip(conts).foreach(t => HastaStore.pages += Page(t._1, t._2))
    }

    def getPages(): String = (for (p <- HastaStore.pages) yield p.puid + " " + p.url).mkString("\n")
}
