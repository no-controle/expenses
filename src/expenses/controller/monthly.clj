(ns expenses.controller.monthly
  (:require [expenses.controller.revenue :as controller.revenue]
            [expenses.controller.fixed :as controller.fixed]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.logic.common-helper :as common-helper]))

(defn to-response-item [item]
  {:title  (:title item)
   :amount (common-helper/round (:amount item) :precision 2)})

(defn to-response [items]
  (map
    (fn [[grp-key values]]
      {:title  grp-key
       :amount (-> (reduce + (map :amount values))
                   (common-helper/round :precision 2))})
    (group-by :category items)))

(defn data-for-period [year month db]
  (let [revenue (controller.revenue/total-revenue-for-period year month db)
        fixed (controller.fixed/active-fixed-expenses db)
        variable (controller.purchases/variable-for-period year month db)
        other (controller.purchases/other-for-period year month db)
        total-expense (->> (concat fixed variable other)
                           (map :amount)
                           (reduce +))]
    {:income   (common-helper/round (:revenue revenue) :precision 2)
     :expenses (common-helper/round total-expense :precision 2)
     :balance  (-> (:revenue revenue) (- total-expense) (common-helper/round :precision 2))
     :fixed    (map to-response-item fixed)
     :variable (to-response variable)
     :extra    (to-response other)}))
