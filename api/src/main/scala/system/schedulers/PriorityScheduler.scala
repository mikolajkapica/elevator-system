package system.schedulers

import server.Schemas.PickupRequest
import system.Direction
import system.ElevatorCab

object PriorityScheduler {

  def findBestCab(elevatorCabs: List[ElevatorCab], request: PickupRequest, floorQuantity: Int)
    : ElevatorCab = {
    val scoredCabs = elevatorCabs.map { cab =>
      val distanceScore = Math.abs(cab.currentFloor - request.fromFloor)
      val directionScore =
        if (
          cab.direction == Direction.Idle ||
          cab.direction == Direction.MovingUp && request.direction > 0 && cab.currentFloor <= request.fromFloor ||
          cab.direction == Direction.MovingDown && request.direction < 0 && cab.currentFloor >= request.fromFloor
        ) 0
        else
          val requestThatCanChangeDirection = cab
            .dropRequests
            .minBy(r =>
              if (request.direction > 0)
                r.targetFloor
              else
                -r.targetFloor
            )
          Math.abs(request.fromFloor - requestThatCanChangeDirection.targetFloor) * 2
      val requestsSizeScore = cab.dropRequests.size * .5
      (cab, distanceScore + directionScore + requestsSizeScore)
    }

    val bestCab = scoredCabs.minBy(_._2)._1
    bestCab
  }

}
