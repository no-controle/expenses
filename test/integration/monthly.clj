(ns integration.monthly
  (:require [midje.sweet :refer :all]
            [integration.helper :as helper]))

;(fact "Should "
;  (helper/do-get-request "/frontendapi/monthly?year=2020&month=10") => {:income   5000
;                                                                        :expenses 3500
;                                                                        :balance  1500
;                                                                        :fixed    [{:title  "Rent"
;                                                                                    :amount 1000}
;                                                                                   {:title  "Internet provider"
;                                                                                    :amount 100}
;                                                                                   {:title  "Mobile bill Mary"
;                                                                                    :amount 50}
;                                                                                   {:title  "Mobile bill John"
;                                                                                    :amount 50}]
;                                                                        :variable [{:title  "Food"
;                                                                                    :amount 1000}
;                                                                                   {:title  "Pets"
;                                                                                    :amount 200}
;                                                                                   {:title  "Medicine"
;                                                                                    :amount 100}
;                                                                                   {:title  "Personal health"
;                                                                                    :amount 200}]
;                                                                        :other    [{:title  "Loan"
;                                                                                    :amount 500}
;                                                                                   {:title  "Recreation"
;                                                                                    :amount 200}
;                                                                                   {:title  "Repairs"
;                                                                                    :amount 100}]})