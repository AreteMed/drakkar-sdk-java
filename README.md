# Drakkar Telehealth SDK (Java/Kotlin)

## Overview
This document describes a way of how to include the "Drakkar Telehealth SDK" into a desired Spring Boot Project

## Quick Setup Guide
### Add the "Drakkar Telehealth SDK" into a Spring-Boot Project
#### Add Dependency
- Gradle
```
implementation("io.aretemed.drakkar:sdk-java:1.0.9")
```
- Or Maven
```
<dependency>
    <groupId>io.aretemed.drakkar</groupId>
    <artifactId>sdk-java</artifactId>
    <version>1.0.9</version>
</dependency>
```
#### Include "Component Scan" for "io.aretemed", e.g.
```
@ComponentScan("io.aretemed.drakkar")
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
