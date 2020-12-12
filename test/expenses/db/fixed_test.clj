(ns expenses.db.fixed-test
  (:require [midje.sweet :refer :all]
            [expenses.db.fixed :as db.fixed]
            [monger.collection :as mc]
            [expenses.logic.db-helper :as db-helper]))

(fact "create fixed expense should add the right id and created-at to the expense"
  (db.fixed/create-expense {:title  "Rent"
                            :amount 800
                            :source "cash"
                            :active true} ..db..) => ..inserted-input..
  (provided
    (db-helper/generate-uuid) => ..uuid..
    (db-helper/current-date) => ..today..
    (mc/insert-and-return ..db.. "fixed" {:_id        ..uuid..
                                          :title      "Rent"
                                          :amount     800
                                          :active     true
                                          :source     "cash"
                                          :created-at ..today..
                                          :updated-at ..today..}) => ..inserted-input..))

(fact "Update fixed expense should add updated-at value"
  (db.fixed/update-expense ..uuid.. {:_id        ..uuid..
                                     :title      "Rent"
                                     :amount     800
                                     :source     "cash"
                                     :created-at ..created-date..} ..db..) => ..updated-expense..
  (provided
    (db-helper/current-date) => ..current-date..
    (mc/update-by-id ..db.. "fixed"  ..uuid.. {:_id        ..uuid..
                                               :title      "Rent"
                                               :amount     800
                                               :source     "cash"
                                               :created-at ..created-date..
                                               :updated-at ..current-date..}) => ..updated-expense..))

(facts "search fixed expenses"
  (fact "should return result for search parameters when exists"
    (db.fixed/search-expense-with {:title  "Rent"
                                   :amount 800
                                   :source "cash"
                                   :active true} ..db..) => ..existent-expenses..
    (provided
      (mc/find-maps ..db.. "fixed" {:title  "Rent"
                                    :amount 800
                                    :source "cash"
                                    :active true}) => ..existent-expenses..))

  (fact "should return nil for search parameters when expense does not exist"
    (db.fixed/search-expense-with {:title  "Rent"
                                   :amount 800
                                   :source "cash"
                                   :active true} ..db..) => []
    (provided
      (mc/find-maps ..db.. "fixed" {:title  "Rent"
                                    :amount 800
                                    :source "cash"
                                    :active true}) => [])))
