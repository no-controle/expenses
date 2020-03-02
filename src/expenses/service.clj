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
        {:get http-in/get-purchases-by-period}
        {:post http-in/create-purchase}
        ["/summary"
         ["/by-category"
          {:get http-in/get-purchases-summary-by-period}]
         ["/by-title"
          {:get http-in/get-purchases-summary-by-title}]]
        ["/batch"
         {:post http-in/create-purchases-batch}]]]]]])
