(ns lawsuitninja.personal-profile
  (:require
   [goog.events :as events]
   [goog.history.EventType :as EventType]
   [goog.dom :as dom]
   [goog.object :as gobj]
   [goog.string :as gstring]
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   [secretary.core :as secretary :include-macros true]
   [cljs-http.client :as http]
   [re-frame.core :refer [register-handler
                          path
                          register-sub
                          dispatch
                          dispatch-sync
                          subscribe]]
   [lawsuitninja.components :refer [navbar
                                    medium-text-box
                                    tool-tipper
                                    popover
                                    save-button
                                    password-switch-buttons
                                    wrap-as-element-in-form
                                    password-form
                                    delete-button
                                    second-password-form]]
   [cljs.core.async :as async :refer [put! <! >! chan]])
  (:require-macros [reagent.ratom :refer [reaction]]
                   [cljs.core.async.macros :refer [go]]))


(defn get-position []
  (let [out (chan)
        geo (.-geolocation js/navigator)]
    (.getCurrentPosition geo (fn [pos] (put! out pos)))
    out))

(defn map-creator []
  (go 
    (let [coords    (.-coords (<! (get-position)))
          lat  (.-latitude coords)
          lon (.-longitude coords)
          center (google.maps.LatLng. lat, lon)
          map-opts (clj->js {:center center
                             :zoom 12})
          map-canvas  (.getElementById js/document "map-canvas")
          mappy    (js/google.maps.Map. map-canvas map-opts)
          request (clj->js {:location center :radius 15000 :types ["courthouse" "municipal court"]})
          service (google.maps.places.PlacesService. mappy)
          marker (google.maps.Marker. (clj->js {:position center
                                                :title "You're somewhere around here"}))
          bounds (google.maps.LatLngBounds.)
          info-window (google.maps.InfoWindow.
                       (clj->js {:content "You're somewhere around here."}))]
      (.setMap marker mappy)
      (google.maps.event.addListener marker "mouseover" 
                                     (fn [] (.open
                                             info-window mappy marker)))

      (.nearbySearch service request #(let [result (clj->js %)]
                                        (loop [[first & rest] result]
                                          (if (some? first)
                                            (do
                                              (let [lat (goog.object.getValueByKeys first "geometry" "location" "G")
                                                    lon (goog.object.getValueByKeys first "geometry" "location" "K")
                                                    vicinity (.-vicinity first)
                                                    name (.-name first)
                                                    marco (google.maps.Marker. 
                                                                    (clj->js
                                                                     {:position 
                                                                      (google.maps.LatLng.
                                                                       lat lon)
                                                                      :title name
                                                                      :animation google.maps.Animation.DROP}))
                                                    info-window (google.maps.InfoWindow.
                                                                 (clj->js {:content 
                                                                           (str "<div> <h6>" name "</h6>"
                                                                                "<p>" vicinity "</p> </div>")}))]
                                               (.setMap marco mappy)
                                               (.extend bounds (.getPosition marco))
;                                               (js/console.log (pr-str (js->clj first)))
                                                (google.maps.event.addListener marco "click" 
                                                                               (fn [] (.open
                                                                                       info-window mappy marco))))
                                              (recur rest))
                                            (do
                                              (.fitBounds mappy bounds)
                                              (js/console.log "All done!")))))))))


(defn map-div []
  [:div
   [:div#map-canvas]])

(defn ^:export nav-tab-on-event []
    (-> (js/$ js/document)
        (.on "shown.bs.tab"
             "a[href='#maps']"
             (fn [e] (map-creator)))))

(defn google-map []
  (reagent/create-class {:reagent-render map-div
                         :component-will-mount nav-tab-on-event}))

(defn profile-page []
  (let [legalname (subscribe [:yourname])
        email (subscribe [:your-email])
        address (subscribe [:your-address])
        phone (subscribe [:your-phone])
        password (atom "New password")
	second-password (atom "confirm password")
        tester (atom false)]
    [:div
     [:div.page-header
      [navbar]]
    [:div.centered-tabs
     [:ul {:class "nav nav-tabs" :role "tablist"}
      [:li {:role "presentation" :class "active"} [:a {:href "#home" :aria-controls "home" :role "tab" :data-toggle "tab"} "Your Profile"]]
      [:li {:role "presentation"} [:a {:href "#profile" :aria-controls "profile" :role "tab" :data-toggle "tab"} "Change Your Profile"]]
      [:li {:role "presentation"} [:a {:href "#password" :aria-controls "password" :role "tab" :data-toggle "tab"} "Change Your Password"]]
      [:li {:role "presentation"} [:a {:href "#maps" :aria-controls "maps" :role "tab" :data-toggle "tab"} "Nearby Courthouses"]]
      [:li {:role "presentation"} [:a {:href "#delete" :aria-controls "delete" :role "tab" :data-toggle "tab"} "Delete Your Account"]]]
     [:div {:class "tab-content"}
      [:div {:role "tabpanel" :class "tab-pane active fade in" :id "home"}
       [:div.parent-container
        [:div.child-column
         [:p [:i "Legal name "]]
         [:p [:i "Your email "]]
         [:p [:i "Your address "]]
         [:p [:i "Your phone number "]]]
        [:div.child-column
         [:p @legalname]
         [:p @email]
         [:p @address]
         [:p @phone]]]]
      [:div {:role "tabpanel" :class "tab-pane fade" :id "profile"} 
       [:div.parent-container
        [:div.child-column
         [:p "Your legal name: " 
          [popover "You can change this if it's different from the name you gave us."]
          [medium-text-box :yourname :enter-yourname]]
         [:p "Your email:"[medium-text-box :your-email :enter-your-email]]]

        [:div.child-column
         [:p "Your address: "[medium-text-box :your-address :enter-your-address]]
         [:p "Your phone number:"[medium-text-box :your-phone :enter-your-phone]]]]
        [save-button]]

      [:div {:role "tabpanel" :class "tab-pane fade" :id "password"} 
       [wrap-as-element-in-form [password-form password]]
       [wrap-as-element-in-form [second-password-form second-password]]
       [password-switch-buttons password second-password]]

      [:div {:role "tabpanel" :class "tab-pane fade" :id "maps"} 
       [google-map]]
 
      [:div {:role "tabpanel" :class "tab-pane fade" :id "delete"} 
       [:div
        [:h5 "Farewell, dear friend."]
        [:p "Even if you're not using us at the moment, it's fine to leave your account up."]
        [:p "That way we're here when you need us."]
        [:p "But if you'd really like to burn it all, please enter your password to confirm."]
        [wrap-as-element-in-form [password-form password]]

        [delete-button password]]]

]]]))

(secretary/defroute "/profile-page" []
  (session/put! :current-page #'profile-page))
