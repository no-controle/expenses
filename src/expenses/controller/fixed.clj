(ns expenses.controller.fixed
  (:require [expenses.db.fixed :as db.fixed]
            [expenses.logic.date-helper :refer [months]]
            [expenses.logic.fixed :as logic.fixed]
            [expenses.logic.date-helper :as date-helper])
  (:import (javax.management InstanceAlreadyExistsException)))

(defn create-fixed
  [fixed db]
  (let [expense-active (-> fixed :start-date nil? (if (assoc fixed :start-date (date-helper/current-date)) fixed) (assoc :active true))
        existent-expense (db.fixed/search-expense-with expense-active db)]
    (if (empty? existent-expense)
      (db.fixed/create-expense expense-active db)
      (throw (InstanceAlreadyExistsException. (str "Expense id: " (:_id existent-expense)))))))

(defn delete-fixed [id db]
  (let [expense (-> (db.fixed/search-expense-with {:_id id} db) first)]
    (when (not-empty expense) (db.fixed/update-expense id (assoc expense :active false) db))
    {:message "Expense deleted"}))

(defn active-fixed-expenses [db]
  (db.fixed/search-expense-with {:active true} db))

(defn fixed-expenses-for-year [year db]
  (let [active-expenses (db.fixed/search-expense-with {:active true} db)
        inactive-expenses (db.fixed/search-expense-with {:active false} db)]
    (map #(logic.fixed/data-for-month-year % year active-expenses inactive-expenses) months)))