import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.concurrent.Await
import scala.concurrent.Promise
import scala.concurrent.duration.Duration

object Server {

  def run() =
    while (true) {
      println("Hello, world!")
      Thread.sleep(1000)
    }
//    implicit val system = ActorSystem(Behaviors.empty, "my-system")
//    implicit val executionContext = system.executionContext
//    val keepAlive = Promise[Unit].future
//
//    val host = "0.0.0.0"
//    val port = 8080
//
//    val bindingFuture = Http()
//      .newServerAt(host, port)
//      .bind(Routes.route)
//
//    Database.test()
//
//    Await.result(keepAlive, Duration.Inf)

}
