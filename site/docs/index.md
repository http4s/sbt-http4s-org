---
layout: home

---

# sbt-http4s-org - SBT plugin for http4s projects [![Build Status](https://travis-ci.com/rossabaker/sbt-http4s-org.svg?branch=master)](https://travis-ci.com/rossabaker/sbt-http4s-org) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.http4s/sbt-http4s-org_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.http4s/sbt-http4s-org_2.12)

## Quick Start

To use sbt-http4s-org in an existing SBT project with Scala 2.11 or a later version, add the following dependencies to your
`build.sbt` depending on your needs:

```scala
libraryDependencies ++= Seq(
  "org.http4s" %% "sbt-http4s-org" % "<version>"
)
```