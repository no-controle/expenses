(ns expenses.http.http-in
  (:require [expenses.controller.purchases :as controller.purchases])
  (:import (com.mongodb DuplicateKeyException)))

(defn create-purchase
  [{:keys [json-params db]}]
  (try {:status 200
        :body   (controller.purchases/create-purchase json-params db)}
       (catch DuplicateKeyException e
         {:status 400
          :body   {:error (str e)}})))
