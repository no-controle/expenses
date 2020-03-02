(ns expenses.http.http-in
  (:require [expenses.adapter.purchases :as adapter.purchases]
            [expenses.controller.purchases :as controller.purchases]))

(defn get-purchases-by-period
  [{:keys [query-params db]}]
  {:status 200
   :body   (-> query-params
               (controller.purchases/get-by-period db))})

(defn create-purchase
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchase
               (controller.purchases/create-purchase db))})

(defn get-purchases-summary-by-period
  [{:keys [query-params db]}]
  {:status 200
   :body   (controller.purchases/get-summary-by-period query-params db)})

(defn create-purchases-batch
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchases-list
               (controller.purchases/create-purchases-list db))})

