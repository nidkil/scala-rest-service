name := "RestService"

organization := "my organisation"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.11.2"

jetty()

libraryDependencies += "javax.servlet" % "servlet-api" % "2.5" % "provided"

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.2"

libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.1.1"
