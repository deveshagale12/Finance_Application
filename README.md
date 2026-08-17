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