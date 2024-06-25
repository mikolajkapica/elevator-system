package system.schedulers

import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import system.Building
import system.ElevatorCab

import scala.util.Random

object FIFOScheduler {

  def findBestCab(elevatorCabs: List[ElevatorCab], request: PickupRequest, floorQuantity: Int)
    : ElevatorCab = elevatorCabs.minBy(_.dropRequests.size)

}
