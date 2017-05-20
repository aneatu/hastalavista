package com.ixxus.hastalavista.store

/**
  * Page related Store
  *
  * Created by Michael.Seddon on 09-May-17.
  */
trait PageStore {

        var pages : Set[Page]

        def +=(p: Page) { pages += p }
        def -=(p: Page) { pages -= p }
}
