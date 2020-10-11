(ns expenses.service
  (:require [expenses.http.http-in :as http-in]
            [expenses.interceptors :as interceptors]
            [io.pedestal.http :as http]
            [io.pedestal.http.body-params :as body-params]))

(defn home-page
  [_]
  {:status 200
   :body   {:name "Expenses Service"}})

(def routes
  `[[["/" {:get home-page}
      ^:interceptors [(body-params/body-params)
                      http/html-body
                      interceptors/db-interceptor
                      interceptors/to-json-response-interceptor
                      interceptors/service-error-handler]
      ["/expenses"

       ["/purchases"
        {:get http-in/get-purchases}
        {:post http-in/create-purchase}
        ["/summary"
         {:get http-in/get-purchases-summary}]
        ["/batch"
         {:post http-in/create-purchases-batch}]]

       ["/fixed"
        {:post http-in/create-fixed-expense}
        ["/:id"
         {:delete http-in/delete-fixed-expense}]]]

      ["/revenue"
       {:post http-in/create-revenue}]]]])
