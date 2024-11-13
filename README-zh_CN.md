# kronos-example-vertx

-------------------------

[English](https://github.com/Kronos-orm/kronos-example-vertx/blob/main/README.md) | 简体中文

这是一个基于 Vert.x + Kronos ORM + JDK 17 + Gradle + Kotlin 2.0.0（2.0.20 即将适配）的示例项目。

如果您想了解更多关于Kronos的信息，请访问[Kronos](https://www.kotlinorm.com/)。

## 引入Gradle依赖

**1. 添加Kronos依赖**

```kts
dependencies {
  implementation("com.kotlinorm:kronos-core:0.1.0-SNAPSHOT")
  implementation("com.kotlinorm:kronos-jdbc-wrapper:0.1.0-SNAPSHOT")
}
```

**2. 添加Kotlin编译器插件**

```kts
plugins {
  id("com.kotlinorm.kronos-gradle-plugin") version "0.1.0"
}
```

## 配置数据源

项目中使用了Kronos-jdbc-wrapper，您可以按以下方式配置数据源，也可以替换为其他wrapper或自定义wrapper。

```kotlin
import com.kotlinorm.Kronos
import com.kotlinorm.KronosBasicWrapper

class MainVerticle : AbstractVerticle() {
  private val ds = BasicDataSource().apply {
    url = "jdbc:mysql://localhost:3306/kotlinorm"
    username = "root"
    password = "**********"
  }

  override fun start(startPromise: Promise<Void>) {
    Kronos.apply {
      dataSource = { KronosBasicWrapper(ds) }
      fieldNamingStrategy = LineHumpNamingStrategy
      tableNamingStrategy = LineHumpNamingStrategy
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

## 运行项目

运行项目后，访问以下URL，即可查看结果：

```
http://localhost:8888
```

如果接口返回的结果如下图所示，则表示Kronos已成功运行，编译器插件也已正常工作。

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
