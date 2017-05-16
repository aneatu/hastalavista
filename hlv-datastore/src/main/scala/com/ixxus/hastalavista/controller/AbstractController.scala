package com.ixxus.hastalavista.controller

import com.ixxus.hastalavista.ConfigObject

/**
  * Created by Michael.Seddon on 11-May-17.
  */
trait AbstractController {
    val pageStore = ConfigObject.hastaStore.pageStore
}
