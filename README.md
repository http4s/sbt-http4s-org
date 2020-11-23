# sbt-http4s-org

This project is intended to set up sensible defaults and standards for projects under http4s.org.
If it's useful outside http4s.org, it probably should be pushed further upstream into [sbt-spiewak](https://github.com/djspiewak/sbt-spiewak/).

## Installing it

The plugin is deployed to Sonatype.

```scala
addSbtPlugin("org.http4s" % "sbt-http4s-org" % http4sOrgV)
```

## What does it do?

### `Http4sOrgPlugin`

Triggers automatically.  Use on all http4s modules.

* Activates the sensible defaults of [sbt-spiewak](https://github.com/djspiewak/sbt-spiewak/)
* Sets the organization info
* Enables automated SPDX license headers with appropriate years
* Enables [sbt-github-actions](https://github.com/djspiewak/sbt-github-actions) with settings appropriate to our workflow

### `AlpnBootPlugin`

Enable with `enablePlugins(AlpnBootPlugin)`.
Use on example projects that require ALPN support.

* Sets up `-Xbootclasspath` for [ALPN](https://www.eclipse.org/jetty/documentation/current/alpn-chapter.html). Useful primarily for example projects.
