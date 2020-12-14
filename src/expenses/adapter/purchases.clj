(ns expenses.adapter.purchases
  (:require [expenses.logic.csv-helper :as csv-helper]))

(defn not-null-or-throw [document field]
  (if (nil? (field document))
    (throw (throw (IllegalArgumentException. (str "error found for this field: " field))))
    (field document)))

(defn http-in->purchase
  [purchase]
  {:title      (not-null-or-throw purchase :title)
   :date       (not-null-or-throw purchase :date)
   :amount     (not-null-or-throw purchase :amount)
   :category   (not-null-or-throw purchase :category)
   :source     (not-null-or-throw purchase :source)
   :bill-month (not-null-or-throw purchase :bill-month)
   :bill-year  (not-null-or-throw purchase :bill-year)})

(defn http-in->purchases-list
  [csv]
  (-> csv
      :csv
      csv-helper/parse-csv))