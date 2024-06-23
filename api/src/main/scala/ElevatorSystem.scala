import com.github.nscala_time.time.Imports.*

enum Direction:
  case Up, Down, Open

case class ElevatorCabState(floor: Int, direction: Direction)
case class Capacity(weight: Int, people: Int, velocity: Int)
class ElevatorCab(state: ElevatorCabState, capacity: Capacity, requests: List[Request])
case class Building(floors: Int, elevatorCabs: List[ElevatorCab])
case class Request(floor: Int, direction: Direction, time: DateTime)

val elevators =
  List.fill(16)(
    ElevatorCab(
      ElevatorCabState(0, Direction.Up),
      Capacity(1000, 10, 10),
      List.empty,
    )
  )

val building = Building(10, elevators)

val requests = List(
  Request(5, Direction.Up, DateTime.now()),
  Request(3, Direction.Down, DateTime.now()),
  Request(7, Direction.Up, DateTime.now()),
  Request(2, Direction.Down, DateTime.now()),
  Request(8, Direction.Up, DateTime.now()),
  Request(1, Direction.Down, DateTime.now()),
  Request(9, Direction.Up, DateTime.now()),
)
