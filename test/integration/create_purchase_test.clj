(ns integration.create-purchase-test
  (:require [clojure.data.json :as json]
            [expenses.server :as server]
            [io.pedestal.http :as bootstrap]
            [io.pedestal.test :refer :all]
            [midje.sweet :refer :all]))

(def spy #(do (println "DEBUG:" %) %))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet server/configuration)))

(defn do-post-request [url body]
  (let [response (response-for service
                               :post url
                               :headers {"Content-Type" "application/json"}
                               :body (json/write-str body))]
    (-> response
        :body
        (json/read-str :key-fn keyword))))

;(fact "Should create a new purchase successfully"
;  (do-post-request "/expenses/purchases" {:date      "2020-01-01"
;                                          :category  "Supermercado"
;                                          :title     "The Grocery Store"
;                                          :amount    18
;                                          :bill-date "2020-01"
;                                          :source    "Bank card"}) => {:message "Purchase created"})
