[![Build Status](https://travis-ci.org/johanaschan/queue-ticket-api.svg?branch=master)](https://travis-ci.org/johanaschan/queue-ticket-service)
[![codecov](https://codecov.io/gh/johanaschan/queue-ticket-api/branch/master/graph/badge.svg)](https://codecov.io/gh/johanaschan/queue-ticket-api)
[![coverity](https://scan.coverity.com/projects/10157/badge.svg)](https://scan.coverity.com/projects/johanaschan-queue-ticket-api)
[![Dependency Status](https://www.versioneye.com/user/projects/57d7afabbf2b4b0050f30acc/badge.svg?style=flat-square)](https://www.versioneye.com/user/projects/57d7afabbf2b4b0050f30acc)

# QueueTicketApi
API for QueueTicket built with Spring Boot using REST and Websocket, using Redis as a database and deployed at [Heroku](https://queue-ticket-api.herokuapp.com).

### Swagger ###
Swagger is exposed at /v2/api-docs

### Lombok ###
[Project Lombok](http://projectlombok.org/) is used to get rid of boilerplate code.

To use this with Idea, install the Lombok Plugin (available from the plugin repository),
and enable _Enable Annotation Processing_ _(Preferences_ -> _Build, Execution, Deployment_ ->
_Compiler_ -> _Annotation Processors)_.
