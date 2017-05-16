package com.ixxus.hastalavista.store

import scala.collection.immutable.HashSet

/**
  * Analytics related Store
  *
  * Created by aneatu
  */
trait AnaStoreComponent {

    trait AnaStore {
        var words: Map[String, Int]
        var terms: Map[String, Int]

        def addWord(word: String) { words updated (word, words.getOrElse(word, 0) + 1) }
        def addTerm(term: String) { terms updated (term, terms.getOrElse(term, 0) + 1) }
        //def updateMap[String, Int](map: Map[String, Int], key: String) =  map updated (key, map.getOrElse(key, 0) + 1)
    }

    val anaStore: AnaStore

    class AnaStoreImpl extends AnaStore {
        override var words: Map[String, Int] = Map()
        override var terms: Map[String, Int] = Map()
    }
}
