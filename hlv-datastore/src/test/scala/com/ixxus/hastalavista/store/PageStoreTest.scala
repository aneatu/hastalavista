package com.ixxus.hastalavista.store

import com.ixxus.hastalavista.StoreTest

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class PageStoreTest extends StoreTest {

    val pageStore: PageStore = HastaStore
    val pageToAdd = Page("url1", "contents")
    var pageToAddRemove = Page("url2", "contents")

    override def beforeEach(): Unit = {
        pageStore.pages = Set()
    }

    "PageStore" should "exist" in {
        pageStore should not be nullable
    }

    it should "have a Set of Pages" in {
        pageStore.pages should not be nullable
        pageStore.pages.size should be(0)
    }

    it should "allow a Page to be added the Set" in {
        pageStore += pageToAdd

        pageStore.pages.size should be(1)
        pageStore.pages.head should be(pageToAdd)
    }

    it should "allow a Page to be removed from the Set" in {
        pageStore += pageToAdd
        pageStore += pageToAddRemove
        pageStore -= pageToAddRemove

        pageStore.pages.size should be(1)
        pageStore.pages.head should be(pageToAdd)
    }

    it should "allow a Page to be retrieved from the Set" in {
        pageStore += pageToAdd

        pageStore.pages(pageToAdd) should be(true)

        val retrievedPage = pageStore.pages.find(p => p == pageToAdd)
        retrievedPage.get should be(pageToAdd)
    }

}
