package system.schedulers

import org.scalatest.funsuite.AnyFunSuite
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import system.Direction
import system.ElevatorCab
import system.schedulers.FIFOScheduler.findBestCab

import scala.collection.immutable.SortedSet

class PrioritySchedulerSuite extends AnyFunSuite {
  implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)

  class ElevatorSystemTest extends AnyFunSuite {

    test("Single Elevator") {
      val elevator = ElevatorCab(1, 1, SortedSet(DropRequest(3)))
      val request = PickupRequest(5, 1)
      val result = findBestCab(List(elevator), request, 10)
      assert(result == elevator)
    }

    test("Multiple Elevators, Same Floor Requests") {
      val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)), Direction.MovingUp)
      val elevator2 = ElevatorCab(2, 5, SortedSet(DropRequest(4)), Direction.MovingUp)
      val request = PickupRequest(5, 1)
      val result = findBestCab(List(elevator1, elevator2), request, 10)
      assert(result == elevator2)
    }

    test("Multiple Elevators, Different Floor Requests") {
      val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)), Direction.MovingUp)
      val elevator2 = ElevatorCab(
        2,
        2,
        SortedSet(DropRequest(4), DropRequest(5)),
        Direction.MovingUp,
      )
      val request = PickupRequest(6, 1)
      val result = findBestCab(List(elevator1, elevator2), request, 10)
      assert(result == elevator2)
    }

    test("Elevators with Different Directions") {
      val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)), Direction.MovingUp)
      val elevator2 = ElevatorCab(2, 5, SortedSet(DropRequest(2)), Direction.MovingDown)
      val request = PickupRequest(4, 1)
      val result = findBestCab(List(elevator1, elevator2), request, 10)
      assert(result == elevator1)
    }

    test("Elevators in Idle State") {
      val elevator1 = ElevatorCab(1, 1, SortedSet(), Direction.Idle)
      val elevator2 = ElevatorCab(2, 5, SortedSet(DropRequest(2)), Direction.MovingDown)
      val request = PickupRequest(4, 1)
      val result = findBestCab(List(elevator1, elevator2), request, 10)
      assert(result == elevator1)
    }

    test("Elevator Already on the Pickup Floor") {
      val elevator1 = ElevatorCab(1, 4, SortedSet(DropRequest(3)))
      val elevator2 = ElevatorCab(2, 5, SortedSet(DropRequest(2)))
      val request = PickupRequest(4, 1)
      val result = findBestCab(List(elevator1, elevator2), request, 10)
      assert(result == elevator1)
    }

    test("No Elevators Available") {
      val request = PickupRequest(4, 1)
      val result = findBestCab(List(), request, 10)
      assert(result == null)
    }

    test("Elevators with Mixed Requests and Directions") {
      val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)), Direction.MovingUp)
      val elevator2 = ElevatorCab(
        2,
        4,
        SortedSet(DropRequest(6), DropRequest(7)),
        Direction.MovingUp,
      )
      val elevator3 = ElevatorCab(3, 2, SortedSet(DropRequest(5)), Direction.MovingDown)
      val request = PickupRequest(3, 1)
      val result = findBestCab(List(elevator1, elevator2, elevator3), request, 10)
      assert(result == elevator1)
    }

    test("Elevators with Idle and Busy States") {
      val elevator1 = ElevatorCab(1, 3, SortedSet(), Direction.Idle)
      val elevator2 = ElevatorCab(
        2,
        1,
        SortedSet(DropRequest(4), DropRequest(6)),
        Direction.MovingUp,
      )
      val elevator3 = ElevatorCab(3, 5, SortedSet(DropRequest(7)), Direction.MovingDown)
      val request = PickupRequest(2, 1)
      val result = findBestCab(List(elevator1, elevator2, elevator3), request, 10)
      assert(result == elevator1)
    }
  }

}
