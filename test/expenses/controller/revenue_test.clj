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
      (db.revenue/search-revenue-with {:_id ..id..} ..db..) => {:_id ..id.. :active true}
      (db.revenue/update-revenue ..id..{:_id ..id.. :active false} ..db..) => ..ok..))

  (fact "should not call delete revenue when does not exist for id"
    (controller.revenue/delete-revenue ..id.. ..db..) => {:message "Revenue deleted"}
    (provided
      (db.revenue/search-revenue-with {:_id ..id..} ..db..) => nil)))