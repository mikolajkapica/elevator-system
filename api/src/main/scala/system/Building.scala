package system

import server.Schemas.PickupRequest

import scala.collection.immutable.HashMap

type PeopleWaiting = Int
type FloorNumber = Int

case class Building(
  floors: HashMap[FloorNumber, PeopleWaiting],
  elevatorCabs: List[ElevatorCab],
  outsideRequests: List[PickupRequest],
) {

  def addPersonWaitingOnFloor(floor: Int): Building = this.copy(floors =
    floors.updated(floor, floors(floor) + 1)
  )

  def addPickupRequest(request: PickupRequest): Building = this.copy(
    outsideRequests = request :: outsideRequests,
    floors = floors.updated(request.fromFloor, floors(request.fromFloor) + 1),
  )

  def addPickupRequests(requests: List[PickupRequest]): Building = this.copy(
    outsideRequests = outsideRequests ::: requests,
    floors = requests.foldLeft(floors)((f, r) => f.updated(r.fromFloor, f(r.fromFloor) + 1)),
  )

}
