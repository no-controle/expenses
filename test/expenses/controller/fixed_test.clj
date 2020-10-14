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
    (controller.fixed/create-fixed fixed-expense-request ..db..) => (throws Exception)
    (provided
      (db.fixed/search-expense-with {:title  "Rent"
                                     :amount 800
                                     :source "cash"
                                     :active true} ..db..) => fixed-expense)))

(facts "delete fixed expense"
  (fact "should delete expense when exists"
    (controller.fixed/delete-fixed ..id.. ..db..) => {:message "Expense deleted"}
    (provided
      (db.fixed/search-expense-with {:_id ..id..} ..db..) => {:_id ..id.. :active true}
      (db.fixed/update-expense ..id..{:_id ..id.. :active false} ..db..) => ..ok..)))

(facts "Get active fixed expenses"
  (fact "should return all fixed expenses for given year and month"
    (controller.fixed/active-fixed-expenses ..db..) => [{:title "fixed-01"
                                                         :amount      200}
                                                        {:title  "fixed-02"
                                                         :amount 100}]
    (provided
      (db.fixed/search-expense-with {:active true} ..db..) => [{:title  "fixed-01"
                                                                :amount 200}
                                                               {:title  "fixed-02"
                                                                :amount 100}])))
