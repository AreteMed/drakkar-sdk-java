import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    id("org.springframework.boot") version "2.5.9"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id ("org.jetbrains.dokka") version "1.7.10"
    `maven-publish`
    signing
    jacoco
}

group = "io.aretemed.drakkar"
version = "1.0.4"

val springBootVersion by extra("2.1.5.RELEASE")
val kotlinxCoroutinesVersion by extra("1.6.4")


repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:${springBootVersion}")
    implementation("org.springframework.boot:spring-boot-starter-webflux:${springBootVersion}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${kotlinxCoroutinesVersion}")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:${kotlinxCoroutinesVersion}")
    implementation("com.google.code.gson:gson:2.9.0")
    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("com.squareup.okhttp3:mockwebserver")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${kotlinxCoroutinesVersion}")
}



java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenCentral") {
            groupId = group.toString()
            artifactId = rootProject.name
            version = version

            from(components["java"])
            suppressPomMetadataWarningsFor("runtimeElements")
            pom {
                name.set("Drakkar Telehealth SDK")
                description.set("Wrapper of Drakkar API")
                url.set("https://github.com/AreteMed/drakkar-sdk-java")
                scm {
                    connection.set("scm:git:git://github.com/AreteMed/drakkar-sdk-java.git")
                    developerConnection.set("scm:git@github.com:AreteMed/drakkar-sdk-java.git")
                    url.set("https://github.com/AreteMed/drakkar-sdk-java")
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("Oleg")
                        name.set("Oleg Bogriakov")
                        email.set("oleg.bogriakov@aretemed.io")
                    }
                }
            }
        }
    }
    repositories {
        maven {
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.properties["ossrhUsername"].toString()
                password = project.properties["ossrhPassword"].toString()
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenCentral"])
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType(JacocoReport::class.java).all {
    reports {
        xml.required.set(true)
        xml.outputLocation.set(File("$buildDir/reports/jacoco/report.xml"))
    }
}

tasks.withType<Test> {
    jacoco {
        toolVersion = "0.8.3"
        reportsDirectory.set(File("$buildDir/reports/jacoco"))
    }
    finalizedBy("jacocoTestReport")
}