(ns expenses.server
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [expenses.service :as service]))

(def configuration {:env                 :prod
                    ::http/routes        service/routes
                    ::http/resource-path "/public"
                    ::http/type          :jetty
                    ::http/port          8890})

(defonce runnable-service (http/create-server configuration))

(defn -main
  "The entry-point for 'lein run'"
  [& _]
  (println "\nCreating your server...")
  (http/start runnable-service))
