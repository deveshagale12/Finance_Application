# Finance_Application

                    POST /api/v1/customers
                              │
                              ▼
                    CustomerController
                              │
                       @Valid Request
                              │
                              ▼
                    CustomerService
                              │
                    ┌─────────┴─────────┐
                    │                   │
                    ▼                   ▼
             Duplicate Check      Business Rules
                    │                   │
                    └─────────┬─────────┘
                              ▼
                    CustomerRepository
                              │
                              ▼
                         PostgreSQL
                              │
                              ▼
                       CustomerResponse



POST API:
curl --location 'https://finance-application-9zbc.onrender.com/api/v1/customers' \
--header 'Content-Type:  application/json' \
--data-raw '{
    "firstName": "Devesh",
    "middleName": "Prakash",
    "lastName": "Agale",
    "dateOfBirth": "2000-01-15",
    "gender": "MALE",
    "email": "devesh@example.com",
    "phoneNumber": "9876543210",
    "customerType": "INDIVIDUAL",
    "nationality": "INDIAN"
}'