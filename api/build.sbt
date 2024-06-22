name := "api"
version := "1.0"
scalaVersion := "3.4.2"

resolvers += "Akka library repository" at "https://repo.akka.io/maven"

enablePlugins(JavaAppPackaging)

val scalatestVersion = "3.2.18"
val AkkaVersion = "2.9.3"
val AkkaHttpVersion = "10.6.3"
val MongoDBVersion = "5.1.0"
libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "org.slf4j" % "slf4j-simple" % "2.0.13",
  )
}
libraryDependencies += ("org.mongodb.scala" %% "mongo-scala-driver" % MongoDBVersion).cross(
  CrossVersion.for3Use2_13
)
