package com.ixxus.hastalavista.store

/**
  * The main store, through this object all the other stores are accessible.
  *
  * TODO: Make it thread safe
  *
  * Created by alexneatu on 22/05/2017.
  */
object HastaStore extends AnaStore with PageStore {
    override var words: Map[String, Int] = Map()
    override var terms: Map[String, Int] = Map()
    override var urls: Map[String, Int] = Map()
    override var pages: Set[Page] = Set()
}
