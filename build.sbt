val scala3Version = "3.6.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "alu-chess",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % Test
  )
