(ns expenses.logic.db-helper
  (:require [monger.util :as mu]
            [monger.operators :refer :all]
            [clj-time.local :as time-local]
            [clj-time.format :as time-format]))

(defn generate-uuid [] (mu/random-uuid))

(defn current-date []
  (let [now (time-local/local-now)]
    (-> (time-format/formatter "yyyy-MM-dd")
        (time-format/unparse now))))

(defn add-id-and-created-at [expense] (-> expense
                                          (assoc :_id (generate-uuid))
                                          (assoc :created-at (current-date))))

(defn add-updated-at [expense] (-> expense
                                   (assoc :updated-at (current-date))))

(defn format-search-parameters [parameters]
  (-> parameters
      (cond-> (not (nil? (:period parameters))) (assoc :updated-at {$regex (str (:period parameters) ".*")})
              (false? (:recurrent parameters)) (assoc :recurrent  {$ne true})
              (false? (:active parameters)) (assoc :active  {$ne true}))
      (dissoc :period)))