/*
 * Copyright 2020-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

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
