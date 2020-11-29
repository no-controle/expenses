(ns expenses.db.revenue
  (:require [monger.collection :as mc]
            [monger.operators :refer :all]
            [io.pedestal.log :as log]
            [expenses.logic.db-helper :as db-helper]))

(def revenue-collection "revenue")

(defn create-revenue [revenue db]
  (log/info :msg (str "Creating revenue: " revenue))
  (->> revenue
       db-helper/add-id-and-created-at
       (mc/insert-and-return db revenue-collection)))

(defn search-revenue-with [search-parameters db]
  (log/info :msg (str "Searching revenue with parameters: " search-parameters))
  (mc/find-maps db revenue-collection search-parameters))

(defn search-revenue-for-period [year month db]
  (log/info :msg (str "Searching revenue for period: " year "-" month))
  (mc/find-maps db revenue-collection {:created-at {$regex (str year "-" month ".*")}
                                       :recurrent  {$ne true}}))

(defn update-revenue [id revenue db]
  (log/info :msg (str "Updating revenue with id " id " with: " revenue))
  (mc/update-by-id db revenue-collection id revenue))