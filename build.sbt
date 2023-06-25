name := """pii-test"""
organization := "com.kim"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.10"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "org.mockito" % "mockito-core" % "1.9.5" % "test"
libraryDependencies ++= Seq(
  jdbc,
  "org.postgresql" % "postgresql" % "42.2.17",
  "org.playframework.anorm" %% "anorm" % "2.6.7",
  "org.flywaydb" %% "flyway-play" % "6.0.0",
)