(ns lawsuitninja.personaldetails
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]

              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [cljs.reader :as reader]
              [re-frame.core :refer [register-handler
                                     path
                                     register-sub
                                     dispatch
                                     dispatch-sync
                                     subscribe]]
              [lawsuitninja.db :refer [update-us]]
              [lawsuitninja.components :refer [navbar
                                               paginator
                                               save-button
                                               switch-buttons
                                               medium-text-box]]
              [lawsuitninja.login :refer [login-page]])
    (:import goog.History)
    (:require-macros
     [lawsuitninja.mismacros :as mymacros]
     [reagent.ratom :refer [reaction]]))


(defn personal-details []
  (let [logged-in? (subscribe [:logged-in?])]
  [:div
   [:div.page-header.form-centered
    [navbar]
    [paginator]]

   [:div.form-centered
    [:h5 "Well done, brave litigant! You've made your (possibly first) legal argument!"]
    [:p "That was the  hard part - now the court needs some details about you and your landlord."]

    [:div.parent-container
     [:div.child-column
      [:p "Your email:"[medium-text-box :your-email :enter-your-email]]
      [:p "Your address: "[medium-text-box :your-address :enter-your-address]]
      [:p "Your phone number:"[medium-text-box :your-phone :enter-your-phone]]]
     [:div.child-column
      [:p "Your landlord's address:" [medium-text-box :landlord-address :enter-landlord-address]]
      [:p "Your landlord's name:" [medium-text-box :landlordname :enter-landlordname]]]]
    [:div [:a {:href "#/firstform"} "Go to the court form!"]
     [:br]
     [:br]
     [switch-buttons logged-in? [save-button][:div (mymacros/info-button login-page "Log In")]]
     [:br]
     [:br]]]]))

(secretary/defroute "/personal-details" []
  (session/put! :current-page #'personal-details))