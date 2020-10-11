(ns integration.revenue-test
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

(background (before :facts (-> (mg/get-db (mg/connect) "expenses") (mc/remove "revenue"))))

(defn do-post-request [url body]
  (let [response (response-for service
                               :post url
                               :headers {"Content-Type" "application/json"}
                               :body (json/write-str body))]
    (-> response
        :body
        (json/read-str :key-fn keyword))))

(fact "Should create a new revenue successfully"
  (do-post-request "/revenue" {:title     "Salary"
                               :amount    3500
                               :recurrent true}) => (contains {:title      "Salary"
                                                               :amount     3500
                                                               :recurrent  true
                                                               :active     true
                                                               :created-at (db-helper/current-date)}))
