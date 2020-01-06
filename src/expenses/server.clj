(ns expenses.server
  (:gen-class)
  (:require [io.pedestal.http :as http]
            [io.pedestal.http.route :as route]
            [expenses.service :as service]))

(def configuration {:env                 :prod
                    ::http/routes        service/routes
                    ::http/resource-path "/public"
                    ::http/type          :jetty
                    ::http/port          8890})

(defonce runnable-service (http/create-server configuration))

(defn run-dev
  "The entry-point for 'lein run-dev'"
  [& args]
  (println "\nCreating your [DEV] server...")
  (-> configuration
      (merge {:env                   :dev
              ::http/join?           false
              ::http/routes          #(route/expand-routes (deref #'service/routes))
              ::http/allowed-origins {:creds true :allowed-origins (constantly true)}
              ::http/secure-headers  {:content-security-policy-settings {:object-src "'none'"}}})
      http/default-interceptors
      http/dev-interceptors
      http/create-server
      http/start))

(defn -main
  "The entry-point for 'lein run'"
  [& args]
  (println "\nCreating your server...")
  (http/start runnable-service))
