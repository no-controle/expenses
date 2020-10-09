(ns expenses.http.http-in
  (:require [expenses.adapter.purchases :as adapter.purchases]
            [expenses.controller.purchases :as controller.purchases]
            [expenses.controller.fixed :as controller.fixed]))

(defn get-purchases
  [{:keys [query-params db]}]
  {:status 200
   :body   (controller.purchases/get-by-period (:year query-params) (:month query-params) db)})

(defn create-purchase
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchase
               (controller.purchases/create-purchase db))})

(defn create-fixed-expense
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               (controller.fixed/create-fixed db))})

(defn get-purchases-summary
  [{:keys [query-params db]}]
  {:status 200
   :body   (controller.purchases/get-summary query-params db)})

(defn create-purchases-batch
  [{:keys [json-params db]}]
  {:status 200
   :body   (-> json-params
               adapter.purchases/http-in->purchases-list
               (controller.purchases/create-purchases-list db))})

