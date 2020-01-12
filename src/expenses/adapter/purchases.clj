(ns expenses.adapter.purchases)

(defn not-null-or-throw [document field]
  (if (nil? (field document))
    (throw (throw (IllegalArgumentException. (str "error found for this field: " field))))
    (field document)))

(defn http-in->purchase
  [purchase]
  {:title     (not-null-or-throw purchase :title)
   :date      (not-null-or-throw purchase :date)
   :amount    (not-null-or-throw purchase :amount)
   :category  (not-null-or-throw purchase :category)
   :source    (not-null-or-throw purchase :source)
   :bill-date (not-null-or-throw purchase :bill-date)})

(defn http-in->purchases-list
  [purchases-list]
  (map http-in->purchase purchases-list))