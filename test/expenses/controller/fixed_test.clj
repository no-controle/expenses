(ns expenses.controller.fixed-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.db.fixed :as db.fixed]))

(def fixed-expense-request {:title  "Rent"
                            :amount 800
                            :source "cash"})

(def fixed-expense {:_id           1234
                    :title        "Rent"
                    :amount       800
                    :source       "cash"
                    :active       true
                    :created-date "2020-10-10"})

(facts "create a new fixed expense"
  (fact "should return newly created expense when does not exist on db"
    (controller.fixed/create-fixed fixed-expense-request ..db..) => fixed-expense
    (provided
      (db.fixed/search-expense-with {:title  "Rent"
                                     :amount 800
                                     :source "cash"
                                     :active true} ..db..) => nil
      (db.fixed/create-expense {:title  "Rent"
                                :amount 800
                                :source "cash"
                                :active true} ..db..) => fixed-expense))

  (fact "should return error when creating existing expense"
    (controller.fixed/create-fixed fixed-expense-request ..db..) => {:message "Expense with this values already exists, expense id: 1234"}
    (provided
      (db.fixed/search-expense-with {:title  "Rent"
                                     :amount 800
                                     :source "cash"
                                     :active true} ..db..) => fixed-expense)))

