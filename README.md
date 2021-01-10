### Brief

While modern banks have evolved to serve a plethora of functions, at their core, banks must provide certain basic features. Today, your task is to build the basic HTTP API for one of those banks! Imagine you are designing a backend API for bank employees. It could ultimately be consumed by multiple frontends (web, iOS, Android etc).

### Tasks

- Implement assignment using:
  - Language: **Java**
  - Framework: **Spring**
- There should be API routes that allow them to:
  - Create a new bank account for a customer, with an initial deposit amount. A
    single customer may have multiple bank accounts.
  - Transfer amounts between any two accounts, including those owned by
    different customers.
  - Retrieve balances for a given account.
  - Retrieve transfer history for a given account.
- Write tests for your business logic

Feel free to pre-populate your customers with the following:

```json
[
  {
    "id": 1,
    "name": "Arisha Barron"
  },
  {
    "id": 2,
    "name": "Branden Gibson"
  },
  {
    "id": 3,
    "name": "Rhonda Church"
  },
  {
    "id": 4,
    "name": "Georgina Hazel"
  }
]
```

**********************************************************************************
**********************************************************************************


### Mehmet Aktas's Assignment Solution

All financial amounts such as account balance, transfer amounts are in pence. For ex: £1 = 100p

### API documentation added via Swagger

After starting server please visit http://localhost:8080/swagger-ui to see full api spec

### Create Account

Path: /api/v1/accounts/
Method: POST

Request Model :

```json
{

	"customerId" : 1,
	"initialBalance" : 1000,
	"accountName"  :  "2nd Account"

}
```

Response Model :

```json
{
    "id": 34,
    "name": "2nd Account",
    "customerId": 1,
    "balance": 1000,
    "createdAt": "2020-12-12T18:02:12.805Z",
    "updatedAt": "2020-12-12T18:02:12.805Z"
}
```


### Create Transfer

Path: /api/v1/transfers/
Method: POST

Request Model :

```json
{

	"accountId" : 2,
	"destinationAccountId" : 1 ,
	"description"  :  "First transfer",
	"amount"  :  10

}

```

Response Model :

```json
{
    "id": 43,
    "amount": 10,
    "description": "First transfer",
    "accountId": 2,
    "destinationAccountId": 1,
    "createdAt": "2020-12-12T18:02:28.182Z",
    "updatedAt": "2020-12-12T18:02:28.182Z"
}
```


### Retrieve Transfer List

Path: /api/v1/accounts/{accountId}/transfers
Method: GET


Response Model :

accountId: 1

```json
[
    {
        "id": 43,
        "amount": 10,
        "description": "First transfer",
        "accountId": 2,
        "destinationAccountId": 1,
        "createdAt": "2020-12-12T18:02:28.182Z",
        "updatedAt": "2020-12-12T18:02:28.182Z"
    },
    {
        "id": 42,
        "amount": 10,
        "description": "Second transfer",
        "accountId": 2,
        "destinationAccountId": 1,
        "createdAt": "2020-12-12T18:02:20.903Z",
        "updatedAt": "2020-12-12T18:02:20.903Z"
    }

]
```


### Retrieve Account Detail With Balance

Path: /api/v1/accounts/{accountId}/
Method: GET


Response Model :

accountId: 1

```json
{
    "id": 1,
    "name": "2 nd Account",
    "customerId": 2,
    "balance": 420,
    "createdAt": "2020-12-12T14:34:48.706Z",
    "updatedAt": "2020-12-12T18:02:28.182Z"
}
```



### Retrieve Balance Transaction List

Path: /api/v1/accounts/{accountId}/balance-transactions
Method: GET


Response Model :

accountId: 1

```json
[
    {
        "id": 107,
        "amount": 10,
        "balanceAfter": 380,
        "description": "First transfer",
        "accountId": 1,
        "sourceType": "TRANSFER",
        "sourceId": 39,
        "createdAt": "2020-12-12T18:01:08.527Z",
        "updatedAt": "2020-12-12T18:01:08.527Z"
    },

    {
        "id": 105,
        "amount": 10,
        "balanceAfter": 370,
        "description": "First transfer",
        "accountId": 1,
        "sourceType": "TRANSFER",
        "sourceId": 38,
        "createdAt": "2020-12-12T18:01:08.143Z",
        "updatedAt": "2020-12-12T18:01:08.143Z"
    }
]
```



