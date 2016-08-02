(ns lawsuitninja.core
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]
            [re-frame.core :refer [register-handler
                                   path
                                   register-sub
                                   dispatch
                                   dispatch-sync
                                   subscribe]]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [cljsjs.react :as react]
            [lawsuitninja.homepage :refer [home-page]]
            [lawsuitninja.payup :refer [payup]]
            [lawsuitninja.firstform :refer [firstform]]
            [lawsuitninja.contact :refer [contact-info]]
            [lawsuitninja.about :refer [about-page]]
            [lawsuitninja.calculator :refer [calculator-page]]
            [lawsuitninja.login :refer [login-page]]
            [lawsuitninja.register-page :refer [register-page]]
            [lawsuitninja.reset-password :refer [reset-password]]
            [lawsuitninja.personal-profile :refer [profile-page]]
            [lawsuitninja.sub]
            [lawsuitninja.court-map :refer [plain-map]]
            [lawsuitninja.components :refer [tool-tipper]]
            [lawsuitninja.db :refer [default-value]]
           [lawsuitninja.handlers]) ;; This namespace must always be required. Else Google Closure will drop it and the app won't run.
  (:import goog.History))


;; -------------------------
;; Views

(declare top-panel)

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :current-page #'top-panel))

(secretary/defroute "/firstform" []
  (session/put! :current-page #'firstform))

(secretary/defroute "/payup" []
  (session/put! :current-page #'payup))

(secretary/defroute "/contact-info" []
  (session/put! :current-page #'contact-info))

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))


(register-handler                 ;; setup initial state
  :initialise-db                     ;; usage:  (submit [:initialize-db])
  (fn
    [db _]
    (merge db default-value)))

(register-sub       ;; we can check if there is data
  :initialised?     ;; usage (subscribe [:initialised?])
  (fn [db]
    (reaction (seq @db))))   ;; do we have data

(defn top-panel
  "Checks to see if the data is ready. If not, returns a waiting page.
  If it is, loads the home-page."
  []
  (let [ready?  (subscribe [:initialised?])]
    (fn []
      (if-not @ready?         ;; do we have good data?
        [:div
         [:div.spinner 
         [:div.double-bounce1]
         [:div.double-bounce2]]
         [:p.form-centered "Initialising app....one moment please"]]   ;; tell them we are working on it
        [home-page]))))      ;; all good, render this component


;; -------------------------
;; Initialize app


(defn ^:export mount-root     ;; call this to bootstrap your app
  []
  (dispatch [:initialise-db])
  (reagent/render [current-page]
                  (js/document.getElementById "app")))

(defn init! []
  (hook-browser-navigation!)
  (mount-root))
