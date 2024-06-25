package system

import server.Schemas.DropRequest

import scala.collection.immutable.SortedSet

enum Direction:
  case MovingUp, MovingDown, Idle

implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)

case class ElevatorCab(
  id: Int,
  currentFloor: Int,
  dropRequests: SortedSet[DropRequest],
  direction: Direction = Direction.Idle,
) {

  def targetFloor: Option[Int] =
    direction match
      case Direction.MovingUp =>
        dropRequests
          .minByOption(r => if r.targetFloor > currentFloor then r.targetFloor else Int.MaxValue)
          .map(_.targetFloor)
      case Direction.MovingDown =>
        dropRequests
          .maxByOption(r => if r.targetFloor < currentFloor then r.targetFloor else 0)
          .map(_.targetFloor)
      case Direction.Idle => None

  def step: ElevatorCab =
    if dropRequests.exists(_.targetFloor == currentFloor) then
      this.copy(
        dropRequests = dropRequests.filter(_.targetFloor != currentFloor),
        direction = Direction.Idle,
      )
    else
      direction match
        case Direction.MovingUp   => this.copy(currentFloor = currentFloor + 1)
        case Direction.MovingDown => this.copy(currentFloor = currentFloor - 1)
        case Direction.Idle =>
          this.copy(dropRequests = dropRequests.filter(_.targetFloor != currentFloor))

  def addNewRequest(request: DropRequest): ElevatorCab = this.copy(
    dropRequests = dropRequests + request,
    direction =
      if direction == Direction.Idle then
        if request.targetFloor > currentFloor then Direction.MovingUp
        else if request.targetFloor < currentFloor then Direction.MovingDown
        else direction
      else direction,
  )

  def addMultipleRequests(requests: Seq[DropRequest]): ElevatorCab = this.copy(
    dropRequests = dropRequests ++ requests,
    direction =
      if direction == Direction.Idle then
        val nextFloor = requests.minBy(r => Math.abs(r.targetFloor - currentFloor)).targetFloor
        if nextFloor > currentFloor then Direction.MovingUp
        else if nextFloor < currentFloor then Direction.MovingDown
        else direction
      else direction,
  )

}
