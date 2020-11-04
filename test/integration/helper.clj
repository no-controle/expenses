(ns integration.helper
  (:require [io.pedestal.http :as bootstrap]
            [clojure.data.json :as json]
            [io.pedestal.test :refer :all]
            [expenses.server :as server]
            [monger.core :as mg]
            [monger.collection :as mc]
            [expenses.logic.db-helper :as db-helper]))

(def db-name "expenses-int-test")

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet server/configuration)))

(def current-date (db-helper/current-date))

(defn clean-collection [collection-name]
  (-> (mg/get-db (mg/connect) db-name)
      (mc/remove collection-name)))

(defn do-delete-request [url]
  (let [response (response-for service
                               :delete url)]
    (-> response
        :body
        (json/read-str :key-fn keyword))))

(defn do-post-request [url body]
  (let [response (response-for service
                               :post url
                               :headers {"Content-Type" "application/json"}
                               :body (json/write-str body))]
    (-> response
        :body
        (json/read-str :key-fn keyword))))

(defn do-get-request [url]
  (let [response (response-for service
                               :get url)]
    (-> response
        :body
        (json/read-str :key-fn keyword))))