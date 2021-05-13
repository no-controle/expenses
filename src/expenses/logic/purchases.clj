(ns expenses.logic.purchases
  (:require [expenses.logic.common-helper :as common-helper]))

(defn total-amount-for [month purchase]
  (reduce (fn [sum value]
            (if (-> value :bill-month Integer/parseInt (= month))
              (+ sum (:amount value))
              sum))
          0
          purchase))

(defn data-for-month [month purchases]
  {:title  (:month month)
   :amount (-> (total-amount-for (:month-value month) purchases)
               (common-helper/round :precision 2))})

(defn purchases-equal? [first-purchase second-purchase]
  (and (= (:title first-purchase) (:title second-purchase))
       (= (:amount first-purchase) (:amount second-purchase))
       (= (:date first-purchase) (:date second-purchase))
       (= (:source first-purchase) (:source second-purchase))))

(defn existent-purchase-for [params purchases]
  (clojure.pprint/pprint "Params")
  (clojure.pprint/pprint params)
  (clojure.pprint/pprint "Puchases")
  (clojure.pprint/pprint purchases)
  (some #(purchases-equal? % params) purchases))