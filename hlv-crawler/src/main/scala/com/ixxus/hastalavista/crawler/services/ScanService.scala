package com.ixxus.hastalavista.crawler.services

import java.net.URL

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.io.Source
import scala.util.Try
import scala.concurrent.blocking

/**
  * Created by alexneatu on 10/05/2017.
  */
trait ScanServiceComponent {

    trait ScanService {
        def crawlerToXML(url: String, noLinksToScan: Int): Set[(String, String)]
    }

    val scanService: ScanService

    class ScanServiceImpl extends ScanService {

        override def crawlerToXML(url: String, noLinksToScan: Int): Set[(String, String)] = {
           /* time {
                val futRes = crawler(url, noLinksToScan)
                val res = Await.result(futRes, Duration.Inf)
                println(res.size)
                res.foreach(t => println(s"(${t._1})"))
            }*/
            val futRes = crawler(url, noLinksToScan)
            val res = Await.result(futRes, Duration.Inf)
            res
        }

        implicit val ec = scala.concurrent.ExecutionContext.global

        val findHref = "(?i)<a([^>]+)>(.+?)</a>".r
        val findLink = "\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))".r
        val removeScript = "<(script|style).*?</\\1>".r
        val removeTags = "<.*?>".r


        def scrapper(url: String, html: String): Set[String] = {
            val jUrl = new URL(url)
            val path = jUrl.getFile.substring(0, jUrl.getFile.lastIndexOf('/'))
            val base = jUrl.getProtocol + "://" + jUrl.getHost
            val basePath = base + path
            findHref
                .findAllIn(html)
                .flatMap(s => findLink
                    .findAllIn(s)
                    .matchData
                    .map(_.group(1)))
                .filter(!_.contains('#'))
                .map(s => s.replaceAll("\"", ""))
                .map {
                    case s if s.startsWith("http") => s
                    case s if s.startsWith("/") => base + s
                    case s => basePath + s
                }
                .toSet[String]
        }

        def crawler(url: String, count: Int) = {
            println(s"$url - $count")
            val (links, page) = crawlerPage(url)
            recursiveCrawler(count - 1, links, Set(url), Set(page))
        }

        def crawlerPage(url: String) = {
            println("Downloading..." + Thread.currentThread().getName + " " + url)
            val html = Try {
                // blocking gives you a lot of boost, check the slides!!
                blocking {
                    Source.fromURL(url).mkString
                }
            }.getOrElse("Not found")

            val links: Set[String] = Try {
                scrapper(url, html)
            }.getOrElse(Set())

            val text = removeTags.replaceAllIn(
                removeScript.replaceAllIn(html, "")
                , "")
            (links, (url, text))
        }

        def recursiveCrawler(count: Int, links: Set[String], linksDone: Set[String], pagesDone: Set[(String, String)]): Future[Set[(String, String)]] = {
            if (links.isEmpty || count < 1) {
                Future.successful(pagesDone)
            } else {
                val (pageLinks, page) = crawlerPage(links.head)

                val linksCrawled: Set[String] = linksDone + links.head

                val linksToAnalise: Set[(String, Int)] =
                    (links.tail ++ pageLinks -- linksCrawled)
                        .zipWithIndex
                        .filter { case (_, c) => (count - c - 1) > 0 }

                val futureCrawler: ((String, Int)) => Future[(Set[String], (String, String))] = {
                    case (pUrl, _) =>
                        Future {
                            crawlerPage(pUrl)
                        }
                }

                val initAccumulator: (Set[String], List[(String, String)]) = (Set(), List())

                Future.traverse(linksToAnalise)(futureCrawler)
                    .flatMap(r =>
                        Future {
                            r.foldLeft(initAccumulator)((acc, e) => (acc._1 ++ e._1, e._2 :: acc._2))
                        })
                    .flatMap { case (linksAnalysed, pageAnalysed) =>
                        recursiveCrawler(
                            count - linksAnalysed.size,
                            linksAnalysed,
                            linksCrawled ++ linksAnalysed,
                            pagesDone ++ pageAnalysed + page)
                    }
            }
        }
    }

    def time[R](block: => R): R = {
        import java.time.{Duration, Instant}

        val start = Instant.now()
        val result = block
        val stop = Instant.now()
        println("Elapsed time: " + Duration.between(start, stop))
        result
    }
}

