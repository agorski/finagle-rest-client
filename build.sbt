name := "albgorski-finagle-rest-client"

organization := "albgorski"

version := "1.0.0"

scalaVersion := "2.11.6"

libraryDependencies ++= {
  val finagleVersion = "6.25.0"
  Seq(
    "com.twitter"     %% "finagle-core"     % finagleVersion,
    "com.twitter"     %% "finagle-http"     % finagleVersion,
    "ch.qos.logback"  % "logback-classic"  % "1.1.3"
  )
}

javaOptions in run ++= Seq(
  "-Xms512m", "-Xmx512m"
)

// Assembly settings
mainClass in Global := Some("albgorski.finagle.Main")

assemblyJarName in assembly := s"${name.value}.jar"

fork in run := true