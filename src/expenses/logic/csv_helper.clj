(ns expenses.logic.csv-helper
  (:require [clojure.data.csv :as csv]))

(defn csv-data->maps [csv-data]
  (map zipmap
       (->> (first csv-data)
            (map keyword)
            repeat)
       (rest csv-data)))

(defn parse-to-int [values]
  (map #(assoc % :amount (-> % :amount Double/parseDouble)) values))

(defn parse-csv [csv-value]
  (->> csv-value
       csv/read-csv
       csv-data->maps
       parse-to-int
       (into [])))