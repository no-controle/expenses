(ns expenses.logic.common-helper
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