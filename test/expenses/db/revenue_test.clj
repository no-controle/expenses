(ns expenses.db.revenue-test
  (:require [midje.sweet :refer :all]
            [expenses.db.revenue :as db.revenue]
            [expenses.logic.db-helper :as db-helper]
            [monger.collection :as mc]))

(fact "create revenue should add the right id and created-at to the revenue"
  (db.revenue/create-revenue {:title     "Salary"
                              :amount    3500
                              :recurrent true
                              :active    true} ..db..) => ..inserted-input..
  (provided
    (db-helper/generate-uuid) => ..uuid..
    (db-helper/current-date) => ..today..
    (mc/insert-and-return ..db.. "revenue" {:_id        ..uuid..
                                            :title      "Salary"
                                            :amount     3500
                                            :active     true
                                            :recurrent  true
                                            :created-at ..today..}) => ..inserted-input..))