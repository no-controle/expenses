(ns expenses.controller.revenue
  (:require [expenses.db.revenue :as db.revenue]
            [expenses.logic.date-helper :refer [months]]
            [expenses.logic.revenue :as logic.revenue]
            [expenses.logic.date-helper :as date-helper]))

(defn create-revenue [revenue db]
  (-> revenue :start-date nil?
      (if (assoc revenue :start-date (date-helper/current-date)) revenue)
      (assoc :active true)
      (db.revenue/create-revenue db)))

(defn delete-revenue [id db]
  (let [revenue (-> (db.revenue/search-revenue-with {:_id id} db) first)]
    (when (not-empty revenue) (db.revenue/update-revenue id (assoc revenue :active false) db))
    {:message "Revenue deleted"}))

(defn total-revenue-for-period [year month db]
  (let [incomes-on-period (db.revenue/search-revenue-with {:recurrent false :period (str year "-" month)} db)
        recurrent-income (db.revenue/search-revenue-with {:recurrent true
                                                          :active    true} db)]
    (->> (concat incomes-on-period recurrent-income)
         (map :amount)
         (reduce +)
         (hash-map :revenue))))

(defn revenue-for-year [year db]
  (let [recurrent-active (db.revenue/search-revenue-with {:recurrent true
                                                          :active    true} db)
        recurrent-inactive (db.revenue/search-revenue-with {:recurrent true
                                                            :active    false} db)
        incomes-on-period (db.revenue/search-revenue-with {:recurrent false :period year} db)]
    (map #(logic.revenue/data-for-month-year % year recurrent-active recurrent-inactive incomes-on-period) months)))
