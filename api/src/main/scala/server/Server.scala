package server

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import settings.Settings
import system.Simulation

import scala.concurrent.Await
import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.Promise
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.duration.Duration

object Server {

  var building = Simulation.generateBuilding()

  def run() =
    implicit val system: ActorSystem[Any] = ActorSystem(Behaviors.empty, "api")
    implicit val executionContext: ExecutionContextExecutor = system.executionContext

    val host = "0.0.0.0"
    val port = 8080

    val bindingFuture = Http()
      .newServerAt(host, port)
      .bind(Router.route)

    bindingFuture.onComplete { _ =>
      println(s"Server online at http://$host:$port/")

      if (Settings.RUN_SIMULATION) {
        system
          .scheduler
          .scheduleAtFixedRate(
            initialDelay = FiniteDuration(0, "seconds"),
            interval = FiniteDuration(1, "seconds"),
          ) { () =>
            building = Simulation.run(building)
          }
      }
    }

    val keepAlive = Promise[Unit].future
    Await.result(keepAlive, Duration.Inf)

}
