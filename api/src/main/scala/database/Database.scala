package database

import database.MongoHelpers.*
import org.joda.time.LocalDateTime
import org.mongodb.scala.*
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.result.InsertOneResult
import server.Schemas.DropRequest
import server.Schemas.PickupRequest
import server.Schemas.UpdateRequest

object Database {

  private final val DB_NAME = "requests"
  private final val PICKUP_REQUESTS = "pickup"
  private final val DROP_REQUESTS = "drop"
  private final val UPDATE_REQUESTS = "update"
  private final val DATABASE_URL = sys.env("DATABASE_URL")
  private final val mongo = MongoClient(DATABASE_URL)

  def insertPickupRequest(outsideElevatorRequest: PickupRequest): Seq[InsertOneResult] = mongo
    .getDatabase(DB_NAME)
    .getCollection(PICKUP_REQUESTS)
    .insertOne(
      Document(
        "fromFloor" -> outsideElevatorRequest.fromFloor,
        "direction" -> outsideElevatorRequest.direction,
        "timestamp" -> LocalDateTime.now().toString(),
      )
    )
    .results()

  def insertDropRequest(dropRequest: DropRequest): Seq[InsertOneResult] = mongo
    .getDatabase(DB_NAME)
    .getCollection(DROP_REQUESTS)
    .insertOne(
      Document(
        "targetFloor" -> dropRequest.targetFloor,
        "timestamp" -> LocalDateTime.now().toString(),
      )
    )
    .results()

  def insertUpdateRequest(updateRequest: UpdateRequest): Seq[InsertOneResult] = mongo
    .getDatabase(DB_NAME)
    .getCollection(UPDATE_REQUESTS)
    .insertOne(
      Document(
        "elevatorId" -> updateRequest.elevatorId,
        "currentFloor" -> updateRequest.currentFloor,
        "targetFloor" -> updateRequest.targetFloor,
        "timestamp" -> LocalDateTime.now().toString(),
      )
    )
    .results()

  def getPickupRequests: List[String] =
    mongo
      .getDatabase(DB_NAME)
      .getCollection(PICKUP_REQUESTS)
      .find()
      .results()
      .map(_.toJson())
      .toList

  def getDropRequests: List[String] =
    mongo
      .getDatabase(DB_NAME)
      .getCollection(DROP_REQUESTS)
      .find()
      .results()
      .map(_.toJson())
      .toList

  def getUpdateRequests: List[String] =
    mongo
      .getDatabase(DB_NAME)
      .getCollection(UPDATE_REQUESTS)
      .find()
      .results()
      .map(_.toJson())
      .toList

}
