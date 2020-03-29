# Expenses [![Build Status](https://travis-ci.org/seguraotoba/expenses.svg?branch=master)](https://travis-ci.org/seguraotoba/expenses)

This is an API to help on monthly expenses control. 

Right below you can find the resources the API manages and the actions that you can do with it.

## Tech Stach
* Clojure
* Pedestal
* Mongo

## Get started
1. To execute the application `lein run`
2. The application is accessible by [localhost:8080](http://localhost:8080/)
3. To run the tests `lein midje`
                   
## Endpoints

* `GET /expenses/purchases?month=01&year=2000` Fetches all the purchases filtered by month and year (month is represented by number)

* `GET /expenses/purchases/summary?month=01&year=2020&by=category` Retrieves a summary of the purchases filtered by period and grouped by category or title

* `POST /expenses/purchases` Create new purchase
```json
{
   "date": "2019-11-06",
   "category": "serviços",
   "title": "Mercpago*Pgtodecontas - tim",
   "amount": 49.99,
   "bill-date": "2019-12",
   "source": "Nubank"
}
```

* `POST /expenses/purchases/batch` Create multiple puchases at once
```json
[
    {
       "date": "2019-11-06",
       "category": "Restaurante",
       "title": "McDonalds",
       "amount": 10,
       "bill-date": "2019-12",
       "source": "Bank Card"
    },
    {
       "date": "2019-11-06",
       "category": "Supermercado",
       "title": "Carrefour",
       "amount": 15,
       "bill-date": "2019-12",
       "source": "Bank Card"
    }
]
```
