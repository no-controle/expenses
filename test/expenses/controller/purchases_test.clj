(ns expenses.controller.purchases-test
  (:require [midje.sweet :refer :all]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.db.purchases :as db.purchases]))

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