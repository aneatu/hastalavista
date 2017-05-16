package com.ixxus.hastalavista.store

/**
  * Main store for the search engine
  *
  * Created by Michael.Seddon on 09-May-17.
  */
trait HastaStoreComponent {

    trait HastaStore {
        this: HastaStore with PageStoreComponent with AnaStoreComponent => //we must provide implementations when using HastaStore
        val pageStore : PageStore
        val anaStore : AnaStore
    }

    val hastaStore : HastaStore

    class HastaStoreImpl extends HastaStore with PageStoreComponent with AnaStoreComponent {
        override val pageStore: PageStore = new PageStoreImpl
        override val anaStore: AnaStore = new AnaStoreImpl
    }

}
