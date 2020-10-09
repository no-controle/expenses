(ns expenses.controller.fixed
  (:require [expenses.db.fixed :as db.fixed]))

(defn create-fixed
  [fixed db]
  (let [expense-active (assoc fixed :active true)
        existent-expense (db.fixed/search-expense-with expense-active db)]
    (if (nil? existent-expense)
      (db.fixed/create-expense (assoc fixed :active true) db)
      {:message (str "Expense with this values already exists, expense id: " (:_id existent-expense))})))