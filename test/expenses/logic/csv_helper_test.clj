(ns expenses.logic.csv-helper-test
  (:require [midje.sweet :refer :all]
            [expenses.logic.csv-helper :as logic.csv-helper]))

(def csv "date,category,title,amount,source,bill-month,bill-year\n2017-11-29,transporte,Uber do Brasil Tecnolo,8.89, Credit card, 1, 2018\n2017-11-29,transporte,Uber do Brasil Tecnolo,7.93, Credit card, 1, 2018\n2017-11-30,viagem,Aviancasiteb2c*Lism8t2 2/3,110, Credit card, 1, 2018")

(fact "Parse Csv value to map "
  (logic.csv-helper/parse-csv csv) => [{:amount     8.89
                                        :bill-month " 1"
                                        :bill-year  " 2018"
                                        :category   "transporte"
                                        :date       "2017-11-29"
                                        :source     " Credit card"
                                        :title      "Uber do Brasil Tecnolo"}
                                       {:amount     7.93
                                        :bill-month " 1"
                                        :bill-year  " 2018"
                                        :category   "transporte"
                                        :date       "2017-11-29"
                                        :source     " Credit card"
                                        :title      "Uber do Brasil Tecnolo"}
                                       {:amount     110.0
                                        :bill-month " 1"
                                        :bill-year  " 2018"
                                        :category   "viagem"
                                        :date       "2017-11-30"
                                        :source     " Credit card"
                                        :title      "Aviancasiteb2c*Lism8t2 2/3"}])