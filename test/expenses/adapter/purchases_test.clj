(ns expenses.adapter.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.adapter.purchases :as adapter.purchases]))

(facts "create purchase input based on request"
  (fact "should return purchase with all fields when it has required fields"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      "2019-01-01"
                                          :amount    50
                                          :category  "Restaurant"
                                          :source    "Credit Card"
                                          :bill-date "2019-12"}) => {:title     "The best food place"
                                                                     :date      "2019-01-01"
                                                                     :amount    50
                                                                     :category  "Restaurant"
                                                                     :source    "Credit Card"
                                                                     :bill-date "2019-12"})


  (fact "should throw IllegalArgumentException when title is missing"
    (adapter.purchases/http-in->purchase  {:title     nil
                                           :date      "2019-01-01"
                                           :amount    50
                                           :category  "Restaurant"
                                           :source    "Credit Card"
                                           :bill-date "2019-12"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when date is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      nil
                                          :amount    50
                                          :category  "Restaurant"
                                          :source    "Credit Card"
                                          :bill-date "2019-12"}) => (throws IllegalArgumentException))

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
                                          :bill-date "2019-12"}) => (throws IllegalArgumentException))

  (fact "should throw IllegalArgumentException when source is missing"
    (adapter.purchases/http-in->purchase {:title     "The best food place"
                                          :date      "2019-01-01"
                                          :amount    50
                                          :category  "Restaurant"
                                          :source    nil
                                          :bill-date "2019-12"}) => (throws IllegalArgumentException)))

(facts "create purchases list input based on request"
  (fact "return valid list when request is valid"
    (adapter.purchases/http-in->purchases-list [{:title     "The best food place"
                                                 :date      "2019-01-01"
                                                 :amount    50
                                                 :category  "Restaurant"
                                                 :source    "Credit Card"
                                                 :bill-date "2019-12"}
                                                {:title     "The best dance place"
                                                 :date      "2019-01-02"
                                                 :amount    200
                                                 :category  "Entertainment"
                                                 :source    "Credit Card"
                                                 :bill-date "2019-12"}]) => [{:title     "The best food place"
                                                                              :date      "2019-01-01"
                                                                              :amount    50
                                                                              :category  "Restaurant"
                                                                              :source    "Credit Card"
                                                                              :bill-date "2019-12"}

                                                                             {:title     "The best dance place"
                                                                              :date      "2019-01-02"
                                                                              :amount 200
                                                                              :category  "Entertainment"
                                                                              :source    "Credit Card"
                                                                              :bill-date "2019-12"}])

  (fact "should throw IllegalArgumentException when some of the inputs on the list is not valid"
    (adapter.purchases/http-in->purchases-list [{:title     "The best food place"
                                                 :date      "2019-01-01"
                                                 :amount    50
                                                 :category  nil
                                                 :source    "Credit Card"
                                                 :bill-date "2019-12"}
                                                {:title    "The best dance place"
                                                 :date     "2019-01-02"
                                                 :amount   200
                                                 :category "Entertainment"
                                                 :source   nil}]) => (throws IllegalArgumentException)))
