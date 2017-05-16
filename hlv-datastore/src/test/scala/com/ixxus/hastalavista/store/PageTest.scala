package com.ixxus.hastalavista.store

import com.ixxus.hastalavista.StoreTest
import sun.misc.BASE64Encoder

/**
  * Created by Michael.Seddon on 09-May-17.
  */
class PageTest extends StoreTest {

    "A Page" should "be assigned a unique PUID on creation" in {
        val puidPage = Page("url", "contents")
        val otherPuidPage = Page("url", "contents")

        puidPage.puid should not be nullable
        puidPage.puid should not be empty

        puidPage.puid should not be otherPuidPage.puid
    }

    it should "be able to have related pages linked to it" in {
        val childPage = Page("url", "contents")
        val parentPage = Page("url", "contents")

        parentPage += childPage

        parentPage.related.contains(childPage) should be(true)
        parentPage.related.find(p => p == childPage).get should be(childPage)
    }

    it should "be able to have a tree of pages linked to it" in {
        val grandchildPage = Page("url", "contents")
        val childPage = Page("url", "contents")
        val parentPage = Page("url", "contents")

        childPage += grandchildPage
        parentPage += childPage

        parentPage.related.contains(childPage) should be(true)
        val retrievedChildPage = parentPage.related.find(p => p == childPage).get
        retrievedChildPage should be(childPage)

        childPage.related.contains(grandchildPage) should be(true)
        val retrievedGrandchildPage = childPage.related.find(p => p == grandchildPage).get
        retrievedGrandchildPage should be(grandchildPage)

        val retrievedGcPage2 = retrievedChildPage.related.find(p => p == retrievedGrandchildPage).get
        retrievedGrandchildPage should be(retrievedGcPage2)
    }

    it should "be able to store generic metadata" in {
        val metaPage = Page("url", "contents")
        metaPage += "metaKey1" -> "metaVal1"
        metaPage += "metaKey2" -> "metaVal2"

        metaPage.metadata.size should be(2)
        metaPage.metadata("metaKey1") should be("metaVal1")
        metaPage.metadata("metaKey2") should be("metaVal2")
    }

    it should "be able to store contents in raw HTML format" in {
        val rawContents = "<b>this is valid HTML</b>"
        val rawPage = Page("url", rawContents)

        rawPage.rawContents should not be nullable
        rawPage.rawContents should not be empty
        rawPage.rawContents should be(rawContents)
    }

    it should "be able to store an array of space delimited strings representing content" in {
        val rawContents = "<b>this is valid HTML</b>"

        val splitPage = Page("url", rawContents)
        splitPage.delimContents.length should be(4)
        splitPage.delimContents(2) should be("valid")
    }

    it should "be able to store a map of words and occurrences" in {
        val contents = "smile coat coat sun sun sun sun"
        val occPage = Page("url", contents)

        occPage.parsedContents should not be (nullable)
        occPage.parsedContents should not be empty
        occPage.parsedContents("smile") should be(1)
        occPage.parsedContents("coat") should be(2)
        occPage.parsedContents("sun") should be(4)
    }

    it should "be able to store a URL" in {
        val url = "http://www.scala-lang.org"
        val urlPage = Page(url, "contents")

        urlPage.url should not be nullable
        urlPage.url should not be empty
        urlPage.url should be(url)
    }

    it should "be able to store an encoded URL" in {
        val url = "http://www.scala-lang.org"
        val encUrl = new BASE64Encoder().encode(url.getBytes())
        val encUrlPage = Page(url, "contents")

        encUrlPage.encodedUrl should not be nullable
        encUrlPage.encodedUrl should not be empty
        encUrlPage.encodedUrl should be(encUrl)
    }
}
