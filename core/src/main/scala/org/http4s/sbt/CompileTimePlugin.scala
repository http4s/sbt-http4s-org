package org.http4s.sbt

import sbt._
import sbt.Keys._

object CompileTimePlugin extends AutoPlugin {
  object autoImport

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      ivyConfigurations += CompileTime,
      unmanagedClasspath in Compile ++= update.value.select(configurationFilter("CompileTime"))
    )

  val CompileTime = config("CompileTime").hide
}
