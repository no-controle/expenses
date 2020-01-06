(ns expenses.http.http-in-test
  (:require [midje.sweet :refer :all]
            [expenses.http.http-in :as http-in]
            [expenses.controller.purchases :as controller.purchases])
  (:import (com.mongodb DuplicateKeyException)))

(fact "crete purchase return 200"
  (http-in/create-purchase {:db ..db.. :json-params ..new-purchase..}) => {:status 200
                                                                           :body   {:message "Purchase created"}}
  (provided
    (controller.purchases/create-purchase ..new-purchase.. ..db..) => {:message "Purchase created"}))

(fact "crete purchase return 400"
  (http-in/create-purchase {:db ..db.. :json-params ..new-purchase..}) => {:status 400
                                                                           :body   {:message "Purchase already exists"}}
  (provided
    (controller.purchases/create-purchase ..new-purchase.. ..db..) => (throw (Exception. "error"))))
