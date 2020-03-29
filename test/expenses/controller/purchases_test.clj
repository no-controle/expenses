(ns expenses.controller.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.db.purchases :as db.purchases]))

(def purchase-list [{:title  "Grocery Store"
                     :date   "2020-12-05"
                     :amount 50
                     :period "2020-12"
                     :category "Supermarket"}
                    {:title    "The Fun Fun Fun"
                     :date     "2020-12-09"
                     :amount   300
                     :period   "2020-12"
                     :category "Entertainment"}
                    {:title    "The med place"
                     :date     "2020-12-15"
                     :amount   14
                     :period   "2020-12"
                     :category "Health"}
                    {:title    "Grocery Store"
                     :date     "2020-12-08"
                     :amount   80
                     :period   "2020-12"
                     :category "Supermarket"}])

(facts "create a new purchase"
  (fact "should return purchase created"
    (controller.purchases/create-purchase ..purchase.. ..db..) => {:message "Purchase created"}
    (provided
      (db.purchases/create-purchase ..purchase.. ..db..) => ..mongo-success-result..))

 (fact "when already exists one should return error"
   (controller.purchases/create-purchase ..purchase.. ..db..) => (throws Exception)
   (provided
     (db.purchases/create-purchase ..purchase.. ..db..) => (throw (Exception.)))))

(facts "create a purchases list"
  (fact "should return purchases created"
    (controller.purchases/create-purchases-list ..purchases-list.. ..db..) => {:message "Processo finalizou"}
    (provided
      (db.purchases/create-purchases-list ..purchases-list.. ..db..) => ..mongo-success-result..))

  (fact "should throw"
    (controller.purchases/create-purchases-list ..purchases-list.. ..db..) => (throws Exception)
    (provided
      (db.purchases/create-purchases-list ..purchases-list.. ..db..) => (throw (Exception.)))))

(facts "get purchases list by period"
  (fact "should return puchases result"
    (controller.purchases/get-by-period "2020" "12" ..db..) => {:title  "Purchase title"
                                                                :date   "2020-12-10"
                                                                :amount 50
                                                                :period "2020-12"}
    (provided
      (db.purchases/get-by-period "2020-12" ..db..) => {:title  "Purchase title"
                                                        :date   "2020-12-10"
                                                        :amount 50
                                                        :period "2020-12"})))

(facts "get purchases summary by period and grouped by category"
  (fact "should return summary for given period"
    (controller.purchases/get-summary {:year  "2020"
                                       :month "12"
                                       :by    "category"} ..db..) => [{:category          "Entertainment"
                                                                       :sum       300
                                                                       :count     1
                                                                       :purchases [{:title    "The Fun Fun Fun"
                                                                                    :date     "2020-12-09"
                                                                                    :amount   300
                                                                                    :period   "2020-12"
                                                                                    :category "Entertainment"}]}
                                                                      {:category  "Supermarket"
                                                                       :sum       130
                                                                       :count     2
                                                                       :purchases [{:title    "Grocery Store"
                                                                                    :date     "2020-12-05"
                                                                                    :amount   50
                                                                                    :period   "2020-12"
                                                                                    :category "Supermarket"}
                                                                                   {:title    "Grocery Store"
                                                                                    :date     "2020-12-08"
                                                                                    :amount   80
                                                                                    :period   "2020-12"
                                                                                    :category "Supermarket"}]}
                                                                      {:category  "Health"
                                                                       :sum       14
                                                                       :count     1
                                                                       :purchases [{:title    "The med place"
                                                                                    :date     "2020-12-15"
                                                                                    :amount   14
                                                                                    :period   "2020-12"
                                                                                    :category "Health"}]}]

    (provided
      (db.purchases/get-by-period "2020-12" ..db..) => purchase-list)))

(facts "get purchases summary by period and grouped by title"
  (fact "should return summary for given period"
    (controller.purchases/get-summary {:year  "2020"
                                       :month "12"
                                       :by    "title"} ..db..) => [{:title         "The Fun Fun Fun"
                                                                    :sum       300
                                                                    :count     1
                                                                    :purchases [{:title    "The Fun Fun Fun"
                                                                                 :date     "2020-12-09"
                                                                                 :amount   300
                                                                                 :period   "2020-12"
                                                                                 :category "Entertainment"}]}
                                                                   {:title     "Grocery Store"
                                                                    :sum       130
                                                                    :count     2
                                                                    :purchases [{:title    "Grocery Store"
                                                                                 :date     "2020-12-05"
                                                                                 :amount   50
                                                                                 :period   "2020-12"
                                                                                 :category "Supermarket"}
                                                                                {:title    "Grocery Store"
                                                                                 :date     "2020-12-08"
                                                                                 :amount   80
                                                                                 :period   "2020-12"
                                                                                 :category "Supermarket"}]}
                                                                   {:title     "The med place"
                                                                    :sum       14
                                                                    :count     1
                                                                    :purchases [{:title    "The med place"
                                                                                 :date     "2020-12-15"
                                                                                 :amount   14
                                                                                 :period   "2020-12"
                                                                                 :category "Health"}]}]

      (provided
        (db.purchases/get-by-period "2020-12" ..db..) => purchase-list)))