(ns expenses.logic.db-helper
  (:require [monger.util :as mu]
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