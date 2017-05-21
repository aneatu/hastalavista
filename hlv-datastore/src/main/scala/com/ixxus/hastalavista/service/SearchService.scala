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
        val terms = term.split(" ").toList
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
            // add the searched terms to analytics
            HastaStore.addTerm(term)
            // add each term from searched terms to analytics
            terms.foreach(HastaStore.addWord)

            val found = HastaStore.pages
                        .par // run in parallel
                        .toIterator // doesnt create new datastructue after each call , just keeps everything in the iterator
                        .map(p =>  (p.puid,
                                    p.url,
                                    terms.map(kw => p.parsedContents.getOrElse(kw, 0)).sum,
                                    algDistance(terms, searchTermsInOrder(0, terms, p.delimContents, List()))
                                    )
                        )
                        .filter(_._3 > 0)
                        .filter(_._4 > 0)
                        .filter(_._4 < 100)
                        .toList
                        .sortBy(t => (-t._3, t._4))
            result = (for (f <- found)
                        yield f._1 + " " + f._2 + " --- " + term + " / " + f._3 + " / distance: " + f._4)
                        .mkString("\n")
        }

        result
    }

    /**
      * Calculates distance among List indexes.
      * E.g.: List(1, 3, 4, 5) = 1 - 3 + 3 - 4 + 4 - 5 = -4
      *
      * @param list list of indexes
      * @return distance between indexes
      */
    private def calcDistance(list: List[Int]): Int = if (list.isEmpty || list.tail.isEmpty) 0 else list.head - list.tail.head + calcDistance(list.tail)

    /**
      *
      * TODO: change to tailrec, use an method parametr to keep the result
      *
      * @param termList list of words to search for
      * @param occ list of tuples (word, position) in page for the searched terms
      * @return distance calculated
      */
    private def algDistance(termList: List[String], occ: List[(String, Int)]): Int = {
        if (occ.size < 2) {
            0
        } else {
            // this is a sublist with first elements found in the searched order
            val sublist: List[(String, Int)] = termList.map(term => occ.filter(_._1 == term))
                .flatMap(index => index)

            // check that indexes are in consecutive order
            if (sublist.size == termList.size) {
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

    /**
      * Searches in an array all the matches from term list in consecutive order.
      *
      * @param start index to start
      * @param terms terms to search for
      * @param pageWords array of words from page
      * @param acc accumulator
      * @return list of tuples composed from word and index
      */
    @tailrec
    private def searchTermsInOrder(start: Int, terms: List[String], pageWords: Array[String], acc: List[(String, Int)]): List[(String, Int)] = {
        if(!terms.isEmpty) {
            val term = terms(0)
            val indexOfTerm = pageWords.indexOf(term, start)
            if (indexOfTerm > 0) {
                searchTermsInOrder(indexOfTerm, terms.filterNot(List(term).toSet), pageWords, (term, indexOfTerm)::acc)
            } else {
                searchTermsInOrder(start, terms.filterNot(List(term).toSet), pageWords, acc)
            }
        } else {
            acc.sortBy(t => t._2)
        }
    }
}
