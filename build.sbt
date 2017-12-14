name := "JavaRestAPI"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  "org.projectlombok" % "lombok" % "1.16.14"
)

play.Project.playJavaSettings
