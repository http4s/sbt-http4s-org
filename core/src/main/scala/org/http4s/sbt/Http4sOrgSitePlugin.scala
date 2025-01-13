/*
 * Copyright 2020 http4s.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.http4s.sbt

import cats.data.NonEmptyList
import laika.ast.Path.Root
import laika.ast._
import laika.helium.config.Favicon
import laika.helium.config.HeliumIcon
import laika.helium.config.IconLink
import laika.helium.config.ImageLink
import laika.helium.config.TextLink
import laika.helium.config.ThemeNavigationSection
import laika.sbt.LaikaPlugin
import laika.theme.config.Color
import org.typelevel.sbt.TypelevelGitHubPlugin
import org.typelevel.sbt.TypelevelSitePlugin
import org.typelevel.sbt.site.TypelevelSiteSettings
import sbt._

object Http4sOrgSitePlugin extends AutoPlugin {

  override def requires = TypelevelSitePlugin && LaikaPlugin

  import TypelevelGitHubPlugin.autoImport._
  import TypelevelSitePlugin.autoImport._

  private val relatedProjects: Def.Initialize[ThemeNavigationSection] = Def.setting {
    val mappings = NonEmptyList
      .of(
        "http4s" -> url("https://http4s.org/"),
        // Backends
        "blaze" -> url("https://github.com/http4s/blaze"),
        "http4s-armeria" -> url("https://github.com/http4s/http4s-armeria"),
        "http4s-curl" -> url("https://github.com/http4s/http4s-curl/"),
        "http4s-dom" -> url("https://http4s.github.io/http4s-dom/"),
        "http4s-finagle" -> url("https://http4s.github.io/http4s-finagle/docs/"),
        "http4s-jdk-http-client" -> url("https://jdk-http-client.http4s.org/"),
        "http4s-jetty" -> url("https://github.com/http4s/http4s-jetty/"),
        "http4s-netty" -> url("https://github.com/http4s/http4s-netty/"),
        "http4s-servlet" -> url("https://http4s.github.io/http4s-servlet/"),
        // Frontends
        "rho" -> url("https://github.com/http4s/rho/"),
        // Codecs
        "http4s-boopickle" -> url("https://github.com/http4s/http4s-boopickle/"),
        "http4s-fabric" -> url("https://github.com/http4s/http4s-fabric/"),
        "http4s-fs2-data" -> url("https://http4s.github.io/http4s-fs2-data/"),
        "http4s-scala-xml" -> url("https://http4s.github.io/http4s-scala-xml/"),
        // Metrics
        "http4s-prometheus-metrics" -> url("https://http4s.github.io/http4s-prometheus-metrics/"),
        // Other
        "http4s-scalatags" -> url("https://github.com/http4s/http4s-scalatags/"),
        "http4s-session" -> url("https://http4s.github.io/http4s-session/"),
        "http4s-twirl" -> url("https://github.com/http4s/http4s-twirl/"),
        // Internals
        "sbt-http4s-org" -> url("https://http4s.github.io/sbt-http4s-org/"),
        // Third party
        "feral" -> url("https://github.com/typelevel/feral/")
      )
      .filterNot { case (repo, _) =>
        tlGitHubRepo.value.contains(repo) // omit ourselves!
      }
      .map { case (name, url) =>
        TextLink.external(url.toString, name)
      }
    ThemeNavigationSection(
      "Related Projects",
      mappings.head,
      mappings.tail*
    )
  }

  val chatLink: IconLink = IconLink.external("https://discord.gg/XF3CXcMzqD", HeliumIcon.chat)

  override def projectSettings: Seq[Setting[_]] = Seq(
    tlSiteHelium :=
      tlSiteHelium.value
        .extendWith(site.Http4sHeliumExtensions)
        .site
        .themeColors(
          primary = Color.hex("5B7980"),
          secondary = Color.hex("cc6600"),
          primaryMedium = Color.hex("a7d4de"),
          primaryLight = Color.hex("e9f1f2"),
          text = Color.hex("5f5f5f"),
          background = Color.hex("ffffff"),
          bgGradient =
            (Color.hex("334044"), Color.hex("5B7980")) // only used for landing page background
        )
        .site
        .favIcons(
          Favicon.internal(Root / "images" / "http4s-favicon.svg"),
          Favicon.internal(Root / "images" / "http4s-favicon.png", "32x32")
        )
        .site
        .darkMode
        .disabled
        .site
        .footer(TypelevelSiteSettings.defaultFooter.value: _*)
        .site
        .mainNavigation(appendLinks = Seq(relatedProjects.value))
        .site
        .topNavigationBar(
          homeLink = ImageLink.external(
            "https://http4s.org",
            Image.internal(Root / "images" / "http4s-logo-text-dark.svg")
          ),
          navLinks = Seq(chatLink) // api + github links inherited from sbt-typelevel
        )
  )

}
