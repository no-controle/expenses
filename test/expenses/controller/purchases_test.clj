(ns expenses.controller.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.purchases :as controller.purchases]
            [monger.collection :as mc])
  (:import (clojure.lang ExceptionInfo)))

(fact "create new purchase should return purchase created"
  (controller.purchases/create-purchase {:title  "title"
                                         :date   "20202-01-01"
                                         :amount 10} ..db..) => {:message "Purchase created"}
  (provided
    (mc/insert ..db.. "expenses" {:_id    "-1028434777"
                                  :title  "title"
                                  :date   "20202-01-01"
                                  :amount 10}) => ..mongo-success-result..))

(fact "create new purchase when already exists one should return error"
  (controller.purchases/create-purchase {:title  "title"
                                         :date   "20202-01-01"
                                         :amount 10} ..db..) => (throws ExceptionInfo #"Purchase already exist on database")
  (provided
    (mc/insert ..db.. "expenses" {:_id    "-1028434777"
                                  :title  "title"
                                  :date   "20202-01-01"
                                  :amount 10}) => (throw (Exception. "error"))))
