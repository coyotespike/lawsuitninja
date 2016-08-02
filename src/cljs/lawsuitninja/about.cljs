(ns lawsuitninja.about
  (:require 
   [secretary.core :as secretary :include-macros true]
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   [cljsjs.react :as react]
   [goog.events :as events]
   [clojure.string :as s]
   [goog.history.EventType :as EventType]
   [goog.dom :as dom]
   [cljsjs.react :as react]
   [dommy.core :refer-macros [sel sel1]]
   [re-frame.core :refer [register-handler
                          path
                          register-sub
                          dispatch
                          dispatch-sync
                          subscribe]]
   [lawsuitninja.components :refer [navbar typeahead beeminder-graph]])
  (:import goog.History)
  (:require-macros [reagent.ratom :refer [reaction]]
                   [lawsuitninja.mismacros :as mymacros]))


(defn about-page []
  [:div
  [:div.page-header
   [navbar]]
  [:div.form-centered 
   [:h6 "About and Frequently Asked Questions"]]
;   [:p [:a {:href "#/bubbler"} "Bubbler"]]
  [:ol
   [:a {:href "#whofor"}    [:li "Who is this website for?"]]
   [:a {:href "#needlawyer"} [:li "Do I need a lawyer to start a lawsuit?"]]
   [:a {:href "#mylawyers"}  [:li "Are you my lawyers?"]]
   [:a {:href "#shouldhire"} [:li "Should I hire a lawyer?"]]
   [:a {:href "#shouldstart"}[:li "Should I start a lawsuit?"]]
   [:a {:href "#pdf-form"}   [:li "How does the court form work?"]]
   [:a {:href "#moreinfo"}   [:li "I still don't understand this law stuff."]]
   [:a {:href "#beeminder"}  [:li "How would I keep up with your progress?"]]]
  [:ol.eighty-char
   [:li [:a {:name "whofor"}] [:b "Who is this website for?"]]
   [:p "Right now, LawsuitNinja is focused on people in Texas - let us know where you're from
       if you'd like information specific to your state (use the contact button above). We'll expand soon!"]
   [:p "In addition, you need to be at least 18 years old to file a lawsuit."]
   [:li [:a {:name "needlawyer"}] [:b "Do I need a lawyer to start a lawsuit?"]]
   [:p "No! You're allowed to represent yourself in almost any court in the United States,
whether federal or state. This is called proceeding" [:i " pro se,"] " and it's very common."]
   [:li [:a {:name "mylawyers"}] [:b "Are you my lawyers?"]]
   [:p "Nope, sorry. We built this site because legal information is hard to find online,
and we wanted to make it easier. But we are providing legal information, not legal
advice. If you need legal advice, you should talk to an attorney. This website is not
a substitute for an attorney."]
   [:li  [:a {:name "shouldhire"}] [:b "Should I hire a lawyer?"]]
   [:p "This is another issue you have to decide for yourself. We can't do it for you.
But here's some background information which most people don't know:"]
   [:p "Most lawsuits never reach trial. Instead, the parties and their attorneys
reach a settlement - they cut a deal and drop the lawsuit." ]
   [:p "Even attorneys who have
been practicing for several years may not have gone to trial! This means that if you
start a lawsuit, you might be able to negotiate the settlement. And starting the lawsuit
is not that hard, and can save you some money."] 
   [:p "At some point in the life of your
lawsuit, though, you will probably be better off with an attorney. If you don't settle
upfront, a lawyer can help you negotiate a better settlement, and guide you through the
case."]
   
   [:li  [:a {:name "shouldstart"}] [:b "Should I start a lawsuit?"]]
   [:p "Starting a lawsuit should never be your first choice - or your second."] 
   [:p "Your first step should always be to start a conversation with the person with whom you're having problems, and maybe send a letter of demand if you have to."]
   [:p "But sometimes, you might need to. For instance, if someone has demanded a lot of
money you don't think you owe, you have three choices."] 
   [:ul
    [:li [:p "You can pay up."]]
    [:li [:p "You can refuse to pay and watch your credit take a dive."]]
    [:li [:p "Or you can go to court."]]]
   [:p" That's your third choice, but it might be your best choice. Only you can decide if and when to
go to court."]

   [:li  [:a {:name "pdf-form"}] [:b "How does the court form work?"]]
   [:p "Many courts require a very simple form to begin a lawsuit. This is often
true of small claims courts, justice of the peace courts, and some state district 
courts. Some also require a cover sheet."]
   [:p [:i "If the clerk of the court does not accept the form we provide you,
ask for the form the court requires. Send us a picture of the form - we will 
fill it out for you and send it back to you within 24 hours."]]
   [:li  [:a {:name "moreinfo"}] [:b "I still don't understand this law stuff."]]
   [:p "Seriously, drop us a line with the legal information you're looking for.
We can't give you legal advice, but we'll add some relevant information within
 24 hours, or send you $5."]
   [:li [:a {:name "beeminder"}] [:b "How would I keep up with your progress?"]]
   [:p "Because we're trying to make this site useful, we try to add one improvement every day."]
   [:p "We track our daily improvement on Beeminder. Here's our graph!"]
   [beeminder-graph]
   [:br]
   [:p "If we slip up, we have to pay Beeminder money. If you see our graph go bad 
and message us, we'll give you the same amount we give Beeminder. :-)" ]]])


(secretary/defroute "/about-page" []
  (session/put! :current-page #'about-page))
