(ns expenses.controller.purchases
  (:require [expenses.db.purchases :as db.purchases]))

(defn create-purchase
  [purchase db]
  (db.purchases/create-purchase purchase db)
  {:message "Purchase created"})

(defn create-purchases-list
  [purchases-list db]
  (db.purchases/create-purchases-list purchases-list db)
  {:message "Processo finalizou"})