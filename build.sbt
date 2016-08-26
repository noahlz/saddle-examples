name := "saddle-examples"

organization := "com.github.noahlz"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

crossScalaVersions := Seq("2.10.4", "2.11.2")

libraryDependencies ++= Seq(
  "org.scala-saddle" %% "saddle-core" % "1.3.4"
)

Defaults.itSettings

lazy val root = project.in(file(".")).configs(IntegrationTest)

initialCommands := """
  import org.saddle._
  import com.github.noahlz.saddle.examples.SaddleExamples._
"""
