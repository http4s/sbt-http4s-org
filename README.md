# sbt-http4s-org

This project is intended to set up sensible defaults and standards for projects in the http4s organization.
It is not designed for outside use, but we offer it as an inspiration or warning to others.

## Installing it

The plugin is deployed to Sonatype.

```scala
addSbtPlugin("org.http4s" % "sbt-http4s-org" % http4sOrgV)
```

## What does it do?

### `Http4sOrgPlugin`

Triggers automatically.  Use on all http4s modules.

* Sets the organization to `org.http4s`
* Sets `-Ybackend-parallelism` compiler option
* Sets an appropriate `-doc-source-url` for your Scaladoc
* Enables automated SPDX license headers with appropriate years
* Enables [scalafmt](https://scalameta.org/scalafmt/)
* Enables [MiMa](https://github.com/lightbend/mima) checks for binary compatibility via [sbt-mima-version-check](https://christopherdavenport.github.io/sbt-mima-version-check/)

### `AlpnBootPlugin`

Enable with `enablePlugins(AlpnBootPlugin)`.
Use on example projects that require ALPN support.

* Sets up `-Xbootclasspath` for [ALPN](https://www.eclipse.org/jetty/documentation/current/alpn-chapter.html). Useful primarily for example projects.

### `CompileTimePlugin`

Enable with `enablePlugins(CompileTimePlugin)`.
Mostly useful to support other plugins.

* Sets up a `CompileTime` SBT configuration for libraries that exist at compile time, but not runtime.

### `JavaPlugin`

Enable with `enablePlugins(JavaPlugin)`.

* Turns on `-Xlint:all` warnings
* Enables [sbt-java-formatter](https://github.com/sbt/sbt-java-formatter)

### `PrivateProjectPlugin`

Enable with `enablePlugins(PrivateProjectPlugin)`.
Use on examples, benchmarks, docs, and other projects that should not be published.

* Skips publishing
* Disables MiMa checks.

### `SilencerPlugin`

Enable with `enablePlugins(SilencerPlugin)`

* Adds the [silencer](https://github.com/ghik/silencer) compiler plugin to suppress warnings.
* Adds the supporting library to `CompileTime` and `Test` scopes, so it doesn't leave a trace in production.

