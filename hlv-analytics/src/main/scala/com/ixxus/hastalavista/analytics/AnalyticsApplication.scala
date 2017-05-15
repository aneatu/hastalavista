package com.ixxus.hastalavista.analytics

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class AnalyticsApplication

object AnalyticsApplication  {

    def main(args: Array[String]): Unit = {
        SpringApplication.run(classOf[AnalyticsApplication])
    }

}
