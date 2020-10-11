(ns expenses.db.revenue
  (:require [monger.collection :as mc]
            [expenses.logic.db-helper :as db-helper]))

(def fixed-collection "revenue")

(defn create-revenue [revenue db]
  (->> revenue
       db-helper/add-id-and-created-at
       (mc/insert-and-return db fixed-collection)))