(ns integration.fixed-expense-test
  (:require [midje.sweet :refer :all]
            [integration.helper :as helper]))

(background (before :facts (helper/clean-collection "fixed")))

(fact "Should create a new fixed expense successfully"
   (helper/do-post-request "/expenses/fixed" {:title  "Rent"
                                              :amount 800
                                              :source "cash"}) => (contains {:title      "Rent"
                                                                             :amount     800
                                                                             :source     "cash"
                                                                             :active     true
                                                                             :created-at helper/current-date}))

(fact "Should delete a fixed expense successfully"
  (let [fixed-expense (helper/do-post-request "/expenses/fixed" {:title  "Rent" :amount 800 :source "cash"})]
    (helper/do-delete-request (str "/expenses/fixed/" (:id fixed-expense)))) => {:message "Expense deleted"})
