# Chuck Norris App

![Android Pull Request CI](https://github.com/bfpimentel/chuck-norris-app/workflows/Android%20Pull%20Request%20CI/badge.svg)

## Disclaimer
There is a lot of dependencies on this projects (at least that's what I think).  Although, it's also a showcase project, I tried to show what are my skills using these APIs. 

Maybe the complexity inside this project was overkill to its size, but I think it was the best decision.

Each of the features has its on [branch](https://github.com/bfpimentel/chuck-norris-app/branches) and all of them are mapped inside the issue [Feature Roadmap](https://github.com/bfpimentel/chuck-norris-app/issues/1).

The project contains only one activity hosting the Navigation Controller and all the screens are Fragments.

It uses Github Actions to run the [CI script](./.github/workflows/android-pr.yml) whenever a Pull Request is opened.

## Setup Instructions
Just clone the project wherever you want, no further setup needed :)

## Architecture Explanation
I choose to modularize the project layers focusing on Clean Architecture principles and ease to scale and to maintain.

![architecture](./resources/architecture.png)

### :app
This is the presentation layer, it is responsible for what the user sees.

- **Fragment**: The Fragment is responsible to talk with the ViewModel. It hears the user inputs and the ViewModel outputs. 
- **ViewModel**: It expects the Fragment inputs and calls the UseCases, from *domain* module, then, it can output the data to the Fragments via LiveData observers. All the ViewModels in this project also have a Navigator.
- **Navigator**: It navigates or pops to other fragments.

---
### :domain
This is the domain layer, it holds the entities and is responsible to talk to *data* module.

- **UseCase**: It is responsbile for the business rules on the application, it talks directly to the data module or another use cases.

---
### :data
This is the data layer, it does not contain any business rules, it is responsible to get data from local or remote data sources.

- **Repository**: It is just a composition of local or remote data sources.
- **DataSource**: It is responsbile to talk with the remote server or local database.

---
## Features
### Facts
![facts-first-access](./resources/facts-first-access.png | width=200)   ![facts](./resources/facts.png | width=200)

