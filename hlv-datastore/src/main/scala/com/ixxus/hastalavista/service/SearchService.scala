package com.ixxus.hastalavista.service

/**
  * Created by Michael.Seddon on 12-May-17.
  */
class SearchService extends AbstractService {

    def findPage(url: String): String = {
        val found = pageStore.pages.filter(p => p.url == url)
        (for (f <- found) yield f.puid + " " + f.url + "---" + f.rawContents).mkString("\n")
    }

    def findByTerm(term: String): String = {
        val terms = term.split(" ")
        var result = ""

        if (terms.size == 1) {
            anaStore.addWord(term)
            val found = pageStore.pages
                //.filter(p => p.parsedContents.getOrElse(term, 0) != 0)
                .filter(p => p.delimContents.contains(term))
            result = (for (f <- found) yield f.puid + " " + f.url + " --- " + term + " / " + f.parsedContents.get(term)
                + " / no searches: " + anaStore.words.getOrElse(term, 0)).mkString("\n")
        } else {
            anaStore.addTerm(term)
            terms.map(t => anaStore.addWord(t))
            result = ""
        }

        result
    }


}
