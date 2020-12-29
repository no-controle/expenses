(ns expenses.logic.db-helper
  (:require [expenses.logic.date-helper :as date-helper]
            [monger.util :as mu]
            [monger.operators :refer :all]))

(defn generate-uuid [] (mu/random-uuid))

(defn add-id-and-created-at [expense] (-> expense
                                          (assoc :_id (generate-uuid))
                                          (assoc :created-at (date-helper/current-date))))

(defn add-updated-at [expense] (-> expense
                                   (assoc :updated-at (date-helper/current-date))))

(defn format-search-parameters [parameters]
  (-> parameters
      (cond-> (not (nil? (:period parameters))) (assoc :updated-at {$regex (str (:period parameters) ".*")})
              (false? (:recurrent parameters)) (assoc :recurrent {$ne true})
              (false? (:active parameters)) (assoc :active {$ne true}))
      (dissoc :period)))