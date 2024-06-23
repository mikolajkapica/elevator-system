import org.mongodb.scala.MongoClient
import org.mongodb.scala.MongoDatabase

object Database {

  def connect(db_name: String): MongoDatabase = MongoClient(sys.env("DATABASE_URL"))
    .getDatabase(db_name)

}
