(ns expenses.db.purchases
  (:require [monger.collection :as mc]
            [io.pedestal.log :as log]
            [expenses.logic.db-helper :as db-helper]))

(def purchases-collection "purchases")

(defn create-id [title date amount]
  (str (hash [title date amount])))

(defn add-id-to-purchase
  [purchase]
  (assoc purchase :_id (create-id (:title purchase)
                                  (:date purchase)
                                  (:amount purchase))))

(defn create-purchase
  [purchase db]
  (->> purchase
       db-helper/add-id-and-created-at
       (mc/insert-and-return db purchases-collection)))

(defn create-purchases-list
  [purchases-list db]
  (let [purchases-with-id (->> purchases-list
                               (map add-id-to-purchase))]
    (doseq [purchase purchases-with-id]
      (try
        (create-purchase purchase db)
        (catch Exception ex
          (log/error :exception ex))))))

(defn get-by-period
  [period db]
  (mc/find-maps db purchases-collection {:bill-date period}))

(defn search-purchase-with [search-parameters db]
  (mc/find-one-as-map db purchases-collection search-parameters))

(defn update-purchase [id value db]
  (mc/update-by-id db purchases-collection id value))

(defn search-purchase-in-category-with [category-list search-parameters db]
  (mc/find-maps db purchases-collection (assoc search-parameters :category {:in category-list})))

(defn search-purchase-not-in-category-with [category-list search-parameters db]
  (mc/find-maps db purchases-collection (assoc search-parameters :category {:nin category-list})))
