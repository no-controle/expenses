(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]))

(defn get-by-period
  [period-params db]
  (db.purchases/get-by-period (str (:year period-params) "-" (:month period-params)) db))

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

(defn get-summary-by-period
  [period-params db]
  (let [purchases (get-by-period period-params db)]
    (->> purchases
         (group-by :category)
         seq
         (map #(summary-input % :category))
         (sort-by :sum)
         reverse)))

(defn get-summary-by-title
  [period-params db]
  (let [purchases (get-by-period period-params db)]
    (->> purchases
         (group-by :title)
         seq
         (map #(summary-input % :title))
         (sort-by :sum)
         reverse)))

(defn create-purchases-list
  [purchases-list db]
  (db.purchases/create-purchases-list purchases-list db)
  {:message "Processo finalizou"})
