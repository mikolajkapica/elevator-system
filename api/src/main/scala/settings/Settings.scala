package settings

import system.schedulers.PriorityScheduler

object Settings {
  val RNG_SEED = 42
  val RANDOM = new scala.util.Random(RNG_SEED)
  val CAB_QUANTITY = 16
  val FLOORS = 10
  val MAX_PICKUP_REQUESTS_IN_QUANT = 10
  val DEFAULT_SCHEDULER = PriorityScheduler.findBestCab
  val RUN_SIMULATION = true
  val IS_DB_ENABLED = true
}
