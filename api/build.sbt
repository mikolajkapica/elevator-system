name := "api"
version := "1.0"
scalaVersion := "3.4.2"

resolvers += "Akka library repository" at "https://repo.akka.io/maven"

enablePlugins(JavaAppPackaging)

val AkkaVersion = "2.9.3"
val AkkaHttpVersion = "10.6.3"
val CirceVersion = "0.14.8"
val AkkaHttpJsonSerializersVersion = "1.39.2"
libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "3.2.18" % "test",
    "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
    "org.slf4j" % "slf4j-simple" % "2.0.13",
    "com.github.nscala-time" %% "nscala-time" % "2.32.0",
    ("org.mongodb.scala" %% "mongo-scala-driver" % "5.1.0").cross(CrossVersion.for3Use2_13),
  )
}
