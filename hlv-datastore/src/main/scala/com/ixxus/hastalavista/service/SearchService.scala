package com.ixxus.hastalavista.service

/**
  * Created by Michael.Seddon on 12-May-17.
  */
class SearchService extends AbstractService {

    def findPage(url: String): String = {
        val found = pageStore.pages.filter(p => p.url == url)
        (for (f <- found) yield f.puid + " " + f.url + "---" + f.rawContents).mkString("\n")
    }

}
