(ns integration.monthly_test
  (:require [midje.sweet :refer :all]
            [integration.helper.common :as helper]
            [integration.helper.revenue-builder :as revenue-builder]
            [integration.helper.fixed-builder :as fixed-builder]
            [integration.helper.purchases-builder :as purchases-builder]))

(background (before :facts (helper/clean-collection "purchases")))
(background (before :facts (helper/clean-collection "fixed")))
(background (before :facts (helper/clean-collection "revenue")))

(defn create-data []
  (revenue-builder/create-data)
  (fixed-builder/create-data)
  (purchases-builder/create-data))

(future-fact "Skipping monthly integration tests until understand how to use a memory database"
  (fact "Should return monthly summary data for given period"
    (create-data)
    (helper/do-get-request (str "/monthly?year=" helper/current-year "&month=" helper/current-month)) => {:balance  15290
                                                                                                          :expenses 3710
                                                                                                          :income   19000
                                                                                                          :fixed    [{:amount 1500 :title "Rent"}
                                                                                                                     {:amount 200 :title "Internet provider"}
                                                                                                                     {:amount 35 :title "Netflix"}
                                                                                                                     {:amount 25 :title "Spotify"}
                                                                                                                     {:amount 150 :title "Electric Company"}]
                                                                                                          :variable [{:amount 800 :title "Supermarket"}]
                                                                                                          :extra    [{:amount 1000 :title "Electronic"}]}))