
(ns lawsuitninja.homepage
  (:require   [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
             [lawsuitninja.components :refer [navbar carousel alt-picker]]
             [lawsuitninja.firstinterview :refer [firstinterview]])
    (:require-macros
     [lawsuitninja.mismacros :as mymacros]))

(defn tipperary []
  [:div.form-centered

   [:h5 "1. Write your petition."]
   [:p "We'll help guide you through the court form, for free."]
   [:img {:src "/img/write13.png" :class "img-responsive center-block" :width "10%"}]
   [:br]

   [:h5 "2. File your petition with the court."]
   [:p "You can do this yourself, or we can do it for you."]
   [:img {:src "/img/public6.png" :class "img-responsive center-block" :width "10%"}]
   [:br]

   [:h5 "3. Have a process server give the right papers to your landlord."]
   [:p "We can take care of that for you."]
   [:img {:src "/img/package36.png" :class "img-responsive center-block" :width "10%"}]
   [:br]])



(defn home-page []
  [:div
   [:div.splash-image
    [:h1.splash-title.form-centered [:i "Welcome to the Dojo"]]]

   [:div.splash-content
    [:div.page-header
     [:h1.form-centered "Lawsuit Ninja"]
     [:p.form-centered [:i "Start your lawsuit online, without a lawyer"]]

     [navbar]]

    [:div.form-centered   
     [:p ""]
     [:p "Do you need to kick some legal booty?"]
     [:p [:b "You can represent yourself in small claims court!"]]
     [:p "Starting a lawsuit is a 3-step process."]]
    
    [:div
     [:br]
;     [tipperary]
     [carousel]
     [:br]]

   [:div.form-centered
    [:div (mymacros/huge-danger-button firstinterview "Get Started")]
    [:p ""]]]])

