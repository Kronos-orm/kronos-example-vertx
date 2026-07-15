import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  kotlin ("jvm") version "2.4.0"
  application
  id("com.github.johnrengelman.shadow") version "8.1.1"
  kotlin("plugin.serialization") version "2.4.0"
  id("com.kotlinorm.kronos-gradle-plugin") version "0.2.3"
}

group = "com.kotlinorm.example.vertx"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenLocal()
  mavenCentral()
}

val vertxVersion = "4.5.10"
val junitJupiterVersion = "5.9.1"

val mainVerticleName = "com.kotlinorm.example.vertx.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencies {
  implementation("com.squareup.moshi:moshi:1.15.1")
  implementation("org.apache.commons:commons-dbcp2:2.12.0")
  implementation("com.mysql:mysql-connector-j:8.4.0")
  implementation("org.apache.commons:commons-dbcp2:2.12.0")
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-lang-kotlin")
  implementation("io.netty:netty-resolver-dns-native-macos")
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.kotlinorm:kronos-core:0.2.3")
  implementation("com.kotlinorm:kronos-jdbc-wrapper:0.2.3")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

kotlin {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
    freeCompilerArgs.add("-Xjsr305=strict")
  }
}

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
