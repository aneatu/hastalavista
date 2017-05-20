package com.ixxus.hastalavista.store

import com.ixxus.hastalavista.StoreTest

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class AnaStoreTest extends StoreTest {

    val anaStore: AnaStore = HastaStore

    "AnaStore" should "exist" in {
        anaStore should not be (nullable)
    }
}
