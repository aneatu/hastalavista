package com.ixxus.hastalavista

import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach, FlatSpec, Matchers}

import scala.reflect.ClassTag

/**
  * StoreTest trait providing ScalaTest functionality
  *
  * Created by Michael.Seddon on 09-May-17.
  */
trait StoreTest extends FlatSpec with BeforeAndAfterEach with BeforeAndAfterAll with Matchers {

    /** Matcher for comparing with null. E.g. xxx should nullable */
    def nullable[T: ClassTag] =
        Matcher { (nullObj: T) =>
            val clazz = implicitly[ClassTag[T]].runtimeClass
            val toCompare = nullObj match {
                case anyShould: AnyShouldWrapper[clazz] => anyShould.leftSideValue
                case _ => nullObj
            }
            MatchResult(
                toCompare == null,
                toCompare + " was not null",
                toCompare + " was null"
            )
        }

}
