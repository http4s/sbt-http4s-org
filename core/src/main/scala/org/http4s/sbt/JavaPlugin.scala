package org.http4s.sbt

import sbt._
import sbt.Keys._

import com.lightbend.sbt.JavaFormatterPlugin

object JavaPlugin extends AutoPlugin {
  object autoImport

  override def trigger = allRequirements

  override def requires = JavaFormatterPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      javacOptions += "-Xlint:all"
    )
}
