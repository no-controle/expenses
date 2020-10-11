(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]))

(defn create-purchase
  [purchase db]
  (db.purchases/create-purchase purchase db))

(defn create-purchases-list
  [purchases-list db]
  (db.purchases/create-purchases-list purchases-list db)
  {:message "Processo finalizou"})

(defn refund-purchase [id db]
  (let [purchase (db.purchases/search-purchase-with {:_id id} db)]
    (when (not-empty purchase) (db.purchases/update-purchase id (assoc purchase :refunded true) db))
    {:message "Purchase deleted"}))

(defn get-by-period
  [year month db]
  (db.purchases/get-by-period (str year "-" month) db))

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
