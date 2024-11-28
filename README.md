# Fibonacci Sequence Calculator Service

This is a REST endpoint implementation of fibonacci sequence calculator. It offers a paginated endpoint to retrieve a sequence of fibonacci numbers up to a certain maximum.

This solution does not depend on any external service (db, cache etc.). It's designed to utilize only application memory therefore maximum numbers that can be obtained is limited to 1 million.

### Prerequisites

- JDK
    - 17 ([sdkman](https://sdkman.io/) is a nice choice to easily install and switch between sdks, e.g. `sdk install java 17.0.13-tem`)

After java is configured, to make sure correct version is being used, open terminal and run
`java -version`. 

## Building
To build the application locally and run the unit tests execute the following command

```shell
    ./gradlew clean build
```

## Running
To run the application locally execute the following command (project has to be built first)
```shell
    ./gradlew bootRun
```

## Documentation
### Api Documentation
API documentation is available via Swagger at `http://localhost:8080/swagger-ui.html` while application is running. 

### Postman
To facilitate easier testing, a [Postman](https://www.postman.com/downloads/) collection is provided at `postman` folder at the project root directory.

### Performance testing
To facilitate easier performance testing, a [k6](https://grafana.com/docs/k6/latest/set-up/install-k6/) script is provided at `k6` folder at the project root directory.


## Final Notes
In normal life I'd use a db to store all needed numbers and a cache to speed up retrieval process. Here I opted to limit myself to only working with memory while providing an optimal solution.
I also included 4 non-optimal PoC implementations.
