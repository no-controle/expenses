(ns expenses.logic.revenue
  (:require [expenses.logic.common-helper :as common-helper]))

(defn data-for-month-year [month year recurrent-active recurrent-inactive other-revenue]
  {:title  (:month month)
   :amount (+ (common-helper/total-amount-created-before-for year (:month-value month) recurrent-active)
              (common-helper/total-amount-created-after-for year (:month-value month) recurrent-inactive)
              (common-helper/total-amount-created-on-for year (:month-value month) other-revenue))})