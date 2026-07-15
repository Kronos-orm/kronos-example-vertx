# kronos-example-vertx

-------------------------

English | [简体中文](https://github.com/Kronos-orm/kronos-example-vertx/blob/main/README-zh_CN.md)

This is a sample project based on Vert.x + Kronos ORM 0.2.3 + JDK 17 + Gradle + Kotlin 2.4.0.

If you would like to learn more about Kronos, please visit [Kronos](https://www.kotlinorm.com/).

## Introducing Gradle dependencies

**1. Add Kronos dependency**

```kts
dependencies {
  implementation("com.kotlinorm:kronos-core:0.2.3")
  implementation("com.kotlinorm:kronos-jdbc-wrapper:0.2.3")
}
```

**2. Add Kotlin compiler plugin**

```kts
plugins {
  id("com.kotlinorm.kronos-gradle-plugin") version "0.2.3"
}
```

## Configure the data source

The sample project uses kronos-jdbc-wrapper, and you can configure the data source in the following way.
You can also replace it with another wrapper or customize the wrapper.

```kotlin
import com.kotlinorm.Kronos
import com.kotlinorm.beans.config.LineHumpNamingStrategy
import com.kotlinorm.wrappers.KronosJdbcWrapper

class MainVerticle : AbstractVerticle() {
  private val ds = BasicDataSource().apply {
    url = "jdbc:mysql://localhost:3306/kotlinorm"
    username = "root"
    password = "**********"
  }

  override fun start(startPromise: Promise<Void>) {
    Kronos.apply {
      dataSource = { KronosJdbcWrapper(ds) }
      fieldNamingStrategy = LineHumpNamingStrategy()
      tableNamingStrategy = LineHumpNamingStrategy()
    }
    vertx
      .createHttpServer()
      .requestHandler { req ->
        req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x!")
      }
      .listen(8888) { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause())
        }
      }
  }
}
```

## Run the project

After running the project, visit the following URL to view the results:

```
http://localhost:8888
```

If the interface returns the results shown below, Kronos has run successfully and the compiler plugin is working
properly.

![screen](https://github.com/Kronos-orm/kronos-example-vertx/blob/main/screenshot/img.png?raw=true)

----

# Kronos-example-vertx

![https://vertx.io](https://img.shields.io/badge/vert.x-4.5.10-purple.svg)

This application was generated using http://start.vertx.io

## Building

To launch your tests:

```
./gradlew clean test
```

To package your application:

```
./gradlew clean assemble
```

To run your application:

```
./gradlew clean run
```

## Help

- [Vert.x Documentation](https://vertx.io/docs/)
- [Vert.x Stack Overflow](https://stackoverflow.com/questions/tagged/vert.x?sort=newest&pageSize=15)
- [Vert.x User Group](https://groups.google.com/forum/?fromgroups#!forum/vertx)
- [Vert.x Discord](https://discord.gg/6ry7aqPWXy)


