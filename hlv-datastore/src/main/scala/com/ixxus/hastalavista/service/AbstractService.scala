package com.ixxus.hastalavista.service

import com.ixxus.hastalavista.ConfigObject

/**
  * Created by Michael.Seddon on 11-May-17.
  */
trait AbstractService {
    val pageStore = ConfigObject.hastaStore.pageStore
    val anaStore = ConfigObject.hastaStore.anaStore
}
