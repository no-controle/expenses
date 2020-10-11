(ns integration.fixed-expense-test
  (:require [clojure.data.json :as json]
            [expenses.server :as server]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :refer :all]
            [monger.collection :as mc]
            [monger.core :as mg]
            [midje.sweet :refer :all]
            [expenses.logic.db-helper :as db-helper]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet server/configuration)))

(background (before :facts (-> (mg/get-db (mg/connect) "expenses") (mc/remove "fixed"))))

(defn do-post-request [url body]
  (let [response (response-for service
                               :post url
                               :headers {"Content-Type" "application/json"}
                               :body (json/write-str body))]
    (-> response
        :body
        (json/read-str :key-fn keyword))))

(defn do-delete-request [url]
  (let [response (response-for service
                               :delete url)]
    (-> response
        :body
        (json/read-str :key-fn keyword))))


(fact "Should create a new fixed expense successfullya"
   (do-post-request "/expenses/fixed" {:title  "Rent"
                                       :amount 800
                                       :source "cash"}) => (contains {:title      "Rent"
                                                                      :amount     800
                                                                      :source     "cash"
                                                                      :active     true
                                                                      :created-at (db-helper/current-date)}))

(fact "Should delete a fixed expense successfully"
  (let [fixed-expense (do-post-request "/expenses/fixed" {:title  "Rent" :amount 800 :source "cash"})]
    (do-delete-request (str "/expenses/fixed/" (:id fixed-expense)))) => {:message "Expense deleted"})
