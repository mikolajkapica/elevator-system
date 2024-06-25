import org.scalatest.funsuite.AnyFunSuite
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import system.Building
import system.ElevatorCab

import scala.collection.immutable.HashMap
import scala.collection.immutable.SortedSet

class BuildingTest extends AnyFunSuite {
  implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)

  test("Add person waiting on a specific floor") {
    val initialFloors = HashMap(1 -> 0, 2 -> 0, 3 -> 0)
    val building = Building(initialFloors, List(), List())

    val updatedBuilding = building.addPersonWaitingOnFloor(2)

    assert(updatedBuilding.floors(1) == 0)
    assert(updatedBuilding.floors(2) == 1)
    assert(updatedBuilding.floors(3) == 0)
  }

  test("Add multiple pickup requests") {
    val initialFloors = HashMap(1 -> 0, 2 -> 0, 3 -> 0, 4 -> 0)
    val pickupRequests = List(
      PickupRequest(2, 1),
      PickupRequest(3, -1),
      PickupRequest(2, 1),
    )
    val building = Building(initialFloors, List(), List())

    val updatedBuilding = building.addPickupRequests(pickupRequests)

    assert(updatedBuilding.floors(1) == 0)
    assert(updatedBuilding.floors(2) == 2)
    assert(updatedBuilding.floors(3) == 1)
    assert(updatedBuilding.floors(4) == 0)
    assert(updatedBuilding.outsideRequests == pickupRequests)
  }

  test("Add person waiting on an already populated floor") {
    val initialFloors = HashMap(1 -> 1, 2 -> 3, 3 -> 0)
    val building = Building(initialFloors, List(), List())

    val updatedBuilding = building.addPersonWaitingOnFloor(2)

    assert(updatedBuilding.floors(1) == 1)
    assert(updatedBuilding.floors(2) == 4)
    assert(updatedBuilding.floors(3) == 0)
  }

  test("Add pickup requests with existing requests") {
    val initialFloors = HashMap(1 -> 1, 2 -> 2, 3 -> 0)
    val existingRequests = List(PickupRequest(1, 1))
    val newRequests = List(
      PickupRequest(2, 1),
      PickupRequest(3, -1),
    )
    val building = Building(initialFloors, List(), existingRequests)

    val updatedBuilding = building.addPickupRequests(newRequests)

    assert(updatedBuilding.floors(1) == 1)
    assert(updatedBuilding.floors(2) == 3)
    assert(updatedBuilding.floors(3) == 1)
    assert(updatedBuilding.outsideRequests == existingRequests ++ newRequests)
  }

  test("Add pickup request and check elevators unchanged") {
    val initialFloors = HashMap(1 -> 0, 2 -> 0, 3 -> 0)
    val initialCabs = List(ElevatorCab(1, 1, SortedSet(DropRequest(2))))
    val pickupRequests = List(PickupRequest(2, 1))
    val building = Building(initialFloors, initialCabs, List())

    val updatedBuilding = building.addPickupRequests(pickupRequests)

    assert(updatedBuilding.floors(1) == 0)
    assert(updatedBuilding.floors(2) == 1)
    assert(updatedBuilding.floors(3) == 0)
    assert(updatedBuilding.elevatorCabs == initialCabs)
  }
}
