# Car Service Agency

This is a Spring Boot application for managing appointments in a car service agency. The application allows service operators to book, reschedule, and cancel appointments for customers. It also provides functionality to retrieve booked appointments and available time slots for each service operator.

## Features

- Book an appointment with a specific service operator or any available operator
- Reschedule an existing appointment
- Cancel an existing appointment
- Retrieve booked appointments for a service operator
- Retrieve available time slots for a service operator

## Prerequisites

- Java 17
- Maven
- A compatible database (e.g., MySQL, PostgreSQL)

## Setup

1. Clone the repository:
   ```git clone https://github.com/luckyx3/Car-service-agency```
2. Navigate to the project directory:
   ```cd car-service-agency```
3. Configure the database connection in the `application.yml` file:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: your_username
    password: your_password
```
4. Build the Project:
``mvn clean install``
5. Run the application:
``mvn spring-boot:run``

The application will start running on `http://localhost:8080`.

## API Endpoints

### Book an Appointment

**Endpoint**: `/appointment/book`

**Method**: `POST`

**Query Parameters**:
- `serviceOperatorName` (optional): The name of the service operator (e.g., `ServiceOperator0`)
- `startHour`: The start hour of the appointment (0-23)
- `endHour`: The end hour of the appointment (0-23)

**Response**:
```json
{
  "id": 1,
  "startHour": 9,
  "endHour": 10,
  "serviceOperatorName": "ServiceOperator0"
}
```
### Reschedule an Appointment

**Endpoint**: `/appointment/{appointmentId}/reschedule`

**Method**: `PUT`

**Path Parameter**:
- `appointmentId`: The ID of the appointment to be rescheduled

**Query Parameters**:
- `startHour`: The start hour of the appointment (0-23)
- `endHour`: The end hour of the appointment (0-23)

**Response**:
```json
{
  "id": 1,
  "startHour": 11,
  "endHour": 12,
  "serviceOperatorName": "ServiceOperator1"
}
```
### Cancel an Appointment

**Endpoint**: `/appointment/{appointmentId}/cancel`

**Method**: `DELETE`

**Path Parameter**:

- `appointmentId`: The ID of the appointment to be canceled

**Response**: `200 OK`

### Get Booked Appointments for a Service Operator

**Endpoint**: `/appointment/operator/{serviceOperatorName}/booked`

**Method**: `GET`

**Path Parameter**:

- `serviceOperatorName`: The name of the service operator

**Response**:

```json
[
 {
   "id": 1,
   "startHour": 9,
   "endHour": 10,
   "serviceOperatorName": "ServiceOperator0"
 },
 {
   "id": 2,
   "startHour": 14,
   "endHour": 15,
   "serviceOperatorName": "ServiceOperator0"
 }
]
```

### Get Available Time Slots for a Service Operator

**Endpoint**: `/appointment/operator/{serviceOperatorName}/available`

**Method**: `GET`

**Path Parameter**:

- `serviceOperatorName`: The name of the service operator

**Response**:

```json
[
   {
      "startHour": 0,
      "endHour": 9
   },
   {
      "startHour": 10,
      "endHour": 14
   },
   {
      "startHour": 15,
      "endHour": 24
   }
]
```
