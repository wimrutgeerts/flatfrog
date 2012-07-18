import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "FlatFrog"
    val appVersion      = "1.0-SNAPSHOT"

   val appDependencies = Seq(
      "com.typesafe" %% "play-plugins-mailer" % "2.0.2",
      "org.mindrot" % "jbcrypt" % "0.3m"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = JAVA).settings(
      resolvers += "Apache" at "http://repo1.maven.org/maven2/",
      resolvers += "jBCrypt Repository" at "http://repo1.maven.org/maven2/org/"      
    )

}
