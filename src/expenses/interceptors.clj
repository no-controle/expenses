(ns expenses.interceptors
  (:require [monger.core :as mg]
            [clojure.data.json :as json]))

(def db-interceptor
  {:name  :database-interceptor
   :enter (fn [context]
            (let [connection (mg/connect)
                  db (mg/get-db connection "expenses")]
              (update context :request assoc :db db)))})

(def to-json-response-interceptor
  {:name  :to-json-response
   :leave (fn [context]
            (update context :response assoc
                    :headers {"Content-Type" "application/json"}
                    :body (json/write-str (-> context :response :body))))})