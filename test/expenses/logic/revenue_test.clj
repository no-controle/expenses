(ns expenses.logic.revenue-test
  (:require [midje.sweet :refer :all]
            [expenses.logic.revenue :as logic.revenue]))

(def recurrent-active [{:title      "Salary"
                        :amount     3500
                        :active     true
                        :recurrent  true
                        :created-at "2020-04-30"
                        :updated-at "2020-04-30"}
                       {:title      "Salary other"
                        :amount     4300
                        :active     true
                        :recurrent  true
                        :created-at "2020-01-01"
                        :updated-at "2020-01-01"}])

(def recurrent-inactive [{:title      "Salary"
                          :amount     2200
                          :active     false
                          :recurrent  true
                          :created-at "2019-03-01"
                          :updated-at "2020-02-01"}])

(def other-revenue [{:title      "Jobz"
                     :amount     1100
                     :created-at "2020-02-01"
                     :updated-at "2020-02-01"}
                    {:title      "Jobz"
                     :amount     1100
                     :created-at "2020-06-01"
                     :updated-at "2020-06-01"}])


(facts "Getting data for given month and year"
  (fact "should return total adding current active revenue to Inactive revenue and other for month "
    (logic.revenue/data-for-month-year
      {:month "Fev" :month-value 2} "2020" recurrent-active recurrent-inactive other-revenue) => {:title "Fev" :amount 7600})
  (fact "testing 2"
    (logic.revenue/data-for-month-year
      {:month "Apr" :month-value 4} "2020" recurrent-active recurrent-inactive other-revenue) => {:title "Apr" :amount 7800})
  (fact "testing 3"
    (logic.revenue/data-for-month-year
      {:month "May" :month-value 5} "2019" recurrent-active recurrent-inactive []) => {:title "May" :amount 2200}))
