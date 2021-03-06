(ns expenses.db.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.db.purchases :as db.purchases]
            [monger.collection :as mc]
            [expenses.logic.date-helper :as date-helper]
            [expenses.logic.db-helper :as db-helper]))

(fact "create purchase should add the right id and created/update date to the purchase"
  (db.purchases/create-purchase {:title  "title"
                                 :date   "2020-01-01"
                                 :amount 50} ..db..) => ..inserted-input..
  (provided
    (db-helper/generate-uuid) => ..uuid..
    (date-helper/current-date) => ..today..
    (mc/insert-and-return ..db.. "purchases" {:title      "title"
                                              :date       "2020-01-01"
                                              :amount     50
                                              :_id        ..uuid..
                                              :created-at ..today..
                                              :updated-at  ..today..}) => ..inserted-input..))

(fact "Update purchase should add updated-at to purchase"
  (db.purchases/update-purchase ..uuid.. {:_id        ..uuid..
                                          :title      "title"
                                          :date       "2020-01-01"
                                          :amount     50
                                          :created-at ..created-date..} ..db..) => ..updated-purchase..
  (provided
    (date-helper/current-date) => ..current-date..
    (mc/update-by-id ..db.. "purchases"  ..uuid.. {:_id        ..uuid..
                                                   :title      "title"
                                                   :date       "2020-01-01"
                                                   :amount     50
                                                   :created-at ..created-date..
                                                   :updated-at ..current-date..}) => ..updated-purchase..))


(fact "create purchases list should add the right id to all the purchase"
  (db.purchases/create-purchases-list [{:title  "title"
                                        :date   "2020-01-01"
                                        :amount 50}
                                       {:title  "title 2"
                                        :date   "2020-01-02"
                                        :amount 100}] ..db..) => ..success..
  (provided
    (db-helper/generate-uuid) => ..uuid..
    (date-helper/current-date) => ..today..
    (mc/insert-batch ..db.. "purchases" [{:_id        ..uuid..
                                          :title      "title"
                                          :date       "2020-01-01"
                                          :created-at ..today..
                                          :updated-at ..today..
                                          :amount     50}
                                         {:title      "title 2"
                                          :_id        ..uuid..
                                          :date       "2020-01-02"
                                          :created-at ..today..
                                          :updated-at ..today..
                                          :amount     100}]) => ..success..))

(fact "Searching given category and period should return purchases"
  (db.purchases/search-purchase-in-category-with ["house" "trip" "school"] {:bill-month "10" :bill-year "2020"} ..db..) => ..purchases-list..
  (provided
    (mc/find-maps ..db.. "purchases" {:bill-month "10"
                                      :bill-year  "2020"
                                      :refunded   {"$ne" true}
                                      :category   {"$regex" "house|trip|school" "$options" "i"}}) => ..purchases-list..))

(fact "Searching given category and period should return purchases different from category list"
      (db.purchases/search-purchase-not-in-category-with ["house" "trip" "school"] {:bill-month "10" :bill-year "2020"} ..db..) => ..purchases-list..
      (provided
        (mc/find-maps ..db.. "purchases" {:bill-month "10"
                                          :bill-year  "2020"
                                          :refunded   {"$ne" true}
                                          :category   {"$not" {"$regex" "house|trip|school" "$options" "i"}}}) => ..purchases-list..))

