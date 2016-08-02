(ns lawsuitninja.handlers
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe
                                   debug
                                   after]]
            [schema.core :as s    
             :include-macros true]
            [lawsuitninja.db :refer [valid-schema?]]
  [clairvoyant.core :as trace :include-macros true]))


(defn log-ex
  [handler]
  (fn log-ex-handler
    [db v]
    (try
        (handler db v)        ;; call the handler with a wrapping try
        (catch :default e     ;; ooops
          (do
            (.error js/console e.stack)   ;; print a sane stacktrace
            (throw e))))))


(def standard-middlewares (if ^boolean goog.DEBUG
                            (comp log-ex debug (after valid-schema?))))

(register-handler
 :enter-first-name
 standard-middlewares
 (fn [db [_ name]]
   (assoc db :first-name name)))

(register-handler
 :enter-last-name
 standard-middlewares
 (fn [db [_ name]]
   (assoc db :last-name name)))

(register-handler
   :enter-date-moved-out
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :date-moved-out name)))


(register-handler
   :change-logged-in?
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :logged-in? name)))

(register-handler
   :enter-your-email
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-email name)))

(register-handler
   :enter-contact
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :contact name)))

(register-handler
   :enter-yourname
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :yourname name)))

(register-handler
   :enter-landlordname
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :landlordname name)))

(register-handler
   :enter-landlord-address
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :landlord-address name)))

(register-handler
   :enter-your-signature
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-signature name)))

(register-handler
   :enter-your-printed-name
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-printed-name name)))

(register-handler
   :enter-your-address
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-address name)))

(register-handler
   :enter-city-state
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :city-state name)))

(register-handler
   :enter-your-phone
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-phone name)))

(register-handler
   :enter-the-facts
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :the-facts name)))


(register-handler
   :enter-empty-precinct
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :empty-precinct name)))
(register-handler
   :enter-empty-place
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :empty-place name)))
(register-handler
   :enter-your-county
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :your-county name)))
(register-handler
   :enter-deposit-not-returned
   standard-middlewares
   (fn [db [_ name]]
     (assoc db :deposit-not-returned name)))


(register-handler
 :revamp-db
 standard-middlewares
 (fn [db something]
   (merge db something)))


(register-handler            ;; when the GET succeeds 
  :process-service-1-response    
  standard-middlewares
  (fn
    [db [_ response]]
    (merge db response)))
