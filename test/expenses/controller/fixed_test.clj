(ns expenses.controller.fixed-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.db.fixed :as db.fixed]
            [expenses.logic.date-helper :as date-helper]))

(def fixed-expense-request {:title      "Rent"
                            :amount     800
                            :source     "cash"
                            :start-date "2020-12-02"})

(def fixed-expense {:_id           1234
                    :title        "Rent"
                    :amount       800
                    :source       "cash"
                    :active       true
                    :created-at "2020-10-10"})

(facts "create a new fixed expense"
  (fact "should add active true to expense and return created expense"
    (controller.fixed/create-fixed fixed-expense-request ..db..) => fixed-expense
    (provided
      (db.fixed/search-expense-with {:title      "Rent"
                                     :amount     800
                                     :source     "cash"
                                     :start-date "2020-12-02"
                                     :active     true} ..db..) => nil
      (db.fixed/create-expense {:title  "Rent"
                                :amount 800
                                :source "cash"
                                :start-date "2020-12-02"
                                :active true} ..db..) => fixed-expense))

  (fact "should set start-date to current date when it's empty and return created expense"
        (controller.fixed/create-fixed (dissoc fixed-expense-request :start-date) ..db..) => fixed-expense
        (provided
          (date-helper/current-date) => ..today..
          (db.fixed/search-expense-with {:title      "Rent"
                                         :amount     800
                                         :source     "cash"
                                         :start-date ..today..
                                         :active     true} ..db..) => nil
          (db.fixed/create-expense {:title  "Rent"
                                    :amount 800
                                    :source "cash"
                                    :start-date ..today..
                                    :active true} ..db..) => fixed-expense))

  (fact "should return error when creating existent expense"
    (controller.fixed/create-fixed fixed-expense-request ..db..) => (throws Exception)
    (provided
      (db.fixed/search-expense-with {:title  "Rent"
                                     :amount 800
                                     :source "cash"
                                     :start-date "2020-12-02"
                                     :active true} ..db..) => [fixed-expense])))

(facts "delete fixed expense"
  (fact "should delete expense when exists"
    (controller.fixed/delete-fixed ..id.. ..db..) => {:message "Expense deleted"}
    (provided
      (db.fixed/search-expense-with {:_id ..id..} ..db..) => [{:_id ..id.. :active true}]
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

(facts "Getting fixed expenses for every month on given year"
  (fact "Should return active expenses from created month until december"
    (controller.fixed/fixed-expenses-for-year "2020" ..db..) => [{:title  "Jan"
                                                                  :amount 0.0}
                                                                 {:title  "Fev"
                                                                  :amount 0.0}
                                                                 {:title  "Mar"
                                                                  :amount 0.0}
                                                                 {:title  "Abr"
                                                                  :amount 900.0}
                                                                 {:title  "Mai"
                                                                  :amount 900.0}
                                                                 {:title  "Jun"
                                                                  :amount 900.0}
                                                                 {:title  "Jul"
                                                                  :amount 900.0}
                                                                 {:title  "Ago"
                                                                  :amount 900.0}
                                                                 {:title  "Set"
                                                                  :amount 900.0}
                                                                 {:title  "Out"
                                                                  :amount 920.0}
                                                                 {:title  "Nov"
                                                                  :amount 920.0}
                                                                 {:title  "Dez"
                                                                  :amount 920.0}]
    (provided
      (db.fixed/search-expense-with {:active true} ..db..) => [{:title        "Rent"
                                                                :amount       800
                                                                :active       true
                                                                :start-date "2020-04-01"
                                                                :created-at "2020-04-01"
                                                                :updated-at "2020-04-01"}
                                                               {:title        "Internet Provider"
                                                                :amount       100
                                                                :active       true
                                                                :start-date "2020-04-01"
                                                                :created-at "2020-04-01"
                                                                :updated-at "2020-04-01"}
                                                               {:title        "Netflix"
                                                                :amount       20
                                                                :active       true
                                                                :start-date "2020-10-10"
                                                                :created-at "2020-10-10"
                                                                :updated-at "2020-10-10"}]
      (db.fixed/search-expense-with {:active false} ..db..) => []))

  (fact "Should return inactive expenses until updated date"
    (controller.fixed/fixed-expenses-for-year "2020" ..db..) => [{:title  "Jan"
                                                                  :amount 1500.0}
                                                                 {:title  "Fev"
                                                                  :amount 1500.0}
                                                                 {:title  "Mar"
                                                                  :amount 1500.0}
                                                                 {:title  "Abr"
                                                                  :amount 1500.0}
                                                                 {:title  "Mai"
                                                                  :amount 0.0}
                                                                 {:title  "Jun"
                                                                  :amount 0.0}
                                                                 {:title  "Jul"
                                                                  :amount 0.0}
                                                                 {:title  "Ago"
                                                                  :amount 0.0}
                                                                 {:title  "Set"
                                                                  :amount 0.0}
                                                                 {:title  "Out"
                                                                  :amount 0.0}
                                                                 {:title  "Nov"
                                                                  :amount 0.0}
                                                                 {:title  "Dez"
                                                                  :amount 0.0}]
    (provided
      (db.fixed/search-expense-with {:active true} ..db..) => []
      (db.fixed/search-expense-with {:active false} ..db..) => [{:title        "Rent"
                                                                 :amount       1400
                                                                 :active       false
                                                                 :start-date "2019-04-01"
                                                                 :created-at "2019-04-01"
                                                                 :updated-at "2020-04-01"}
                                                                {:title        "Internet Provider"
                                                                 :amount       100
                                                                 :active       false
                                                                 :start-date "2019-04-01"
                                                                 :created-at "2019-04-01"
                                                                 :updated-at "2020-04-01"}])))
