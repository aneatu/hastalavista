package com.ixxus.hastalavista.datastore

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.{ComponentScan, Configuration}

@Configuration
@EnableAutoConfiguration
@ComponentScan
class DatastoreApplication

object DatastoreApplication  {

    def main(args: Array[String]): Unit = {
        SpringApplication.run(classOf[DatastoreApplication])
    }

}
