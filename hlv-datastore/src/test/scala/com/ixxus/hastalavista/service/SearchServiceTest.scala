package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.store.Page
import com.ixxus.hastalavista.{ConfigObject, StoreTest}

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class SearchServiceTest extends StoreTest {

   val searchService = ConfigObject.searchService
   val pageStore =  ConfigObject.storeService.pageStore

    override def beforeEach(): Unit = {
        pageStore.pages = Set()
    }

    "SearchService" should "have access to the PageStore" in {
        searchService should not be nullable
        searchService.pageStore should not be nullable
    }

    it should "provide a mechanism to search for pages by URL" in {
        pageStore.pages += Page("pageurl", "pagecontents")
        val foundPage: String = searchService.findPage("pageurl")

        foundPage should include ("pageurl")
    }


}
