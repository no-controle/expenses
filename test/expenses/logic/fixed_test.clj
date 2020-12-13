(ns expenses.logic.fixed-test
  (:require [midje.sweet :refer :all]
            [expenses.logic.fixed :as logic.fixed]))

(def active-expenses [{:title        "Rent"
                       :amount       800
                       :active       true
                       :created-at "2020-04-01"
                       :updated-at "2020-04-01"}
                      {:title        "Internet Provider"
                       :amount       100
                       :active       true
                       :created-at "2020-04-01"
                       :updated-at "2020-04-01"}
                      {:title        "Netflix"
                       :amount       20
                       :active       true
                       :created-at "2020-10-10"
                       :updated-at "2020-10-10"}])

(def inactive-expenses [{:title        "Rent"
                         :amount       1400
                         :active       false
                         :created-at "2019-04-01"
                         :updated-at "2020-04-01"}
                        {:title        "Internet Provider"
                         :amount       100
                         :active       false
                         :created-at "2019-04-01"
                         :updated-at "2020-04-01"}])



(facts "Getting data for given month and year"
  (fact "should return total adding current active expenses for month"
    (logic.fixed/data-for-month-year {:month "Fev" :month-value 2} "2020" active-expenses inactive-expenses) => {:title "Fev" :amount 1500})
  (fact "should return total adding current active expenses and Inactive expenses for month"
    (logic.fixed/data-for-month-year {:month "Abr" :month-value 4} "2020" active-expenses inactive-expenses) => {:title "Abr" :amount 2400})
  (fact "should return total adding current Inactive expenses for month"
    (logic.fixed/data-for-month-year {:month "Out" :month-value 10} "2020" active-expenses inactive-expenses) => {:title "Out" :amount 920}))