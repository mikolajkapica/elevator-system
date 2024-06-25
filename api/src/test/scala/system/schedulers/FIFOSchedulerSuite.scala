package system.schedulers

import org.scalatest.funsuite.AnyFunSuite
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import system.ElevatorCab
import system.schedulers.FIFOScheduler.findBestCab

import scala.collection.immutable.SortedSet

class FIFOSchedulerSuite extends AnyFunSuite {
  implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)

  test("Single Elevator") {
    val elevator = ElevatorCab(1, 1, SortedSet(DropRequest(3)))
    val request = PickupRequest(5, 1)
    val result = findBestCab(List(elevator), request, 10)
    assert(result == elevator)
  }

  test("Second elevator is closer") {
    val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)))
    val elevator2 = ElevatorCab(2, 4, SortedSet(DropRequest(4)))
    val request = PickupRequest(5, 1)
    val result = findBestCab(List(elevator1), request, 10)
    assert(result == elevator1)
  }

  test("Second elevator is already on the pickup floor") {
    val elevator1 = ElevatorCab(1, 1, SortedSet(DropRequest(3)))
    val elevator2 = ElevatorCab(2, 4, SortedSet(DropRequest(2)))
    val request = PickupRequest(4, 1)
    val result = findBestCab(List(elevator1, elevator2), request, 10)
    assert(result == elevator1)
  }

}
