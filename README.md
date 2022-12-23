# Drakkar Telehealth SDK (Java/Kotlin)

## Overview
This document describes a way of how to include the "Drakkar Telehealth SDK" into a desired Spring Boot Project

## Quick Setup Guide
### Add the "Drakkar Telehealth SDK" into a Spring-Boot Project
#### Add Dependency
- Gradle
```
implementation("io.aretemed.drakkar:sdk-java:2.1.5.RELEASE")
```
- Or Maven
```
<dependency>
    <groupId>io.aretemed.drakkar</groupId>
    <artifactId>sdk-java</artifactId>
    <version>2.1.5.RELEASE</version>
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
//e.g. by @Autowired
@Autowired
private lateinit var drakkarWebClient: DrakkarWebClient

//or by Constructor
constructor(drakkarWebClient: DrakkarWebClient){
    this.drakkarWebClient = drakkarWebClient
}
```
- Or Java Example
```
//e.g. by @Autowired
@Autowired
private DrakkarWebClient drakkarWebClient;

//or by Constructor
AnyService(DrakkarWebClient drakkarWebClient){
    this.drakkarWebClient = drakkarWebClient;
}
```
#### Enjoy the Drakkar Telehealth SDK
E.g. in order to get Rooms just do the following
```
drakkarWebClient.roomAPI().rooms()
```
