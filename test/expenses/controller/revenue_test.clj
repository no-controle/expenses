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

(facts "Delete revenue"
  (fact "should delete revenue when exists"
    (controller.revenue/delete-revenue ..id.. ..db..) => {:message "Revenue deleted"}
    (provided
      (db.revenue/search-revenue-with {:_id ..id..} ..db..) => [{:_id ..id.. :active true}]
      (db.revenue/update-revenue ..id..{:_id ..id.. :active false} ..db..) => ..ok..))

  (fact "should not call delete revenue when does not exist for id"
    (controller.revenue/delete-revenue ..id.. ..db..) => {:message "Revenue deleted"}
    (provided
      (db.revenue/search-revenue-with {:_id ..id..} ..db..) => [])))

(facts "Get total revenue for period "
  (fact "should return total for given month and year when there is recurrent and regular revenues"
    (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 1000}
    (provided
      (db.revenue/search-revenue-for-period 2020 10 ..db..) => [{:title  "Video clip"
                                                                 :amount 500}]
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [{:title  "Salary"
                                                                     :amount 500}]))

  (fact "should return total for given month and year when there is only regular revenues"
    (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 500}
    (provided
      (db.revenue/search-revenue-for-period 2020 10 ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [{:title  "Salary"
                                                                     :amount 500}]))

  (fact "should return total for given month and year when there is no revenue"
    (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 0}
    (provided
      (db.revenue/search-revenue-for-period 2020 10 ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [])))