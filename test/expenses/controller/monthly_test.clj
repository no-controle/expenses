(ns expenses.controller.monthly-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.monthly :as controller.monthly]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.revenue :as controller.revenue]))

(facts "Monthly data for period"
  (fact "Should return correct data with all attributes"
    (controller.monthly/data-for-period 2020 10 ..db..) => {:income   1000
                                                            :expense  800
                                                            :balance  200
                                                            :fixed    [{:title  "fixed-01"
                                                                        :amount 200}
                                                                       {:title  "fixed-02"
                                                                        :amount 100}]
                                                            :variable [{:title  "variable-01"
                                                                        :amount 100}
                                                                       {:title  "variable-02"
                                                                        :amount 100}
                                                                       {:title  "variable-03"
                                                                        :amount 200}]
                                                            :other    [{:title  "other"
                                                                        :amount 100}]}
      (provided
        (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 1000}
        (controller.purchases/variable-for-period 2020 10 ..db..) => [{:title    "variable-01"
                                                                       :amount   100
                                                                       :category "Supermercado"}
                                                                      {:title    "variable-02"
                                                                       :amount   100
                                                                       :category "Saude"}
                                                                      {:title    "variable-03"
                                                                       :amount   200
                                                                       :category "Pets"}]
        (controller.purchases/other-for-period 2020 10 ..db..) => [{:title    "other"
                                                                    :amount   100
                                                                    :category "Reparos"}]
        (controller.fixed/active-fixed-expenses ..db..) => [{:title  "fixed-01"
                                                             :amount 200
                                                             :source "cash"}
                                                            {:title  "fixed-02"
                                                             :amount 100
                                                             :source "cash"}])))

