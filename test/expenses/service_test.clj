(ns expenses.service-test
  (:require [midje.sweet :refer :all]
            [io.pedestal.test :refer :all]
            [io.pedestal.http :as bootstrap]
            [expenses.server :as server]))

(def service
  (::bootstrap/service-fn (bootstrap/create-servlet server/configuration)))

(facts "true" 
  (fact "true" 
    (= 1 1) => true))

;(fact "home page"
;  (:body (response-for service :get "/")) => "Hello World!"
;  (:headers (response-for service :get "/")) =>
;       {"Content-Type" "text/html;charset=UTF-8"
;        "Strict-Transport-Security" "max-age=31536000; includeSubdomains"
;        "X-Frame-Options" "DENY"
;        "X-Content-Type-Options" "nosniff"
;        "X-XSS-Protection" "1; mode=block"
;        "X-Download-Options" "noopen"
;        "X-Permitted-Cross-Domain-Policies" "none"
;        "Content-Security-Policy" "object-src 'none'; script-src 'unsafe-inline' 'unsafe-eval' 'strict-dynamic' https: http:;"})
