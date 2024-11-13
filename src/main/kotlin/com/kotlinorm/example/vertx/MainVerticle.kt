package com.kotlinorm.example.vertx

import com.kotlinorm.Kronos
import com.kotlinorm.KronosBasicWrapper
import com.kotlinorm.beans.config.LineHumpNamingStrategy
import com.kotlinorm.example.vertx.pojos.User
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.apache.commons.dbcp2.BasicDataSource

class MainVerticle : AbstractVerticle() {
  private val ds = BasicDataSource().apply {
    url = "jdbc:mysql://localhost:3306/kotlinorm"
    username = "root"
    password = "**********"
  }

  @OptIn(ExperimentalStdlibApi::class)
  override fun start(startPromise: Promise<Void>) {
    Kronos.apply {
      dataSource = { KronosBasicWrapper(ds) }
      fieldNamingStrategy = LineHumpNamingStrategy
      tableNamingStrategy = LineHumpNamingStrategy
    }
    vertx
      .createHttpServer()
      .requestHandler { req ->
        val user = User()
        req.response()
          .putHeader("content-type", "application/json")
          .end(
              Moshi.Builder().build()
                .adapter<Map<String, Any>>()
                .toJson(mapOf(
                  "application" to "Vert.x + Kronos ORM",
                  "tableName" to user.kronosTableName(),
                  "columns" to user.kronosColumns().map {
                    mapOf(
                      "name" to it.name,
                      "type" to it.type,
                      "nullable" to it.nullable,
                      "primaryKey" to it.primaryKey.name,
                      "comment" to it.kDoc
                    )
                  }
                ))
          )
      }
      .listen(8888).onComplete { http ->
        if (http.succeeded()) {
          startPromise.complete()
          println("HTTP server started on port 8888")
        } else {
          startPromise.fail(http.cause());
        }
      }
  }
}
