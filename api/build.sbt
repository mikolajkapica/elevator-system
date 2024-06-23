name := "api"
version := "1.0"
scalaVersion := "3.4.2"

resolvers += "Akka library repository" at "https://repo.akka.io/maven"

enablePlugins(JavaAppPackaging)

val AkkaVersion = "2.9.3"
libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "3.2.18" % "test",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % "10.6.3",
    "org.slf4j" % "slf4j-simple" % "2.0.13",
  )
}
libraryDependencies += ("org.mongodb.scala" %% "mongo-scala-driver" % "5.1.0").cross(
  CrossVersion.for3Use2_13
)
