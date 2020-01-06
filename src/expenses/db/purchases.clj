(ns expenses.db.purchases
  (:require [monger.collection :as mc]))

(def ^:private purchase-collection "purchases")

(defn ^:private create-id [title date amount]
  (str (hash [title date amount])))

(defn create-purchase
  [purchase db]
  (mc/insert db purchase-collection (assoc purchase :_id (create-id (:title purchase)
                                                                    (:date purchase)
                                                                    (:amount purchase)))))