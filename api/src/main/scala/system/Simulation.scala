package system

import database.Database
import server.Schemas.PickupRequest
import settings.Settings
import system.ElevatorSystem.*
import system.FloorNumber
import system.PeopleWaiting

import scala.collection.immutable.HashMap
import scala.collection.immutable.SortedSet
import scala.util.Random

val DEBUG = true

object Simulation {

  private def generateCabs(cabQuantity: Int, floorQuantity: Int, random: Random)
    : List[ElevatorCab] =
    Range(1, cabQuantity)
      .map(id =>
        ElevatorCab(
          id,
          1 + random.nextInt(floorQuantity),
          SortedSet.empty,
        )
      )
      .toList

  def generateBuilding(
    cabQuantity: Int = Settings.CAB_QUANTITY,
    floorQuantity: Int = Settings.FLOORS,
    random: Random = Settings.RANDOM,
  ): Building = Building(
    floors = HashMap
      .newBuilder[FloorNumber, PeopleWaiting]
      .addAll(Range(1, Settings.FLOORS + 1).map(_ -> 0))
      .result(),
    elevatorCabs = generateCabs(cabQuantity, floorQuantity, random),
    outsideRequests = Nil,
  )

  def simulateNewPickupRequests(building: Building): List[PickupRequest] =
    Range(1, Settings.RANDOM.nextInt(Settings.MAX_PICKUP_REQUESTS_IN_QUANT))
      .map(_ =>
        PickupRequest(
          1 + Settings.RANDOM.nextInt(building.floors.size),
          if Settings.RANDOM.nextBoolean() then 1 else -1,
        )
      )
      .toList

  def run(
    building: Building = generateBuilding()
  ): Building =
    val newRequests = simulateNewPickupRequests(building)
    if Settings.IS_DB_ENABLED then newRequests.foreach(Database.insertPickupRequest)
    val buildingWithNewRequests = building.addPickupRequests(newRequests)

    if (DEBUG) {
      println("New requests")
      println(newRequests)
    }

    val newBuilding = ElevatorSystem.step(
      buildingWithNewRequests,
      Settings.DEFAULT_SCHEDULER,
      Settings.RANDOM,
    )

    if (DEBUG) {
      println("Building floors")
      println(
        newBuilding
          .floors
          .map(f => s"Floor ${f._1}: ${f._2} people waiting")
          .mkString("\n")
      )

      println("Elevator status")
      println(
        ElevatorSystem
          .status(newBuilding)
          .map(el => s"Elevator(${el(0)}) currentFloor: ${el(1)} targetFloor: ${el(2)}")
          .mkString("\n")
      )
    }

    newBuilding

}
