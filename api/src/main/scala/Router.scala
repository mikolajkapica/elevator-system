import akka.http.scaladsl.model.ContentTypes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.Directives.get
import akka.http.scaladsl.server.Directives.path
import akka.http.scaladsl.server.Directives.pathSingleSlash
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._enhanceRouteWithConcatenation

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
      }

}
