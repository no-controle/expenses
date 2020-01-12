(ns expenses.http.http-in
  (:require [expenses.controller.purchases :as controller.purchases]
            [expenses.adapter.purchases :as adapter.purchases]))

(defn create-purchase
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchase
               (controller.purchases/create-purchase db))})

(defn create-purchases-batch
  [{:keys [json-params db]}]
  {:status 200
   :body (-> json-params
             adapter.purchases/http-in->purchases-list
             (controller.purchases/create-purchases-list db))})

