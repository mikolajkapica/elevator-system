package server

import akka.http.scaladsl.server.Directives
import akka.http.scaladsl.server.Route
import database.Database
import server.Schemas.JsonSupport
import server.Schemas.PickupRequest
import server.Schemas.UpdateRequest
import settings.Settings
import system.ElevatorSystem
import system.Simulation

import scala.language.postfixOps

object Router extends Directives with JsonSupport {

  private val pickupRoute: Route =
    path("elevator" / "pickup") {
      post {
        entity(as[PickupRequest]) { request =>
          Database.insertPickupRequest(request)
          Server.building = ElevatorSystem.pickup(
            Server.building,
            request.fromFloor,
            request.direction,
          )
          complete(s"Pickup request from floor ${request.fromFloor} to go ${
              if request.direction > 0 then "up" else "down"
            }")
        }
      }
    }

  private val updateRoute: Route =
    path("elevator" / "update") {
      patch {
        entity(as[UpdateRequest]) { request =>
          Database.insertUpdateRequest(request)
          Server.building = ElevatorSystem.update(
            Server.building,
            request.elevatorId,
            request.currentFloor,
            request.targetFloor,
          )
          complete(
            s"Update request for elevator ${request.elevatorId} from floor ${request.currentFloor} to go to floor ${request.targetFloor}"
          )
        }
      }
    }

  private val statusRoute: Route =
    path("elevator" / "status") {
      get {
        complete(
          ElevatorSystem
            .status(Server.building)
            .map((id, currentFloor, targetFloor) =>
              s"Elevator $id is on floor $currentFloor and is ${
                  if targetFloor != -1 then s"going to floor $targetFloor" else "idle"
                }"
            )
            .mkString("\n")
        )
      }
    }

  private val floorsRoute: Route =
    path("elevator" / "floors") {
      get {
        complete(
          Server
            .building
            .floors
            .map(f => s"Floor ${f._1}: ${f._2} people waiting")
            .mkString("\n")
        )
      }
    }

  private val stepRoute: Route =
    path("elevator" / "step") {
      post {
        Server.building = ElevatorSystem.step(
          Server.building,
          Settings.DEFAULT_SCHEDULER,
          Settings.RANDOM,
        )
        complete(
          "The simulation has advanced one step"
        )
      }
    }

  private val simulateNewPickupRequestsRoute: Route =
    path("elevator" / "simulate") {
      post {
        val newRequests = Simulation.simulateNewPickupRequests(Server.building)
        newRequests.foreach(Database.insertPickupRequest)
        Server.building = Server.building.addPickupRequests(newRequests)
        complete(
          s"Simulated ${newRequests.size} new pickup requests"
        )
      }
    }

  val route: Route = concat(
    pickupRoute,
    updateRoute,
    statusRoute,
    floorsRoute,
    stepRoute,
    simulateNewPickupRequestsRoute,
  )

}
