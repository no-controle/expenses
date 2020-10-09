(ns expenses.db.fixed
  (:require [monger.collection :as mc]
            [io.pedestal.log :as log])
  (:import (java.util UUID)))

(def fixed-collection "fixed")

(defn generate-uuid [] (UUID/randomUUID))

(defn current-date []
  (let [now (clj-time.local/local-now)]
    (-> (clj-time.format/formatter "yyyy-MM-dd")
        (clj-time.format/unparse now))))

(defn add-id-and-created-at [expense] (-> expense
                                          (assoc :_id (generate-uuid))
                                          (assoc :created-at (current-date))))

(defn create-expense [fixed-expense db]
  (->> fixed-expense
       add-id-and-created-at
       (mc/insert-and-return db fixed-collection)))

(defn search-expense-with [search-parameters db]
  (mc/find-maps db fixed-collection search-parameters))
