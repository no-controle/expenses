(ns expenses.db.fixed-test
  (:require [midje.sweet :refer :all]
            [expenses.db.fixed :as db.fixed]
            [monger.collection :as mc]))

(fact "create fixed expense should add the right id and created-at to the expense"
  (db.fixed/create-expense {:title  "Rent"
                            :amount 800
                            :source "cash"
                            :active true} ..db..) => ..inserted-input..
  (provided
    (db.fixed/generate-uuid) => ..uuid..
    (db.fixed/current-date) => ..today..
    (mc/insert-and-return ..db.. "fixed" {:_id        ..uuid..
                                          :title      "Rent"
                                          :amount     800
                                          :active     true
                                          :source     "cash"
                                          :created-at ..today..}) => ..inserted-input..))

(facts "search fixed expenses"
  (fact "should return result for search parameters when exists"
    (db.fixed/search-expense-with {:title  "Rent"
                                   :amount 800
                                   :source "cash"
                                   :active true} ..db..) => ..existent-expense..
    (provided
      (mc/find-maps ..db.. "fixed" {:title  "Rent"
                                    :amount 800
                                    :source "cash"
                                    :active true}) => ..existent-expense..))

  (fact "should return nil for search parameters when expense does not exist"
    (db.fixed/search-expense-with {:title  "Rent"
                                   :amount 800
                                   :source "cash"
                                   :active true} ..db..) => nil
    (provided
      (mc/find-maps ..db.. "fixed" {:title  "Rent"
                                    :amount 800
                                    :source "cash"
                                    :active true}) => nil)))
