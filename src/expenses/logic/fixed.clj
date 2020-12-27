(ns expenses.logic.fixed
  (:require [expenses.logic.common-helper :as common-helper]))

(defn data-for-month-year [month year active-fixed-expenses inactive-fixed-expenses]
  {:title  (:month month)
   :amount (-> (+ (common-helper/total-amount-created-before-for year (:month-value month) active-fixed-expenses)
                  (common-helper/total-amount-created-after-for year (:month-value month) inactive-fixed-expenses))
               (common-helper/round :precision 2))})