(ns expenses.db.revenue-test
  (:require [midje.sweet :refer :all]
            [expenses.db.revenue :as db.revenue]
            [expenses.logic.db-helper :as db-helper]
            [expenses.logic.date-helper :as date-helper]
            [monger.collection :as mc]))

(fact "create revenue should add the right id and created-at to the revenue"
  (db.revenue/create-revenue {:title     "Salary"
                              :amount    3500
                              :recurrent true
                              :active    true} ..db..) => ..inserted-input..
  (provided
    (db-helper/generate-uuid) => ..uuid..
    (date-helper/current-date) => ..today..
    (mc/insert-and-return ..db.. "revenue" {:_id        ..uuid..
                                            :title      "Salary"
                                            :amount     3500
                                            :active     true
                                            :recurrent  true
                                            :created-at ..today..
                                            :updated-at ..today..}) => ..inserted-input..))

(fact "Update revenue should add updated-at to revenue"
  (db.revenue/update-revenue ..uuid.. {:title      "Salary"
                                       :amount     3500
                                       :recurrent  true
                                       :active     false
                                       :created-at ..created-date..} ..db..) => ..updated-revenue..
  (provided
    (date-helper/current-date) => ..current-date..
    (mc/update-by-id ..db.. "revenue"  ..uuid.. {:title      "Salary"
                                                 :amount     3500
                                                 :active     false
                                                 :recurrent  true
                                                 :created-at ..created-date..
                                                 :updated-at ..current-date..}) => ..updated-revenue..))