(ns expenses.interceptors
  (:require [clojure.data.json :as json]
            [io.pedestal.interceptor.error :as error-int]
            [monger.core :as mg]))

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

(def service-error-handler
  (error-int/error-dispatch
    [ctx ex]

    [{:exception-type :com.mongodb.DuplicateKeyException}]
    ((assoc ctx :response {:status 400 :body {:message     "Trying to insert existent input"
                                              :error-trace (str ex)}}))

    [{:exception-type :java.lang.IllegalArgumentException}]
    (assoc ctx :response {:status 400 :body {:message     "Some field is missing or has a typo"
                                             :error-trace (str ex)}})

    :else
    (assoc ctx :io.pedestal.interceptor.chain/error ex)))
