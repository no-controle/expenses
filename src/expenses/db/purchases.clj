(ns expenses.db.purchases
  (:require [monger.collection :as mc]
            [io.pedestal.log :as log]))

(def purchases-collection "purchases")

(defn create-id [title date amount]
  (str (hash [title date amount])))

(defn add-id-to-purchase
  [purchase]
  (assoc purchase :_id (create-id (:title purchase)
                                  (:date purchase)
                                  (:amount purchase))))

(defn create-purchase
  [purchase db]
  (->> purchase
       add-id-to-purchase
       (mc/insert-and-return db purchases-collection)))

(defn create-purchases-list
  [purchases-list db]
  (let [purchases-with-id (->> purchases-list
                               (map add-id-to-purchase))]
    (doseq [purchase purchases-with-id]
      (try
        (create-purchase purchase db)
        (catch Exception ex
          (log/error :exception ex))))))

(defn get-by-period
  [period db]
  (mc/find-maps db purchases-collection {:bill-date period}))
