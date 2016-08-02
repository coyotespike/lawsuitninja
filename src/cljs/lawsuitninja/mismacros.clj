(ns lawsuitninja.mismacros)

(defmacro heck
  "An intermediate test, en route to fn-name"
 [x] `(fn-name ~x))

(defmacro fn-name
  "Given a function, this macro returns the unevaluated string name."
  [f]
  `(-> ~f var meta :name str))

(defmacro change-page 
  "This function sets the url to the function name, and changes the page
  to the function value."
  [f]
    `(do
      (set! (.-hash js/location) (fn-name ~f))
      (session/put! :current-page ~f)))



(defmacro info-button 
  "Uses change-page, with btn-info style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn btn-info" 
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])


(defmacro primary-button 
  "Uses change-page, with btn-primary style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn btn-primary" 
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])

(defmacro disabled-button 
  "Uses change-page, with btn-disabled (gray) style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn btn-default.disabled:active" 
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])


(defmacro turk-button 
  "Uses change-page, with beautiful turquoise style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn btn-info navbar-btn palette-turquoise" 
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])


(defmacro huge-danger-button 
  "Uses change-page, with big red button style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn btn-hg btn-danger" 
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])

(defmacro sign-in-button 
  "Uses change-page, with lighter green style."
  [nextpage displayvalue] 
  `[:input {:type "button" 
           :class "btn navbar-btn navbar-right"
           :value ~displayvalue
           :on-click #(change-page ~nextpage)}])


(defmacro paginate-page 
  "This macro is intended to generalize the paginator.
  It requires matching an ascending numerical sequence before it can be used."
  [page]
    `[:li [:a {:href (str "#" (fn-name ~page))} 1]])

(defmacro divver 
  "This macro is intended to pass a vector of pages to the paginator-creator,
  and wrap it in a div."
  [causes]
  (vec (for [cause causes]
    [:div
      `(paginate-page ~cause)])))
