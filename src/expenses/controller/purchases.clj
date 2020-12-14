(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]
            [expenses.logic.csv-helper :as csv-helper]
            [expenses.logic.date-helper :refer [months]]
            [expenses.logic.purchases :as logic.purchases])
  (:import (javax.management InstanceAlreadyExistsException)))

(def variable-categories ["Supermercado", "Transporte", "Saude"])

(defn purchase-search-params [purchase]
  {:title  (:title purchase)
   :amount (:amount purchase)
   :date   (:date purchase)
   :source (:source purchase)})

(defn create-purchase
  [purchase db]
  (let [existent-purchase (db.purchases/search-purchase-with (purchase-search-params purchase) db)]
    (if (empty? existent-purchase)
      (db.purchases/create-purchase purchase db)
      (throw (InstanceAlreadyExistsException. (str "Purchase id: " (:_id existent-purchase)))))))

(defn refund-purchase [id db]
  (let [purchase (db.purchases/search-purchase-with {:_id id} db)]
    (when (not-empty purchase) (db.purchases/update-purchase id (assoc purchase :refunded true) db))
    {:message "Purchase deleted"}))

(defn variable-for-period [year month db]
  (db.purchases/search-purchase-in-category-with variable-categories {:bill-month month :bill-year year} db))

(defn other-for-period [year month db]
  (db.purchases/search-purchase-not-in-category-with variable-categories {:bill-month month :bill-year year} db))

(defn variable-purchases-for-year [year db]
  (let [purchases (db.purchases/search-purchase-in-category-with variable-categories {:bill-year year :refunded false} db)]
    (map #(logic.purchases/data-for-month % purchases) months)))

(defn extra-purchases-for-year [year db]
  (let [purchases (db.purchases/search-purchase-not-in-category-with variable-categories {:bill-year year :refunded false} db)]
    (map #(logic.purchases/data-for-month % purchases) months)))

(defn create-purchases-from-csv
  [csv-purchases db]
  (let [purchases-list (csv-helper/parse-csv csv-purchases)]
    (db.purchases/create-purchases-list purchases-list db)
    {:message "Processo finalizou"}))
