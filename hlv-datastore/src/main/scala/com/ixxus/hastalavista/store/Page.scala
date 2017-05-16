package com.ixxus.hastalavista.store

import java.util.UUID

import sun.misc.BASE64Encoder

/**
  * Representation of a Page.
  *
  * Created by Michael.Seddon on 09-May-17.
  */
class Page private(val url: String,
                   val encodedUrl: String,
                   val rawContents: String,
                   val delimContents: Array[String],
                   val parsedContents: Map[String, Int]) {

    private[this] var relatedPages: Set[Page] = Set()
    private[this] var meta: Map[String, String] = Map()

    val puid: String = UUID.randomUUID.toString

    def +=(p: Page) { relatedPages += p }

    def +=(t: (String, String)) { meta += t }

    def related = relatedPages
    def metadata = meta
}

object Page {

    val defEnc = (u: String) => new BASE64Encoder().encode(u.getBytes())
    val defDelim = (c: String) => c.split(' ')
    val defParse = (c: String) => defDelim(c).groupBy(identity).mapValues(_.size)

    def apply(url: String, contents: String,
              enc: (String) => String = defEnc,
              delim: (String) => Array[String] = defDelim,
              parse: (String) => Map[String, Int] = defParse) = new Page(url, enc(url), contents, delim(contents), parse(contents))
}
