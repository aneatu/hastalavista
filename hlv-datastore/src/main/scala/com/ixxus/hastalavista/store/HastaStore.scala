package com.ixxus.hastalavista.store

/**
  * Main store for the search engine
  *
  * Created by Michael.Seddon on 09-May-17.
  *
  * TODO: Make it thread safe
  *
  */
object HastaStore extends AnaStore with PageStore {
    override var words: Map[String, Int] = Map()
    override var terms: Map[String, Int] = Map()
    override var urls: Map[String, Int] = Map()
    override var pages: Set[Page] = Set()
}
