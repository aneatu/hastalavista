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
                .filter(p => p.delimContents.contains(term))
                .toList
                .sortWith(_.parsedContents.getOrElse(term, 0) > _.parsedContents.getOrElse(term, 0))

            result = (for (f <- found) yield f.puid + " " + f.url + " --- " + term + " / " + f.parsedContents.get(term)
                + " / no searches: " + anaStore.words.getOrElse(term, 0)).mkString("\n")
        } else {
            def diff(list: List[Int]): Int = if (list.isEmpty || list.tail.isEmpty) 0 else list.head - list.tail.head + diff(list.tail)

            anaStore.addTerm(term)
            terms.map(t => anaStore.addWord(t))
            val found = pageStore.pages
                    .map(p => (p.puid,
                                p.url,
                                terms.map(kw => p.parsedContents.getOrElse(kw, 0)).toList.sum,
                                -diff(terms.map(kw => p.delimContents.indexOf(kw)).toList)))
                    .filter(_._3 > 0)
                    .filter(t => t._4 > 0 && t._4 < 100)
                    .toList
                    .sortBy(t => (-t._3, t._4))
                    //.sortBy(_._4)
                    //.sortWith((x, y) => (x._4 > y._4 && x._3 < y._3) )
                    //.sortBy(t => (t._3,t._4))
                    //.sortWith(_._3 > _._3)
                    //.sortWith(_._4 < _._4)
                //.filter(p => p.delimContents.zipWithIndex.find((terms(0), _)) == p.delimContents.zipWithIndex.find((terms(0),_)) + 1)
            result = (for (f <- found) yield f._1 + " " + f._2 + " --- " + term + " / " + f._3
                + " / distance: " + f._4).mkString("\n")
        }

        result
    }


}
