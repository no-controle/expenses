(ns expenses.controller.revenue-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.revenue :as controller.revenue]
            [expenses.logic.db-helper :as db-helper]
            [expenses.db.revenue :as db.revenue]))

(facts "Create revenue"
  (fact "should create successfully new revenue"
    (controller.revenue/create-revenue {:title     "salary"
                                        :amount    3000
                                        :recurrent true} ..db..) => {:title      "salary"
                                                                     :amount     3000
                                                                     :recurrent  true
                                                                     :active     true
                                                                     :created-at (db-helper/current-date)}
    (provided
      (db.revenue/create-revenue {:title "salary"
                                  :amount 3000
                                  :recurrent true
                                  :active true} ..db..) => {:title      "salary"
                                                            :amount     3000
                                                            :recurrent  true
                                                            :active     true
                                                            :created-at (db-helper/current-date)})))