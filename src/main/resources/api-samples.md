# AgroVault — Sample API Responses

---

## 1. Register Farmer

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Balaji Thorat",
    "email": "farmer1@agrovault.com",
    "password": "farmer123",
    "role": "FARMER"
  }'
```

--- Response (201 Created) ---

```json
{
  "success": true,
  "message": "Registration successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXJtZXIxQGFncm92YXVsdC5jb20iLCJyb2xlIjoiRkFSTUVSIiwiaWF0IjoxNzQxNTk2MDAwfQ.xxxxx",
    "role": "FARMER",
    "name": "Balaji Thorat",
    "email": "farmer1@agrovault.com"
  }
}
```

---

## 2. Login

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "farmer1@agrovault.com",
    "password": "farmer123"
  }'
```

--- Response (200 OK) ---

```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXJtZXIxQGFncm92YXVsdC5jb20iLCJyb2xlIjoiRkFSTUVSIiwiaWF0IjoxNzQxNTk2MDAwfQ.xxxxx",
    "role": "FARMER",
    "name": "Balaji Thorat",
    "email": "farmer1@agrovault.com"
  }
}
```

---

## 3. Create Storage (Storage Owner)

```bash
curl -X POST http://localhost:8080/storages \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <owner_jwt_token>" \
  -d '{
    "name": "Nashik GrapeFrost Vault",
    "cityId": 1,
    "latitude": 20.0259,
    "longitude": 73.7894,
    "totalCapacity": 500.0,
    "availableCapacity": 400.0,
    "temperatureMin": 2.0,
    "temperatureMax": 8.0
  }'
```

--- Response (201 Created) ---

```json
{
  "success": true,
  "message": "Storage created successfully",
  "data": {
    "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "name": "Nashik GrapeFrost Vault",
    "cityName": "Nashik",
    "latitude": 20.0259,
    "longitude": 73.7894,
    "totalCapacity": 500.0,
    "availableCapacity": 400.0,
    "temperatureMin": 2.0,
    "temperatureMax": 8.0,
    "ownerName": "Rajan Patil"
  }
}
```

---

## 4. Discover Storages by City (Farmer)

```bash
curl -X GET "http://localhost:8080/storages/city?city=Nashik" \
  -H "Authorization: Bearer <farmer_jwt_token>"
```

--- Response (200 OK) ---

```json
{
  "success": true,
  "data": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "Nashik GrapeFrost Vault",
      "availableCapacity": 350.0,
      "totalCapacity": 500.0,
      "temperatureMin": 2.0,
      "temperatureMax": 8.0,
      "cityName": "Nashik",
      "ownerName": "Rajan Patil"
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "name": "Nashik WineCool Storage",
      "availableCapacity": 220.0,
      "totalCapacity": 300.0,
      "temperatureMin": -5.0,
      "temperatureMax": 5.0,
      "cityName": "Nashik",
      "ownerName": "Suresh Deshmukh"
    }
  ]
}
```

--- Response (404 Not Found — city not in database) ---

```json
{
  "success": false,
  "message": "City not found: InvalidCity"
}
```

---

## 5. Create Booking (Farmer)

```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <farmer_jwt_token>" \
  -d '{
    "storageId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "produceType": "Grapes",
    "quantity": 50.0,
    "startDate": "2025-01-10",
    "endDate": "2025-03-10"
  }'
```

--- Response (201 Created) ---

```json
{
  "success": true,
  "message": "Booking created successfully",
  "data": {
    "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
    "storageName": "Nashik GrapeFrost Vault",
    "produceType": "Grapes",
    "quantity": 50.0,
    "startDate": "2025-01-10",
    "endDate": "2025-03-10",
    "status": "PENDING",
    "createdAt": "2025-01-08T14:30:00"
  }
}
```

--- Response (409 Conflict — insufficient capacity) ---

```json
{
  "success": false,
  "message": "Insufficient storage capacity"
}
```

---

## 6. Get Farmer's Bookings

```bash
curl -X GET http://localhost:8080/bookings/user \
  -H "Authorization: Bearer <farmer_jwt_token>"
```

--- Response (200 OK) ---

```json
{
  "success": true,
  "data": [
    {
      "id": "c3d4e5f6-a7b8-9012-cdef-123456789012",
      "storageName": "Nashik GrapeFrost Vault",
      "produceType": "Grapes",
      "quantity": 50.0,
      "startDate": "2025-01-10",
      "endDate": "2025-03-10",
      "status": "COMPLETED",
      "createdAt": "2025-01-08T14:30:00"
    },
    {
      "id": "d4e5f6a7-b8c9-0123-defa-234567890123",
      "storageName": "Pune AgriChill Hub",
      "produceType": "Tomatoes",
      "quantity": 40.0,
      "startDate": "2025-02-01",
      "endDate": "2025-04-01",
      "status": "CONFIRMED",
      "createdAt": "2025-01-25T09:15:00"
    }
  ]
}
```

---

## 7. Log Temperature (Storage Owner)

```bash
curl -X POST http://localhost:8080/temperature-logs \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <owner_jwt_token>" \
  -d '{
    "storageId": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "temperature": 5.2,
    "humidity": 78.0
  }'
```

--- Response (201 Created) ---

```json
{
  "success": true,
  "data": "Temperature logged successfully"
}
```

---

## 8. Get All Storages

```bash
curl -X GET http://localhost:8080/storages \
  -H "Authorization: Bearer <any_jwt_token>"
```

--- Response (200 OK) ---

```json
{
  "success": true,
  "data": [
    {
      "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "name": "Nashik GrapeFrost Vault",
      "cityName": "Nashik",
      "latitude": 20.0259,
      "longitude": 73.7894,
      "totalCapacity": 500.0,
      "availableCapacity": 350.0,
      "temperatureMin": 2.0,
      "temperatureMax": 8.0,
      "ownerName": "Rajan Patil"
    },
    {
      "id": "b2c3d4e5-f6a7-8901-bcde-f12345678901",
      "name": "Nashik WineCool Storage",
      "cityName": "Nashik",
      "latitude": 19.9759,
      "longitude": 73.7994,
      "totalCapacity": 300.0,
      "availableCapacity": 220.0,
      "temperatureMin": -5.0,
      "temperatureMax": 5.0,
      "ownerName": "Suresh Deshmukh"
    },
    {
      "id": "e5f6a7b8-c9d0-1234-efab-345678901234",
      "name": "Pune AgriChill Hub",
      "cityName": "Pune",
      "latitude": 18.5304,
      "longitude": 73.8367,
      "totalCapacity": 450.0,
      "availableCapacity": 340.0,
      "temperatureMin": 2.0,
      "temperatureMax": 8.0,
      "ownerName": "Anita Shinde"
    }
  ]
}
```
