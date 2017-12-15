import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName = "playApplication"
  val appVersion = "1.0"

  val appDependencies = Seq(
    javaCore,
    javaJdbc,
    javaJpa,
    cache,
    "org.projectlombok" % "lombok" % "1.16.14",
    "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final"
    // Add your project dependencies here,
  )

  val main = play.Project(appName, appVersion, appDependencies, settings = Defaults.defaultSettings)
    .settings(projectSettings: _*)
    .settings(resolvers ++= Seq(
      "Jboss repo" at "https://repository.jboss.org/nexus/content/groups/public",
      "Spy Repository" at "http://files.couchbase.com/maven2"))
    .settings(Keys.fork in (Test) := true,
      // uncomment this line to enable remote debugging
      //javaOptions in (Test) += "-Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=9998",
      javaOptions in (Test) += "-Xms512M",
      javaOptions in (Test) += "-Xmx2048M",
      javaOptions in (Test) += "-Xss1M",
      javaOptions in (Test) += "-XX:MaxPermSize=384M")

}