# Drakkar Telehealth SDK (Java/Kotlin)

## Overview
This document describes a way of how to include this "Drakkar Telehealth SDK" into a desired Spring Boot Project

## Quick Setup Guide
### Publish into the local Maven Repository
Note, this part could be ignored once this tool is available via an External Maven Repository
#### Clone
```
git clone https://github.com/AreteMed/drakkar-sdk-java.git
```
#### Build
```
./gradlew build
```
#### Publish locally
Comment the following block in the build.gradle.kts
```
signing {
  sign(publishing.publications["mavenCentral"])
}
```
Run Publish
```
./gradlew publishToMavenLocal
```
#### Verify
Make sure the "Drakkar Telehealth SDK" available on the local Maven's repository
- Unix/Mac (by default)
```
~/.m2/repository/io/aretemed/drakkar/sdk-java
```
- Or Windows (by default)
```
C:\Users\{Username}\.m2\repository\io\aretemed\drakkar\sdk-java
```
### Add the "Drakkar Telehealth SDK" into a Spring-Boot Project
#### Add Dependency
- Gradle
```
implementation("io.aretemed.drakkar:sdk-java:1.0.3")
```
- Or Maven
```
<dependency>
    <groupId>io.aretemed.drakkar</groupId>
    <artifactId>sdk-java</artifactId>
    <version>1.0.3</version>
</dependency>
```
#### Include "Component Scan" for "io.aretemed", e.g.
```
@ComponentScan("io.aretemed")
```
#### Specify io.aretemed.drakkar.token inside Spring's configuration
For example, resources/application.yml
```
io.aretemed.drakkar:
  token: xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```
#### "Inject" DrakkarWebClient
- Kotlin Example
```
@Autowired
private lateinit var drakkarWebClient: DrakkarWebClient
```
- Or Java Example
```
@Autowired
private DrakkarWebClient drakkarWebClient;
```
#### Enjoy the Drakkar Telehealth SDK
E.g. in order to get Rooms just do the following
```
drakkarWebClient.roomAPI().rooms()
```