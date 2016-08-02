(ns lawsuitninja.firstform
  (:require-macros [reagent.ratom :refer [reaction]])
      (:require 
       [secretary.core :as secretary :include-macros true]
       [reagent.session :as session]
       [reagent.core :as reagent :refer [atom]]
       [clojure.string :refer [blank? join split]]
       [no.en.core :refer [url-encode url-decode]]
       [re-frame.core :refer [register-handler
                              path
                              register-sub
                              dispatch
                              dispatch-sync
                              subscribe]]
       [lawsuitninja.db :refer [update-us]]
       [lawsuitninja.components :refer [atom-input
                                        wide-text-box
                                        tool-tipper
                                        small-text-box
                                        save-button
                                        text-area-input]]))


(defn- encode-val [k v]
  (str (url-encode (name k)) "=" (url-encode (str v))))

(defn- encode-vals [k vs]
  (->>
    vs
    (map #(encode-val k %))
    (join "&")))

(defn- encode-param [[k v]]
  (if (coll? v)
    (encode-vals k v)
    (encode-val k v)))

(defn generate-query-string [params]
  (->>
    params
    (map encode-param)
    (join "&")))

(defn pdfer []
  (let [username (subscribe [:your-email])]
  [:a {:href (str "/make-pdf?" (generate-query-string {:username @username}))
       :class "btn btn-primary"
       :target "_blank"} "Make My Form!"]))

(defn pdfer2 []
  [:a {:href "/make-pdf2"
       :class "btn btn-primary"
       :target "_blank"} "Expunction Petition"])
    
        

(defn header []
  [:div
   [:div#container
    [:h6#right-sibling [:b "NO. _________________"]
    [tool-tipper "#/about-page" "Review all your information, and then hit 'Save' and 'Make My PDF' at the bottom. For more information, please see question 6 on our FAQ."]]]
   [:table.center
    [:tr
     [:td [wide-text-box :yourname]]
     [:td ")"]
     [:td.form-center-children "IN THE JUSTICE COURT"]]
    [:tr
     [:td.form-center-children "PLAINTIFF,"]
     [:td ")"]
     [:td ""]]
    [:tr
     [:td ""]
     [:td ")"]
     [:td ""]]
    [:tr
     [:td.form-center-children "vs."]
     [:td ")"]
     [:td.form-center-children 
      "PRECINCT" [small-text-box :empty-precinct :enter-empty-precinct]
      ", PLACE" [small-text-box :empty-place :enter-empty-place]]]
    [:tr
     [:td ""]
     [:td ")"]
     [:td ""]]
    [:tr
     [:td [wide-text-box :landlordname :enter-landlordname]]
     [:td ")"]
     [:td.form-center-children [atom-input :your-county :enter-your-county] "COUNTY, TEXAS"]]
    [:tr
     [:td.form-center-children "DEFENDANT."]
     [:td ")"]
     [:td ""]]]])

(defn factsAndcauses []
  [:div
   [:p.form-center-children [:b "IV. FACTS AND CAUSES OF ACTION"]]
   [:ol {:start 5}
    [:li "Plaintiff is a tenant and leases premises from Defendant."]
    [:div
     [:div#left-sibling.form-help-tip [:p "Don't use legalese. Just describe exactly what your landlord did, or failed to do."]]
     [:li "The following events occurred in violation of the law: "]]
     [text-area-input :the-facts :enter-the-facts]]])



(defn firstform []
  [:div.form-body
   [header]
   [:p.form-center-children [:strong [:ins "PLAINTIFF'S ORIGINAL PETITION"]]]
   [:p "TO THE HONORABLE JUDGE OF THE COURT:"]
   [:p "     Plaintiff files this original petition in the the above-styled and numbered cause, and in support, shows the Court as follows:"]

   [:p.form-center-children [:b "I. DISCOVERY"]]
   [:ol
    [:li "Plaintiff intends to conduct discovery under Level 1."]]
    [:p.form-center-children [:b "II. PARTIES"]]
   [:ol {:start 2}

    [:li 
     [atom-input :yourname :enter-yourname]
     ", Plaintiff, is a resident of the county of this Court, within Texas."]

    [:li 
     [atom-input :landlordname :enter-landlordname]
     ", Defendant, may be served at the following address:" 
     [atom-input :landlord-address :enter-landlord-address]]]

   [:p.form-center-children [:b "III. JURISDICTION AND VENUE"]]
   [:ol {:start 4}
    [:li "The amount in controversy is within the jurisdictional limits of of this Court, and venue is proper as the cause of action arose in the county of this Court within Texas."]]
   [factsAndcauses]
   [:p.form-center-children [:b "V. REQUEST FOR RELIEF"]]
   [:p "Plaintiff requests this Court grant the following relief: "]
   [:ol {:type "a"}
    [:li "actual damages;"]
    [:li "civil penalties if available under law;"]
    [:li "court costs; and"]
    [:li "any other relief to which Plaintiff is entitled."]]


   [:div.form-right 
    [:p"Respectfully submitted,"] 
    [atom-input :your-signature :enter-your-signature]
    [atom-input :your-county :enter-your-county]
    [atom-input :yourname :enter-yourname]
    [atom-input :your-address :enter-your-address]
    [atom-input :city-state :enter-city-state]
    [atom-input :your-phone :enter-your-phone]]

   [:div.form-center-children
    [save-button]
    [:br]
    [:br]
;    [pdfer]
    [pdfer2]
    [:br]
    [:br]
    [:br]]])
