package com.ixxus.hastalavista

import com.ixxus.hastalavista.service.{SearchService, StoreService}
import com.ixxus.hastalavista.store.HastaStoreComponent
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

object ConfigObject extends HastaStoreComponent {
    val hastaStore = new HastaStoreImpl()
    val storeService = new StoreService()
    val searchService = new SearchService()
}
