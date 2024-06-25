package server

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol
import spray.json.RootJsonFormat

object Schemas {

  case class PickupRequest(
    fromFloor: Int,
    direction: Int,
  ) // direction is positive for up, negative for down

  case class DropRequest(targetFloor: Int)

  case class UpdateRequest(elevatorId: Int, currentFloor: Int, targetFloor: Int)

  trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

    implicit val pickupRequestFormat: RootJsonFormat[PickupRequest] = jsonFormat2(
      PickupRequest.apply
    )

    implicit val dropRequestFormat: RootJsonFormat[DropRequest] = jsonFormat1(
      DropRequest.apply
    )

    implicit val updateFormat: RootJsonFormat[UpdateRequest] = jsonFormat3(
      UpdateRequest.apply
    )

  }

}
