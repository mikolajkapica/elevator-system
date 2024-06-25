package system

import org.scalatest.funsuite.AnyFunSuite
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import system.Building
import system.ElevatorCab

import scala.collection.immutable.HashMap
import scala.collection.immutable.SortedSet

class ElevatorCabSuite extends AnyFunSuite {
  implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)

  test("Get target floor when moving up") {
    val dropRequests = SortedSet(DropRequest(5), DropRequest(10))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, dropRequests, Direction.MovingUp)
    assert(elevator.targetFloor.contains(5))
  }

  test("Get target floor when moving down") {
    val dropRequests =
      SortedSet(DropRequest(1), DropRequest(2), DropRequest(5))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 4, dropRequests, Direction.MovingDown)
    assert(elevator.targetFloor.contains(2))
  }

  test("Get target floor when idle") {
    val dropRequests = SortedSet(DropRequest(1), DropRequest(2))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 4, dropRequests, Direction.Idle)
    assert(elevator.targetFloor.isEmpty)
  }

  test("Step when moving up") {
    val dropRequests = SortedSet(DropRequest(5))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, dropRequests, Direction.MovingUp)
    val updatedElevator = elevator.step
    assert(updatedElevator.currentFloor == 4)
  }

  test("Step when moving down") {
    val dropRequests = SortedSet(DropRequest(1))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, dropRequests, Direction.MovingDown)
    val updatedElevator = elevator.step
    assert(updatedElevator.currentFloor == 2)
  }

  test("Step when idle and on target floor") {
    val dropRequests = SortedSet(DropRequest(3))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, dropRequests, Direction.Idle)
    val updatedElevator = elevator.step
    assert(updatedElevator.dropRequests.isEmpty)
  }

  test("Add new request when idle") {
    val dropRequests = SortedSet(DropRequest(5))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, SortedSet.empty, Direction.Idle)
    val updatedElevator = elevator.addNewRequest(DropRequest(5))
    assert(updatedElevator.dropRequests.contains(DropRequest(5)))
    assert(updatedElevator.direction == Direction.MovingUp)
  }

  test("Add new request when moving up") {
    val dropRequests = SortedSet(DropRequest(7))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 5, dropRequests, Direction.MovingUp)
    val updatedElevator = elevator.addNewRequest(DropRequest(9))
    assert(updatedElevator.dropRequests.contains(DropRequest(9)))
    assert(updatedElevator.direction == Direction.MovingUp)
  }

  test("Add new request when moving down") {
    val dropRequests = SortedSet(DropRequest(3))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 5, dropRequests, Direction.MovingDown)
    val updatedElevator = elevator.addNewRequest(DropRequest(1))
    assert(updatedElevator.dropRequests.contains(DropRequest(1)))
    assert(updatedElevator.direction == Direction.MovingDown)
  }

  test("Add multiple requests when idle") {
    val dropRequests = Seq(DropRequest(5), DropRequest(2))
    val expectedDropRequests = SortedSet(DropRequest(2), DropRequest(5))(dropRequestOrdering)
    val elevator = ElevatorCab(1, 3, SortedSet.empty, Direction.Idle)
    val updatedElevator = elevator.addMultipleRequests(dropRequests)
    assert(updatedElevator.dropRequests == expectedDropRequests)
    assert(updatedElevator.direction == Direction.MovingDown)
  }

  test("Add multiple requests when moving up") {
    val initialRequests = SortedSet(DropRequest(7))(dropRequestOrdering)
    val newRequests = Seq(DropRequest(9), DropRequest(10))
    val elevator = ElevatorCab(1, 5, initialRequests, Direction.MovingUp)
    val updatedElevator = elevator.addMultipleRequests(newRequests)
    assert(
      updatedElevator.dropRequests == SortedSet(DropRequest(7), DropRequest(9), DropRequest(10))
    )
    assert(updatedElevator.direction == Direction.MovingUp)
  }

  test("Add multiple requests when moving down") {
    val initialRequests = SortedSet(DropRequest(3))(dropRequestOrdering)
    val newRequests = Seq(DropRequest(1), DropRequest(2))
    val elevator = ElevatorCab(1, 5, initialRequests, Direction.MovingDown)
    val updatedElevator = elevator.addMultipleRequests(newRequests)
    assert(
      updatedElevator.dropRequests == SortedSet(DropRequest(1), DropRequest(2), DropRequest(3))
    )
    assert(updatedElevator.direction == Direction.MovingDown)
  }
}
