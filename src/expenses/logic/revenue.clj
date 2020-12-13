(ns expenses.logic.revenue
  (:require [expenses.logic.date-helper :as date-helper]))

(defn total-amount-created-before-for [year month revenues]
  (reduce (fn [sum value]
            (if (-> (date-helper/parse (:updated-at value))
                    (date-helper/before-or-equal? (date-helper/last-day-of-the-month year month)))
              (+ sum (:amount value))
              sum))
          0
          revenues))

(defn total-amount-created-after-for [year month revenues]
  (reduce (fn [sum value]
            (if (-> (date-helper/parse (:updated-at value))
                    (date-helper/after-or-equal? (date-helper/first-day-of-the-month year month)))
              (+ sum (:amount value))
              sum))
          0
          revenues))

(defn total-amount-created-on-for [year month revenues]
  (reduce (fn [sum value]
            (if (and
                  (-> (date-helper/parse (:updated-at value))
                      (date-helper/before-or-equal? (date-helper/last-day-of-the-month year month)))
                  (-> (date-helper/parse (:updated-at value))
                      (date-helper/after-or-equal? (date-helper/first-day-of-the-month year month))))
              (+ sum (:amount value))
              sum))
          0
          revenues))

(defn data-for-month-year [month year recurrent-active recurrent-inactive other-revenue]
  {:title (:month month)
   :value (+ (total-amount-created-before-for year (:month-value month) recurrent-active)
             (total-amount-created-after-for year (:month-value month) recurrent-inactive)
             (total-amount-created-on-for year (:month-value month) other-revenue))})