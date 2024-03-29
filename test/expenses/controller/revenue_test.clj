(ns expenses.controller.revenue-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.revenue :as controller.revenue]
            [expenses.logic.date-helper :as date-helper]
            [expenses.db.revenue :as db.revenue]))

(facts "Create revenue"
  (fact "should create recurrent revenue with active true"
    (controller.revenue/create-revenue {:title      "salary"
                                        :amount     3000
                                        :start-date "2020-10-01"
                                        :recurrent  true} ..db..) => {:title          "salary"
                                                                      :amount     3000
                                                                      :recurrent  true
                                                                      :active     true
                                                                      :start-date "2020-10-01"
                                                                      :created-at (date-helper/current-date)}
    (provided
      (db.revenue/create-revenue {:title      "salary"
                                  :amount     3000
                                  :recurrent  true
                                  :start-date "2020-10-01"
                                  :active     true} ..db..) => {:title      "salary"
                                                                :amount     3000
                                                                :recurrent  true
                                                                :active     true
                                                                :start-date "2020-10-01"
                                                                :created-at (date-helper/current-date)}))

  (fact "should create regular revenue with active true"
    (controller.revenue/create-revenue {:title      "Jobz"
                                        :amount     3000
                                        :start-date "2020-10-01"} ..db..) => {:title      "Jobz"
                                                                              :amount     3000
                                                                              :active     true
                                                                              :start-date "2020-10-01"
                                                                              :created-at (date-helper/current-date)}
    (provided
      (db.revenue/create-revenue {:title      "Jobz"
                                  :amount     3000
                                  :active     true
                                  :start-date "2020-10-01"} ..db..) => {:title      "Jobz"
                                                                        :amount     3000
                                                                        :active     true
                                                                        :start-date "2020-10-01"
                                                                        :created-at (date-helper/current-date)}))

  (fact "should set start date to current date when it is empty"
    (controller.revenue/create-revenue {:title      "Jobz"
                                        :amount     3000} ..db..) => {:title      "Jobz"
                                                                      :amount     3000
                                                                      :active     true
                                                                      :start-date (date-helper/current-date)
                                                                      :created-at (date-helper/current-date)}
    (provided
      (db.revenue/create-revenue {:title      "Jobz"
                                  :amount     3000
                                  :active     true
                                  :start-date (date-helper/current-date)} ..db..) => {:title      "Jobz"
                                                                                      :amount     3000
                                                                                      :active     true
                                                                                      :start-date (date-helper/current-date)
                                                                                      :created-at (date-helper/current-date)})))

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
      (db.revenue/search-revenue-with {:recurrent false :period "2020-10"} ..db..) => [{:title  "Video clip"
                                                                                        :amount 500}]
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [{:title  "Salary"
                                                                     :amount 500}]))

  (fact "should return total for given month and year when there is only regular revenues"
    (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 500}
    (provided
      (db.revenue/search-revenue-with {:recurrent false :period "2020-10"} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [{:title  "Salary"
                                                                     :amount 500}]))

  (fact "should return total for given month and year when there is no revenue"
    (controller.revenue/total-revenue-for-period 2020 10 ..db..) => {:revenue 0}
    (provided
      (db.revenue/search-revenue-with {:recurrent false :period "2020-10"} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [])))

(facts "Getting revenue for every month on given year"
  (fact "Should return recurrent revenue from created month until december "
    (controller.revenue/revenue-for-year "2020" ..db..) => [{:title  "Jan"
                                                             :amount 0.0}
                                                            {:title  "Fev"
                                                             :amount 0.0}
                                                            {:title  "Mar"
                                                             :amount 3500.0}
                                                            {:title  "Abr"
                                                             :amount 3500.0}
                                                            {:title  "Mai"
                                                             :amount 3500.0}
                                                            {:title  "Jun"
                                                             :amount 3500.0}
                                                            {:title  "Jul"
                                                             :amount 3500.0}
                                                            {:title  "Ago"
                                                             :amount 3500.0}
                                                            {:title  "Set"
                                                             :amount 3500.0}
                                                            {:title  "Out"
                                                             :amount 3500.0}
                                                            {:title  "Nov"
                                                             :amount 3500.0}
                                                            {:title  "Dez"
                                                             :amount 3500.0}]
    (provided
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => [{:_id        ..uuid..
                                                                     :title      "Salary"
                                                                     :amount     3500
                                                                     :active     true
                                                                     :recurrent  true
                                                                     :start-date "2020-03-01"
                                                                     :created-at "2020-03-01"
                                                                     :updated-at "2020-03-01"}]
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    false} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent false :period "2020"} ..db..) => []))

  (fact "Should return inactive recurrent revenue until updated date"
    (controller.revenue/revenue-for-year "2020" ..db..) => [{:title  "Jan"
                                                             :amount 3500.0}
                                                            {:title  "Fev"
                                                             :amount 3500.0}
                                                            {:title  "Mar"
                                                             :amount 3500.0}
                                                            {:title  "Abr"
                                                             :amount 0.0}
                                                            {:title  "Mai"
                                                             :amount 0.0}
                                                            {:title  "Jun"
                                                             :amount 0.0}
                                                            {:title  "Jul"
                                                             :amount 0.0}
                                                            {:title  "Ago"
                                                             :amount 0.0}
                                                            {:title  "Set"
                                                             :amount 0.0}
                                                            {:title  "Out"
                                                             :amount 0.0}
                                                            {:title  "Nov"
                                                             :amount 0.0}
                                                            {:title  "Dez"
                                                             :amount 0.0}]
    (provided
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    false} ..db..) => [{:_id        ..uuid..
                                                                      :title      "Salary"
                                                                      :amount     3500
                                                                      :active     true
                                                                      :recurrent  true
                                                                      :start-date "2020-01-01"
                                                                      :created-at "2020-03-01"
                                                                      :updated-at "2020-03-01"}]
      (db.revenue/search-revenue-with {:recurrent false :period "2020"} ..db..) => []))

  (fact "Should return non-recurrent revenues on created dates"
    (controller.revenue/revenue-for-year "2020" ..db..) => [{:title  "Jan"
                                                             :amount 0.0}
                                                            {:title  "Fev"
                                                             :amount 0.0}
                                                            {:title  "Mar"
                                                             :amount 3500.0}
                                                            {:title  "Abr"
                                                             :amount 0.0}
                                                            {:title  "Mai"
                                                             :amount 0.0}
                                                            {:title  "Jun"
                                                             :amount 4700.0}
                                                            {:title  "Jul"
                                                             :amount 0.0}
                                                            {:title  "Ago"
                                                             :amount 0.0}
                                                            {:title  "Set"
                                                             :amount 10000.0}
                                                            {:title  "Out"
                                                             :amount 0.0}
                                                            {:title  "Nov"
                                                             :amount 0.0}
                                                            {:title  "Dez"
                                                             :amount 0.0}]
    (provided
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    true} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent true
                                       :active    false} ..db..) => []
      (db.revenue/search-revenue-with {:recurrent false :period "2020"} ..db..) => [{:_id        ..uuid..
                                                                                     :title      "Jobz 1"
                                                                                     :amount     3500
                                                                                     :created-at "2020-03-01"
                                                                                     :updated-at "2020-03-01"}
                                                                                    {:_id        ..uuid2..
                                                                                     :title      "Jobz 2"
                                                                                     :amount     4700
                                                                                     :created-at "2020-06-01"
                                                                                     :updated-at "2020-06-01"}
                                                                                    {:_id        ..uuid3..
                                                                                     :title      "Jobz 3"
                                                                                     :amount     8700
                                                                                     :created-at "2020-09-01"
                                                                                     :updated-at "2020-09-01"}
                                                                                    {:_id        ..uuid4..
                                                                                     :title      "Jobz 4"
                                                                                     :amount     1300
                                                                                     :created-at "2020-09-30"
                                                                                     :updated-at "2020-09-30"}])))
