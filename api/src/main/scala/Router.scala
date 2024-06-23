import Database.connect
import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.pathSingleSlash
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation
import org.mongodb.scala.bson.collection.immutable.Document
import tour.Helpers._

object Router {

  val route: Route =
    pathSingleSlash {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Welcome to the API</h1>"))
      }
    } ~
      path("hello") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
        }
      } ~
      path("health") {
        get {
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>OK</h1>"))
        }
      } ~
      path("db-test") {
        get {
          val db = connect("test")
          db.createCollection("test")
          db.getCollection("test").insertOne(Document("name" -> "test")).results()
          val result = db.getCollection("test").find().results().map(_.toJson).mkString(", ")

          complete(
            HttpEntity(
              ContentTypes.`text/html(UTF-8)`,
              s"<h1>DB Test</h1><p>Result: $result</p>",
            )
          )
        }
      }

}
