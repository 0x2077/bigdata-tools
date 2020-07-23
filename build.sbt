import sbt._
import Keys._


version := "0.1-SNAPSHOT"

scalaVersion := "2.13.1"

val scalaTestVersion = "3.0.8"
val scallopVersion = "3.5.0"
val sttpClientVersion = "2.2.2"
val circeVersion = "0.13.0"


lazy val bigdata_tools = (project in file("."))
  .settings(
    assemblyMergeStrategy in assembly := {
      case "META-INF/io.netty.versions.properties" => MergeStrategy.first
      case x =>
        val oldStrategy = (assemblyMergeStrategy in assembly).value
        oldStrategy(x)
    },
    mainClass in assembly := Some("my.example.domain.Main"),
    name := "bigdata_tools",
    libraryDependencies ++= Seq(
      "org.scala-lang" % "scala-library" % scalaVersion.value,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test,
      "org.rogach" %% "scallop" % scallopVersion,
      "com.softwaremill.sttp.client" %% "core" % sttpClientVersion,
      "com.softwaremill.sttp.client" %% "circe" % sttpClientVersion,
      "com.softwaremill.sttp.client" %% "async-http-client-backend-future" % sttpClientVersion,
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
      ).map(_ % circeVersion)
  )
