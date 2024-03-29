(ns expenses.interceptors
  (:require [clojure.data.json :as json]
            [io.pedestal.interceptor.error :as error-int]
            [monger.core :as mg]))

(def db-name "expenses-int-test")
;(def db-name "expenses-dev")
;(def db-name "expenses")

(def db-interceptor
  {:name  :database-interceptor
   :enter (fn [context]
            (let [connection (mg/connect)
                  db (mg/get-db connection db-name)]
              (update context :request assoc :db db)))})

(defn id-to-string [response]
  (if (not (nil? (:_id response)))
    (-> response
        (assoc :id (-> response :_id str))
        (dissoc :_id))
    response))

(def to-json-response-interceptor
  {:name  :to-json-response
   :leave (fn [context]
            (update context :response assoc
                    :headers {"Content-Type" "application/json"}
                    :body (json/write-str (-> context
                                              :response
                                              :body
                                              id-to-string))))})

(def service-error-handler
  (error-int/error-dispatch
    [ctx ex]

    [{:exception-type :com.mongodb.DuplicateKeyException}]
    ((assoc ctx :response {:status 400 :body {:message     "Trying to insert existent input"
                                              :error-trace (str ex)}}))

    [{:exception-type :java.lang.IllegalArgumentException}]
    (assoc ctx :response {:status 400 :body {:message     "Some field is missing or has a typo"
                                             :error-trace (str ex)}})

    [{:exception-type :javax.management.InstanceAlreadyExistsException}]
    (assoc ctx :response {:status 403 :body {:message     "Duplicate instance"
                                             :error-trace (str ex)}})

    :else
    (assoc ctx :io.pedestal.interceptor.chain/error ex)))
