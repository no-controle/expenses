(ns integration.purchase-test
  (:require [midje.sweet :refer :all]
            [integration.helper.common :as helper]))

(background (before :facts (helper/clean-collection "purchases")))

(def csv "date,category,title,amount,source,bill-month,bill-year\n2017-11-29,transporte,Uber do Brasil Tecnolo,8.89, Credit card, 1, 2018\n2017-11-29,transporte,Uber do Brasil Tecnolo,7.93, Credit card, 1, 2018\n2017-11-30,viagem,Aviancasiteb2c*Lism8t2 2/3,110, Credit card, 1, 2018")

(fact "Should create a new purchase successfully"
  (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                :amount     240
                                                :source     "Credit Card"
                                                :category   "Supermarket"
                                                :date       "2020-10-20"
                                                :bill-month "11"
                                                :bill-year  "2020"}) => (contains {:title      "Target"
                                                                                   :amount     240
                                                                                   :source     "Credit Card"
                                                                                   :category   "Supermarket"
                                                                                   :date       "2020-10-20"
                                                                                   :bill-month "11"
                                                                                   :bill-year  "2020"}))

(fact "Should delete purchase successfully"
  (let [purchase (helper/do-post-request "/expenses/purchase" {:title      "Target"
                                                               :amount     240
                                                               :source     "Credit Card"
                                                               :category   "Supermarket"
                                                               :date       "2020-10-20"
                                                               :bill-month "11"
                                                               :bill-year  "2020"})]
    (helper/do-delete-request (str "/expenses/purchase/" (:id purchase)))) => {:message "Purchase deleted"})

(fact "Should create purchases list"
  (helper/do-post-request "/purchases-from-csv" {:csv csv}) => {:message "Processo finalizou"})