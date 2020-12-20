# Introduction
Example repo an architect will compose as a source for a Starter template to create store-backend-api

The backend service fits into a larger Online Store application, which includes a WebUI, an edge service, as well as caching and database services.

The service can be accessed from a Web UI or directly at the endpoints it exposes.

## The Domain Model
The Domain Model is a StoreObject which has an id, a title, a category, the deadline and the status.
```json
ex.:
{
    "category": "personal",
    "complete": false,
    "deadline": "2020/10/01",
    "id": "4d0918be-36fa-4dcd-b7b8-14200ef31d4c",
    "title": "StoreItem"
}
```
## Endpoints

`store-backend-api` exposes multiple endpoints, by default running at port `8082`:

* `/` - [GET] returns all TODO items 
* `/{id}` - [GET] returns a TODO items for the specified id
* `/` - [POST] creates a new TODO item
* `/{id}` - [POST] creates a new TODO item with a specified id, in lieu of an auto-generated id
* `/{id}` - [PATCH] updates an existing TODO item 
* `/` - [DELETE] - deletes all TODOs in the database and evicts the cache
* `/{id}` - [DELETE] - deletes a TODO for the specified id

`store-backend-api` exposes additionally a separate set of endpoints for introspection, out-of-the-box and customized

* `/actuator` - [GET] returns all actuators enabled in the application; by default almost all available actuators have been enabled, for educational purposes
* `/actuator/health` - [GET] returns the OOB health info + customized health info collected from the service
* `/actuator/custom` - [GET] implements a custom actuator
* `/actuator/info` - [GET] collects customized info from the application

## Pre-requisites for using store-backend-api 
Building and running the application assumes that you have installed a number of pre-requisites:

* Java 8 - configured to run the application by default. You can decided to build and run the app also with Java 11 or 14 
* Maven - compiling the application and running tests
* Helm v3 - for installing the caching and database solutions. [Helm installation link](https://helm.sh/docs/intro/install/).
* Skaffold - for building, pushing, deploying and debugging the application. [Skaffold installation link](https://skaffold.dev/docs/install/).
* Kustomize - for using a template-free way to customize application configuration that simplifies the use of off-the-shelf applications. [Kustomize installation link](https://kubernetes-sigs.github.io/kustomize/installation/).
* HTTPie - highly recommended as a cUrl replacement for a user-friendly command-line HTTP client for the API era. It comes with JSON support, syntax highlighting, persistent sessions, wget-like downloads, plugins, etc. [HTTPie installation link](https://httpie.org/).
* Kubectl - the Kubernetes CLI, allows you to run commands against Kubernetes clusters. [Kubectl installation link](https://kubernetes.io/docs/tasks/tools/install-kubectl/).

