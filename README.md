# Product Platform

Product Platform is a stand-alone service that allows retailers and brands to manage and interact with product data. It ingests product data from a JSON feed, provides REST endpoints to retrieve and update product information, and allows for setting stock levels.

## Features

- Ingests product data from an external JSON feed on application startup
- Exposes REST endpoints for retrieving product information
- Supports filtering products by category and stock availability
- Allows for updating product fields and stock levels

## Technologies Used

- Java Spring Boot
- MyBatis
- H2 Database (in-memory)
- Gradle

## Getting Started

### Prerequisites

- Java 8 or above
- Docker (optional, if running the external JSON feed as a Docker container)

### Installation

1. Clone the repository:

git clone https://github.com/Erenkara/product

Build the project:

cd product-platform
./gradlew build


## Usage

Start the application:
./gradlew bootRun


1- The application will ingest product data from the external JSON feed on startup.The application will ingest product data from the external JSON feed on startup.
2- Access the REST endpoints using an HTTP client or web browser.

To retrieve information for a single product:

GET http://localhost:8080/products/{id}

To retrieve all products of a given category (with optional stock filter):

GET http://localhost:8080/products/category/{category}?inStockOnly={true/false}

To update a product:

PUT http://localhost:8080/products/{id}

Request body: JSON representation of the updated product

To set the current stock level for a given product:

PUT http://localhost:8080/products/{id}/stock

Request body: JSON object with a "stockLevel" field representing the new stock level


## License

This project is licensed under the MIT License. See the LICENSE file for more details.
