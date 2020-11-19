package org.http4s.sbt

import com.typesafe.tools.mima.plugin.MimaPlugin
import com.typesafe.tools.mima.plugin.MimaPlugin.autoImport.mimaPreviousArtifacts
import sbt._
import sbt.Keys._
import explicitdeps.ExplicitDepsPlugin
import explicitdeps.ExplicitDepsPlugin.autoImport.{
  undeclaredCompileDependenciesTest,
  unusedCompileDependenciesTest
}

object PrivateProjectPlugin extends AutoPlugin {
  override def trigger = noTrigger

  override def requires =
    Http4sOrgPlugin &&
      MimaPlugin &&
      ExplicitDepsPlugin

  override lazy val projectSettings: Seq[Setting[_]] =
    Seq(
      publish / skip := true,
      mimaPreviousArtifacts := Set.empty,
      undeclaredCompileDependenciesTest := {},
      unusedCompileDependenciesTest := {}
    )
}
