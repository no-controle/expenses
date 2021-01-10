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

(defn total-amount [purchases]
  (-> (->> purchases
           (map :amount)
           (reduce +))
      (common-helper/round :precision 2)))

(defn data-for-period [year month db]
  (let [month-without-leading-zero (-> month (Integer/parseInt) str)
        revenue (controller.revenue/total-revenue-for-period year month-without-leading-zero db)
        fixed (controller.fixed/active-fixed-expenses db)
        variable (controller.purchases/variable-for-period year month-without-leading-zero db)
        other (controller.purchases/other-for-period year month-without-leading-zero db)
        total-expense (->> (concat fixed variable other)
                           total-amount)]
    {:income               (common-helper/round (:revenue revenue) :precision 2)
     :expenses             total-expense
     :balance              (-> (:revenue revenue) (- total-expense) (common-helper/round :precision 2))
     :fixed                (total-amount fixed)
     :fixed-by-category    (map to-response-item fixed)
     :variable             (total-amount variable)
     :variable-by-category (to-response variable)
     :extra                (total-amount other)
     :extra-by-category    (to-response other)}))
