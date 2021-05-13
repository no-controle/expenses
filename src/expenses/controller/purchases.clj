(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]
            [expenses.logic.date-helper :refer [months]]
            [expenses.logic.purchases :as logic.purchases])
  (:import (javax.management InstanceAlreadyExistsException)))

(def variable-categories ["Supermercado", "Supermarket" "Transporte", "Transportation", "Saúde", "Saude", "Health"])

(defn build-based-on-similar [new-purchase similar-purchases]
  (let [category (if-not (empty? similar-purchases) (-> similar-purchases first :category) (:category new-purchase))]
    (assoc new-purchase :category category)))

(defn create-purchase [purchase db]
  (clojure.pprint/pprint "controller.create-purchase")
  (clojure.pprint/pprint "purchase")
  (clojure.pprint/pprint purchase)
  (let [similar-purchases (db.purchases/search-similar-purchases-for (:title purchase) db)
        existent-purchase (logic.purchases/existent-purchase-for purchase similar-purchases)]
    (if (empty? existent-purchase)
      (db.purchases/create-purchase (build-based-on-similar purchase similar-purchases) db)
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

(defn map-refunded [purchase]
  (if (neg? (:amount purchase))
    (assoc purchase
      :refunded true
      :amount (* -1 (:amount purchase)))
    purchase))

(defn create-purchases-list
  [purchases-list db]
  (clojure.pprint/pprint "controller.create-purchases-list")
  (clojure.pprint/pprint "purchases list")
  (clojure.pprint/pprint purchases-list)
  (doseq [purchase purchases-list]
    (-> purchase
        map-refunded
        (create-purchase db)))
  {:message "Processo finalizou"})
