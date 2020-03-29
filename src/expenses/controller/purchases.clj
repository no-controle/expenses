(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]))

(defn get-by-period
  [year month db]
  (db.purchases/get-by-period (str year "-" month) db))

(defn create-purchase
  [purchase db]
  (db.purchases/create-purchase purchase db)
  {:message "Purchase created"})

(defn sum-amount
  [purchases-list]
  (->> purchases-list
       (map :amount)
       (reduce +)))

(defn summary-input
  [category-and-list input-type]
  {input-type (first category-and-list)
   :sum       (->> category-and-list second sum-amount)
   :count     (->> category-and-list second count)
   :purchases (->> category-and-list second)})

(defn get-summary
  [params db]
  (let [group-type (-> params :by keyword)
        purchases (get-by-period (:year params) (:month params) db)]
    (->> purchases
         (group-by group-type)
         seq
         (map #(summary-input % group-type))
         (sort-by :sum)
         reverse)))

(defn create-purchases-list
  [purchases-list db]
  (db.purchases/create-purchases-list purchases-list db)
  {:message "Processo finalizou"})
