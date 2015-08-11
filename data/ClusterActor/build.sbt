import AssemblyKeys._

assemblySettings

name := "HeavyPiClusterProject"

version := "1.0"
     
scalaVersion := "2.10.3"
     
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
     
libraryDependencies +=
    "com.typesafe.akka" %% "akka-actor" % "2.2.3"

libraryDependencies +=
    "com.typesafe.akka" %% "akka-cluster" % "2.2.3"