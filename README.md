# Employee Api Interface

## Endpoints

- **GET /**: Retrieve a list of all employees.
- **GET /search/{searchString}**: Search for employees by name.
- **GET /{id}**: Retrieve an employee by ID.
- **GET /highestSalary**: Retrieve the highest salary among all employees.
- **GET /topTenHighestEarningEmployeeNames**: Retrieve the names of the top ten highest-earning employees.
- **POST /**: Create a new employee.
- **DELETE /{id}**: Delete an employee by ID.

## Usage

### GET /
```http
GET /
```
Response:
- Status: 200 OK
- Body: List of employees

### GET /search/{searchString}
```http
GET /search/{searchString}
```
Response:
- Status: 200 OK
- Body: List of employees matching the search criteria

### GET /{id}
```http
GET /{id}
```
Response if found:
- Status: 200 OK
- Body: Employee data

Response if not found:
- Status: 404 
- Body: null

### GET /highestSalary
```http
GET /highestSalary
```
Response:
- Status: 200 OK
- Body: Highest salary among all employees

### GET /topTenHighestEarningEmployeeNames
```http
GET /topTenHighestEarningEmployeeNames
```
Response:
- Status: 200 OK
- Body: List of names of the top ten highest-earning employees

### POST /
```http
POST /
```
Request Body:
```json
{
  "employee_name": "John Doe",
  "employee_age": 30,
  "employee_salary": 50000
}
```
Response if success:
- Status: 200 OK
- Body: Created employee data

Response if the input does not match expectations:
- Status: 422 Unprocessable Entity

### DELETE /{id}
```http
DELETE /{id}
```
Response:
- Status: 200 OK
- Body: Success message
