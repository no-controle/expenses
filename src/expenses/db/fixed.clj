(ns expenses.db.fixed
  (:require [monger.collection :as mc]
            [expenses.logic.db-helper :as db-helper]))

(def fixed-collection "fixed")

(defn create-expense [fixed-expense db]
  (->> fixed-expense
       db-helper/add-id-and-created-at
       db-helper/add-updated-at
       (mc/insert-and-return db fixed-collection)))

(defn update-expense [id value db]
  (->> value
       db-helper/add-updated-at
       (mc/update-by-id db fixed-collection id)))

(defn search-expense-with [search-parameters db]
  (mc/find-maps db fixed-collection search-parameters))