name := "PSLQAlgo"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.apache.spark" %% "spark-core" % "1.4.0"
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "1.4.0"
libraryDependencies += "org.scalanlp" %% "breeze" % "0.11.2"
libraryDependencies += "org.scalanlp" %% "breeze-natives" % "0.11.2"
libraryDependencies += "org.scalanlp" %% "breeze-viz" % "0.11.2"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"

resolvers ++= Seq(
  "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/"
)


mainClass in (Compile) := Some("PSLQRepl")