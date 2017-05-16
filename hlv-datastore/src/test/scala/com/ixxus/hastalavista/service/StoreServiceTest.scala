package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.{ConfigObject, StoreTest}

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class StoreServiceTest extends StoreTest {

   val storeService = ConfigObject.storeService
   val pageStore =  ConfigObject.hastaStore.pageStore

    override def beforeEach(): Unit = {
        pageStore.pages = Set()
    }

    "StoreService" should "have access to the PageStore" in {
        storeService should not be nullable
        storeService.pageStore should not be nullable
    }

    it should "provide a mechanism to add a Page to the PageStore" in {
        val spxml = "<page><url>testurl</url><contents>testcontents</contents></page>"
        storeService.addPage(spxml)

        pageStore.pages.size should be (1)
        pageStore.pages.head.url should be ("testurl")
        pageStore.pages.head.rawContents should be ("testcontents")
    }

    it should "provide a mechanism to add multiple Pages to the PageStore" in {
        val p1 = "<page><url>testurl</url><contents>testcontents</contents></page>"
        val p2 = "<page><url>testurl2</url><contents>testcontents2</contents></page>"
        val mpxml = s"<pages>$p1$p2</pages>"

        storeService.addPages(mpxml)

        pageStore.pages.size should be (2)
        pageStore.pages.head.url should be ("testurl")
        pageStore.pages.head.rawContents should be ("testcontents")
        pageStore.pages.tail.head.url should be ("testurl2")
        pageStore.pages.tail.head.rawContents should be ("testcontents2")

    }


}
