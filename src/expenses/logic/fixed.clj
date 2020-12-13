(ns expenses.logic.fixed
  (:require [expenses.logic.date-helper :as date-helper]))

(defn total-amount-created-before-for [year month input]
  (reduce (fn [sum value]
            (if (-> (date-helper/parse (:updated-at value))
                    (date-helper/before-or-equal? (date-helper/last-day-of-the-month year month)))
              (+ sum (:amount value))
              sum))
          0
          input))

(defn total-amount-created-after-for [year month input]
  (reduce (fn [sum value]
            (if (-> (date-helper/parse (:updated-at value))
                    (date-helper/after-or-equal? (date-helper/first-day-of-the-month year month)))
              (+ sum (:amount value))
              sum))
          0
          input))

(defn total-amount-created-on-for [year month input]
  (reduce (fn [sum value]
            (if (and
                  (-> (date-helper/parse (:updated-at value))
                      (date-helper/before-or-equal? (date-helper/last-day-of-the-month year month)))
                  (-> (date-helper/parse (:updated-at value))
                      (date-helper/after-or-equal? (date-helper/first-day-of-the-month year month))))
              (+ sum (:amount value))
              sum))
          0
          input))

(defn data-for-month-year [month year active-fixed-expenses inactive-fixed-expenses]
  {:title  (:month month)
   :amount (+ (total-amount-created-before-for year (:month-value month) active-fixed-expenses)
              (total-amount-created-after-for year (:month-value month) inactive-fixed-expenses))})