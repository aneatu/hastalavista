package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.store.HastaStore

import scala.annotation.tailrec

/**
  * Search Service.
  *
  * Created by alexneatu on 17-May-17.
  */
class SearchService {

    def findPage(url: String): String = {
        val found = HastaStore.pages.filter(p => p.url == url)
        (for (f <- found) yield f.puid + " " + f.url + "---" + f.rawContents).mkString("\n")
    }

    /**
      * Search for a specific term in the PageStore.
      *
      * @param term term to search for
      * @return     list of results
      */
    def findByTerm(term: String): List[(String, String, Int, Int)] = {
        val terms = term.split(" ").toList

        if (terms.size == 1) {
            HastaStore.addWord(term)

            HastaStore.pages
                        .filter(p => p.delimContents.contains(term))
                        .toList
                        .sortWith(_.parsedContents.getOrElse(term, 0) > _.parsedContents.getOrElse(term, 0))
                        .map(p => (p.puid, p.url, p.parsedContents.getOrElse(term, 0), 0))
        } else {
            // add the searched terms to analytics
            HastaStore.addTerm(term)
            // add each term from searched terms to analytics
            terms.foreach(HastaStore.addWord)

            HastaStore.pages
                        .par // run in parallel
                        .toIterator // doesnt create new datastructue after each call , just keeps everything in the iterator
                        .map(p =>  (p.puid,
                                    p.url,
                                    terms.map(kw => p.parsedContents.getOrElse(kw, 0)).sum,
                                    algDistance(terms, searchTermsInOrder(0, terms, p.delimContents, List()), 0)
                                    )
                        )
                        .filter(_._3 > 0)
                        .filter(_._4 > 0)
                        .filter(_._4 < 100)
                        .toList
                        .sortBy(t => (-t._3, t._4))
        }
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
      * TODO: change to tailrec, use an method parameter to keep the result
      *
      * @param termList list of words to search for
      * @param occ list of tuples (word, position) in page for the searched terms
      * @param acc accumulator for final result
      * @return distance calculated
      */
    @tailrec
    private def algDistance(termList: List[String], occ: List[(String, Int)], acc: Int): Int = {
        if (occ.size < 2) {
            acc
        } else {
            // this is a sublist with first elements found in the searched order
            val sublist: List[(String, Int)] = termList.map(term => occ.filter(_._1 == term))
                .flatMap(index => index)

            // calculate the distance
            if (sublist.size == termList.size) {
                val distance = -calcDistance(sublist.map(t => t._2))
                if (distance > 10) {
                    algDistance(termList, occ.filterNot(sublist.toSet), acc)
                } else {
                    algDistance(termList, occ.filterNot(sublist.toSet), distance + acc)
                }
            } else {
                algDistance(termList.filterNot(List(termList.head).toSet), occ.filterNot(sublist.filterNot(List(sublist.head).toSet).toSet), acc)
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
