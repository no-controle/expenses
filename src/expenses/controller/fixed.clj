(ns expenses.controller.fixed
  (:require [expenses.db.fixed :as db.fixed])
  (:import (javax.management InstanceAlreadyExistsException)))

(defn create-fixed
  [fixed db]
  (let [expense-active (assoc fixed :active true)
        existent-expense (db.fixed/search-expense-with expense-active db)]
    (if (empty? existent-expense)
      (db.fixed/create-expense expense-active db)
      (throw (InstanceAlreadyExistsException. (str "Expense id: " (:_id existent-expense)))))))