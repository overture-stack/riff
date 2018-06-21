<h1 align="center"> Riff </h1> <br>

<p align="center">
  A Microservice for URL Shortening and sharing.
</p>


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
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/75b155fe7c4846408c57d20431f57e19)](https://www.codacy.com/app/overture-stack/riff?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=overture-stack/riff&amp;utm_campaign=Badge_Grade)
[![CircleCI](https://circleci.com/gh/overture-stack/riff.svg?style=svg)](https://circleci.com/gh/overture-stack/riff)
[![Slack](http://slack.overture.bio/badge.svg)](http://slack.overture.bio)

TODO: Replace with introduction

## Features
TODO: Description of features

* Include a list of
* all the many beautiful
* web server features


## Requirements
The application can be run locally or in a docker container, the requirements for each setup are listed below.


### EGO
A running instance of [EGO](https://github.com/overture-stack/ego/) is required to generate the Authorization tokens and to provide the verification key.

[EGO](https://github.com/overture-stack/ego/) can be cloned and run locally if no public server is available. 


### Local
* [Java 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
* [Maven](https://maven.apache.org/download.cgi)


### Docker
* [Docker](https://www.docker.com/get-docker)


## Quick Start
Make sure the JWT Verification Key URL is configured, then you can run the server in a docker container or on your local machine.

### Configure JWT Verification Key
Update __application.yml__. Set `auth.jwt.publicKeyUrl` to the URL to fetch the JWT verification key. The application will not start if it can't set the verification key for the JWTConverter.

The default value in the __application.yml__ file is set to connect to EGO running locally on its default port `8081`.

### Run Local
```bash
$ mvn spring-boot:run
```

Application will run by default on port `1234`

Configure the port by changing `server.port` in __application.yml__


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

Configure the port by changing `services.api.ports` in __docker-compose.yml__. Port 1234 was used by default so the value is easy to identify and change in the configuration file.


## Testing
TODO: Additional instructions for testing the application.


## API
TODO: API Reference with examples, or a link to a wiki or other documentation source.

## Acknowledgements
TODO: Show folks some love.
