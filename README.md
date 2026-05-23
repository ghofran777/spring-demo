# Spring Demo Backend

Backend Spring Boot project for managing merchants, products, product attributes, attribute values, and SKUs.

## Description

This project is a REST API built with Spring Boot.

It allows managing:

- Merchants
- Products
- Attribute definitions
- Attribute values
- SKUs

A merchant can have many products.

A product can have many attribute definitions, for example:

- Taille
- Couleur
- Matiere

Each attribute definition can have many values, for example:

- Taille: S, M, L
- Couleur: Rouge, Noir, Blanc

A SKU is created from a product and selected attribute values.

Example:

```txt
Product: Robe
Couleur: Rouge
Taille: S

Generated SKU:
R-COLOR_R-TAILLE_S
```

## Technologies

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Docker
- Maven

## Project Structure

```txt
src/main/java/com/example/demo
├── controller
├── dto
├── entity
├── repository
└── service
```

## Database

The project uses PostgreSQL with Docker.

Default database information:

```txt
Host: localhost
Port: 5433
Database: springdb
Username: ghofrane
```

The database password is configured locally in `application.properties`.

## Run PostgreSQL

Start PostgreSQL with Docker Compose:

```bash
docker compose up -d
```

Check if PostgreSQL is running:

```bash
docker ps
```

You should see something like:

```txt
0.0.0.0:5433->5432/tcp
```

## Run Backend

Compile the project:

```bash
./mvnw clean compile
```

Run the application:

```bash
./mvnw spring-boot:run
```

The backend runs on:

```txt
http://localhost:8080
```

---

# API Endpoints

## Merchants

Merchants represent stores or sellers.

### Get all merchants

```http
GET /api/merchants
```

### Get merchant by ID

```http
GET /api/merchants/{id}
```

### Create merchant

```http
POST /api/merchants
```

Example body:

```json
{
  "name": "Mofavo Store",
  "country": "Tunisia",
  "isActive": true
}
```

### Delete merchant

```http
DELETE /api/merchants/{id}
```

---

## Products

Products belong to merchants.

### Get all products

```http
GET /api/products
```

### Get product by ID

```http
GET /api/products/{id}
```

### Get products by merchant

```http
GET /api/products/merchant/{merchantId}
```

### Create product

```http
POST /api/products
```

Example body:

```json
{
  "merchantId": "MERCHANT_ID",
  "name": "Robe",
  "category": "Vetements",
  "description": "Robe noire élégante",
  "price": 85,
  "stock": 10,
  "isActive": true
}
```

### Update product

```http
PUT /api/products/{id}
```

Example body:

```json
{
  "merchantId": "MERCHANT_ID",
  "name": "Robe Rouge",
  "category": "Vetements",
  "description": "Robe rouge élégante",
  "price": 95,
  "stock": 12,
  "isActive": true
}
```

### Delete product

```http
DELETE /api/products/{id}
```

---

## Attribute Definitions

Attribute definitions describe the characteristics of a product.

Examples:

```txt
Taille
Couleur
Matiere
```

### Get all attribute definitions

```http
GET /api/attribute-definitions
```

### Get attribute definition by ID

```http
GET /api/attribute-definitions/{id}
```

### Get attribute definitions by product

```http
GET /api/attribute-definitions/product/{productId}
```

### Create attribute definition

```http
POST /api/attribute-definitions
```

Example body:

```json
{
  "productId": "PRODUCT_ID",
  "name": "Taille"
}
```

Another example:

```json
{
  "productId": "PRODUCT_ID",
  "name": "Couleur"
}
```

### Update attribute definition

```http
PUT /api/attribute-definitions/{id}
```

Example body:

```json
{
  "productId": "PRODUCT_ID",
  "name": "Matiere"
}
```

### Delete attribute definition

```http
DELETE /api/attribute-definitions/{id}
```

---

## Attribute Values

Attribute values are the possible values of an attribute definition.

Example:

```txt
Taille: S, M, L
Couleur: Rouge, Noir, Blanc
```

### Get all attribute values

```http
GET /api/attribute-values
```

### Get attribute value by ID

```http
GET /api/attribute-values/{id}
```

### Get values by attribute definition

```http
GET /api/attribute-values/attribute-definition/{attributeDefinitionId}
```

### Create attribute value

```http
POST /api/attribute-values
```

Example for Taille S:

```json
{
  "attributeDefinitionId": "TAILLE_ATTRIBUTE_ID",
  "value": "S",
  "sortOrder": 1
}
```

Example for Taille M:

```json
{
  "attributeDefinitionId": "TAILLE_ATTRIBUTE_ID",
  "value": "M",
  "sortOrder": 2
}
```

Example for Couleur Rouge:

```json
{
  "attributeDefinitionId": "COULEUR_ATTRIBUTE_ID",
  "value": "Rouge",
  "sortOrder": 1
}
```

### Update attribute value

```http
PUT /api/attribute-values/{id}
```

Example body:

```json
{
  "attributeDefinitionId": "COULEUR_ATTRIBUTE_ID",
  "value": "Noir",
  "sortOrder": 2
}
```

### Delete attribute value

```http
DELETE /api/attribute-values/{id}
```

---

## SKUs

A SKU represents a product variation based on selected attribute values.

Example:

```txt
Product: Robe
Couleur: Rouge
Taille: S

Generated SKU:
R-COLOR_R-TAILLE_S
```

### Get all SKUs

```http
GET /api/skus
```

### Get SKU by ID

```http
GET /api/skus/{id}
```

### Get SKUs by product

```http
GET /api/skus/product/{productId}
```

### Create SKU

```http
POST /api/skus
```

Example body:

```json
{
  "productId": "PRODUCT_ID",
  "attributeValueIds": [
    "TAILLE_S_VALUE_ID",
    "COULEUR_ROUGE_VALUE_ID"
  ]
}
```

Example response:

```json
{
  "id": "SKU_ID",
  "productId": "PRODUCT_ID",
  "productName": "Robe",
  "ref": "R-COLOR_R-TAILLE_S",
  "barcode": "R-COLOR_R-TAILLE_S-XXXXXXXX",
  "isActive": true,
  "values": [
    {
      "attributeValueId": "COULEUR_ROUGE_VALUE_ID",
      "attributeName": "Couleur",
      "value": "Rouge",
      "sortOrder": 1
    },
    {
      "attributeValueId": "TAILLE_S_VALUE_ID",
      "attributeName": "Taille",
      "value": "S",
      "sortOrder": 1
    }
  ],
  "createdAt": "2026-05-23T00:00:00",
  "updatedAt": "2026-05-23T00:00:00"
}
```

### Delete SKU

```http
DELETE /api/skus/{id}
```

---

# SKU Generation Rules

The SKU reference is generated using this format:

```txt
PRODUCT_PREFIX-COLOR_VALUE-TAILLE_VALUE
```

Product prefix rules:

```txt
Robe                         -> R
T-shirt Oversize             -> TSO
Pantalon Noir Homme          -> PNH
T-shirt Oversize Noir Extra  -> TSO
```

Attribute value rules:

```txt
Couleur Rouge -> COLOR_R
Taille S      -> TAILLE_S
```

Final examples:

```txt
Robe + Couleur Rouge + Taille S
=> R-COLOR_R-TAILLE_S

T-shirt Oversize + Couleur Noir + Taille M
=> TSO-COLOR_N-TAILLE_M

Pantalon Noir Homme + Couleur Noir + Taille L
=> PNH-COLOR_N-TAILLE_L
```

---

# Example Workflow

This is the normal testing order in Postman.

## 1. Create merchant

```http
POST /api/merchants
```

```json
{
  "name": "Mofavo Store",
  "country": "Tunisia",
  "isActive": true
}
```

Take the returned merchant ID.

## 2. Create product

```http
POST /api/products
```

```json
{
  "merchantId": "MERCHANT_ID",
  "name": "Robe",
  "category": "Vetements",
  "description": "Robe noire élégante",
  "price": 85,
  "stock": 10,
  "isActive": true
}
```

Take the returned product ID.

## 3. Create attribute definitions

Create Taille:

```http
POST /api/attribute-definitions
```

```json
{
  "productId": "PRODUCT_ID",
  "name": "Taille"
}
```

Create Couleur:

```http
POST /api/attribute-definitions
```

```json
{
  "productId": "PRODUCT_ID",
  "name": "Couleur"
}
```

Take the returned attribute definition IDs.

## 4. Create attribute values

Create Taille S:

```http
POST /api/attribute-values
```

```json
{
  "attributeDefinitionId": "TAILLE_ATTRIBUTE_ID",
  "value": "S",
  "sortOrder": 1
}
```

Create Couleur Rouge:

```http
POST /api/attribute-values
```

```json
{
  "attributeDefinitionId": "COULEUR_ATTRIBUTE_ID",
  "value": "Rouge",
  "sortOrder": 1
}
```

Take the returned attribute value IDs.

## 5. Create SKU

```http
POST /api/skus
```

```json
{
  "productId": "PRODUCT_ID",
  "attributeValueIds": [
    "TAILLE_S_VALUE_ID",
    "COULEUR_ROUGE_VALUE_ID"
  ]
}
```

Expected result:

```txt
R-COLOR_R-TAILLE_S
```

---

# Important Notes

- A merchant can have many products.
- A product belongs to one merchant.
- A product can have many attribute definitions.
- An attribute definition can have many values.
- A SKU belongs to one product.
- A SKU is generated from selected attribute values.
- The same SKU combination cannot be created twice.
- A SKU cannot use attribute values from another product.

---

# Author