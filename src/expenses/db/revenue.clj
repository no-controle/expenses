(ns expenses.db.revenue
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]
            [expenses.logic.db-helper :as db-helper]))

(def revenue-collection "revenue")

(defn create-revenue [revenue db]
  (->> revenue
       db-helper/add-id-and-created-at
       (mc/insert-and-return db revenue-collection)))

(defn search-revenue-with [search-parameters db]
  (mc/find-maps db revenue-collection search-parameters))

(defn search-revenue-for-period [year month db]
  (mc/find-maps db revenue-collection {:created-at {$regex (str year "-" month ".*")}
                                       :recurrent  {$ne true}}))

(defn update-revenue [id revenue db]
  (mc/update-by-id db revenue-collection id revenue))