(ns integration.helper.purchases-builder
  (:require [integration.helper.common :as helper]))

(def twentieth-from-current-month (str helper/current-year "-" helper/current-month "-20"))

(def tenth-from-last-month (str helper/previous-month-year "-" helper/previous-month "-10"))

(defn create-data []
  (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                :amount     300
                                                :source     "Credit Card"
                                                :category   "Supermarket"
                                                :date       twentieth-from-current-month
                                                :bill-month (str helper/current-month)
                                                :bill-year  (str helper/current-year)})
  (helper/do-post-request "/expenses/purchase" {:title      "Best Buy"
                                                :amount     1000
                                                :source     "Credit Card"
                                                :category   "Electronic"
                                                :date       twentieth-from-current-month
                                                :bill-month (str helper/current-month)
                                                :bill-year  (str helper/current-year)})
  (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                :amount     500
                                                :source     "Credit Card"
                                                :category   "Supermarket"
                                                :date       twentieth-from-current-month
                                                :bill-month (str helper/current-month)
                                                :bill-year  (str helper/current-year)})
  (helper/do-post-request "/expenses/purchase" {:title      "Walgreens"
                                                :amount     200
                                                :source     "Credit Card"
                                                :category   "Health"
                                                :date       tenth-from-last-month
                                                :bill-month (str helper/previous-month)
                                                :bill-year  (str helper/previous-month-year)})
  (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                :amount     700
                                                :source     "Credit Card"
                                                :category   "Supermarket"
                                                :date       tenth-from-last-month
                                                :bill-month (str helper/previous-month)
                                                :bill-year  (str helper/previous-month-year)})
  (helper/do-post-request "/expenses/purchase" {:title      "Guitar Center"
                                                :amount     1800
                                                :source     "Credit Card"
                                                :category   "Entertainment"
                                                :date       tenth-from-last-month
                                                :bill-month (str helper/previous-month)
                                                :bill-year  (str helper/previous-month-year)}))