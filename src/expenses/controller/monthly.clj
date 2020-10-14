(ns expenses.controller.monthly
  (:require [expenses.controller.revenue :as controller.revenue]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.purchases :as controller.purchases]))

(defn to-response [item]
  {:title  (:title item)
   :amount (:amount item)})

(defn data-for-period [year month db]
  (let [revenue (controller.revenue/total-revenue-for-period year month db)
        fixed (controller.fixed/active-fixed-expenses db)
        variable (controller.purchases/variable-for-period year month db)
        other (controller.purchases/other-for-period year month db)
        total-expense (->> (concat fixed variable other)
                           (map :amount)
                           (reduce +))]
    {:income   (:revenue revenue)
     :expense  total-expense
     :balance  (-> (:revenue revenue) (- total-expense))
     :fixed    (map to-response fixed)
     :variable (map to-response variable)
     :other    (map to-response other)}))