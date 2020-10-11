(ns expenses.db.revenue
  (:require [monger.collection :as mc]
            [expenses.logic.db-helper :as db-helper]))

(def revenue-collection "revenue")

(defn create-revenue [revenue db]
  (->> revenue
       db-helper/add-id-and-created-at
       (mc/insert-and-return db revenue-collection)))

(defn search-revenue-with [search-parameters db]
  (mc/find-one-as-map db revenue-collection search-parameters))

(defn update-revenue [id revenue db]
  (mc/update-by-id db revenue-collection id revenue))