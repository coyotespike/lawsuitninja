(ns lawsuitninja.components
  (:require 
   [reagent.core :as reagent :refer [atom]]
   [reagent.session :as session]
   [clojure.string :as s]
   [dommy.core :refer-macros [sel sel1]]
   [dommy.core :refer [hide! show! remove-attr!]]
   [secretary.core :as secretary :include-macros true]
   [re-frame.core :refer [register-handler
                          path
                          register-sub
                          dispatch
                          dispatch-sync
                          subscribe]]
   [reagent-forms.core :refer [bind-fields init-field value-of]]
   [lawsuitninja.pikaday :as pikaday]
   [lawsuitninja.db :refer [pay-up
                            email-us
                            update-us
                            register-user
                            login-user
                            new-password
                            delete-profile
                            email-random-password]]
   [clairvoyant.core :as trace :include-macros true])
  (:require-macros [reagent.ratom :refer [reaction]]
                   [lawsuitninja.mismacros :as mymacros]
                   [reagent-forms.macros :refer [render-element]]))


(defn switch-buttons [atom default alternative]
  (fn []
    (if @atom
      default
      alternative)))

(defn blue-swap-button [showme label]
  (fn []
     [:input {:class "btn btn-info palette-turquoise"
              :type "button"
              :value label
              :on-click #(.-preventDefault (swap! showme not))}]))

;; Without the outer fn, the component refused to render, with error.
;; Invalid Hiccup form, nil.
(defn disabled-button  [displayvalue] 
  [:input {:type "button" 
           :class "btn btn-default.disabled:active" 
           :value displayvalue}])


(defn first-change-button [] 
    (fn []
      [:input {:type "button" 
               :class "btn btn-info" 
               :value "First change"
               :on-click #(dispatch 
                           [:enter-yourname "First change, mes amis"])}]))

(defn second-change-button [] 
    (fn []
      [:input {:type "button" 
               :class "btn btn-info" 
               :value "Second change"
               :on-click #(dispatch 
                           [:enter-yourname "Second change, mis compadres"])}]))


(defn check-button []
    (fn []
      [:input {:type "button" 
               :class "btn btn-info" 
               :value "app-db value"
               :on-click #(js/console.log (pr-str @(subscribe [:app-db])))}]))

(defn delete-button [password]
    (fn []
      [:input {:type "button" 
               :class "btn btn-danger" 
               :value "Delete Profile"
               :on-click #(delete-profile @password)}]))

(defn new-password-button [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-info" 
           :value "Confirm"
           :on-click #(do
                        (email-random-password)
                        (js/history.back))}]))

(defn wait-button [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value "Just do it"
           :on-click #(js/alert "Sorry, we need your email!")}]))

(defn pay-wait [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value "Take our money"
           :on-click #(js/alert "Did you fill everything out?")}]))

(defn save-button [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-info" 
           :value "Save"
           :on-click #(update-us)}]))


(defn email-button [showme] 
  (fn []
    [:input {:type "button" 
             :class "btn btn-primary" 
             :value "Shazam"
             :on-click #(do
                          (.-preventDefault (swap! showme not))
                          (email-us))}]))

(defn pay-button [showme] 
  (fn []
    [:input {:type "button" 
             :class "btn btn-primary" 
             :value "Shazam"
             :on-click #(do
                          (.-preventDefault (swap! showme not))
                          (pay-up))}]))


 (defn back-button []
   (fn []
     [:input {:type "button"
              :class "btn btn-primary"
              :value "Take me back!" 
              :on-click #(js/history.back)}]))

(defn name-input []
  (let [namer (subscribe [:yourname])]
    (fn []
      [:input {:type "text"
               :placeholder @namer
               :on-change #(dispatch [:enter-yourname 
                                      (-> % .-target .-value)])}])))

(defn name-input2 [sub handler]
  (let [namer (subscribe [sub])]
    (fn []
      [:input {:type "text"
               :placeholder @namer
               :on-change #(dispatch [handler 
                                      (-> % .-target .-value)])}])))


(defn atom-input [sub handler]
  (let [value (subscribe [sub])]
    (fn []
      [:input {:type "text"
               :placeholder @value
               :on-change #(dispatch [handler 
                                      (-> % .-target .-value)])}])))

(defn wide-text-box [sub handler]
  (let [value (subscribe [sub])]
    (fn []      
      [:input {:type "text"
               :placeholder @value
               :size 40
               :on-change #(dispatch [handler (-> % .-target .-value)])}])))


(defn small-text-box [sub handler]
  (let [value (subscribe [sub])]
    (fn []
      [:input {:type "text"
               :placeholder @value
               :size 2
               :on-change #(dispatch [handler (-> % .-target .-value)])}])))

(defn medium-text-box [sub handler]
  (let [value (subscribe [sub])]
    (fn []
      [:input.form-control {:type "text"
                            :placeholder @value
                            :size 20
                            :on-change #(dispatch [handler 
                                                   (-> % .-target .-value)])}])))


(defn text-area-input [sub handler]
  (let [value (subscribe [sub])]
  (fn []
    [:textarea.form-notes {:rows 4 
                           :cols 80
                           :value @value
                           :on-change #(dispatch [handler 
                                                  (-> % .-target .-value)])}])))

(defn text-area-output [sub handler]
  (let [value (subscribe [sub])]
    (fn []
    [:div.form-group.has-feedback
      [:textarea
       {:class "form-control input-hg"
        :rows 6
        :cols 80
        :placeholder @value
        :on-change #(dispatch [handler (-> % .-target .-value)])}]])))

(defn dropdown-element []
     [:ul
      {:class "nav navbar-nav"}
      [:li
       {:class "dropdown"}
       [:a
        {:href "#",
         :class "dropdown-toggle",
         :data-toggle "dropdown",
         :role "button",
         :aria-expanded "false"
         :aria-haspopup "true"}
        "Topics"
        [:span {:class "caret"}]]
       [:ul
        {:class "dropdown-menu", :role "menu"}
        [:li [:a {:href "#"} "Home"]]
        [:li {:class "divider"}]
        [:li [:a {:href "#/firstform"} "Basic Form"]]
        [:li [:a {:href "#/firstinterview"} "The Funnel"]]]]])


(defn user-name []
  (let [firstname (subscribe [:first-name])
        lastname (subscribe [:last-name])]
  (fn []
  [:div {:class "navbar-right"}
    [:p.navbar-text 
     (str "Signed in as " @firstname " " @lastname)]
   [:a {:href "#/profile-page" :type "button" 
        :class "btn btn-default navbar-btn"} "My Account"]
   [:a {:on-click #(dispatch [:initialise-db]) :href "/" :type "button"
        :class "btn btn-social-linkedin navbar-spacer"} "Log Out"]])))

(defn register-login []
  (fn []
  [:div {:class "navbar-right"}
   [:a {:href "#/login-page" :type "button" 
        :class "btn btn-default navbar-btn"} "Login"]
   [:a {:href "#/register-page" :type "button" 
        :class "btn btn-oranj navbar-spacer"} "Register"]]))

;; ---------------- Navbar -----------------
(defn navbar []
  (let [logged-in? (subscribe [:logged-in?])]
  [:nav {:class "navbar navbar-inverse navbar-fixed-top"}
   [:div {:class "container-fluid"}
    [:div {:class "navbar-header"}
     [:button
      {:type "button",
       :class "navbar-toggle collapse",
       :data-toggle "collapse",
       :data-target "#bs-example-navbar-collapse-1"}
      [:span {:class "sr-only"} "Toggle navigation"]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]
      [:span {:class "icon-bar"}]]
     [:a {:class "navbar-brand", :href "#"} "lawsuitninja.com" [:i " beta"]]]
    [:div
     {:class "collapse navbar-collapse",
      :id "bs-example-navbar-collapse-1"}
     [dropdown-element]
     [:ul
      {:class "nav navbar-nav"}
      [:li [:a {:href "#/contact-info"} "Contact for Info"]]
      [:li [:a {:href "#/about-page"} "About"]]
;      [:li [:a {:href "#/nearby-courthouses"} "Nearby Courts"]]
]
     [switch-buttons logged-in? [user-name] [register-login]]]]]))

;; ------------- End Navbar ----------------------

;;;; ------------- Bootstrap Carousel ----------------------


(defn carouseller []
  [:div.container
   [:div {:class "row"}
;    [:div ;{:class  "mtc"}
     [:div {:id "myCarousel" :class "carousel slide"}
      [:ol {:class "carousel-indicators carousel-indicators--thumbnails"}

       [:li {:data-target "#myCarousel" :data-slide-to "0" :class "active"}
        [:div.thumbnail
         [:img {:src "/img/legal7.png" :class "img-responsive"}]]]

       [:li {:data-target "#myCarousel" :data-slide-to "1"}
        [:div.thumbnail
         [:img {:src "/img/legal7.png" :class "img-responsive"}]]]

       [:li {:data-target "#myCarousel" :data-slide-to "2"}
        [:div.thumbnail
         [:img {:src "/img/legal7.png" :class "img-responsive"}]]]]

      [:div {:class "carousel-inner"}

       [:div {:class "active item"}
        [:img {:src "/img/write13.png" 
               :class "img-responsive center-block" 
               :width "25%"}]
        [:div {:class "carousel-caption"} 
         [:h3 "1. Write your petition."]
         [:p "We'll help guide you through the court form."]]]

       [:div {:class "item"}
        [:img {:src "/img/public6.png" 
               :class "img-responsive center-block" 
               :width "25%"}]
        [:div {:class "carousel-caption"} 
         [:h3 "2. File your petition with the court."]
         [:p "You can do this yourself, or we can do it for you."]]]


       [:div {:class "item"}
        [:img {:src "/img/package36.png" 
               :class "img-responsive center-block" 
               :width "25%"}]
        [:div {:class "carousel-caption"} 
         [:h3 "3. Have a process server give the right papers to your landlord."]
         [:p "We can take care of that for you."]]]


       [:a {:class "carousel-control fui-arrow-left left" 
            :href "#myCarousel" :data-slide "prev"}]
       [:a {:class "carousel-control fui-arrow-right right" 
            :href "#myCarousel" :data-slide "next"}]]]]])


(defn mounter []
  (js/$ (fn []
        (.carousel (js/$ "#myCarousel"))
        )))


(defn carousel []
  (reagent/create-class {:component-did-mount mounter
                         :reagent-render carouseller}))


;; ------------ End Carousel  ------------


;; ------------ Pagination ------------

; get current-page. get the href and assoc to that page "active"

(defn paginator []
  [:div {:class "pagination pagination-centered"}
   [:ul
    [:li {:class 
          (when (re-find #"#left-property" (.-hash js/location)) "active")}   
     [:a {:href "#left-property"} 1]]
    [:li {:class 
          (when (re-find #"#time-limit-page" (.-hash js/location)) "active")} 
     [:a {:href "#time-limit-page"} 2]]
    [:li {:class 
          (when (re-find #"#written-reason" (.-hash js/location)) "active")} 
     [:a {:href "#written-reason"} 3]]
    [:li {:class 
          (when (re-find #"#owe-rent" (.-hash js/location)) "active")} 
     [:a {:href "#owe-rent"} 4]]
    [:li {:class 
          (when (re-find #"#disagree-rent" (.-hash js/location)) "active")} 
     [:a {:href "#disagree-rent"} 5]]
    [:li {:class 
          (when (re-find #"#personal-details" (.-hash js/location)) "active")} 
     [:a {:href "#personal-details"} 6]]]])

;; ------------ End Paginator -----------



;; Tool-tipper.
(defn tool-tipper [link title]
   [:a {:href link
         :target "_blank"}
     [:span {:class "fui-question-circle" 
             :data-toggle "tooltip" 
             :title title
             :data-placement "right"}]])

(defn popover [ title]
     [:span {:class "fui-question-circle" 
             :data-toggle "tooltip" 
             :title title
             :data-placement "right"}])


;; --------- Toggler -------------
(defn toggle-class [a k class1 class2]
  (if (= (@a k) class1)
    (swap! a assoc k class2)
    (swap! a assoc k class1)))

(defn toggler []
  (let [state (atom {:btn-class "btn btn-info"})]
    (fn []
      [:div
       [:input {:class (@state :btn-class)
                :type "button"
                :value "Click me"
                :on-click #(toggle-class state :btn-class 
                                         "btn btn-info" "btn btn-danger")}]])))


(defn toggle-yes [yes-message]
  (let [state (atom {:secret-class "hidden-secret"})]
    (fn []
      [:div
       [:input {:class "btn btn-info palette-turquoise"
                :type "button"
                :value "Yes"
                :on-click #(toggle-class state :secret-class 
                                         "hidden-secret" "nothidden")}]
       [:span {:class (@state :secret-class)} yes-message]])))

(defn toggle-no [no-message]
  (let [state (atom {:secret-class "hidden-secret"})]
    (fn []
      [:div
       [:input {:class "btn btn-info palette-carrot"
                :type "button"
                :value "No"
                :on-click #(toggle-class state :secret-class 
                                         "hidden-secret" "nothidden")}]
       [:span {:class (@state :secret-class)} no-message]])))

;; ------------- End Toggler -------------- 


(defn- before
  "Return a new js/Date which is the given number of days before the given date"
  [date days]
  (js/Date. (.getFullYear date) (.getMonth date) (- (.getDate date) days)))

(defn date? [x]
  (= (type x) js/Date))


(defn days-between 
  "Return the number of days between the two js/Date instances"
  [x y]
  (when (every? date? [x y])
    (let [ms-per-day (* 1000 60 60 25)
          x-ms (.getTime x)
          y-ms (.getTime y)]
      (.round js/Math (.abs js/Math (/ (- x-ms y-ms) ms-per-day))))))


(def today (js/Date.))
(def start-date (atom today))
(def end-date (atom today))
(def total-days-selected (reaction (days-between @(subscribe [:date-moved-out]) today)))

(defn alt-picker []
  [:div.form-group
   [:div.input-group
     [:span {:class "input-group-btn"}
      [:button {:class "btn" :type "button"}
       [:span {:class "fui-calendar"}]]]

     [pikaday/date-selector 
      {:date-atom (subscribe [:date-moved-out])
       :max-date-atom end-date
       :class "form-control"
       :type "text"
       :pikaday-attrs {:max-date today}}]]])


(defn alt-picker2 [day max-date]
  [:div.form-group
   [:div.input-group
     [:span {:class "input-group-btn"}
      [:button {:class "btn" :type "button"}
       [:span {:class "fui-calendar"}]]]

     [pikaday/date-selector2
      {:date-atom day
       :max-date-atom max-date
       :class "form-control"
       :type "text"
       :pikaday-attrs {:max-date today}}]]])


(defn alt-picker3 [day start-date end-date]
  [:div.parent-container

   [:div.child-column
    [:p "Start Date"]
    [:div.form-group
     [:div.input-group
      [:span {:class "input-group-btn"}
       [:button {:class "btn" :type "button"}
        [:span {:class "fui-calendar"}]]]
      [pikaday/date-selector2
       {:date-atom start-date
        :max-date-atom end-date
        :class "form-control"
        :type "text"
        :input-attrs {:id "start"}
        :pikaday-attrs {:max-date today}}]]]]

    [:div.child-column 
     [:p "End Date"]
     [:div.form-group
      [:div.input-group
       [:span {:class "input-group-btn"}
        [:button {:class "btn" :type "button"}
         [:span {:class "fui-calendar"}]]]
       [pikaday/date-selector2
        {:date-atom end-date
         :min-date-atom start-date
         :class "form-control"
         :type "text"
         :input-attrs {:id "end"}
         :pikaday-attrs {:max-date today}}]]]]])






;; --------- Sortable Portlets ---------
(declare nowhere)
(declare left-property)
(defn disabled-portlet-button [cause]
   [:div.portlet
    [:div.portlet-content (mymacros/disabled-button nowhere cause)]])

(defn ready-portlet-button [cause]
   [:div.portlet
    [:div.portlet-content (mymacros/primary-button left-property cause)]])



(defn portlet-page [ready-causes not-ready-causes]
  [:div
   [:h6 "Choose everything that looks like a problem worth going to court over."]

   [:div.portlet-column
    [:div.portlet [:p "Place here"]]]

   [:div.portlet-column
    (for [ready-cause ready-causes]
      ^{:key ready-cause}[ready-portlet-button ready-cause])]

   [:div.portlet-column
    (for [not-cause not-ready-causes]
      ^{:key not-cause} [disabled-portlet-button not-cause])]])

(def ready-causes ["Deposit Withheld"])

(def not-ready-causes ["Noisy Neighbor" 
                     "Rat Infestation" 
                     "Breach of Contract" 
                     "TDTPA" 
                     "Assault and/or Battery"])


(defn sortable-portlets []
  (js/$ (fn []
          (.sortable (js/$ ".portlet-column") 
                     (clj->js 
                      {:connectWith ".portlet-column"
                       :handle ".portlet-content"
                       :cancel ".portlet-toggle"
                       :placeholder "portlet-placeholder ui-corner-all"})))))

;; (defn portlet-component []
;;   (reagent/create-class {:reagent-render #(portlet-page ready-causes upfront-causes)
;;                          :component-did-mount sortable-portlets}))



;; -------------------------
;; Sortable Portlets Key Function



(defmethod init-field :typeahead
  [[type {:keys [id data-source input-class list-class item-class highlight-class result-fn choice-fn clear-on-focus?]
          :as attrs
          :or {result-fn identity
               choice-fn identity
               clear-on-focus? true}}] {:keys [doc get save!]}]
  (let [typeahead-hidden? (atom true)
        mouse-on-list? (atom false)
        selected-index (atom 0)
        selections (atom [])
        choose-selected #(do (let [choice (nth @selections @selected-index)]
                               (save! id choice)
                               (choice-fn choice))
                             (reset! typeahead-hidden? true))]
    (render-element attrs doc
                    [type
                     [:input {:type        :text
                              :class       input-class
                              :placeholder "State"
                              :value       (let [v (get id)]
                                             (if-not (iterable? v)
                                               v (first v)))
                              :on-focus    #(when clear-on-focus? (save! id ""))
                              :on-blur     #(when-not @mouse-on-list?
                                              (reset! typeahead-hidden? true)
                                              (reset! selected-index 0))
                              :on-change   #(do
                                              (reset! selections (data-source (.toLowerCase (value-of %))))
                                              (save! id (value-of %))
                                              (reset! typeahead-hidden? false)
                                              (reset! selected-index 0))
                              :on-key-down #(do
                                              (case (.-which %)
                                                38 (do
                                                     (.preventDefault %)
                                                     (if-not (= @selected-index 0)
                                                       (reset! selected-index (- @selected-index 1))))
                                                40 (do
                                                     (.preventDefault %)
                                                     (if-not (= @selected-index (- (count @selections) 1))
                                                       (reset! selected-index (+ @selected-index 1))))
                                                9  (choose-selected)
                                                13 (choose-selected)
                                                27 (do (reset! typeahead-hidden? true)
                                                       (reset! selected-index 0))
                                                "default"))}]

                     [:ul {:style {:display (if (or (empty? @selections) @typeahead-hidden?) :none :block) }
                           :class list-class
                           :on-mouse-enter #(reset! mouse-on-list? true)
                           :on-mouse-leave #(reset! mouse-on-list? false)}
                      (doall
                       (map-indexed
                        (fn [index result]
                          [:li {:tab-index     index
                                :key           index
                                :class         (if (= @selected-index index) highlight-class  item-class)
                                :on-mouse-over #(do
                                                  (reset! selected-index (js/parseInt (.getAttribute (.-target %) "tabIndex"))))
                                :on-click      #(do
                                                  (reset! typeahead-hidden? true)
                                                  (save! id result)
                                                  (choice-fn result))}
                           (result-fn result)])
                        @selections))]])))

(def US-states
["Alabama"
"Alaska"
"Arizona"
"Arkansas"
"California"
"Colorado"
"Connecticut"
"Delaware"
"Florida"
"Georgia"
"Hawaii"
"Idaho"
"Illinois"
"Indiana"
"Iowa"
"Kansas"
"Kentucky"
"Louisiana"
"Maine"
"Maryland"
"Massachusetts"
"Michigan"
"Minnesota"
"Mississippi"
"Missouri"
"Montana"
"Nebraska"
"Nevada"
"New Hampshire"
"New Jersey"
"New Mexico"
"New York"
"North Carolina"
"North Dakota"
"Ohio"
"Oklahoma"
"Oregon"
"Pennsylvania"
"Rhode Island"
"South Carolina"
"South Dakota"
"Tennessee"
"Texas"
"Utah"
"Vermont"
"Virginia"
"Washington"
"West Virginia"
"Wisconsin"
"Wyoming"
"District of Columbia"
"Puerto Rico"
"Guam"
"American Samoa"
"U.S. Virgin Islands"
"Northern Mariana Islands"])

(defn state-source [text]
  (filter
    #(-> % (.toLowerCase %) (.indexOf text) (> -1))
    US-states))

(defn row [label input]
  [:div.row
    [:div.col-md-2 [:label label]]
    [:div.col-md-2 input]])

(def form-template
 [:div
   (row ""
        [:div {:field           :typeahead
               :id              "typer"
               :data-source     state-source
               :input-class     "form-control"
               :list-class      "typeahead-list"
               :item-class      "typeahead-item"
               :highlight-class "highlighted"}])])

(defn typeahead []
  (let [doc (atom {})]
    [:div
     [bind-fields
      form-template
      doc]]))


;;;;;;;;;; Progress Bar;;;;;;;;;;;

(defn compute-strength [password]  
    (str (aget (js/zxcvbn @password) "score")))

(defn progress-bar [password]
  (let [strength (compute-strength password)]
  [:div.progress
   [:div.progress-bar.progress-bar-danger 
    {:role "progressbar" :style {:width "15%"}}]
   (when (>= strength password 1)
   [:div.progress-bar.progress-bar-carrot {:style {:width "25%"}}])
   (when (>= strength 2)
   [:div.progress-bar.progress-bar-warning {:style {:width "25%"}}])
   (when (>= strength 3)
   [:div.progress-bar {:style {:width "30%"}}])
   (when (>= strength 4)
   [:div.progress-bar.progress-bar-success {:style {:width "30%"}}])]))

;;;;;;;;  End Progress Bar ;;;;;;;;;;;

(defn check-nil-then-predicate
  "Check if the value is nil, then apply the predicate"
  [value predicate]
  (if (nil? value)
    false
    (predicate value)))

;; Regex for a valid email address
(def email-regex #"[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?")
;(def email-regex #".+\@.+\..+")

(defn valid-email?
  "Determine if the specified email address is valid according to our email regex."
  [email]
  (let [email @email]
  (and (not (nil? email)) (re-matches email-regex email))))


(defn eight-or-more-characters?
  [word]
  (check-nil-then-predicate word (fn [arg] (> (count arg) 7))))


(defn has-special-character?
  [word]
  (check-nil-then-predicate word (fn [arg] (boolean (first (re-seq #"\W+" arg))))))


(defn has-number?
  [word]
  (check-nil-then-predicate word (fn [arg] (boolean (re-seq #"\d+" arg)))))


(defn prompt-message
  "A prompt that will animate to help the user with a given input"
  [message]
  [:div {:class "my-messages"}
   [:div {:class "prompt message-animation"} [:p message]]])


(defn password-input-element
  "An input element which updates its value and on focus parameters on change, blur, and focus"
  [id name type password in-focus]
  [:input {:id id
           :name name
           :class "form-control"
           :type type
           :required ""
           :placeholder @password
           :on-change #(reset! password (-> % .-target .-value))
           :on-focus #(swap! in-focus not)
           :on-blur #(swap! in-focus not)}])

(defn email-input-element
  "An input element which updates its value and on focus parameters on change, blur, and focus"
  [id name type value in-focus]
  [:input {:id id
           :name name
           :class "form-control"
           :type type
           :required ""
           :placeholder @value
           :on-change #(dispatch [:enter-your-email (-> % .-target .-value)])
;           :on-focus #(swap! in-focus not)
           :on-blur (fn [arg] (if (nil? @value) (reset! value ""))(swap! in-focus not))}])


(defn input-and-prompt
  "Creates an input box and a prompt box that appears above the input when the input comes into focus. Also throws in a little required message"
  [input-element label-value input-name input-type input-element-arg prompt-element required?]
  (let [input-focus (atom false)]
    (fn []
      [:div
       [input-element input-name input-name input-type input-element-arg input-focus]
       (if (and required? (= "" @input-element-arg))
         [:div "Field is required!"]
         [:div])])))

(defn password-input-and-prompt
  "Creates an input box and a prompt box that appears above the input when the input comes into focus. Also throws in a little required message"
  [input-element label-value input-name input-type password]
  (let [input-focus (atom false)]
    (fn []
      [:div
       [input-element input-name input-name input-type password input-focus]
       (when @input-focus 
         [:div
          [:br]
         [progress-bar password]])])))

(defn email-form [email-address-atom]
  (input-and-prompt email-input-element
                    "email"
                    "email"
                    "email"
                    email-address-atom
                    (prompt-message "What's your email address?")
                    true))


(defn password-requirements
  "A list to describe which password requirements have been met so far"
  [password requirements]
  [:div
   [:ul (->> requirements
             (filter (fn [req] (not ((:check-fn req) password))))
             (doall)
             (map (fn [req] ^{:key req} [:li (:message req)])))]])


(defn password-form
  [password]
    (fn []
      [:div
       [(password-input-and-prompt password-input-element
                          "password"
                          "password"
                          "password"
                          password)]]))

(defn second-password-form
  [password]
    (fn []
      [:div
       [(input-and-prompt password-input-element
                          "password"
                          "password"
                          "password"
                          password
                          "hello"
                          true)]]))


(defn wrap-as-element-in-form
  [element]
  [:div {:class "row form-group"}
   [:div
    [:form {:class "form-inline" :role "form"}
     [:div {:class "form-group"}
      element]]]])


(defn change-legal-name []
  "Taking the first and last name, this creates a default legal name."
  (let [firstname (subscribe [:first-name])
        lastname (subscribe [:last-name])]
    (dispatch [:enter-yourname (str @firstname " " @lastname)])))

(defn register-button [password] 
  (fn []
    [:input {:type "button" 
             :class "btn btn-info" 
             :value "Do it"
             :on-click #(do (change-legal-name)
                            (register-user @password))}]))


(defn login-button [password forgot-password] 
  (fn []
    [:input {:type "button" 
             :class "btn btn-alizarin" 
             :value "Do it"
             :on-click #(login-user @password forgot-password)}]))


(defn update-password-button [password] 
  (fn []
    [:input {:type "button" 
             :class "btn btn-alizarin" 
             :value "Do it"
             :on-click #(new-password @password)}]))


(defn email-wait-button [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value "Register"
           :on-click #(js/alert "Sorry, we need your email!")}]))

(defn password-mismatch-button [value] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value value
           :on-click #(js/alert "Sorry, your passwords don't match!")}]))

(defn bad-password-button [value] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value value
           :on-click #(js/alert "Please make sure your password has eight 
characters, and a special character or a number.")}]))

(defn need-name-button [] 
  (fn []
  [:input {:type "button" 
           :class "btn btn-oranj" 
           :value "Register"
           :on-click #(js/alert "Please enter your first and last names.")}]))

(defn good-password? [password]
  (let [password @password]
  (and
   (eight-or-more-characters? password)
   (or (has-special-character? password)
       (has-number? password)))))

(defn password-match? [password second-password]
  (= @password @second-password))

(defn name-filled? []
    (and
     (not= "your first name" @(subscribe [:first-name]))
     (not= "your last name" @(subscribe [:last-name]))))

(defn complete-credentials? [password second-password email-address]
  (and 
   (valid-email? email-address)
   (good-password? password)
   (name-filled?)))


(defn register-switch-buttons [password second-password email-address]
  (fn []
    [:div
     (cond
       (not (name-filled?)) [need-name-button]
       (not (valid-email? email-address)) [email-wait-button]
       (not (good-password? password)) [bad-password-button "Register"]
       (not (password-match? password second-password)) [password-mismatch-button "Register"]
       :else [register-button password])]))


(defn switch-buttons2 [atom default alternative]
  (fn []
    (if-not @atom
      default
      alternative)))

(defn password-switch-buttons [password second-password]
  (fn []
    [:div
     (cond
       (not (good-password? password)) [bad-password-button "Update"]
       (not (password-match? password second-password)) [password-mismatch-button "Update"]
       :else [update-password-button password])]))

(defn login-switch-buttons [password email-address forgot-password]
  (fn []
    [:div
     (if (valid-email? email-address)

       [login-button password forgot-password]
       [wait-button])]))

(defn beeminder-graph []
  [:div
   [:iframe {:src "https://www.beeminder.com/widget?slug=dailyuvi&username=sensei&countdown=true"
             :height "245px"
             :width "230px"
             :frameborder "0px"}]])

;;;; HTML5 LocalStorage Utility Functions
(defn set-item!
  "Set `key' in browser's localStorage to `val`."
  [key val]
  (.setItem (.-localStorage js/window) key val))

(defn get-item
  "Returns value of `key' from browser's localStorage."
  [key]
  (.getItem (.-localStorage js/window) key))

(defn remove-item!
  "Remove the browser's localStorage value for the given `key`"
  [key]
  (.removeItem (.-localStorage js/window) key))
