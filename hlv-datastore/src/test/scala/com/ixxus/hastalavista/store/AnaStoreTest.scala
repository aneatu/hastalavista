package com.ixxus.hastalavista.store

import com.ixxus.hastalavista.StoreTest

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class AnaStoreTest extends StoreTest with AnaStoreComponent {

    override val anaStore: AnaStore = new AnaStoreImpl

    "AnaStore" should "exist" in {
        anaStore should not be (nullable)
    }
}
