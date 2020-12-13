(ns expenses.logic.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.logic.purchases :as logic.purchases]))

(def purchase-list [{:title    "Grocery Store"
                     :date     "2020-12-05"
                     :amount   50
                     :bill-year "2020"
                     :bill-month "03"
                     :category "Supermarket"}
                    {:title    "The Fun Fun Fun"
                     :date     "2020-12-09"
                     :amount   300
                     :bill-year "2020"
                     :bill-month "03"
                     :category "Entertainment"}
                    {:title    "The med place"
                     :date     "2020-12-15"
                     :amount   14
                     :bill-year "2020"
                     :bill-month "04"
                     :category "Health"}
                    {:title    "Grocery Store"
                     :date     "2020-12-08"
                     :amount   80
                     :bill-year "2020"
                     :bill-month "05"
                     :category "Supermarket"}])


(facts "Getting data for given month and year"
  (fact "should return total purchases for given March"
    (logic.purchases/data-for-month {:month "Mar" :month-value 3} purchase-list) => {:title "Mar" :amount 350})
  (fact "should return total purchases for given May"
    (logic.purchases/data-for-month {:month "Mai" :month-value 5} purchase-list) => {:title "Mai" :amount 80}))