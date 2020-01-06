(ns expenses.service
  (:require [expenses.interceptors :as interceptors]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]
            [monger.collection :as mc]
            [expenses.http.http-in :as http-in]))

(defn home-page
  [_]
  {:status 200
   :body   {:name "Expenses Service"}})

(def routes
  `[[["/" {:get home-page}
      ^:interceptors [(body-params/body-params)
                      http/html-body
                      interceptors/db-interceptor
                      interceptors/to-json-response-interceptor]
      ["/expenses"
       ["/purchase"
        {:post http-in/create-purchase}]]]]])
