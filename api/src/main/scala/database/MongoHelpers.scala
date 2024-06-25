package database

import org.mongodb.scala.*

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object MongoHelpers {

  implicit class DocumentObservable[C](val observable: Observable[Document])
    extends ImplicitObservable[Document] {
    override val converter: Document => String = doc => doc.toJson
  }

  implicit class GenericObservable[C](val observable: Observable[C]) extends ImplicitObservable[C] {
    override val converter: C => String = doc => Option(doc).map(_.toString).getOrElse("")
  }

  trait ImplicitObservable[C] {
    val observable: Observable[C]
    val converter: C => String

    def results(): Seq[C] = Await.result(observable.toFuture(), Duration(10, TimeUnit.SECONDS))
    def headResult(): C = Await.result(observable.head(), Duration(10, TimeUnit.SECONDS))

    def printResults(initial: String = ""): Unit = {
      if (initial.nonEmpty)
        print(initial)
      results().foreach(res => println(converter(res)))
    }

    def printHeadResult(initial: String = ""): Unit = println(
      s"$initial${converter(headResult())}"
    )

  }

}
