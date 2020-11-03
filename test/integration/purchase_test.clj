(ns integration.purchase-test
  (:require [midje.sweet :refer :all]
            [integration.helper :as helper]))

;(background (before :facts (helper/clean-collection "purchases")))

;(fact "Should create a new purchase successfully"
;  (helper/do-post-request "/expenses/purchase" {:title      "Target"
;                                                :amount     240
;                                                :source     "Credit Card"
;                                                :category   "Supermarket"
;                                                :date       "2020-10-20"
;                                                :bill-month "11"
;                                                :bill-year  "2020"}) => (contains {:title      "Target"
;                                                                                   :amount     240
;                                                                                   :source     "Credit Card"
;                                                                                   :category   "Supermarket"
;                                                                                   :date       "2020-10-20"
;                                                                                   :bill-month "11"
;                                                                                   :bill-year  "2020"}))
;
;(fact "Should delete purchase successfully"
;  (let [purchase (helper/do-post-request "/expenses/purchase" {:title      "Target"
;                                                               :amount     240
;                                                               :source     "Credit Card"
;                                                               :category   "Supermarket"
;                                                               :date       "2020-10-20"
;                                                               :bill-month "11"
;                                                               :bill-year  "2020"})]
;    (helper/do-delete-request (str "/expenses/purchase/" (:id purchase)))) => {:message "Purchase deleted"})