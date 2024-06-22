import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.concurrent.Await
import scala.concurrent.Promise
import scala.concurrent.duration.Duration

object Server {

  def run() =
    implicit val system = ActorSystem(Behaviors.empty, "api")
    implicit val executionContext = system.executionContext

    val host = "0.0.0.0"
    val port = 8080

    val bindingFuture = Http()
      .newServerAt(host, port)
      .bind(Router.route)

    println(s"Server online at http://$host:$port/")

    val keepAlive = Promise[Unit].future
    Await.result(keepAlive, Duration.Inf)

}
