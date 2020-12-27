(ns expenses.logic.purchases
  (:require [expenses.logic.common-helper :as common-helper]))

(defn total-amount-for [month purchase]
  (reduce (fn [sum value]
            (if (-> value :bill-month Integer/parseInt (= month))
              (+ sum (:amount value))
              sum))
          0
          purchase))

(defn data-for-month [month purchases]
  {:title  (:month month)
   :amount (-> (total-amount-for (:month-value month) purchases)
               (common-helper/round :precision 2))})