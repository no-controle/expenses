(ns integration.revenue-test
  (:require [midje.sweet :refer :all]
            [integration.helper.common :as helper]))

(background (before :facts (helper/clean-collection "revenue")))

(future-fact "Skipping revenue integration tests until understand how to use a memory database"
 (fact "Should create a new revenue successfully"
   (helper/do-post-request "/revenue" {:title     "Salary"
                                       :amount    3500
                                       :recurrent true}) => (contains {:title      "Salary"
                                                                       :amount     3500
                                                                       :recurrent  true
                                                                       :active     true
                                                                       :created-at helper/current-date})))

(fact "Should delete revenue successfully"
  (let [revenue (helper/do-post-request "/revenue" {:title "Salary" :amount 3500 :recurrent true})]
    (helper/do-delete-request (str "/revenue/" (:id revenue)))) => {:message "Revenue deleted"})
