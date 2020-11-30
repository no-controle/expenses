(ns integration.helper.common
  (:require [clj-time.core :as time]
            [clojure.data.json :as json]
            [expenses.logic.db-helper :as db-helper]
            [expenses.server :as server]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :refer :all]
            [monger.collection :as mc]
            [monger.core :as mg]))

(def now (time/now))
(def current-month (time/month now))
(def current-year (time/year now))
(def previous-month
  (if (= (- current-month 1) 0)
    12
    (- current-month 1)))
(def previous-month-year
  (if (= (- current-month 1) 0)
    (- current-year 1)
    current-year))

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