(ns expenses.db.fixed
  (:require [monger.collection :as mc]
            [monger.util :as mu]
            [io.pedestal.log :as log]
            [clj-time.local :as time-local]
            [clj-time.format :as time-format]))

(def fixed-collection "fixed")

(defn generate-uuid [] (mu/random-uuid))

(defn current-date []
  (let [now (time-local/local-now)]
    (-> (time-format/formatter "yyyy-MM-dd")
        (time-format/unparse now))))

(defn add-id-and-created-at [expense] (-> expense
                                          (assoc :_id (generate-uuid))
                                          (assoc :created-at (current-date))))

(defn create-expense [fixed-expense db]
  (->> fixed-expense
       add-id-and-created-at
       (mc/insert-and-return db fixed-collection)))

(defn search-expense-with [search-parameters db]
  (mc/find-one-as-map db fixed-collection search-parameters))

(defn update-expense [id value db]
  (mc/update-by-id db fixed-collection id value))
