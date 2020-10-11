(ns expenses.controller.revenue
  (:require [expenses.db.revenue :as db.revenue]))

(defn create-revenue [revenue db]
  (db.revenue/create-revenue (assoc revenue :active true) db))