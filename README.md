# Gracenote Sports - Soccer data processor

## Description

For the Scala Test, a simple usage of a [Pekko Streams](https://pekko.apache.org/docs/pekko/current/stream/index.html) 
application is provided.

Even though a limited dataset is provided, I want to show the ability of processing data in streaming fashion with Pekko
Streams.

## Tech stack

The application has been developed using:
- [Scala](https://www.scala-lang.org/) v2.13.12
- [sbt](https://www.scala-sbt.org/) v1.9.7
- [Pekko](https://pekko.apache.org/docs/pekko/current/index.html) v1.0.2

The reason behind using *Pekko* instead of the more well known
*[Akka](https://akka.io/)* is due to the adoption of the Business Source License in Akka since `v2.6.x`. From the Pekko github
repo:
> Apache Pekko is an open-source framework for building applications that are concurrent, distributed, resilient and 
> elastic. Pekko uses the Actor Model to provide more intuitive high-level abstractions for concurrency. Using these 
> abstractions, Pekko also provides libraries for persistence, streams, HTTP, and more.
> Pekko is a fork of Akka 2.6.x, prior to the Akka project's adoption of the Business Source License

## How to run it

As a commons `sbt` application, one can just execute in their terminal `sbt run` to run the application, or `sbt test`
to run the tests.

### Configuration

| Env var            | Description                                   | Default                              |
|:-------------------|:----------------------------------------------|:-------------------------------------|
| GRACENOTE_DATASET  | CSV file name that has soccer actions stored  | `Dataset2roundsEredivie20172018.csv` |

Please, place the CSV dataset in the [`src/main/resources`](src/main/resources) and update the above mentioned env var
if your file has a different name.

The results will be logged on the terminal, where each line represents a player on the provided dataset, and all their
actions registered.

## Improvements

There is a large set of improvements to be listed, include code, logic, or preparation real world application:

- [ ] Connect the application to other systems like Kafka or PostgreSQL, so data is stored permanently
  - [ ] Add Integration Tests
- [ ] Implement a larger set of aggregations that are relevant for the consumers of the application
- [ ] Containerize the application to make it deployable in other systems like Kubernetes
- [ ] Add a more stable source of data
- [ ] Expand configuration options
- [ ] Add various scala plugins for code guidelines (formatting, linting)
- [ ] Add releasing / CI/CD configuration
