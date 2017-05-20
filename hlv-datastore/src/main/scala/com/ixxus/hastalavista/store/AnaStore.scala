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

    def addWord(word: String) =  words += word -> (words.getOrElse(word, 0) + 1)
    def addTerm(term: String) =  terms += term -> (terms.getOrElse(term, 0) + 1)

    //def updateMap[String, Int](map: Map[String, Int], key: String) =  map updated (key, map.getOrElse(key, 0) + 1)  -- this is wrong because it uses generics
    //def updateMap(map: Map[String, Int], key: String): Map[String, Int] = map updated(key, map.getOrElse(key, 0) + 1) -- nice one

    // This could be used but multiple maps fields are defined
    //def +=(t: (String, Int)) {words +=t}

}
