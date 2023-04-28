# Wine breakdown API — bug hunt & refactor challenge

Time: 1-2h (time boxed)

Tech:

- Java8
- Spring Boot
- Gradle

## Context

Blended wines consist of grapes from different sources. These grapes have their properties such as year, variety (e.g. Chardonnay, Pinot Noir) and region (e.g. Yarra Valley, Mornington Peninsula, Macedon Ranges, etc.). When bottling a wine, the winery needs to know the percentage breakdown of these properties to know what they legally can claim on the wine's label.

In this repository, you will find an almost complete REST API that should return a breakdown of the total percentage grouped by year, variety, region, and year + variety combination for a batch of wine in JSON format. The order is from highest to lowest percentage. The endpoints of the API are:

- **/api/breakdown/year/{lotCode}** — breakdown of total percentage by year
- **/api/breakdown/variety/{lotCode}** — breakdown of total percentage by variety
- **/api/breakdown/region/{lotCode}** — breakdown of total percentage by region
- **/api/breakdown/year-variety/{lotCode}** — breakdown of total percentage by year + variety combination

Run the API with:

```shell
./gradlew bootRun
```

The API will run at http://localhost:8080

## Part 1: Bug hunt

Our QAs found that the endpoints are not returning the correct results. Automated tests are added for each endpoint to verify the response with a correct JSON result.

These tests are located in the `BreakdownControllerTest` class and can be executed with:

```shell
./gradlew test
```

The tests will fail and indicate what is wrong with the API.

Your task is to identify what is wrong, implement a fix that makes the tests pass, and write down your findings in a markdown file. Good luck.

## Part 2: Refactor

Imagine you work for a consultancy company, and one of your colleagues has been doing some work on this API. The contract is for 10 hours of work, and your colleague has spent 8.5 hours working on it. Unfortunately, she has fallen
ill. Your boss has asked you to take over. She wants you to spend an hour or so refactoring the code and making some notes, so you can give your colleague feedback on her chosen design. Furthermore, prepare to talk to your boss about the value of this refactoring work, over and above the extra billable hours.

Your task is to refactor the code for about 1 hour and write down your notes and findings in a markdown file.

For this part, we are looking for improvements that make the code more readable, maintainable & testable. While doing this, imagine that the code is part of a larger codebase that has more endpoints, business logic, and ways to access data. Good luck.

## Deliverables

What you need to send back:

- A markdown file with your explanation of both parts.
- The complete repository with the changes you made for both parts.
