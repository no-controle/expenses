(ns expenses.service
  (:require [expenses.http.http-in :as http-in]
            [expenses.interceptors :as interceptors]
            [io.pedestal.http.body-params :as body-params]))

(defn home-page
  [_]
  {:status 200
   :body   {:name "Expenses Service"}})

(def routes
  `[[["/" {:get home-page}
      ^:interceptors [(body-params/body-params)
                      interceptors/db-interceptor
                      interceptors/to-json-response-interceptor
                      interceptors/service-error-handler]
      ["/expenses"

       ["/purchase"
        {:get http-in/get-purchases}
        {:post http-in/create-purchase}
        ["/:id" {:delete http-in/refund-purchase}]
        ["/summary" {:get http-in/get-purchases-summary}]
        ["/batch" {:post http-in/create-purchases-batch}]
        ["/variable" {:get http-in/variable-purchases-for-period}]
        ["/other" {:get http-in/other-purchases-for-period}]]

       ["/fixed" {:post http-in/create-fixed-expense}
        ["/:id" {:delete http-in/delete-fixed-expense}]]]

      ["/revenue" {:post http-in/create-revenue}
       ["/:id" {:delete http-in/delete-revenue}]]

      ["/monthly" {:get http-in/monthly-data-for-period}]]]])
