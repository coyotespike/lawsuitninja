(ns lawsuitninja.login
  (:require   [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [cljs.reader :as reader]
              [reagent-modals.modals :as reagent-modals]
              [re-frame.core :refer [register-handler
                                     path
                                     register-sub
                                     dispatch
                                     dispatch-sync
                                     subscribe]]
              [lawsuitninja.components :refer [login-button
                                               back-button
                                               wait-button
                                               wrap-as-element-in-form
                                               email-form
                                               password-form
                                               switch-buttons2
                                               login-switch-buttons
                                               navbar]])
    (:require-macros
     [lawsuitninja.mismacros :as mymacros]
     [reagent.ratom :refer [reaction]]))


(defn daniel-san []
  (fn []
    [:div
     [:h2.burnt-orange-font "You're logged in! Keep going, Daniel-san!"]
     [back-button]
     [:br]
     [:br]]))

(defn modal-window-button []
  [:div.btn.btn-primary {:on-click #(reagent-modals/modal! [:div "some message to the user!"]
                                                           {:size :sm})} 
   "My Modal"])



(defn welcome-to-my-castle []
  (let [email-address (subscribe [:your-email])
        password (atom "Secr3t P@ssword!")
        forgot-password? (atom false)]
        (fn []
          [:div
          [:div.burnt-orange-font
           [:h2 "Come right in."]
           [:div.form-centered.yellow-font
            [wrap-as-element-in-form [email-form email-address]]
            [:br]
            [wrap-as-element-in-form [password-form password]]

            (when @forgot-password?
              [:a.yellow-font {:href "#/reset-password"} "Need to reset your password?"])
            [:br]
            [:br]
            [login-switch-buttons password email-address forgot-password?]
            [:br]
            [:br]]]])))

(defn login-page []
  (let [logged-in? (subscribe [:logged-in?])]
    [:div.login-backgrounder.hide-overflow
     [:div.page-header
      [navbar]]
     [:div.form-centered
      [switch-buttons2 logged-in? [welcome-to-my-castle] [daniel-san]]]]))


(secretary/defroute "/login-page" []
  (session/put! :current-page #'login-page))

