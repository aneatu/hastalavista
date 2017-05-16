package com.ixxus.hastalavista.store

import com.ixxus.hastalavista.StoreTest

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class HastaStoreTest extends StoreTest with HastaStoreComponent {

    override val hastaStore : HastaStore = new HastaStoreImpl

    "HastaStore" should "exist" in {
        hastaStore should not be (nullable)
    }

    it should "have a PageStore" in {
        hastaStore.pageStore should not be (nullable)
    }

    it should "have an AnalyticsStore" in {
        hastaStore.anaStore should not be (nullable)
    }
}
