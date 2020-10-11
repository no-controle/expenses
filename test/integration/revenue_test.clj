(ns integration.revenue-test
  (:require [midje.sweet :refer :all]
            [integration.helper :as helper]))

(background (before :facts (helper/clean-collection "revenue")))

(fact "Should create a new revenue successfully"
  (helper/do-post-request "/revenue" {:title     "Salary"
                                      :amount    3500
                                      :recurrent true}) => (contains {:title      "Salary"
                                                                      :amount     3500
                                                                      :recurrent  true
                                                                      :active     true
                                                                      :created-at helper/current-date}))
