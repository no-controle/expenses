(ns expenses.adapter.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.adapter.purchases :as adapter.purchases]))

(def csv "date,category,title,amount,source,bill-month,bill-year\n2017-11-29,transporte,Uber do Brasil Tecnolo,8.89, Credit card, 1, 2018\n2017-11-29,transporte,Uber do Brasil Tecnolo,7.93, Credit card, 1, 2018\n2017-11-30,viagem,Aviancasiteb2c*Lism8t2 2/3,110, Credit card, 1, 2018")

(facts "create purchase input based on request"
  (fact "should return purchase with all fields when it has required fields"
    (adapter.purchases/http-in->purchase {:title      "The best food place"
                                          :date       "2019-01-01"
                                          :amount     50
                                          :category   "Restaurant"
                                          :source     "Credit Card"
                                          :bill-month "12"
                                          :bill-year  "2019"}) => {:title      "The best food place"
                                                                   :date       "2019-01-01"
                                                                   :amount     50
                                                                   :category   "Restaurant"
                                                                   :source     "Credit Card"
                                                                   :bill-month "12"
                                                                   :bill-year  "2019"})


  (fact "should throw IllegalArgumentException when title is missing"
    (adapter.purchases/http-in->purchase  {:title     nil
                                           :date      "2019-01-01"
                                           :amount    50
                                           :category  "Restaurant"
                                           :source    "Credit Card"
                                           :bill-month "12"
                                           :bill-year  "2019"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when date is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      nil
                                          :amount    50
                                          :category  "Restaurant"
                                          :source    "Credit Card"
                                          :bill-month "12"
                                          :bill-year  "2019"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when amount is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      "2019-01-01"
                                          :amount    nil
                                          :category  "Restaurant"
                                          :source    "Credit Card"
                                          :bill-date "2019-12"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when category is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      "2019-01-01"
                                          :amount    50
                                          :category  nil
                                          :source    "Credit Card"
                                          :bill-month "12"
                                          :bill-year  "2019"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when source is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      "2019-01-01"
                                          :amount    50
                                          :category  "Restaurant"
                                          :source    nil
                                          :bill-month "12"
                                          :bill-year  "2019"}) => (throws IllegalArgumentException)))

(facts "create purchases list input based on request"
  (fact "return valid list when request is valid "
    (adapter.purchases/http-in->purchases-list {:csv csv}) => [{:amount     8.89
                                                                :bill-month " 1"
                                                                :bill-year  " 2018"
                                                                :category   "transporte"
                                                                :date       "2017-11-29"
                                                                :source     " Credit card"
                                                                :title      "Uber do Brasil Tecnolo"}
                                                               {:amount     7.93
                                                                :bill-month " 1"
                                                                :bill-year  " 2018"
                                                                :category   "transporte"
                                                                :date       "2017-11-29"
                                                                :source     " Credit card"
                                                                :title      "Uber do Brasil Tecnolo"}
                                                               {:amount     110.0
                                                                :bill-month " 1"
                                                                :bill-year  " 2018"
                                                                :category   "viagem"
                                                                :date       "2017-11-30"
                                                                :source     " Credit card"
                                                                :title      "Aviancasiteb2c*Lism8t2 2/3"}]))
