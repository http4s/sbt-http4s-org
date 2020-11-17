/*
 * Copyright 2020-2020 http4s.org
 *
 * SPDX-License-Identifier: Apache-2.0
 */

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
