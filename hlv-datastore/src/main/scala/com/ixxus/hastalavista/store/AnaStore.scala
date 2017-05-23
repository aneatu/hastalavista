package com.ixxus.hastalavista.store

import scala.collection.immutable.HashSet

/**
  * Analytics related Store.
  * Updates the words and terms used in search queries.
  *
  * Created by aneatu
  */

trait AnaStore {

    var words: Map[String, Int]
    var terms: Map[String, Int]
    var urls: Map[String, Int]

    def addWord(word: String) =  words += word -> (words.getOrElse(word, 0) + 1)
    def addTerm(term: String) =  terms += term -> (terms.getOrElse(term, 0) + 1)
    def addUrls(url: String) =  urls += url -> (urls.getOrElse(url, 0) + 1)

}
