package com.ixxus.hastalavista.store

/**
  * Analytics related Store
  *
  * Created by Michael.Seddon on 09-May-17.
  */
trait AnaStoreComponent {

    trait AnaStore {
        //val page : Page
    }

    val anaStore: AnaStore

    class AnaStoreImpl extends AnaStore {
        //override val page: Page = new Page
    }
}
