package system

import database.Database
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import settings.Settings

import scala.collection.immutable
import scala.util.Random

object ElevatorSystem {

  def pickup(building: Building, floor: Int, direction: Int): Building =
    val newFloors = building.floors.updated(floor, building.floors(floor) + 1)
    val newOutsideRequests = building.outsideRequests :+ PickupRequest(floor, direction)
    building.copy(floors = newFloors, outsideRequests = newOutsideRequests)

  def update(building: Building, elevatorId: Int, currentFloor: Int, targetFloor: Int): Building =
    val newCabs = building
      .elevatorCabs
      .map(cab =>
        if cab.id == elevatorId then
          val peopleWaiting = building.floors(currentFloor)
          cab.copy(
            currentFloor = currentFloor,
            dropRequests =
              cab.dropRequests.filter(_.targetFloor != currentFloor)
                ++ Range(1, peopleWaiting)
                  .map(_ => DropRequest(1 + Random.nextInt(building.floors.size)))
                + DropRequest(targetFloor),
          )
        else cab
      )
    building.copy(elevatorCabs = newCabs)

  def step(
    building: Building,
    findBestCab: (List[ElevatorCab], PickupRequest, Int) => ElevatorCab,
    random: Random,
  ): Building =

    val buildingRequestsHanded = building.copy(
      elevatorCabs =
        building
          .outsideRequests
          .foldLeft(building.elevatorCabs) { (currentCabs, r) =>
            val newCab = findBestCab(currentCabs, r, building.floors.size)
              .addNewRequest(DropRequest(r.fromFloor))
            currentCabs.map(cab => if cab.id == newCab.id then newCab else cab)
          },
      outsideRequests = Nil,
    )

    val buildingWithNewFloors =
      buildingRequestsHanded.elevatorCabs.foldLeft(buildingRequestsHanded) { (b, cab) =>
        if cab.direction == Direction.Idle then
          val peopleWaiting = b.floors(cab.currentFloor)
          val newRequests = Range(0, peopleWaiting)
            .map(_ =>
              val newDropRequest = DropRequest(1 + random.nextInt(b.floors.size))
              if Settings.IS_DB_ENABLED then Database.insertDropRequest(newDropRequest) else None
              newDropRequest
            )
          val newFloors = b.floors.updated(cab.currentFloor, 0)
          val newCabs = b
            .elevatorCabs
            .map(c =>
              if c.id == cab.id && newRequests.nonEmpty then cab.addMultipleRequests(newRequests)
              else c
            )
          b.copy(floors = newFloors, elevatorCabs = newCabs)
        else b
      }

    val buildingWithCabsStep = buildingWithNewFloors.copy(
      elevatorCabs = buildingWithNewFloors.elevatorCabs.map(_.step)
    )

    buildingWithCabsStep

  def status(building: Building): List[(Int, Int, Int)] = building
    .elevatorCabs
    .map(cab => (cab.id, cab.currentFloor, cab.targetFloor.getOrElse(-1)))

}
