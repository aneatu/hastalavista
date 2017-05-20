package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.store.HastaStore

import scala.annotation.tailrec

/**
  * Created by alexneatu on 17-May-17.
  */
class SearchService {

    def findPage(url: String): String = {
        val found = HastaStore.pages.filter(p => p.url == url)
        (for (f <- found) yield f.puid + " " + f.url + "---" + f.rawContents).mkString("\n")
    }

    def findByTerm(term: String): String = {
        val terms = term.split(" ")
        var result = ""

        if (terms.size == 1) {
            HastaStore.addWord(term)
            val found = HastaStore.pages
                .filter(p => p.delimContents.contains(term))
                .toList
                .sortWith(_.parsedContents.getOrElse(term, 0) > _.parsedContents.getOrElse(term, 0))

            result = (for (f <- found) yield f.puid + " " + f.url + " --- " + term + " / " + f.parsedContents.get(term)
                + " / no searches: " + HastaStore.words.getOrElse(term, 0)).mkString("\n")
        } else {

            def calcDistance(list: List[Int]): Int = if (list.isEmpty || list.tail.isEmpty) 0 else list.head - list.tail.head + calcDistance(list.tail)

            // change to use tailrec
            def algDistance(termList: List[String], occ: List[(String, Int)]): Int = {
                if (occ.size < 2) {
                    0
                } else {
                    val sublist: List[(String, Int)] = termList.map(term => occ.filter(_._1 == term)).flatMap(index => index) //.map(t => t._2)).flatMap(index => index)
                    // check that indexes are in consecutive order
                    if (sublist.size == terms.size) {
                        val distance = -calcDistance(sublist.map(t => t._2))
                        if (distance > 10) {
                            algDistance(termList, occ.filterNot(sublist.toSet))
                        } else {
                            distance + algDistance(termList, occ.filterNot(sublist.toSet))
                        }
                    } else {
                        algDistance(termList.filterNot(List(termList.head).toSet), occ.filterNot(sublist.filterNot(List(sublist.head).toSet).toSet))
                    }
                }
            }

            HastaStore.addTerm(term)
            terms.foreach(HastaStore.addWord)
            val found = HastaStore.pages
                        .toIterator // doesnt create new datastructue after each call , just keeps everything in the iterator
                        .map(p => (p.puid,
                                p.url,
                                terms.map(kw => p.parsedContents.getOrElse(kw, 0)).toList.sum,
                                //-calcDistance(terms.map(kw => p.delimContents.indexOf(kw)).toList)))
                                algDistance(terms.toList, terms
                                                            .map(kw => (kw, p.delimContents.indexOf(kw)))
                                                            .filter(_._2 > 0)
                                                            .toList)))
                        .filter(_._3 > 0)
                        .filter(_._4 > 0)
                        .filter(_._4 < 100)
                        .toList
                        .sortBy(t => (-t._3, t._4))
                    //.sortBy(_._4)
                    //.sortWith((x, y) => (x._4 > y._4 && x._3 < y._3) )
                    //.sortBy(t => (t._3,t._4))
                    //.sortWith(_._3 > _._3)
                    //.sortWith(_._4 < _._4)
                //.filter(p => p.delimContents.zipWithIndex.find((terms(0), _)) == p.delimContents.zipWithIndex.find((terms(0),_)) + 1)
            result = (for (f <- found)
                        yield f._1 + " " + f._2 + " --- " + term + " / " + f._3 + " / distance: " + f._4)
                        .mkString("\n")
        }

        result
    }


}
