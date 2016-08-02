(ns lawsuitninja.register-page
  (:require   [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [cljsjs.react :as react]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [re-frame.core :refer [register-handler
                                     path
                                     register-sub
                                     dispatch
                                     dispatch-sync
                                     subscribe]]
              [lawsuitninja.components :refer [back-button
                                               medium-text-box
                                               email-form
                                               password-form
                                               second-password-form
                                               wrap-as-element-in-form
                                               register-switch-buttons
                                               switch-buttons2
                                               navbar
                                               progress-bar]]
	      [lawsuitninja.login :refer [login-page]])
  (:require-macros
   [lawsuitninja.mismacros :as mymacros]
   [reagent.ratom :refer [reaction]]))



(defn daniel-san []
  (fn []
    [:div.form-centered
     [:h2.burnt-orange-font "Congratulations, you've registered with us!"]
     [back-button]
     [:br]
     [:br]]))


(defn welcome-to-my-castle []
  (let [email-address (subscribe [:your-email])
        password (atom "Secr3t P@ssword!")
	second-password (atom "confirm password")]
    (fn []
      [:div.hide-overflow
       [:div.burnt-orange-font.form-centered
        [:h2 "Register here"]
        [wrap-as-element-in-form [medium-text-box :first-name :enter-first-name]]
        [wrap-as-element-in-form 
         [medium-text-box :last-name :enter-last-name]]
        [:div.form-centered.yellow-font
         [wrap-as-element-in-form [email-form email-address]]
         [wrap-as-element-in-form [password-form password]]
         [wrap-as-element-in-form [second-password-form second-password]]
         [register-switch-buttons password second-password email-address]
         [:br]
         [:br]]]])))


(defn register-page []
  (let [logged-in? (subscribe [:logged-in?])]
    [:div.login-backgrounder
     [:div.page-header
      [navbar]]

     [:div.form-left
      [switch-buttons2 logged-in? [welcome-to-my-castle] [daniel-san]]	
      [:br]]]))


(secretary/defroute "/register-page" []
  (session/put! :current-page #'register-page))
