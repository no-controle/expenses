(ns expenses.logic.date-helper
  (:require [clj-time.core :as time]
            [clj-time.format :as time-format]
            [clj-time.local :as time-local]))

(defn current-date []
  (let [now (time-local/local-now)]
    (-> (time-format/formatter "yyyy-MM-dd")
        (time-format/unparse now))))

(def months [{:month "Jan" :month-value 1} {:month "Fev" :month-value 2} {:month "Mar" :month-value 3} {:month "Abr" :month-value 4}
             {:month "Mai" :month-value 5} {:month "Jun" :month-value 6} {:month "Jul" :month-value 7} {:month "Ago" :month-value 8}
             {:month "Set" :month-value 9} {:month "Out" :month-value 10} {:month "Nov" :month-value 11} {:month "Dez" :month-value 12}])

(defn before-or-equal? [first-date second-date]
  (or (time/before? first-date second-date)
      (time/equal? first-date second-date)))

(defn after-or-equal? [first-date second-date]
  (or (time/after? first-date second-date)
      (time/equal? first-date second-date)))

(defn parse [date]
  (time-format/parse date))

(defn last-day-of-the-month [year month]
  (time/last-day-of-the-month (Integer/parseInt year) month))

(defn first-day-of-the-month [year month]
  (time/first-day-of-the-month (Integer/parseInt year) month))