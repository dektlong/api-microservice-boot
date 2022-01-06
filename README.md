# Introduction
An Accelerator to create API-driven backend with Spring Boot.

The service should expose well defined API routes.

## Internal API Endpoints

Implementations should support at the minimum these route endpoints

Retrieve all store items
* path: /api/store-items
* method: GET
* secured: no
* filters: RateLimit

Retrieve current authenticated user's information
* path: /api/whoami
* method: GET
* secured: yes, token-relay and sso

Manage a request to transact a given store-item
* path: /api/store-item/*/transaction-requests/**",
* method: POST,PUT,DELETE
* secured: yes, token-relay and sso

API Actuator endpoints
* path: /api/actuator/**
* method: GET
* secured: no
    

## The Domain Model
The Domain Model is a StoreItem object which has an id, a title and a category

* id: store-item-category-unique-id
* title: store-item-name
* category: store-item-category

## Pre-requisites for using store-backend-api 
Building and running the application assumes that you have installed a number of pre-requisites:

* Java 8 - configured to run the application by default. You can decided to build and run the app also with Java 11 or 14 
* Maven - compiling the application and running tests
* Helm v3 - for installing the caching and database solutions. [Helm installation link](https://helm.sh/docs/intro/install/).
* Skaffold - for building, pushing, deploying and debugging the application. [Skaffold installation link](https://skaffold.dev/docs/install/).
* Kustomize - for using a template-free way to customize application configuration that simplifies the use of off-the-shelf applications. [Kustomize installation link](https://kubernetes-sigs.github.io/kustomize/installation/).
* HTTPie - highly recommended as a cUrl replacement for a user-friendly command-line HTTP client for the API era. It comes with JSON support, syntax highlighting, persistent sessions, wget-like downloads, plugins, etc. [HTTPie installation link](https://httpie.org/).
* Kubectl - the Kubernetes CLI, allows you to run commands against Kubernetes clusters. [Kubectl installation link](https://kubernetes.io/docs/tasks/tools/install-kubectl/).

