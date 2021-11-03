<h1 align="center"> Riff </h1> <br>

<p align="center">
  A Microservice for URL shortening and sharing.
</p>

<p align="center"><img alt="Release Candidate" title="Release Candidate" src="http://www.overture.bio/img/progress-horizontal-RC.svg" width="320" /></p>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Testing](#testing)
- [API](#requirements)
- [Acknowledgements](#acknowledgements)

## Introduction

[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/75b155fe7c4846408c57d20431f57e19)](https://www.codacy.com/app/overture-stack/riff?utm_source=github.com&utm_medium=referral&utm_content=overture-stack/riff&utm_campaign=Badge_Grade)
[![CircleCI](https://circleci.com/gh/overture-stack/riff.svg?style=svg)](https://circleci.com/gh/overture-stack/riff)
[![Slack](http://slack.overture.bio/badge.svg)](http://slack.overture.bio)


## Features

See: https://kf-strides-riff.kidsfirstdrc.org/swagger-ui.html for API documentation

## Requirements

The application can be run locally or in a docker container, the requirements for each setup are listed below.

### Keycloak

A running instance of [Keycloak](https://www.keycloak.org/) is required to generate the Authorization tokens and to verify the tokens.

[Keycloak](https://www.keycloak.org/) can be run locally in a docker container see [here](https://www.keycloak.org/getting-started/getting-started-docker) or you can use the QA instance.

### Local

- [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Maven](https://maven.apache.org/download.cgi)

### Docker

- [Docker](https://www.docker.com/get-docker)

### PostgreSQL

Easy to run in a docker container: 
  
`docker run --name Riff_PostgreSQL -e POSTGRES_PASSWORD=postgres -e POSTGRES_USERNAME=postgres -p 5432:5432 -d postgres`

Make sure to create database and the schema (see flyway migration scripts).

#### Database Migrations with Flyway

Database migrations and versioning is managed by [flyway](https://flywaydb.org/).

1. Download the flyway cli client here: [flyway-commandline](https://flywaydb.org/download/community)
2. Unpack the client in a directory of your choosing
3. Execute the flyway client pointing it to the configuration and migration directories in this repository.

Get current version information:

```bash
./flyway -configFiles=<path_to_riff>/riff/src/main/resources/flyway/conf/flyway.conf -locations=filesystem:<path_to_riff>/riff/src/main/resources/flyway/sql info
```

Run outstanding migrations:

```bash
./flyway -configFiles=<path_to_riff>/riff/src/main/resources/flyway/conf/flyway.conf -locations=filesystem:<path_to_riff>/riff/src/main/resources/flyway/sql migrate
```

To see the migration naming convention, [click here.](https://flywaydb.org/documentation/migrations#naming)

### Run Local

```bash
$ mvn spring-boot:run
```

Application will run by default on port `1234`

Configure the port by changing `server.port` in **application.yml**

### Run Docker

First build the image:

```bash
$ docker-compose build
```

When ready, run it:

```bash
$ docker-compose up
```

Application will run by default on port `1234`

Configure the port by changing `services.api.ports` in **docker-compose.yml**. Port 1234 was used by default so the value is easy to identify and change in the configuration file.

## Testing

The easiest way to test your dev is to run RiffIntregrationTest, it starts a DB postgres and a keycloak server. 

It also starts a riff server and send some requests to it.

Feel free to add integration tests in it if needed.

## API

See https://kf-strides-riff.kidsfirstdrc.org/swagger-ui.html

## Acknowledgements

TODO: Show folks some love.
