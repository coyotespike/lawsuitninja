(ns lawsuitninja.templates
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [include-js include-css]]
            [clojure.java.io :as io]
            [ring.middleware.anti-forgery :refer [*anti-forgery-token*]]
            [net.cgrand.enlive-html :refer [deftemplate 
                                            set-attr 
                                            html-resource
                                            defsnippet
                                            at
                                            emit*]])
  (import java.io.StringReader))


;; (def e-parsed (html-resource "templates/homepage.html"))


;; (defn home-page [token]
;;   (let [replaced (at e-parsed [:#anti-forgery-token] (set-attr :value token))]
;;     (apply str (emit* replaced))))

(defn home-page [token]
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "css/bootstrap.min.css")
     (include-css "css/flat-ui-pro.css")]
    [:body
     [:div#app]
     [:div {:id "anti-forgery-token" :value token}]
     (include-js                  
      "/js/jquery.min.js"
      "/js/flat-ui-pro.min.js"
      "/js/zxcvbn.async.js"

      ;;; These were helpful for the bubbletree data visualization.
      ;; "/js/bubbletree/lib/jquery-1.5.2.min.js"
      ;; "/js/bubbletree/lib/jquery.history.js"
      ;; "/js/bubbletree/lib/raphael.js"
      ;; "/js/bubbletree/lib/Tween.js"
      ;; "js/bubbletemp.js"

;      "/js/bubbletree/lib/vis4.js"
;      "/js/bubbletree/build/bubbletree.js"

      "https://maps.googleapis.com/maps/api/js?key=AIzaSyAVdSLmg2FE0VX2_oaWGbLy-xvmOQl3QtE&libraries=places,geometry&amp;sensor=false&amp;"
      "https://www.google.com/jsapi"

      "/js/app.js")]]))


(def error-page
  (html
   [:html
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1"}]
     (include-css "css/vendor/bootstrap.min.css")
     (include-css "css/flat-ui-pro.css")]

    [:body.error-backgrounder ;{:background "/img/congruent_pentagon.png"}
     [:div.leftcol 
      [:h3 "Sorry, that page can't be found!"]
      [:h5 "Drop us a note and we'll add some helpful resources soon!"]]]]))
