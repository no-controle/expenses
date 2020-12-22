(ns integration.general_test
  (:require [midje.sweet :refer :all]
            [integration.helper.common :as helper]
            [integration.helper.revenue-builder :as revenue-builder]
            [integration.helper.fixed-builder :as fixed-builder]
            [integration.helper.purchases-builder :as purchases-builder]))

(background (before :facts (helper/clean-collection "purchases")))
(background (before :facts (helper/clean-collection "fixed")))
(background (before :facts (helper/clean-collection "revenue")))

(defn create-data []
  (revenue-builder/create-data)
  (fixed-builder/create-data)
  (purchases-builder/create-data))

(future-fact "Skipping general integration tests until understand how to use a memory database"
  (fact "Should return general summary data for given year"
    (create-data)
    (helper/do-get-request (str "/general?year=" helper/current-year)) => {:income   [{:title  "Jan"
                                                                                       :amount 0}
                                                                                      {:title  "Fev"
                                                                                       :amount 0}
                                                                                      {:title  "Mar"
                                                                                       :amount 0}
                                                                                      {:title  "Abr"
                                                                                       :amount 0}
                                                                                      {:title  "Mai"
                                                                                       :amount 0}
                                                                                      {:title  "Jun"
                                                                                       :amount 0}
                                                                                      {:title  "Jul"
                                                                                       :amount 0}
                                                                                      {:title  "Ago"
                                                                                       :amount 0}
                                                                                      {:title  "Set"
                                                                                       :amount 0}
                                                                                      {:title  "Out"
                                                                                       :amount 0}
                                                                                      {:title  "Nov"
                                                                                       :amount 0}
                                                                                      {:title  "Dez"
                                                                                       :amount 12000}]
                                                                           :expenses []
                                                                           :fixed    [{:title  "Jan"
                                                                                       :amount 0}
                                                                                      {:title  "Fev"
                                                                                       :amount 0}
                                                                                      {:title  "Mar"
                                                                                       :amount 0}
                                                                                      {:title  "Abr"
                                                                                       :amount 0}
                                                                                      {:title  "Mai"
                                                                                       :amount 0}
                                                                                      {:title  "Jun"
                                                                                       :amount 0}
                                                                                      {:title  "Jul"
                                                                                       :amount 0}
                                                                                      {:title  "Ago"
                                                                                       :amount 0}
                                                                                      {:title  "Set"
                                                                                       :amount 0}
                                                                                      {:title  "Out"
                                                                                       :amount 0}
                                                                                      {:title  "Nov"
                                                                                       :amount 0}
                                                                                      {:title  "Dez"
                                                                                       :amount 19000}]
                                                                           :variable [{:title  "Jan"
                                                                                       :amount 0}
                                                                                      {:title  "Fev"
                                                                                       :amount 0}
                                                                                      {:title  "Mar"
                                                                                       :amount 0}
                                                                                      {:title  "Abr"
                                                                                       :amount 0}
                                                                                      {:title  "Mai"
                                                                                       :amount 0}
                                                                                      {:title  "Jun"
                                                                                       :amount 0}
                                                                                      {:title  "Jul"
                                                                                       :amount 0}
                                                                                      {:title  "Ago"
                                                                                       :amount 0}
                                                                                      {:title  "Set"
                                                                                       :amount 0}
                                                                                      {:title  "Out"
                                                                                       :amount 0}
                                                                                      {:title  "Nov"
                                                                                       :amount 900}
                                                                                      {:title  "Dez"
                                                                                       :amount 800}]
                                                                           :other    [{:title  "Jan"
                                                                                       :amount 0}
                                                                                      {:title  "Fev"
                                                                                       :amount 0}
                                                                                      {:title  "Mar"
                                                                                       :amount 0}
                                                                                      {:title  "Abr"
                                                                                       :amount 0}
                                                                                      {:title  "Mai"
                                                                                       :amount 0}
                                                                                      {:title  "Jun"
                                                                                       :amount 0}
                                                                                      {:title  "Jul"
                                                                                       :amount 0}
                                                                                      {:title  "Ago"
                                                                                       :amount 0}
                                                                                      {:title  "Set"
                                                                                       :amount 0}
                                                                                      {:title  "Out"
                                                                                       :amount 0}
                                                                                      {:title  "Nov"
                                                                                       :amount 1800}
                                                                                      {:title  "Dez"
                                                                                       :amount 1000}]}))
