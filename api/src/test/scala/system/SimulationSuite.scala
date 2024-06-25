package system

import org.scalatest.funsuite.AnyFunSuite
import server.Schemas.DropRequest
import server.Schemas.PickupRequest

import scala.collection.immutable.HashMap
import scala.util.Random

class SimulationSuite extends AnyFunSuite {
  implicit val dropRequestOrdering: Ordering[DropRequest] = Ordering.by(_.targetFloor)
  private final val RNG_SEED = 42
  private final val RANDOM = new Random(RNG_SEED)
  private final val CAB_QUANTITY = 16
  private final val FLOORS = 10
  private final val MAX_PICKUP_REQUESTS_PER_STEP = 10

  class SimulationTest extends AnyFunSuite {

    test("Generate Building") {
      val building = Simulation.generateBuilding()
      assert(building.floors.size == FLOORS)
      assert(building.floors.forall(_._2 == 0))
      assert(building.elevatorCabs.size == CAB_QUANTITY)
      assert(building.outsideRequests.isEmpty)
    }

    test("Run Simulation Step") {
      val initialBuilding = Simulation.generateBuilding()
      val updatedBuilding = Simulation.run(initialBuilding)

      assert(updatedBuilding.floors.size == FLOORS)
      assert(updatedBuilding.elevatorCabs.size == CAB_QUANTITY)
    }

    test("Simulation handles new requests correctly") {
      val initialBuilding = Simulation.generateBuilding()
      val newRequests = List(PickupRequest(1, 1), PickupRequest(2, -1))
      val buildingWithNewRequests = initialBuilding.addPickupRequests(newRequests)
      assert(buildingWithNewRequests.outsideRequests == newRequests)
      assert(newRequests.forall(req => buildingWithNewRequests.floors(req.fromFloor) > 0))
    }
  }

}
