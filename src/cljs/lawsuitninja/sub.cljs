(ns lawsuitninja.sub
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
            [clairvoyant.core :as trace :include-macros true]))




(register-sub
 :first-name
 (fn [db]
   (reaction (:first-name @db))))

(register-sub
 :last-name
 (fn [db]
   (reaction (:last-name @db))))

(register-sub
   :logged-in?
   (fn [db]
     (reaction (:logged-in? @db))))

(register-sub
   :date-moved-out
   (fn [db]
     (reaction (:date-moved-out @db))))

(register-sub
   :your-email
   (fn [db]
     (reaction (:your-email @db))))

(register-sub
   :contact
   (fn [db]
     (reaction (:contact @db))))

(register-sub
   :yourname
   (fn [db]
     (reaction (:yourname @db))))
(register-sub
   :landlordname
   (fn [db]
     (reaction (:landlordname @db))))
(register-sub
   :landlord-address
   (fn [db]
     (reaction (:landlord-address @db))))
(register-sub
   :your-signature
   (fn [db]
     (reaction (:your-signature @db))))
(register-sub
   :your-printed-name
   (fn [db]
     (reaction (:your-printed-name @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :your-address             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:your-address @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :city-state             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:city-state @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :your-phone             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:your-phone @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :the-facts             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:the-facts @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :empty-precinct             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:empty-precinct @db))))   ;; pulls out :name
(register-sub        ;; a new subscription handler
   :empty-place             ;; usage (subscribe [:name])
   (fn [db]
     (reaction (:empty-place @db))))   ;; pulls out :name
(register-sub
 :your-county
 (fn [db]
   (reaction (:your-county @db))))
(register-sub
   :deposit-not-returned
   (fn [db]
     (reaction (:deposit-not-returned @db))))

(register-sub
 :app-db
 (fn [db]
   db))
