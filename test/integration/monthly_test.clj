(ns integration.monthly_test
  (:require [midje.sweet :refer :all]
            [integration.helper :as helper]))

(background (before :facts (helper/clean-collection "purchases")))
(background (before :facts (helper/clean-collection "fixed")))
(background (before :facts (helper/clean-collection "revenue")))

(defn create-data []
  (helper/do-post-request "/revenue" {:title     "Salary"
                                      :amount    5000
                                      :recurrent true})
  (helper/do-post-request "/expenses/fixed" {:title  "Rent"
                                             :amount 1500
                                             :source "cash"})
  (helper/do-post-request "/expenses/fixed" {:title  "Internet provider"
                                             :amount 200
                                             :source "cash"})
  (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                :amount     300
                                                :source     "Credit Card"
                                                :category   "Supermercado"
                                                :date       "2020-10-20"
                                                :bill-month "11"
                                                :bill-year  "2020"})
  (helper/do-post-request "/expenses/purchase" {:title      "Best Buy"
                                                :amount     1000
                                                :source     "Credit Card"
                                                :category   "Eletronico"
                                                :date       "2020-10-20"
                                                :bill-month "11"
                                                :bill-year  "2020"})
  (helper/do-post-request "/expenses/purchase" {:title      "Macys"
                                                :amount     500
                                                :source     "Credit Card"
                                                :category   "Supermercado"
                                                :date       "2020-10-20"
                                                :bill-month "11"
                                                :bill-year  "2020"}))

(fact "Should return monthly summary data for given period"
  (create-data)
  (helper/do-get-request "/monthly?year=2020&month=11") => {:income   5000
                                                            :expenses 3500
                                                            :balance  1500
                                                            :fixed    [{:title  "Rent"
                                                                        :amount 1500}
                                                                       {:title  "Internet provider"
                                                                        :amount 200}]
                                                            :variable [{:title  "Supermercado"
                                                                        :amount 800}]
                                                            :other    [{:title  "Eletronico"
                                                                        :amount 1000}]})
