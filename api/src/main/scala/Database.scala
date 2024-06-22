import org.mongodb.scala.Document
import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoCollection

import tour.Helpers._

object Database {

  def test(): Unit =
    println("Connecting to MongoDB...")
    val mongoClient = MongoClient(sys.env("DATABASE_URL"))
    val database = mongoClient.getDatabase("mydb")
    database.createCollection("test2")
    val collection: MongoCollection[Document] = database.getCollection("test2")

    val document = Document(
      "name" -> "MongoDB",
      "type" -> "database",
      "count" -> 1,
      "info" -> Document("x" -> 203, "y" -> 102),
    )

    collection.insertOne(document).results()
    collection.find().printResults()
    println("Document inserted")

}
