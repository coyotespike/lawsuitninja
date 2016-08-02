(ns lawsuitninja.firstinterview
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [cljs.reader :as reader]

              [lawsuitninja.depositwitheld :refer [left-property]]
              [lawsuitninja.components :refer [atom-input
                                               small-text-box
                                               wide-text-box
                                               medium-text-box
                                               disabled-button
                                               text-area-input
                                               navbar
                                               paginator
                                               tool-tipper]]
              [re-frame.core :refer [register-handler
                                     path
                                     register-sub
                                     dispatch
                                     dispatch-sync
                                     subscribe]])

    (:import goog.History)
    (:require-macros
     [lawsuitninja.mismacros :as mymacros]
     [reagent.ratom :refer [reaction]]))


(declare nowhere)

(defn name-page []
  [:div.vertical-child
   [:h4 "Please enter your first and last name."]])

(defn landlordname-page []
  [:div.vertical-child
   [:p "What's your landlord's name?"]
   [:p "This is the name of the person or company who owns the property, not the property manager. The name of this person or company is the one who has withheld your deposit."]
   [atom-input :landlordname]
   [:div (mymacros/info-button nowhere "Next step")]])


(defn address-page []
  [:div.vertical-child
   [:p "What is the address of the property you're staying at?"]
   [atom-input :your-address]
   [atom-input :city-state]
   [:div (mymacros/info-button landlordname-page "Next step")]])



(defn cause-page []
  [:div
   [:div.page-header
  [navbar]]
   [:div.form-centered
    [:h4 "Let's get started!"]
    [:h6 "Choose everything that looks like a problem worth going to court over."]
    [:p [:i "The gray buttons represent issues that aren't ready yet."]]
    [:p [:i "If you need one soon, contact us and we'll prioritize!"]]]

   [:div.grid.grid-pad
    [:div.col-1-3
     [:div (mymacros/primary-button left-property "Security Deposit Witheld")]
     [:br]
     [disabled-button "Failure to Make Repairs"]]

    [:div.col-1-3
     [disabled-button "Assault and/or Battery"]
     [:br]
     [:br]
     [disabled-button "Noisy Neighbor"]]
               
    [:div.col-1-3
     [disabled-button "Deceptive Business Practices"]
     [:br]
     [:br]
     [disabled-button "Application Deposit Withheld"]]]

   [:div.grid.grid-pad
    [:div.col-1-3
     [:br]
     [disabled-button "Occupational Driver's License"]]

    [:div.col-1-3
     [:br]
     [disabled-button "Pretrial Diversion Request"]]
               
    [:div.col-1-3
     [:br]
     [disabled-button "Misclassifed as Contractor"]]]])


(defn firstinterview []
  [:div
   [:div.page-header
  [navbar]]
   [:div.form-centered
       [:h3 "Before we go..."]
    [:p "On this website, you'll be able to start your own lawsuit."
    [:br]
     [:b "However, this website is not a substitute for the advice of an attorney."] 
     
     [:br] 
     "LawsuitNinja only provides legal information, " [:br] 

     "and nothing on this website constitutes legal advice." 
     [:a {:href "http://www.statutes.legis.state.tx.us/Docs/GV/htm/GV.81.htm"
          :target "_blank"}
      [:span {:class "fui-question-circle" 
              :data-toggle "tooltip" 
              :title "Please see Section 81.101 of the Texas Government Code."
              :data-placement "right"}]]

     [:br] [:br]
     "You're responsible for what you write, " [:br]
     "for the legal decisions you make," [:br] 
     "and for the outcome of your case."]

    [:div (mymacros/info-button cause-page "Got it!")]]])



;; -------------------------
;; Routes

(secretary/defroute "/firstinterview" []
  (session/put! :current-page #'firstinterview))

(secretary/defroute "/cause-page" []
  (session/put! :current-page #'cause-page))

(secretary/defroute "/landlordname-page" []
  (session/put! :current-page #'landlordname-page))

(secretary/defroute "/address-page" []
  (session/put! :current-page #'address-page))
