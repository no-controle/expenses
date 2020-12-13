(ns expenses.controller.general
  (:require [expenses.controller.revenue :as controller.revenue]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.purchases :as controller.purchases]))

(defn data-for-period [year db]
  (let [income-for-year (controller.revenue/revenue-for-year year db)
        fixed-expenses-for-year (controller.fixed/fixed-expenses-for-year year db)
        variable-purchases-for-year (controller.purchases/variable-purchases-for-year year db)
        extra-purchases-for-year (controller.purchases/extra-purchases-for-year year db)]
    {:income   income-for-year
     :expenses []
     :fixed    fixed-expenses-for-year
     :variable variable-purchases-for-year
     :extra    extra-purchases-for-year}))