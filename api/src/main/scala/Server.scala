import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Server {

  def run(): Unit =
    implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "my-system")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val host = "0.0.0.0"
    val port = 8080

    val bindingFuture = Http()
      .newServerAt(host, port)
      .bind(Routes.route)

    println(s"Server online at http://$host:$port/\nPress RETURN to stop...")

    Database.test()

    StdIn.readLine()
    bindingFuture
      .flatMap(_.unbind())
      .onComplete(_ => system.terminate())

}
