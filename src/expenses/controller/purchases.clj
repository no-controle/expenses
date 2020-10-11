(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]
            [clojure.data.csv :as csv])
  (:import (javax.management InstanceAlreadyExistsException)))

(defn purchase-search-params [purchase]
  {:title  (:title purchase)
   :amount (:amount purchase)
   :source (:source purchase)})

(defn create-purchase
  [purchase db]
  (let [existent-purchase (db.purchases/search-purchase-with (purchase-search-params purchase) db)]
    (if (empty? existent-purchase)
      (db.purchases/create-purchase purchase db)
      (throw (InstanceAlreadyExistsException. (str "Purchase id: " (:_id existent-purchase)))))))

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

; Process CSV to map list
; Save each item
  ; Check if exists
  ; Save
