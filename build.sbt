import sbt._
import Keys._


version := "0.2-SNAPSHOT"

scalaVersion := "2.12.13" // "2.13.1"

val scalaTestVersion = "3.0.8"
val scallopVersion = "3.5.0"
val sttpClientVersion = "2.2.2"
val circeVersion = "0.14.2"


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

      "org.apache.spark" %% "spark-sql" % "2.4.0",
      "org.apache.spark" %% "spark-core" % "2.4.0",
    ) ++ Seq("io.circe" %% "circe-yaml" % "0.14.1")
      ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
      ).map(_ % circeVersion)
  )
