(ns expenses.db.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.db.purchases :as db.purchases]
            [monger.collection :as mc]))

(fact "create purchase should add the right id to the purchase"
  (db.purchases/create-purchase {:title  "title"
                                 :date   "2020-01-01"
                                 :amount 50} ..db..) => ..inserted-input..
  (provided
    (mc/insert-and-return ..db.. "purchases" {:_id    "-864163186"
                                              :title  "title"
                                              :date   "2020-01-01"
                                              :amount 50}) => ..inserted-input..))

(fact "create purchases list should add the right id to all the purchase"
  (db.purchases/create-purchases-list [{:title  "title"
                                        :date   "2020-01-01"
                                        :amount 50}
                                       {:title  "title 2"
                                        :date   "2020-01-02"
                                        :amount 100}] ..db..) => nil
  (provided
    (mc/insert-and-return ..db.. "purchases" {:_id    "-864163186"
                                               :title  "title"
                                               :date   "2020-01-01"
                                               :amount 50}) => ..inserted-input1..
    (mc/insert-and-return ..db.. "purchases" {:title  "title 2"
                                              :_id    "447888407"
                                              :date   "2020-01-02"
                                              :amount 100}) => ..inserted-input2..))
