package com.ixxus.hastalavista

import com.ixxus.hastalavista.service.{AnalyticsService, SearchService, StoreService}
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class StoreApplication

object StoreApplication {

    def main(args: Array[String]): Unit = {
        SpringApplication.run(classOf[StoreApplication])
    }
}

/**
  * Used to wire up all the services
  */
object ConfigObject {
    val storeService = new StoreService()
    val searchService = new SearchService()
    val analyticsService = new AnalyticsService()
}
