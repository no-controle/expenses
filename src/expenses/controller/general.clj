(ns expenses.controller.general
  (:require [expenses.controller.revenue :as controller.revenue]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.purchases :as controller.purchases]))

(defn amount-for-month
  [month year-values]
  (->> year-values
       (filter #(= month (:title %)))
       first
       :amount))

(defn data-for-period [year db]
  (let [income-for-year (controller.revenue/revenue-for-year year db)
        fixed-expenses-for-year (controller.fixed/fixed-expenses-for-year year db)
        variable-purchases-for-year (controller.purchases/variable-purchases-for-year year db)
        extra-purchases-for-year (controller.purchases/extra-purchases-for-year year db)]
    {:income   income-for-year
     :expenses (->> fixed-expenses-for-year
                    (map #(assoc % :amount (+ (:amount %)
                                              (amount-for-month (:title %) variable-purchases-for-year)
                                              (amount-for-month (:title %) extra-purchases-for-year)))))
     :fixed    fixed-expenses-for-year
     :variable variable-purchases-for-year
     :extra    extra-purchases-for-year}))
