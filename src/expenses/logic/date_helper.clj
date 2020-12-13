(ns expenses.logic.date-helper
  (:require [clj-time.core :as time]
            [clj-time.format :as time-format]))

(defn before-or-equal? [first-date second-date]
  (or (time/before? first-date second-date)
      (time/equal? first-date second-date)))

(defn after-or-equal? [first-date second-date]
  (or (time/after? first-date second-date)
      (time/equal? first-date second-date)))

(defn parse [date]
  (time-format/parse date))

(defn last-day-of-the-month [year month]
  (time/last-day-of-the-month year month))

(defn first-day-of-the-month [year month]
  (time/first-day-of-the-month year month))