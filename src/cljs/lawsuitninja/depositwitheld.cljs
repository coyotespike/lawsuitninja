(ns lawsuitninja.depositwitheld

    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [cljsjs.react :as react]
              [cljs.reader :as reader]
              [re-frame.core :refer [subscribe]]
              [lawsuitninja.personaldetails :refer [personal-details]]
              [lawsuitninja.components :refer [atom-input
                                               text-area-input
                                               navbar
                                               paginator
                                               alt-picker
                                               total-days-selected
                                               switch-buttons
                                               blue-swap-button
                                               tool-tipper]])

    (:require-macros
     [lawsuitninja.mismacros :as mymacros]
     [reagent.ratom :refer [reaction]]))


(defn deposit-no [] 
  [:div 
   [:p "You are probably not entitled to
get your deposit back until your lease has ended, 
you have left the property, and you have
given your landlord a forwarding address."]
   [:p "Go ahead and make sure you do all of that before you worry about getting your deposit back."]
   [:p "We'll still be here if you need us. Contact us if you need some information you don't see here."]])

(defn owed-explanation []
  [:div
   [:p "Then your landlord owed you an explanation about why they kept your deposit."]
   [:p "Tell the court what happened, and why your landlord was wrong to keep your money. (Make sure you're logged in, so we can save your work!"]
   [text-area-input :the-facts :enter-the-facts]
   [:div (mymacros/info-button personal-details "Next")]])



(defn no-exp []
  [:div
   [:p "If there was no controversy about how much rent you owed, and your landlord kept your security deposit to pay for that rent, then your landlord did not need to provide a written explanation."]
   [:p "Relax and enjoy your new apartment."]])


(def written-no 
  [:div 
   [:p "Your landlord must return your deposit or give you written reasons
        why it was not returned within 30 days of receiving a forwarding address.
        Since your landlord did neither,
        you can ask the court to make your landlord give the deposit back."]
   [:p "Would you like to say something like this to the court?"]
   [:p "Change this to your own words, then save before proceeding."]
   [text-area-input :deposit-not-returned :enter-deposit-not-returned]
   [:div (mymacros/info-button personal-details "Next")]])

(defn written-yes []
  [:div
   [:p "If you think your landlord's reasons for not returning the deposit
were incorrect or unfair, you can ask the court to make your landlord give you your
deposit back."]
   [:p "Tell the court what happened, and why your landlord was wrong to keep your money. (Make sure you're logged in, so we can save your work!)"]
   [text-area-input :the-facts :enter-the-facts]
   [:div (mymacros/info-button personal-details "Next")]])

(defn no-explanation []
  [:div
   [:p "If you owed your landlord rent at the time you moved out, then the landlord
does not need to provide a written explanation."]])



(defn more-than-30 []
  [:div
   [:p "Great, that's more than the minimum time, so let's keep going."]])

(defn less-than-30 []
  [:div
   [:p "You need to give your landlord at least 30 days after you move out to return your deposit."]
   [:p "After that, make sure you contact them about it. If you're still having troubles, come back here."]
   [:p "Need some information you're not seeing here? Contact us about it! We love to help."]])


   
(defn disagree-rent []
  (let [showme (atom true)
        no-show-me (atom true)]
  [:div
   [:div.page-header.form-centered
    [navbar]
    [paginator]]
   [:div.parent-container
   [:div.wide-child-column
   [:p "Did you and your landlord disagree about how much rent you owed? "
    [tool-tipper "http://www.texaspropertycode.org/chapter-92-texas-property-code.html#92-1031" "Pay attention to 92.104(c)(1)."]]
   [:br]
    [switch-buttons showme [blue-swap-button showme "Yes"] [owed-explanation]]
    [:br]
    [:br]
    [switch-buttons no-show-me [blue-swap-button no-show-me "No"] [no-exp]]]]]))



(defn owe-rent []
  (let [showme (atom true)]
  [:div
   [:div.page-header.form-centered
    [navbar]
    [paginator]]
   [:div.parent-container
   [:div.wide-child-column
    [:p "if you owed rent when you moved out, and you and your landlord both
           know that you owe rent, your landlord did not have to give you your
                                                                 deposit back."]
    [:p "Did you owe any rent when you moved out? "
     [tool-tipper "http://www.texaspropertycode.org/chapter-92-texas-property-code.html#92-1031"
                                              "Pay attention to 92.104(c)(1)."]]
    [:div (mymacros/info-button disagree-rent "Yes")]
    [:br]
    [switch-buttons showme [blue-swap-button showme "No"] [owed-explanation]]]]]))



(defn written-reason []
  (let [showme (atom true)]
  [:div
   [:div.page-header.form-centered
    [navbar]
    [paginator]]
   [:div.parent-container
    [:div.wide-child-column
    [:p "if you've provided a forwarding address, you should have received your
         security deposit in the mail within 30 days. if you didn't receive it,
          you should have received a written response from your landlord, which
           explained why he or she chose to withhold some or all of the deposit."]
           [:br]
    [:p "Did your landlord provide written reasons for refusing to return your deposit?"
   [:a {:href "http://www.texaspropertycode.org/chapter-92-texas-property-code.html#92-104"
         :target "_blank"}
     [:span {:class "fui-question-circle" 
             :data-toggle "tooltip" 
             :title "Ordinarily, your landlord must provide written reasons."
             :data-placement "right"}]]]
    [:div.wide-child-column
    [switch-buttons showme [blue-swap-button showme "Yes"] written-yes]
     [:br]
     [:br]
    [:div (mymacros/info-button owe-rent "No")]]]]]))

(defn cond-switch []
  (fn []
    (cond
      (zero? @total-days-selected) [:div]
      (<= 30 @total-days-selected) [more-than-30]
      (> 30 @total-days-selected) [less-than-30])))


(defn time-limit-page []
  [:div
   [:div.page-header.form-centered
    [navbar]
    [paginator]]
   [:div.parent-container
    [:div.wide-child-column
    [:p "Your security deposit is not due to be returned until the lease has come to an end and you've moved out."] [:br]
    [:p "After you provide a forwarding address, your landlord has 30 days to return your deposit."]
    [:p "When did you provide your landlord with a forwarding address? "
    [:a {:href "http://www.texaspropertycode.org/chapter-92-texas-property-code.html#92-107"
         :target "_blank"}
     [:span {:class "fui-question-circle" 
             :data-toggle "tooltip" 
             :title "Your landlord's 30 days to return the deposit starts only after you give your forwarding address in writing."
             :data-placement "right"}]]]
     [alt-picker]
     [cond-switch]
    [:div (mymacros/info-button written-reason "Next Step")]]]])


(defn left-property []
  (let [showme (atom true)]
    [:div
     [:div.page-header.form-centered
      [paginator]
      [navbar]]

     [:div.parent-container
      [:div.wide-child-column
      [:h7 "First, your security deposit is not due to be returned until the lease has come to an end and you've moved out."] [:br]
      [:p "Have you already left the property (that is, you're not still living there)? "
      [:a {:href "http://www.texaspropertycode.org/chapter-92-texas-property-code.html#92-103"
           :target "_blank"}
       [:span {:class "fui-question-circle" 
               :data-toggle "tooltip" 
               :title "Your landlord has 30 days after you leave the property to return your security deposit."
               :data-placement "right"}]]]
      [:div#container
       [:div (mymacros/turk-button time-limit-page "Yes")]
       [:br]
       [:div
        [switch-buttons showme [blue-swap-button showme "No"] [deposit-no]]]]]]]))




(secretary/defroute "/left-property" []
  (session/put! :current-page #'left-property))

(secretary/defroute "/time-limit-page" []
  (session/put! :current-page #'time-limit-page))

(secretary/defroute "/written-reason" []
  (session/put! :current-page #'written-reason))

(secretary/defroute "/disagree-rent" []
  (session/put! :current-page #'disagree-rent))

(secretary/defroute "/owe-rent" []
  (session/put! :current-page #'owe-rent))
