(ns expenses.controller.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.db.purchases :as db.purchases]))

(def variable-categories ["Supermercado", "Supermarket" "Transporte", "Transportation", "Saúde", "Saude", "Health"])

(facts "create a new purchase"
  (fact "should return purchase created"
    (controller.purchases/create-purchase {:title      "Target"
                                           :amount     240
                                           :source     "Credit Card"
                                           :category   "Supermarket"
                                           :date       "2020-10-20"
                                           :bill-month "11"
                                           :bill-year  "2020"} ..db..) => ..mongo-success-result..
    (provided
      (db.purchases/search-purchase-with {:title  "Target"
                                          :amount 240
                                          :date   "2020-10-20"
                                          :source "Credit Card"} ..db..) => nil
      (db.purchases/create-purchase {:title      "Target"
                                     :amount     240
                                     :source     "Credit Card"
                                     :category   "Supermarket"
                                     :date       "2020-10-20"
                                     :bill-month "11"
                                     :bill-year  "2020"} ..db..) => ..mongo-success-result..))

  (fact "when already exists one should return error"
    (controller.purchases/create-purchase {:title      "Target"
                                           :amount     240
                                           :source     "Credit Card"
                                           :category   "Supermarket"
                                           :date       "2020-10-20"
                                           :bill-month "11"
                                           :bill-year  "2020"} ..db..) => (throws Exception)
    (provided
      (db.purchases/search-purchase-with {:title  "Target"
                                          :amount 240
                                          :date   "2020-10-20"
                                          :source "Credit Card"} ..db..) => ..existent-purchase..)))

(facts "Variable purchases for given year and month"
  (fact "should return list of variable purchases"
    (controller.purchases/variable-for-period 2020 10 ..db..) => [{:title    "variable-01"
                                                                   :amount   100
                                                                   :category "Supermercado"}
                                                                  {:title    "variable-02"
                                                                   :amount   100
                                                                   :category "Transporte"}
                                                                  {:title    "variable-03"
                                                                   :amount   200
                                                                   :category "Saude"}]
    (provided
      (db.purchases/search-purchase-in-category-with variable-categories
                                                     {:bill-month 10 :bill-year 2020}
                                                     ..db..) => [{:title    "variable-01"
                                                                  :amount   100
                                                                  :category "Supermercado"}
                                                                 {:title    "variable-02"
                                                                  :amount   100
                                                                  :category "Transporte"}
                                                                 {:title    "variable-03"
                                                                  :amount   200
                                                                  :category "Saude"}]))

  (fact "should return list of variable purchases"
    (controller.purchases/other-for-period 2020 10 ..db..) => [{:title    "other-01"
                                                                :amount   100
                                                                :category "Entretenimento"}
                                                               {:title    "other-02"
                                                                :amount   100
                                                                :category "Servicos"}
                                                               {:title    "other-03"
                                                                :amount   200
                                                                :category "Lazer"}]
    (provided
      (db.purchases/search-purchase-not-in-category-with variable-categories
                                                         {:bill-month 10 :bill-year 2020}
                                                         ..db..) => [{:title    "other-01"
                                                                      :amount   100
                                                                      :category "Entretenimento"}
                                                                     {:title    "other-02"
                                                                      :amount   100
                                                                      :category "Servicos"}
                                                                     {:title    "other-03"
                                                                      :amount   200
                                                                      :category "Lazer"}])))

(facts "Getting purchases for every month on given year"
  (fact "Should return variable purchases for every month"
    (controller.purchases/variable-purchases-for-year "2020" ..db..) => [{:title  "Jan"
                                                                          :amount 0}
                                                                         {:title  "Fev"
                                                                          :amount 0}
                                                                         {:title  "Mar"
                                                                          :amount 350}
                                                                         {:title  "Abr"
                                                                          :amount 14}
                                                                         {:title  "Mai"
                                                                          :amount 80}
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
                                                                          :amount 0}]
    (provided
      (db.purchases/search-purchase-in-category-with variable-categories {:bill-year "2020" :refunded false} ..db..) => [{:title      "Grocery Store"
                                                                                                                          :date       "2020-12-05"
                                                                                                                          :amount     50
                                                                                                                          :bill-year  "2020"
                                                                                                                          :bill-month "03"
                                                                                                                          :category   "Supermarket"}
                                                                                                                         {:title      "The Fun Fun Fun"
                                                                                                                          :date       "2020-12-09"
                                                                                                                          :amount     300
                                                                                                                          :bill-year  "2020"
                                                                                                                          :bill-month "03"
                                                                                                                          :category   "Entertainment"}
                                                                                                                         {:title      "The med place"
                                                                                                                          :date       "2020-12-15"
                                                                                                                          :amount     14
                                                                                                                          :bill-year  "2020"
                                                                                                                          :bill-month "04"
                                                                                                                          :category   "Health"}
                                                                                                                         {:title      "Grocery Store"
                                                                                                                          :date       "2020-12-08"
                                                                                                                          :amount     80
                                                                                                                          :bill-year  "2020"
                                                                                                                          :bill-month "05"
                                                                                                                          :category   "Supermarket"}]))

  (fact "Should return extra purchases for every month"
    (controller.purchases/extra-purchases-for-year "2020" ..db..) => [{:title  "Jan"
                                                                       :amount 0}
                                                                      {:title  "Fev"
                                                                       :amount 0}
                                                                      {:title  "Mar"
                                                                       :amount 350}
                                                                      {:title  "Abr"
                                                                       :amount 14}
                                                                      {:title  "Mai"
                                                                       :amount 80}
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
                                                                       :amount 0}]
    (provided
      (db.purchases/search-purchase-not-in-category-with variable-categories {:bill-year "2020" :refunded false} ..db..) => [{:title      "Grocery Store"
                                                                                                                              :date       "2020-12-05"
                                                                                                                              :amount     50
                                                                                                                              :bill-year  "2020"
                                                                                                                              :bill-month "03"
                                                                                                                              :category   "Restaurant"}
                                                                                                                             {:title      "The Fun Fun Fun"
                                                                                                                              :date       "2020-12-09"
                                                                                                                              :amount     300
                                                                                                                              :bill-year  "2020"
                                                                                                                              :bill-month "03"
                                                                                                                              :category   "Electronic"}
                                                                                                                             {:title      "The med place"
                                                                                                                              :date       "2020-12-15"
                                                                                                                              :amount     14
                                                                                                                              :bill-year  "2020"
                                                                                                                              :bill-month "04"
                                                                                                                              :category   "Restaurant"}
                                                                                                                             {:title      "Grocery Store"
                                                                                                                              :date       "2020-12-08"
                                                                                                                              :amount     80
                                                                                                                              :bill-year  "2020"
                                                                                                                              :bill-month "05"
                                                                                                                              :category   "Restaurant"}])))

(facts "create a purchases list from csv"
  (fact "should add refunded true when there is negative items on the list"
    (controller.purchases/create-purchases-list [{:amount -10 :category "Supermercado"}
                                                 {:amount -15 :category "Saude"}
                                                 {:amount 20 :category "Supermercado"}] ..db..) => {:message "Processo finalizou"}
    (provided
      (db.purchases/create-purchases-list [{:amount 10 :refunded true :category "Supermercado"}
                                           {:amount 15 :refunded true :category "Saude"}
                                           {:amount 20 :category "Supermercado"}] ..db..) => ..mongo-success-result..))
  (fact "should return purchases created"
    (controller.purchases/create-purchases-list [{:amount -10 :category "Supermercado"}
                                                 {:amount -15 :category "Saude"}
                                                 {:amount 20 :category "Supermercado"}] ..db..) => {:message "Processo finalizou"}
    (provided
      (db.purchases/create-purchases-list [{:amount 10 :refunded true :category "Supermercado"}
                                           {:amount 15 :refunded true :category "Saude"}
                                           {:amount 20 :category "Supermercado"}] ..db..) => ..mongo-success-result..))

  (fact "should throw"
    (controller.purchases/create-purchases-list [{:amount -10 :category "Supermercado"}
                                                 {:amount -15 :category "Saude"}
                                                 {:amount 20 :category "Supermercado"}] ..db..) => (throws Exception)
    (provided
      (db.purchases/create-purchases-list [{:amount 10 :refunded true :category "Supermercado"}
                                           {:amount 15 :refunded true :category "Saude"}
                                           {:amount 20 :category "Supermercado"}] ..db..) => (throw (Exception.)))))