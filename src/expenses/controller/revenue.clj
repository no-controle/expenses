(ns expenses.controller.revenue
  (:require [expenses.db.revenue :as db.revenue]))

(defn create-revenue [revenue db]
  (db.revenue/create-revenue (assoc revenue :active true) db))

(defn delete-revenue [id db]
  (let [revenue (db.revenue/search-revenue-with {:_id id} db)]
    (when (not-empty revenue) (db.revenue/update-revenue id (assoc revenue :active false) db))
    {:message "Revenue deleted"}))

(defn total-revenue-for-period [year month db]
  (let [incomes-on-period (db.revenue/search-revenue-for-period year month db)
        recurrent-income (db.revenue/search-revenue-with {:recurrent true
                                                          :active    true} db)]
    (->> (concat incomes-on-period recurrent-income)
         (map :amount)
         (reduce +)
         (hash-map :revenue))))
