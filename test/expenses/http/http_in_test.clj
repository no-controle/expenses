(ns expenses.http.http-in-test
  (:require [midje.sweet :refer :all]
            [expenses.http.http-in :as http-in]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.adapter.purchases :as adapter.purchases]))

(facts "crete a new purchase"
  (fact "should return 200 with the created purchase message"
    (http-in/create-purchase {:db ..db.. :json-params ..new-purchase..}) => {:status 200
                                                                             :body   {:message "Purchase created"}}
    (provided
      (adapter.purchases/http-in->purchase ..new-purchase..) => ..valid-purchase..
      (controller.purchases/create-purchase ..valid-purchase.. ..db..) => {:message "Purchase created"}))

  (fact "throws IllegalArgumentException when the input is not valid"
    (http-in/create-purchase {:db ..db.. :json-params ..new-purchase..}) => (throws IllegalArgumentException)
    (provided
      (adapter.purchases/http-in->purchase ..new-purchase..) => (throw (IllegalArgumentException.))))

  (fact "throws DuplicateKeyException when the input already exists on the database"
    (http-in/create-purchase {:db ..db.. :json-params ..new-purchase..}) => (throws Exception)
    (provided
      (adapter.purchases/http-in->purchase ..new-purchase..) => ..valid-purchase..
      (controller.purchases/create-purchase ..valid-purchase.. ..db..) => (throw (Exception.)))))

(facts "create purchases by batch"
  (fact "should return 200 with created purchases list"
    (http-in/create-purchases-batch {:db ..db.. :json-params ..purchases-list..}) => {:status 200
                                                                                      :body {:message "Purchases list created"}}
    (provided
      (adapter.purchases/http-in->purchases-list ..purchases-list..) => ..valid-list..
      (controller.purchases/create-purchases-list ..valid-list.. ..db..) => {:message "Purchases list created"})))
